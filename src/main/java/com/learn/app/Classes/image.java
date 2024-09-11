package com.learn.app.Classes;

import jakarta.persistence.*;

@Entity
public class image {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String path;
    
    private Long userID;
    private Long FlashCardId;
    @Column(name = "id_of_flashCard")
    private Long id_of_flashCard;

    @OneToOne
    @JoinColumn(name="userID" , insertable=false, updatable=false)
    private UserData userData;

    @OneToOne
    @JoinColumn(name = "flashCardId", insertable=false, updatable=false)
    private FlashCards flashCard;

    public Long getUserID() {
        return userID;
    }
    public void setUserID(Long userID) {
        this.userID = userID;
    }
    public image() {
    }

    public Long getFlashCardId() {
        return FlashCardId;
    }

    public void setFlashCardId(Long flashCardId) {
        FlashCardId = flashCardId;
    }

    public image(String path, Long userID, Long FlashCardId) {
        this.path = path;
        this.userID = userID;
        this.FlashCardId = FlashCardId;
    }

    public image(Long userID, String path, Long id_of_flashCard) {
        this.userID = userID;
        this.path = path;
        this.id_of_flashCard = id_of_flashCard;
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

    public Long getId_of_flashCard() {
        return id_of_flashCard;
    }

    public void setId_of_flashCard(Long id_of_flashCard) {
        this.id_of_flashCard = id_of_flashCard;
    }
}
