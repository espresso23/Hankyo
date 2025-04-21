package model;

import java.util.Date;

public class Login {
    private int LoginID;
    private  User user;
    private Date lastDateLogin;
    private int loginDays;

    public Login() {
    }

    public Login(int loginID, User user, Date lastDateLogin, int loginDays) {
        LoginID = loginID;
        this.user = user;
        this.lastDateLogin = lastDateLogin;
        this.loginDays = loginDays;
    }

    public Login(User user, Date lastDateLogin, int loginDays) {
        this.user = user;
        this.lastDateLogin = lastDateLogin;
        this.loginDays = loginDays;
    }

    public int getLoginID() {
        return LoginID;
    }

    public void setLoginID(int loginID) {
        LoginID = loginID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLastDateLogin() {
        return lastDateLogin;
    }

    public void setLastDateLogin(Date lastDateLogin) {
        this.lastDateLogin = lastDateLogin;
    }

    public int getLoginDays() {
        return loginDays;
    }

    public void setLoginDays(int loginDays) {
        this.loginDays = loginDays;
    }

    @Override
    public String toString() {
        return "Login{" +
                "LoginID=" + LoginID +
                ", user=" + user +
                ", lastDateLogin=" + lastDateLogin +
                ", loginDays=" + loginDays +
                '}';
    }
}
