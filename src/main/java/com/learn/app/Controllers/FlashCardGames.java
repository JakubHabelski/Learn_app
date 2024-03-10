package com.learn.app.Controllers;

import com.learn.app.Classes.FlashCardSet;
import com.learn.app.Classes.FlashCards;
import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


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

        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        user.setUserName(LoggedUser.getUserName());
        user.setUserSurname(LoggedUser.getUserSurname());
        ArrayList slidecard = new ArrayList<FlashCards>();
        slidecard.addAll(addFlashCardInterface.customFindBySetID(SetID));
        model.addAttribute("slidecard", slidecard);
        model.addAttribute("user", user);
        model.addAttribute("flashCard", addFlashCardInterface.customFindBySetID(SetID));
        return "QuizGame";
    }

    @GetMapping("/MatchingGame")
    public String MatchingGame(HttpSession session,
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
            return "MatchingGame";
     }

    @RequestMapping(value = "redirect_EditFlashCardSet")
    public String redirect_EditFlashCardSet(HttpSession session,
                                            Model model){
        Long SetID = (Long) session.getAttribute("SetID");
        return "redirect:/EditFlashCardSet/" + SetID;       
    }
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void test(HttpSession session,
                     @RequestParam ("flashCardId") Long flashCardId,
                     @RequestParam ("learned") boolean learned
                     ) {
        Long SetID = (Long) session.getAttribute("SetID");
       FlashCards flashCard = addFlashCardInterface.customFindByID(Long.valueOf(flashCardId));
      //  flashCard.setDefinition(flashCard.getDefinition());
        FlashCardSet flashCardSet = addFlashCardSetInterface.findBySetID(SetID);

        Integer count_learned = Math.toIntExact(addFlashCardInterface.find_learnedFlashCards(SetID, true).stream().count());
        Integer count_set_size = Math.toIntExact(addFlashCardInterface.customFindBySetID(SetID).stream().count());
        double progres = (double) count_learned /count_set_size;
        flashCardSet.setProgression(progres);
        flashCard.setLearned(learned);
        addFlashCardSetInterface.save(flashCardSet);
        addFlashCardInterface.save(flashCard);

    }
    
}
