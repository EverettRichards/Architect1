package edu.sdccd.cisc191.common.entities;

/**
 * User is a program that creates the account and information about a new user of the web program
 */
public abstract class User {
    public static int currentID = 0;            //the current id that is used for access
    private String username;                    //the users selected account name
    private int userid; // Unique numerical identifier for each user, regardless of status as admin/broker/customer

    private int authorizationLevel; // 0 = customer, 1 = broker, 2 = admin

    /**
     * Get the username that is listed by this account
     * @return username the name of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Constructor for creating a new user using the name and authorization id
     * @param name the username that was added
     * @param authLevel the authorizationLevel granted for accessing the program features
     */
    public User(String name, int authLevel){ // constructor for the abstract class
        username = name;
        authorizationLevel = authLevel;
        userid = ++currentID;
    }

    /**
     * Sets the username to the string passed in
     * @param name username to be added
     */
    public void setUsername(String name) {
        username = name;
    }

    /**
     * Gets the UserID
     * @return userid the id that is currently being used
     */
    public int getUserID() {
        return userid;
    }

    /**
     * Sets the UserID
     * @param id the userid that will be added
     */
    public void setUserID(int id) {
        userid = id;
    }

    /**
     * Gets the authorizationLevel for the user
     * @return authorizationLevel the current access level
     */
    public int getAuthorizationLevel() {
        return authorizationLevel;
    }

    /**
     * Sets the authorizationLevel for the user
     * @param level set authorization level for access control
     */
    public void setAuthorizationLevel(int level) {
        authorizationLevel = level;
    }
}
