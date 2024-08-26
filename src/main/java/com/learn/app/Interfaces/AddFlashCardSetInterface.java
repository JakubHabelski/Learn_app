package com.learn.app.Interfaces;

import com.learn.app.Classes.FlashCardSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddFlashCardSetInterface extends JpaRepository<FlashCardSet,Long> {
    @Query("SELECT fs FROM FlashCardSet fs WHERE fs.UserID = :userID")
    List<FlashCardSet> findByUserID(@Param("userID") Long userID);

    @Query("SELECT fs FROM FlashCardSet fs WHERE fs.SetID= :SetID")
    FlashCardSet findBySetID(@Param("SetID") Long setID);
    @Query("SELECT DISTINCT t.tag, t.TagID " +
            "FROM FlashCardSet fcs " +
            "JOIN FlashCardTags fct ON fcs.SetID = fct.flashCardSet.SetID " +
            "JOIN Tags t ON fct.tags.TagID = t.TagID " +
            "WHERE fcs.SetID = :setID")
    List<Object[]> getTagsBySetID(@Param("setID") Long setID);

}
