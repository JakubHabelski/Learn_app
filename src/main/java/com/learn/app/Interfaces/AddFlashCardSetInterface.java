package com.learn.app.Interfaces;

import com.learn.app.Classes.FlashCardSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddFlashCardSetInterface extends JpaRepository<FlashCardSet,Long> {
    @Query("SELECT fs FROM FlashCardSet fs WHERE fs.UserID = :userID")
    List<FlashCardSet> findByUserID(@Param("userID") Long userID);


}
