package com.learn.app.Interfaces;

import com.learn.app.Classes.FlashCards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddFlashCardInterface extends JpaRepository<FlashCards, Long> {
    @Query("SELECT f FROM FlashCards f WHERE f.SetID = :setID")
    List<FlashCards> customFindBySetID(@Param("setID") Long setID);
    @Modifying
    @Query("DELETE FROM FlashCards f WHERE f.SetID = :SetID")
    void deleteCardBySetID(@Param("SetID") Long setID);

    @Query("SELECT f.Definition, f.Description, i.path FROM FlashCards f JOIN image i ON f.FlashCardId = i.FlashCardId")
    List<Object[]> findDefinitionDescriptionAndImagePath();

    @Query("SELECT f FROM FlashCards f WHERE f.FlashCardId = :flashCardId")
    FlashCards customFindByID(@Param("flashCardId") Long flashCardId);

    @Query("SELECT f FROM FlashCards f WHERE f.SetID = :setID AND f.Learned = :learned")
    List<FlashCards> find_learnedFlashCards(@Param("setID") Long setID, @Param("learned") boolean learned);

}


