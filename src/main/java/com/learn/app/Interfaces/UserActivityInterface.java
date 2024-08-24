package com.learn.app.Interfaces;


import com.learn.app.Classes.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityInterface extends JpaRepository<UserActivity, Long> {

}
