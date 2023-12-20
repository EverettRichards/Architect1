package edu.sdccd.cisc191.client.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * AuthUser - extends User from Spring Security to add additional
 * instance variables to the User object being stored in the
 * Spring Security session management.
 */
public class AuthUser extends User {

    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
