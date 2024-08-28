package com.learn.app.Interfaces;

import com.learn.app.Classes.FlashCardTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface FlashCardTagsInterface extends JpaRepository<FlashCardTags, Long> {
    @Modifying
    @Transactional
    @Query("delete from FlashCardTags fct where fct.flashCardSet.SetID = :setID")
    void deleteBySetID(Long setID);
}
