package com.learn.app.Classes;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class FlashCardSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long SetID;
    private Long UserID;
    private String SetName;
    private String SetDescription;

    @Column(columnDefinition = "double default 0.0")
    private double Progression;


    @ManyToOne
    @JoinColumn(name = "UserID", insertable=false, updatable=false)
    private UserData userData;

    @OneToMany(mappedBy = "flashCardSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlashCards> flashCards;



    public FlashCardSet() {
    }

    public FlashCardSet(Long userID, String setName, String setDescription, double progression) {
        UserID = userID;
        SetName = setName;
        SetDescription = setDescription;
        Progression = progression;
    }

    public Long getSetID() {
        return SetID;
    }

    public void setSetID(Long setID) {
        SetID = setID;
    }

    public Long getUserID() {
        return UserID;
    }

    public void setUserID(Long userID) {
        UserID = userID;
    }

    public String getSetName() {
        return SetName;
    }

    public void setSetName(String setName) {
        SetName = setName;
    }

    public String getSetDescription() {
        return SetDescription;
    }

    public void setSetDescription(String setDescription) {
        SetDescription = setDescription;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public List<FlashCards> getFlashCards() {
        return flashCards;
    }

    public void setFlashCards(List<FlashCards> flashCards) {
        this.flashCards = flashCards;
    }

    public double getProgression() {
        return Progression;
    }

    public void setProgression(double progression) {
        Progression = progression;
    }
}