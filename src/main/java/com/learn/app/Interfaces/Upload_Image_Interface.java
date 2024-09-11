package com.learn.app.Interfaces;

import com.learn.app.Classes.image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Upload_Image_Interface extends JpaRepository<image, Long>{
    @Query("SELECT i.path FROM image i WHERE i.FlashCardId = :FlashCardId")
    String findPathByFlashCardId(@Param("FlashCardId") Long FlashCardId);
    @Query("SELECT i.id FROM image i WHERE i.userID = :userID")
    String findIdByUserID(@Param("userID") Long userID);
    @Query("SELECT i.path FROM image i WHERE i.userID = :userID")
    String findPathByUserID(@Param("userID") Long userID);
    @Query("SELECT i.id FROM image i WHERE i.FlashCardId = :FlashCardId")
    Long findIdByFlashCardId(@Param("FlashCardId") Long FlashCardId);
}
