package com.learn.app;


import com.learn.app.Classes.FlashCardSet;
import com.learn.app.Classes.FlashCards;
import com.learn.app.Classes.UserData;
import com.learn.app.Config.MyMailSenderService;
import com.learn.app.Controllers.EditSetController;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import com.learn.app.Interfaces.TagsInterface;
import com.learn.app.Interfaces.UserInterface;
import com.learn.app.Services.ImageUploadService;
import com.learn.app.Services.TestDatabaseService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EditSetControllerTest {

    @InjectMocks
    private EditSetController editSetController;

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

    @Mock
    private TagsInterface tagsInterface;








@Test
public void EditFlashCardSet_GET_FromEditTrue() throws Exception {
    Long setID = 1L;
    UserData loggedUser = new UserData();
    loggedUser.setUserID(123L);
    when(session.getAttribute("LoggedUser")).thenReturn(loggedUser);
    when(addFlashCardSetInterface.findBySetID(setID)).thenReturn(new FlashCardSet());
    when(addFlashCardInterface.find_learnedFlashCards(setID, true)).thenReturn(new ArrayList<>());
    when(addFlashCardInterface.customFindBySetID(setID)).thenReturn(new ArrayList<>());

    editSetController.EditFlashCardSet_GET(setID, true, model, new FlashCards(), session);

    verify(model).addAttribute("fromEdit", true);
}

@Test
public void EditFlashCardSet_GET_FromEditFalse() throws Exception {
    Long setID = 1L;
    UserData loggedUser = new UserData();
    loggedUser.setUserID(123L);
    when(session.getAttribute("LoggedUser")).thenReturn(loggedUser);
    when(addFlashCardSetInterface.findBySetID(setID)).thenReturn(new FlashCardSet());
    when(addFlashCardInterface.find_learnedFlashCards(setID, true)).thenReturn(new ArrayList<>());
    when(addFlashCardInterface.customFindBySetID(setID)).thenReturn(new ArrayList<>());

    editSetController.EditFlashCardSet_GET(setID, false, model, new FlashCards(), session);

    verify(model).addAttribute("fromEdit", false);
}

// EditSetControllerTest.java
@Test
public void EditFlashCardSet_GET_HandlesNullLoggedUser() throws Exception {
    Long setID = 1L;
    UserData loggedUser = new UserData();
    loggedUser.setUserID(123L);
    when(session.getAttribute("LoggedUser")).thenReturn(null);
    when(addFlashCardSetInterface.findBySetID(setID)).thenReturn(new FlashCardSet());
    when(addFlashCardInterface.find_learnedFlashCards(setID, true)).thenReturn(new ArrayList<>());
    when(addFlashCardInterface.customFindBySetID(setID)).thenReturn(new ArrayList<>());

    String viewName = editSetController.EditFlashCardSet_GET(setID, false, model, new FlashCards(), session);

    assertEquals("redirect:/loginform", viewName);
}




}
