package com.learn.app.Interfaces;

import com.learn.app.Classes.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterface extends JpaRepository<UserData, Long> {
    @Query("SELECT u FROM UserData u WHERE u.UserLogin = :userLogin")
    UserData findByUserLogin(@Param("userLogin") String userLogin);
    @Query("SELECT U FROM UserData U WHERE U.UserID = :UserID")
    UserData findByUserID(@Param("UserID") Long UserID);
    @Query("SELECT U FROM UserData U WHERE U.UserToken = :UserToken")
    UserData findByUserToken(@Param("UserToken") String UserToken);
}
