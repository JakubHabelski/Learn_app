package com.learn.app.Classes;


import jakarta.persistence.*;

import java.util.Date;


@Entity
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userID;

    @Column(name = "flashcard_set_id", nullable = false)
    private Long flashCardSetID;

    @Column(nullable = false)
    private String actionType; // np. "viewed", "edited", "rated"

    @Column(nullable = false)
    private Date timestamp;

    // Dodatkowe informacje, jeśli potrzebne
    private String additionalInfo;

    public UserActivity() {
    }
    public UserActivity(Long userID, Long flashCardSetID, String actionType, Date timestamp) {
        this.userID = userID;
        this.flashCardSetID = flashCardSetID;
        this.actionType = actionType;
        this.timestamp = timestamp;
    }



}
