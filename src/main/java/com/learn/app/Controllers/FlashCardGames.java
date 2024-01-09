package com.learn.app.Controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.learn.app.Classes.FlashCards;
import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;

import jakarta.servlet.http.HttpSession;


@Controller
public class FlashCardGames {
    @Autowired
    private final AddFlashCardInterface addFlashCardInterface;
    private final AddFlashCardSetInterface addFlashCardSetInterface;
    public FlashCardGames(AddFlashCardInterface addFlashCardInterface, AddFlashCardSetInterface addFlashCardSetInterface) {
        this.addFlashCardInterface = addFlashCardInterface;
        this.addFlashCardSetInterface = addFlashCardSetInterface;
    }

    UserData user = new UserData();

    @GetMapping("/FlashCardGame")
    public String FlashCardGame_GET(HttpSession session,
                                        Model model) {
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        FlashCards flashCard = new FlashCards();
        Long SetID = (Long) session.getAttribute("SetID");
        model.addAttribute("user", user);
        model.addAttribute("flashCard", addFlashCardInterface.customFindBySetID(SetID));
        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        ArrayList slidecard = new ArrayList<FlashCards>();
        slidecard.addAll(addFlashCardInterface.customFindBySetID(SetID));
        model.addAttribute("slidecard", slidecard);                                    
                                           
        return "FlashCardGame";
    }    
    @GetMapping("/QuizGame")
    public String QuizGame(HttpSession session,
                            Model model){
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        FlashCards flashCard = new FlashCards();
        Long SetID = (Long) session.getAttribute("SetID");
        model.addAttribute("user", user);
        model.addAttribute("flashCard", addFlashCardInterface.customFindBySetID(SetID));
        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        ArrayList slidecard = new ArrayList<FlashCards>();
        slidecard.addAll(addFlashCardInterface.customFindBySetID(SetID));
        model.addAttribute("slidecard", slidecard);                        
        return "QuizGame";
    }

    
}