package com.learn.app;

import com.learn.app.Classes.UserData;
import com.learn.app.Config.MyMailSenderService;
import com.learn.app.Controllers.RegisterController;
import com.learn.app.Interfaces.UserInterface;
import com.learn.app.Services.ImageUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterControllerTest {

    private UserInterface userInterface;
    private PasswordEncoder passwordEncoder;
    private MyMailSenderService myMailSenderService;
    private ImageUploadService imageUploadService;
    private RegisterController registerController;

    @BeforeEach
    void setUp() {
        userInterface = mock(UserInterface.class);
        passwordEncoder = mock(PasswordEncoder.class);
        myMailSenderService = mock(MyMailSenderService.class);
        imageUploadService = mock(ImageUploadService.class);
        registerController = new RegisterController(userInterface, passwordEncoder);
        registerController.myMailSenderService = myMailSenderService;
        registerController.imageUploadService = imageUploadService;
    }

    @Test
    void testGetRegister_ReturnsRegisterForm() {
        String result = registerController.GetRegister();
        assertEquals("RegisterForm", result);
    }

    @Test
    void testPostRegister_UserAlreadyExists() throws Exception {
        // Arrange
        String userLogin = "testUser";
        when(userInterface.findByUserLogin(userLogin)).thenReturn(new UserData());
        Model model = mock(Model.class);

        // Act
        String result = registerController.PostRegister("John", "Doe", userLogin,
                "password", "john.doe@example.com", mock(MultipartFile.class), model);

        // Assert
        assertEquals("redirect:/GetRegister", result);
        verify(model).addAttribute("login_error", "login_error");
    }

    @Test
    void testPostRegister_NewUserRegistration() throws Exception {
        // Arrange
        String userLogin = "newUser";
        when(userInterface.findByUserLogin(userLogin)).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Model model = mock(Model.class);
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        // Act
        String result = registerController.PostRegister("Jane", "Smith", userLogin, "password", "jane.smith@example.com", file, model);

        // Assert
        assertEquals("redirect:/loginform?fromRegister", result);
        ArgumentCaptor<UserData> userCaptor = ArgumentCaptor.forClass(UserData.class);
        verify(userInterface).save(userCaptor.capture());
        assertEquals("Jane", userCaptor.getValue().getUserName());
        assertEquals("Smith", userCaptor.getValue().getUserSurname());
        assertEquals("encodedPassword", userCaptor.getValue().getUserPass());
        assertFalse(userCaptor.getValue().getUserActive());
        verify(myMailSenderService).sendEmail(eq("jane.smith@example.com"), eq("Potwierdzenie rejestracji"), anyString());
    }

    @Test
    void testRegisterSuccess_ActivatesUser() {
        // Arrange
        String userToken = "user-token";
        UserData user = new UserData();
        when(userInterface.findByUserToken(userToken)).thenReturn(user);

        // Act
        String result = registerController.register_success(userToken);

        // Assert
        assertEquals("redirect:/loginform", result);
        assertTrue(user.getUserActive());
        verify(userInterface).save(user);
    }

    @Test
    void testCheckLogin_ReturnsTrueIfLoginExists() {
        // Given
        String userLogin = "existingUser";
        when(userInterface.findByUserLogin(userLogin)).thenReturn(new UserData());

        // When
        ResponseEntity<Boolean> response = registerController.checkLogin(userLogin);

        // Then
        assertTrue(response.getBody());
    }

    @Test
    void testCheckMail_ReturnsTrueIfMailExists() {
        // Given
        String userMail = "existing@example.com";
        when(userInterface.findByUserMail(userMail)).thenReturn(new UserData());

        // When
        ResponseEntity<Boolean> response = registerController.checkMail(userMail);

        // Then
        assertTrue(response.getBody());
    }
}
