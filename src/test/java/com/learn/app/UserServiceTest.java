package com.learn.app;

import com.learn.app.Classes.UserData;
import com.learn.app.Config.UserService;
import com.learn.app.Interfaces.UserInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserInterface userInterface;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        UserData user = new UserData();
        user.setUserLogin("testUser");
        user.setUserPass("testPass");
        when(userInterface.findByUserLogin("testUser")).thenReturn(user);

        // Act
        UserDetails userDetails = userService.loadUserByUsername("testUser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPass", userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(userInterface.findByUserLogin("nonExistentUser")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonExistentUser"));
    }
}
