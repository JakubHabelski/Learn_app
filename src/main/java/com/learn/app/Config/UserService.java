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
        return new org.springframework.security.core.userdetails.User(user.getUserLogin(), passwordEncoder().encode(user.getUserPass()), new ArrayList<>());
    }
}
