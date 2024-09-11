package com.learn.app;

import com.detectlanguage.errors.APIError;
import com.learn.app.Classes.UserData;
import com.learn.app.Config.MyMailSenderService;
import com.learn.app.Controllers.LoginController;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import com.learn.app.Interfaces.UserInterface;
import com.learn.app.Services.TestDatabaseService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private AddFlashCardSetInterface addFlashCardSetInterface;

    @Mock
    private AddFlashCardInterface addFlashCardInterface;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TestDatabaseService testDatabaseService;

    @Mock
    private UserInterface userInterface;

    @Mock
    private MyMailSenderService myMailSenderService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginform() throws APIError {
        // Arrange: przygotowanie środowiska testowego
        when(session.getAttribute("login_error")).thenReturn("login_error");

        // Act: wywołanie metody testowanej
        String viewName = loginController.loginform(model, session);

        // Assert: sprawdzenie wyników
        ArgumentCaptor<UserData> userDataCaptor = ArgumentCaptor.forClass(UserData.class);
        verify(model).addAttribute(eq("user"), userDataCaptor.capture());

        UserData capturedUser = userDataCaptor.getValue();
        assertEquals("", capturedUser.getUserLogin());
        assertEquals("", capturedUser.getUserPass());

        verify(model).addAttribute("login_error", "login_error");

        assertEquals("LoginForm", viewName);
    }



    @Test
    public void testLoginformError() {
        // Wywołanie metody testowanej
        String viewName = loginController.loginformError(model, session);

        // Użycie ArgumentCaptor do przechwycenia wartości dodanej do modelu
        ArgumentCaptor<UserData> userDataCaptor = ArgumentCaptor.forClass(UserData.class);
        verify(model).addAttribute(eq("user"), userDataCaptor.capture());

        // Pobranie przechwyconej wartości i sprawdzenie jej pól
        UserData capturedUser = userDataCaptor.getValue();
        assertEquals("", capturedUser.getUserLogin());
        assertEquals("", capturedUser.getUserPass());

        // Sprawdzenie, czy login_error został ustawiony w sesji
        verify(session).setAttribute("login_error", "login_error");

        // Sprawdzenie zwróconej nazwy widoku
        assertEquals("redirect:/loginform", viewName);
    }




    @Test
    public void testUserpanelNotAuthenticated() {
        UserData user = new UserData();
        user.setUserID(1L);
        user.setUserActive(true);

        when(session.getAttribute("LoggedUser")).thenReturn(user);
        when(addFlashCardSetInterface.findByUserID(user.getUserID())).thenReturn(new ArrayList<>());

        String viewName = loginController.userpanel(model, authentication, session);

        verify(model).addAttribute("user", user);
        verify(model).addAttribute("flashCardSets", new ArrayList<>());
        assertEquals("UserPanel", viewName);
    }

    @Test
    public void testLogout() {
        String viewName = loginController.Logout(session);

        verify(session).invalidate();
        assertEquals("redirect:/loginform", viewName);
    }

    @Test
    public void testForgotPass() {
        String viewName = loginController.ForgotPass();
        assertEquals("ForgotPass", viewName);
    }

    @Test
    public void testResetPass() {
        UserData user = new UserData();
        user.setUserMail("user@example.com");
        user.setUserLogin("exampleUser"); // Ustaw poprawną wartość loginu

        when(userInterface.findByUserLogin("user@example.com")).thenReturn(user);
        when(userInterface.findByUserMail("user@example.com")).thenReturn(user);

        String viewName = loginController.ResetPass("user@example.com");

        verify(myMailSenderService).sendEmail(eq("user@example.com"), eq("Reset Password"),
                eq("Click this link to reset your password: https://project-jh-425111.ew.r.appspot.com/reset_pass/exampleUser")); // Upewnij się, że login jest poprawny
        assertEquals("redirect:/loginform", viewName);
    }

    @Test
    public void testResetPassUserNotFound() {
        when(userInterface.findByUserLogin("user@example.com")).thenReturn(null);
        when(userInterface.findByUserMail("user@example.com")).thenReturn(null);

        String viewName = loginController.ResetPass("user@example.com");
        assertEquals("redirect:/loginform", viewName);
    }

    @Test
    public void testResetPassWithInvalidUser() {
        UserData user = new UserData();
        user.setUserMail("user@example.com");
        user.setUserLogin("exampleUser"); // Ustaw prawidłowy login użytkownika

        // Symulacja braku użytkownika na podstawie loginu, ale istnieje na podstawie e-maila
        when(userInterface.findByUserLogin("user@example.com")).thenReturn(null);
        when(userInterface.findByUserMail("user@example.com")).thenReturn(user);

        // Wywołanie metody
        String viewName = loginController.ResetPass("user@example.com");

        // Sprawdzenie, czy e-mail został wysłany na poprawny adres z poprawnym linkiem
        verify(myMailSenderService).sendEmail(
                eq(user.getUserMail()),
                eq("Reset Password"),
                eq("Click this link to reset your password: https://project-jh-425111.ew.r.appspot.com/reset_pass/exampleUser")
        );

        // Sprawdzenie przekierowania
        assertEquals("redirect:/loginform", viewName);
    }


    @Test
    public void testResetPassWhenUserIsNull() {
        when(userInterface.findByUserLogin("user@example.com")).thenReturn(null);
        when(userInterface.findByUserMail("user@example.com")).thenReturn(null);

        String viewName = loginController.ResetPass("user@example.com");

        assertEquals("redirect:/loginform", viewName);
    }

    @Test
    public void testResetPassWithInvalidEmail() {
        String viewName = loginController.ResetPass("invalid@example.com");
        assertEquals("redirect:/loginform", viewName);
    }

    @Test
    public void testResetPassWithNoUser() {
        when(userInterface.findByUserLogin("invalidUser")).thenReturn(null);
        when(userInterface.findByUserMail("invalidUser")).thenReturn(null);

        String viewName = loginController.ResetPass("invalidUser");

        assertEquals("redirect:/loginform", viewName);
    }

    @Test
    public void testChangePass() {
        UserData user = new UserData();
        user.setUserLogin("user");
        when(userInterface.findByUserLogin("user")).thenReturn(user);

        String viewName = loginController.ChangePass("newPass", "user");

        verify(userInterface).save(user);
        assertEquals("redirect:/loginform", viewName);
    }

    @Test
    public void testChangePassWithNullValues() {
        String viewName = loginController.ChangePass(null, null);
        assertEquals("Error", viewName);
    }


}