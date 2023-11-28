package edu.sdccd.cisc191.client.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;

/**
 * AuthUser - extends User from Spring Security to add additional
 * instance variables to the User object being stored in the
 * Spring Security session management.
 */
public class AuthUser extends User {

    //Extended instance variables to store in session management
    private String nickname;

    private ArrayList<String> followedTickers;

    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String nickname, ArrayList<String> followedTickers) {
        super(username, password, authorities);
        this.nickname = nickname;
        this.followedTickers = followedTickers;
    }

    //Getter method for nickname instance variable
    public String getNickname() {
        return this.nickname;
    }

    //Setter method for nickname instance variable
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    //Getter method for nickname instance variable
    public ArrayList<String> getFollowedTickers() {
        return this.followedTickers;
    }

    //Setter method for nickname instance variable
    public void setFollowedTickers(ArrayList<String> followedTickers) {
        this.followedTickers = followedTickers;
    }
}
