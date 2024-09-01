package com.learn.app.Interfaces;


import com.learn.app.Classes.FlashCardSetRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashCardSetRatingInterface extends JpaRepository<FlashCardSetRating, Long> {

    @Query("SELECT AVG(f.rating) FROM FlashCardSetRating f WHERE f.flashCardSet.SetID = :setID")
    Double getAverageRating(Long setID);

    @Query("SELECT COUNT(f) FROM FlashCardSetRating f WHERE f.flashCardSet.SetID = :setID")
    Integer getRatingCount(Long setID);

    @Query("SELECT COUNT(f.rating) FROM FlashCardSetRating f WHERE f.rating = :rating and f.flashCardSet.SetID = :setID")
    Integer getRatingCountByRating(Integer rating, Long setID);
    @Query("SELECT f FROM FlashCardSetRating f WHERE f.userData.UserID = :userID AND f.flashCardSet.SetID = :setID")
    FlashCardSetRating getRatingByUserAndSet(Long userID, Long setID);

    @Query("select f from FlashCardSetRating f where f.flashCardSet.SetID = :setID")
    FlashCardSetRating findBySetID(Long setID);

    @Query("select f.comment, f.userData.UserName, f.rating, f.id from FlashCardSetRating f where f.flashCardSet.SetID = :setID")
    List<Object[]> getComments(Long setID);




}
