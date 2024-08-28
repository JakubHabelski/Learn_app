package com.learn.app.Classes;


import jakarta.persistence.*;

@Entity
public class FlashCardTags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SetID", referencedColumnName = "setid")
    private FlashCardSet flashCardSet;

    @ManyToOne
    @JoinColumn(name = "TagID", referencedColumnName = "tagid")
    private Tags tags;

    public FlashCardTags() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlashCardSet getFlashCardSet() {
        return flashCardSet;
    }

    public void setFlashCardSet(FlashCardSet flashCardSet) {
        this.flashCardSet = flashCardSet;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }
}
