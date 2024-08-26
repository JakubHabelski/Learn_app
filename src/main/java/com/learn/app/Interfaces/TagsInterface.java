package com.learn.app.Interfaces;

import com.learn.app.Classes.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsInterface extends JpaRepository<Tags, Long> {

    @Query("SELECT t.tag from Tags t")
    List<String> getAllTags();

    @Query("SELECT t from Tags t where t.tag =:tag")
    Tags findByTag(@Param("tag") String tag);


}
