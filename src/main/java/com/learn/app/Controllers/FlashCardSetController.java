package com.learn.app;


import com.learn.app.Interfaces.AddFlashCardSetInterface;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class FlashCardSetController {
    @Autowired
    private final AddFlashCardSetInterface addFlashCardSetInterface;
    public FlashCardSetController(AddFlashCardSetInterface addFlashCardSetInterface) {
        this.addFlashCardSetInterface = addFlashCardSetInterface;
    }

    UserData user = new UserData();



    @GetMapping(value ="/AddFlashCardSet/{userID}")
    public String AddPage( HttpSession session, @PathVariable ("userID") Long userID, Model model)
        throws Exception{
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        model.addAttribute("user" , user);

        return("AddFlashCardSet");
   }
    @PostMapping(value ="/Add")
    public String AddCard(@ModelAttribute("flashCards") FlashCards flashCards,
                            Model model, HttpSession session,
                          @RequestParam("SetName") String SetName,
                          @RequestParam("Description") String Description) throws Exception {

        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        FlashCardSet flashCardSet1= new FlashCardSet( );
        flashCardSet1.setSetName(SetName);
        flashCardSet1.setSetDescription(Description);
        flashCardSet1.setUserID(LoggedUser.getUserID());
        addFlashCardSetInterface.save(flashCardSet1);
        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        model.addAttribute("user" , user);
        model.addAttribute("flashCardSets", addFlashCardSetInterface.findAll());
        // return "AddFlashCard";
        return "UserPanel";

    }



}
