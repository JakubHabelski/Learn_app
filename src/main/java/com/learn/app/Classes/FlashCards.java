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

    @ManyToOne
    @JoinColumn(name = "SetID", insertable=false, updatable=false)
    private FlashCardSet flashCardSet;

    public FlashCards(Long flashCardId, Long setID, String definition, String description) {
        FlashCardId = flashCardId;
        SetID = setID;
        Definition = definition;
        Description = description;
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
}





