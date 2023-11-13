package edu.sdccd.cisc191.common.cryptography;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import edu.sdccd.cisc191.common.entities.ID;
import edu.sdccd.cisc191.common.entities.User;

public class SessionCookie {
    // Note: every program restart will invalidate all existing session token

    private class secrets {
        public static Algorithm generate() {
            KeyPairGenerator generator;
            try {
                generator = KeyPairGenerator.getInstance("RSA");
            } catch(NoSuchAlgorithmException e) {
                System.err.println("The algorithm for sessioncookie does not exist");
                return null; // unreachable
            }

            generator.initialize(2048);
            KeyPair pair = generator.generateKeyPair();

            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            return Algorithm.RSA256((RSAPublicKey) publicKey, (RSAPrivateKey) privateKey);
        }
    }

    public static Algorithm algorithm = secrets.generate();
    private String token;

    public static String createToken(User user) {
        try {
            Instant now = Instant.now();

            return JWT.create()
                .withIssuer("auth0")
                .withPayload(new ID(user.getId().toString()).toJson())
                .withExpiresAt(now.plus(1, ChronoUnit.DAYS))
                .sign(algorithm);
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
        }

        return null;
    }

    public SessionCookie(String userToken) {
        this.token = userToken;
    }

    public boolean isValid() {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                // specify an specific claim validations
                .withIssuer("auth0")
                // reusable verifier instance
                .build();
                
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            // Invalid signature/claims
        }

        return false;
    }

    public Long getUserId() throws JWTVerificationException {
        DecodedJWT decodedJWT;
        JWTVerifier verifier = JWT.require(algorithm)
            // specify an specific claim validations
            .withIssuer("auth0")
            // reusable verifier instance
            .build();
            
        decodedJWT = verifier.verify(token);

        return Long.parseLong(decodedJWT.toString());
    }
}
