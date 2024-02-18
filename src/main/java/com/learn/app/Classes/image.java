package com.learn.app.Classes;

import jakarta.annotation.Generated;
import jakarta.persistence.*;

@Entity
public class image {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;
    private String path;
    
    private Long userID;

    @OneToOne
    @JoinColumn(name="userID" , insertable=false, updatable=false)
    private UserData userData;

    public Long getUserID() {
        return userID;
    }
    public void setUserID(Long userID) {
        this.userID = userID;
    }
    public image() {
    }
    public image(String path, Long userID) {
        this.path = path;
        this.userID = userID;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}