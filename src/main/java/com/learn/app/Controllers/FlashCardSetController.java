package com.learn.app.Controllers;


import com.learn.app.Classes.FlashCardSet;
import com.learn.app.Classes.FlashCards;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import com.learn.app.Classes.UserData;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
public class FlashCardSetController {
    @Autowired
    private final AddFlashCardInterface addFlashCardInterface;
    private final AddFlashCardSetInterface addFlashCardSetInterface;
    public FlashCardSetController(AddFlashCardInterface addFlashCardInterface, AddFlashCardSetInterface addFlashCardSetInterface) {
        this.addFlashCardInterface = addFlashCardInterface;
        this.addFlashCardSetInterface = addFlashCardSetInterface;
    }

    UserData user = new UserData();



    @GetMapping(value ="/AddFlashCardSet/{userID}")
    public String AddPage( HttpSession session, @PathVariable Long userID, Model model)
        throws Exception{
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        user.setUserName(LoggedUser.getUserName());
        user.setUserSurname(LoggedUser.getUserSurname());
        model.addAttribute("user" , user);

        return("AddFlashCardSet");
   }
    @PostMapping(value ="/Add")
    public String AddCard(@ModelAttribute FlashCards flashCards,
                            Model model, HttpSession session,
                          @RequestParam String SetName,
                          @RequestParam String Description) throws Exception {

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
        model.addAttribute("flashCardSets", addFlashCardSetInterface.findByUserID(LoggedUser.getUserID()));
        // return "AddFlashCard";

            return "redirect:/userpanel";
       // return "redirect:/AddFlashCardSet/{userID}";

    }

    @RequestMapping(value="/DeletFlashCardSet", method = { RequestMethod.GET, RequestMethod.POST } )
    public String deleteFlashCardSet(Model model, 
                                     HttpSession session,
                                     @RequestParam("SetID") Long SetID) {
        UserData loggedUser = (UserData) session.getAttribute("LoggedUser");
        
            // Tutaj dodaj logikę usuwania zestawu kart
            addFlashCardSetInterface.deleteById(SetID);

            // Aktualizacja danych do wyświetlenia w widoku UserPanel
            model.addAttribute("user", loggedUser);
            model.addAttribute("flashCardSets", addFlashCardSetInterface.findByUserID(loggedUser.getUserID()));
                                        
            return "redirect:/userpanel";
         
    }
    
    

}
