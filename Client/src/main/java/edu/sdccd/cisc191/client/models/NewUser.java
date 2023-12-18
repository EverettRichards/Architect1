package edu.sdccd.cisc191.client.models;

//Class new users on sign up.
public class NewUser {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String password;

    public String getUsername(){ return this.username; }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname(){ return this.nickname; }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail(){ return this.email; }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword(){ return this.password; }
    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() { return this.id; }
    public void setId(Long id) {
        this.id = id;
    }
}
