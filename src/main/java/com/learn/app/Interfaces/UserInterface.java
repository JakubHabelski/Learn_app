package com.learn.app.Interfaces;

import com.learn.app.Classes.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInterface extends JpaRepository<UserData, Long> {
    @Query("SELECT u FROM UserData u WHERE u.UserLogin = :userLogin")
    UserData findByUserLogin(@Param("userLogin") String userLogin);

}
