package edu.sdccd.cisc191.common.cryptography;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import edu.sdccd.cisc191.common.entities.User;

public class Hasher {

    private static byte[] pepper = "can1g0t0s1eepn0w?".getBytes();

    private static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte salt[] = new byte[256];
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] computeHash(byte[] cookedPassword, byte[] salt, byte[] pepper) {
        try {
            ByteBuffer passwordDinner = ByteBuffer.wrap(new byte[cookedPassword.length + salt.length + pepper.length]);
            passwordDinner.put(cookedPassword);
            passwordDinner.put(salt);
            passwordDinner.put(pepper);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(passwordDinner.array());
        } catch(NoSuchAlgorithmException e) {
            System.err.println("hash algorithm doesn't exist");
            return null;
        }

    }

    public static String hashNewPassword(String password) {
        byte[] salt = getSalt();
        byte[] cookedPassword = password.getBytes();
        byte[] hash = computeHash(cookedPassword, salt, pepper);
        return bytesToHex(hash)+":"+toBase64String(salt);
    }

    public static boolean isCorrectPassword(User user, String password) {
        String[] passwordHash = user.getPasswordHash().split(":");
        byte[] digestedPassword = passwordHash[0].getBytes();
        String saltBase64String = passwordHash[1];
        byte[] salt = fromBase64String(saltBase64String);

        byte[] hash = computeHash(password.getBytes(), salt, pepper);
        return Arrays.equals(hash, digestedPassword);
    }

    public static String toBase64String(byte[] bytes) {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }

    public static byte[] fromBase64String(String base64String) {
        Decoder decoder = Base64.getDecoder();
        return decoder.decode(base64String);
    }

    public static byte[] hexStringToBytes(String hex) {
        return HexFormat.of().parseHex(hex);
    }

    public static String bytesToHex(byte[] bytes) {
        HexFormat formatter = HexFormat.of();
        return formatter.formatHex(bytes);
    }

}
