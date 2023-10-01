package edu.sdccd.cisc191.server;

public class AdminAccount {

    public String adminAccountName;
    public String adminAccountPassword;

    public boolean isActive = false;

    //TODO methods to manage admin accounts

    public AdminAccount (String name, String passWord){
        this.adminAccountName = name;
        this.adminAccountPassword =  passWord;
    }

    public String getAdminAccountName() {
        return adminAccountName;
    }

    public void setAdminAccountName(String adminAccountName) {
        this.adminAccountName = adminAccountName;
    }
}
