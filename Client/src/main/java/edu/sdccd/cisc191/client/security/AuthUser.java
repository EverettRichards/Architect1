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

    //Extended instance variables to store in session management
    private Long id;
    private String nickname;
    private List<String> followedTickers;

    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, String nickname, List<String> followedTickers) {
        super(username, password, authorities);
        this.id = id;
        this.nickname = nickname;
        this.followedTickers = followedTickers;
    }

    //Getter method for id instance variable
    public Long getId() { return this.id; }

    //Setter method for id instance variable
    public void setId(Long id) { this.id = id; }

    //Getter method for nickname instance variable
    public String getNickname() {
        return this.nickname;
    }

    //Setter method for nickname instance variable
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    //Getter method for nickname instance variable
    public List<String> getFollowedTickers() {
        return this.followedTickers;
    }

    //Setter method for nickname instance variable
    public void setFollowedTickers(ArrayList<String> followedTickers) {
        this.followedTickers = followedTickers;
    }
}
