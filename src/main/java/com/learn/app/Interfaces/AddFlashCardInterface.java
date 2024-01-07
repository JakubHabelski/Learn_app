package com.learn.app;

import com.learn.app.FlashCards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddFlashCardInterface extends JpaRepository<FlashCards, Long> {
}
