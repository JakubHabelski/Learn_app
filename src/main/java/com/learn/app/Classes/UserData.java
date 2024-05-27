package com.learn.app.Classes;

//import com.learn.app.Classes.FlashCardSet;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name="user_data")
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserID;
    private String UserName;
    private String UserSurname;
    private String UserLogin;
    private String UserPass;
    private String UserMail;
    private String UserToken;
    @Column(columnDefinition = "boolean default false", name = "user_active")
    private Boolean UserActive ;


    @OneToMany(mappedBy = "userData")
    private List<FlashCardSet> flashCardSets;

    @OneToOne(mappedBy = "userData")
    private image Image;



    public UserData() {
    }

    public UserData(Long userID, String userName, String userSurname, String userLogin, String userPass, String userMail) {
        UserID = userID;
        UserName = userName;
        UserSurname = userSurname;
        UserLogin = userLogin;
        UserPass = userPass;
        UserMail = userMail;
    }

    public UserData(String userName, String userSurname, String userLogin, String userPass, String userMail) {
        UserName = userName;
        UserSurname = userSurname;
        UserLogin = userLogin;
        UserPass = userPass;
        UserMail = userMail;
    }

    public UserData(Long userID, String userLogin, String userPass) {
        UserID = userID;
        UserLogin = userLogin;
        UserPass = userPass;
    }

    public UserData(String userLogin, String userPass) {
        UserLogin = userLogin;
        UserPass = userPass;
    }

    public UserData( String userName, String userSurname, String userLogin, String userPass, String userMail, String userToken, Boolean userActive) {
        UserName = userName;
        UserSurname = userSurname;
        UserLogin = userLogin;
        UserPass = userPass;
        UserMail = userMail;
        UserToken = userToken;
        UserActive = userActive;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "UserID=" + UserID +
                ", UserName='" + UserName + '\'' +
                ", UserSurname='" + UserSurname + '\'' +
                ", UserLogin='" + UserLogin + '\'' +
                ", UserPass='" + UserPass + '\'' +
                ", UserMail='" + UserMail + '\'' +
                '}';
    }

    public Long getUserID() {
        return UserID;
    }

    public void setUserID(Long userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserSurname() {
        return UserSurname;
    }

    public void setUserSurname(String userSurname) {
        UserSurname = userSurname;
    }

    public String getUserLogin() {
        return UserLogin;
    }

    public void setUserLogin(String userLogin) {
        UserLogin = userLogin;
    }

    public String getUserPass() {
        return UserPass;
    }

    public void setUserPass(String userPass) {
        UserPass = userPass;
    }

    public String getUserMail() {
        return UserMail;
    }

    public void setUserMail(String userMail) {
        UserMail = userMail;
    }

    public String getUserToken() {
        return UserToken;
    }

    public void setUserToken(String userToken) {
        UserToken = userToken;
    }

    public Boolean getUserActive() {
        return UserActive;
    }

    public void setUserActive(Boolean userActive) {
        UserActive = userActive;
    }
}
