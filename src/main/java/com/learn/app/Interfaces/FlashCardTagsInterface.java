package com.learn.app.Interfaces;

import com.learn.app.Classes.FlashCardTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashCardTagsInterface extends JpaRepository<FlashCardTags, Long> {
}
