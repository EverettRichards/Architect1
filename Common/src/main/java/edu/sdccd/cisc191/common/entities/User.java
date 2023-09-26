package edu.sdccd.cisc191.common.entities;

public abstract class User {
    public static int currentID = 0;
    private String username;
    private int userid; // Unique numerical identifier for each user, regardless of status as admin/broker/customer

    private int authorizationLevel; // 0 = customer, 1 = broker, 2 = admin

    public String getUsername() {
        return username;
    }

    public User(String name, int authLevel){
        username = name;
        authorizationLevel = authLevel;
        userid = ++currentID;
    }
    public void setUsername(String name) {
        username = name;
    }

    public int getUserID() {
        return userid;
    }

    public void setUserID(int id) {
        userid = id;
    }

    public int getAuthorizationLevel() {
        return authorizationLevel;
    }

    public void setAuthorizationLevel(int level) {
        authorizationLevel = level;
    }
}
