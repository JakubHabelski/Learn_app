package com.learn.app;

import com.learn.app.Classes.UserData;
import com.learn.app.Config.MyMailSenderService;
import com.learn.app.Controllers.EditUserProfileController;
import com.learn.app.Controllers.LoginController;
import com.learn.app.Controllers.TestImageUpload;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import com.learn.app.Interfaces.UserInterface;
import com.learn.app.Services.ImageUploadService;
import com.learn.app.Services.TestDatabaseService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EditUserProfileControllerTest {

    @InjectMocks
    private EditUserProfileController editUserProfileController;

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
        // Ensure TestImageUpload is properly mocked
        mockStatic(TestImageUpload.class);
    }




    @Test
    public void UserProfile_SetsCorrectAttributes_WhenLoggedUserHasPath() throws Exception {
        UserData loggedUser = new UserData();
        loggedUser.setPath("some/path");
        when(session.getAttribute("LoggedUser")).thenReturn(loggedUser);
        when(TestImageUpload.getImageUrl2(loggedUser.getPath())).thenReturn("displayUrl");

        editUserProfileController.UserProfile(model, session);

        verify(model).addAttribute("image", new File("displayUrl"));
        verify(model).addAttribute("user", loggedUser);
    }





}
