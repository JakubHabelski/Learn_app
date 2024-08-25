package com.learn.app.Classes;


import jakarta.persistence.*;

@Entity
public class FlashCardTags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SetID")
    private FlashCardSet flashCardSet;

    @ManyToOne
    @JoinColumn(name = "TagID")
    private Tags tags;
}
