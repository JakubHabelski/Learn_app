package com.learn.app.Interfaces;

import com.learn.app.Classes.image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface upload_Image_Interface extends JpaRepository<image, Long>{
    @Query("SELECT i.path FROM image i WHERE i.FlashCardId = :FlashCardId")
    String findPathByFlashCardId(@Param("FlashCardId") Long FlashCardId);



}
