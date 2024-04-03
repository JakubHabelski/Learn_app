package com.learn.app.Config;

import com.learn.app.Classes.UserData;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class CustomUserDetails implements UserDetails {

    private UserData user;

    public CustomUserDetails(UserData user) {
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }
    @Override
    public String getPassword() {
        return user.getUserPass();
    }

    @Override
    public String getUsername() {
        return user.getUserLogin();
    }


    public Long getUserId() {
        return user.getUserID();
    }
    public String getUserFirstName() {
        return user.getUserName();
    }
    public String getUserLastName() {
        return user.getUserSurname();
    }
    public String getUserEmail() {
        return user.getUserMail();
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getUserID() {
        return user.getUserID();
    }
}
