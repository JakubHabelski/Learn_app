package com.learn.app.Interfaces;

import com.learn.app.Classes.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsInterface extends JpaRepository<Tags, Long> {
}
