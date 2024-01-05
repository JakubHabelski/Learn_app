package com.learn.app.Controllers;


import com.learn.app.Classes.FlashCards;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import com.learn.app.Classes.UserData;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class EditSetController {


   @Autowired
    private final AddFlashCardInterface addFlashCardInterface;
    private final AddFlashCardSetInterface addFlashCardSetInterface;
    public EditSetController(AddFlashCardInterface addFlashCardInterface, AddFlashCardSetInterface addFlashCardSetInterface) {
        this.addFlashCardInterface = addFlashCardInterface;
        this.addFlashCardSetInterface = addFlashCardSetInterface;
    }

    UserData user = new UserData();



    @GetMapping (value = "/EditFlashCardSet/{SetID}")
    public String EditFlashCardSet_GET(@PathVariable Long SetID,
                                       Model model,
                                       FlashCards flashCards,
                                       HttpSession session) {
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        flashCards.setSetID(SetID);
        model.addAttribute("user", user);
        model.addAttribute("flashCard", addFlashCardInterface.customFindBySetID(SetID));
        ArrayList slidecard = new ArrayList<FlashCards>();
        slidecard.addAll(addFlashCardInterface.customFindBySetID(SetID));
        model.addAttribute("slidecard", slidecard);
       // FlashCards[] slideCards = new FlashCards[1000];
       // slideCards =addFlashCardInterface.customFindBySetID(SetID);
       // model.addAttribute("slidecard", )
        return  ("EditFlashCardSet");

    }
    @PostMapping(value = "/AddCard")
    public String EditFlashCardSet_POST(@RequestParam Long SetID,
                                        @RequestParam String Definition,
                                        @RequestParam String Description,
                                        HttpSession session,
                                        Model model){
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        model.addAttribute("user" , user);
        model.addAttribute("flashCardSets", addFlashCardSetInterface.findByUserID(user.getUserID()));

        FlashCards flashCard = new FlashCards();
        flashCard.setSetID(SetID);
        flashCard.setDefinition(Definition);
        flashCard.setDescription(Description);
        addFlashCardInterface.save(flashCard);
        //return "UserPanel";
        return "redirect:/EditFlashCardSet/" + SetID;
    }
    
    @RequestMapping("/deleteCard")
    public String deleteCard(@RequestParam String FlashCardId,
                             @RequestParam String SetID){
        addFlashCardInterface.deleteById(Long.valueOf(FlashCardId));
        return "redirect:/EditFlashCardSet/"  + SetID;
    }

    @PostMapping("/EditCard")
    public String EditCard(@RequestParam ("FlashCardId") Long FlashCardId,
                           @RequestParam ("SetID") Long SetID,
                           @RequestParam ("Definition") String Definition,
                           @RequestParam ("Description") String Description

    ){
        FlashCards flashCard = new FlashCards();
        flashCard.setFlashCardId(FlashCardId);
       //
       
       flashCard.setSetID(SetID);
        flashCard.setDefinition(Definition);
        flashCard.setDescription(Description);
        addFlashCardInterface.save(flashCard);
        //return "UserPanel";
        return "redirect:/EditFlashCardSet/" + SetID;
    }

    
    @PostMapping("/Return_UserPanel")
    public String Return_UserPanel( HttpSession session,
                                    Model model
                                    ){
        
        model.addAttribute("user" , user);
       
        return "redirect:/userpanel";
    }
        
}
