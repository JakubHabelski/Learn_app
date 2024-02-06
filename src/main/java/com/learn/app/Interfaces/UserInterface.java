package com.learn.app.Interfaces;

import com.learn.app.Classes.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserInterface extends JpaRepository<UserData, Long> {
}
