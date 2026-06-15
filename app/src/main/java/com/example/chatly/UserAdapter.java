package com.example.chatly;

public class UserAdapter {

    // User Information
    String profilepicAdpt;
    String mailAdpt;
    String userNameAdpt;
    String passwordAdpt;
    String userIdAdpt;
    String lastMsgAdpt;
    String statusAdpt;

    // Empty Constructor Required By Firebase
    public UserAdapter() {

    }

    // Main Constructor
    public UserAdapter(String idAdpt,
                       String nameAdpt,
                       String emailAdpt,
                       String passwordAdpt,
                       String imgUriStr,
                       String statusAdpt) {

        this.userIdAdpt = idAdpt;
        this.userNameAdpt = nameAdpt;
        this.mailAdpt = emailAdpt;
        this.passwordAdpt = passwordAdpt;
        this.profilepicAdpt = imgUriStr;
        this.statusAdpt = statusAdpt;
    }

    // ======================
    // Getter And Setter
    // ======================

    public String getProfilepicAdpt() {
        return profilepicAdpt;
    }

    public void setProfilepicAdpt(String profilepicAdpt) {
        this.profilepicAdpt = profilepicAdpt;
    }

    public String getMailAdpt() {
        return mailAdpt;
    }

    public void setMailAdpt(String mailAdpt) {
        this.mailAdpt = mailAdpt;
    }

    public String getUserNameAdpt() {
        return userNameAdpt;
    }

    public void setUserNameAdpt(String userNameAdpt) {
        this.userNameAdpt = userNameAdpt;
    }

    public String getPasswordAdpt() {
        return passwordAdpt;
    }

    public void setPasswordAdpt(String passwordAdpt) {
        this.passwordAdpt = passwordAdpt;
    }

    public String getUserIdAdpt() {
        return userIdAdpt;
    }

    public void setUserIdAdpt(String userIdAdpt) {
        this.userIdAdpt = userIdAdpt;
    }

    public String getLastMsgAdpt() {
        return lastMsgAdpt;
    }

    public void setLastMsgAdpt(String lastMsgAdpt) {
        this.lastMsgAdpt = lastMsgAdpt;
    }

    public String getStatusAdpt() {
        return statusAdpt;
    }

    public void setStatusAdpt(String statusAdpt) {
        this.statusAdpt = statusAdpt;
    }

}

