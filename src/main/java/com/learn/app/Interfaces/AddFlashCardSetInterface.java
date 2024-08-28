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

    @Query("SELECT fcs2, COUNT(fct2.tags.TagID) AS tagCount " +
            "FROM FlashCardSet fcs1 " +
            "JOIN FlashCardTags fct1 ON fcs1.SetID = fct1.flashCardSet.SetID " +
            "JOIN FlashCardTags fct2 ON fct1.tags.TagID = fct2.tags.TagID " +
            "JOIN FlashCardSet fcs2 ON fct2.flashCardSet.SetID = fcs2.SetID " +
            "WHERE fcs1.UserID = :userID " +
            "AND fcs2.SetID NOT IN (" +
            "    SELECT fcs3.SetID " +
            "    FROM FlashCardSet fcs3 " +
            "    WHERE fcs3.UserID = :userID" +
            ") " +
            "GROUP BY fcs2 " +
            "ORDER BY tagCount DESC")
    List<FlashCardSet> getRecommendedSets(@Param("userID") Long userID);


    
}
