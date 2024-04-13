package com.learn.app.Config;

import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserInterface userInterface;
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    };
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = userInterface.findByUserLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found"+ username);
        }

        List<Long> authorities = new ArrayList<>();
        authorities.add(user.getUserID());
        return new org.springframework.security.core.userdetails.User(user.getUserLogin(), user.getUserPass(),  new ArrayList<>());
       // return new CustomUserDetails(user);
    }
}
