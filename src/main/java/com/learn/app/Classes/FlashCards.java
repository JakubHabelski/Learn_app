package com.learn.app.Classes;

import jakarta.persistence.*;


@Entity
@Table(name="FlashCards")
public class FlashCards {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long FlashCardId;
    private Long SetID;
    private String Definition;
    private String Description;

    private String path;
    @Column(columnDefinition = "boolean default false") // Ustawienie domyślnej wartości na false
    private boolean Learned;

    @ManyToOne
    @JoinColumn(name = "SetID", insertable=false, updatable=false)
    private FlashCardSet flashCardSet;

    @OneToOne(mappedBy = "flashCard", cascade = CascadeType.ALL)
    private image image;


    public FlashCards(Long flashCardId, Long setID, String definition, String description, boolean learned) {
        FlashCardId = flashCardId;
        SetID = setID;
        Definition = definition;
        Description = description;
        Learned = learned;
    }


    public FlashCards() {
    }

    public Long getFlashCardId() {
        return FlashCardId;
    }

    public void setFlashCardId(Long flashCardId) {
        FlashCardId = flashCardId;
    }

    public Long getSetID() {
        return SetID;
    }

    public void setSetID(Long setID) {
        SetID = setID;
    }

    public String getDefinition() {
        return Definition;
    }

    public void setDefinition(String definition) {
        Definition = definition;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getLearned() {
        return Learned;
    }

    public void setLearned(boolean learned) {
        Learned = learned;
    }
}





