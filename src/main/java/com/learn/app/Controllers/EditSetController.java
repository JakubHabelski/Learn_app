package com.learn.app;


import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    FlashCards flashCard = new FlashCards();



    @GetMapping (value = "/EditFlashCardSet/{SetID}")
    public String EditFlashCardSet_GET(@PathVariable ("SetID") Long SetID, Model model, FlashCards flashCards) {
        flashCards.setSetID(SetID);
        model.addAttribute("flashCards", flashCards);
        return  ("EditFlashCardSet");

    }
    @PostMapping(value = "/AddCard")
    public String EditFlashCardSet_POST(@RequestParam ("SetID") Long SetID,
                                        @RequestParam ("Definition") String Definition,
                                        @RequestParam ("Description") String Description,
                                        HttpSession session,
                                        Model model){
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        model.addAttribute("user" , user);
        model.addAttribute("flashCardSets", addFlashCardSetInterface.findAll());

        flashCard.setSetID(SetID);
        flashCard.setDefinition(Definition);
        flashCard.setDescription(Description);
        addFlashCardInterface.save(flashCard);
        return "UserPanel";
    }
}
