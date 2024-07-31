package com.learn.app.Controllers;

import com.learn.app.Classes.FlashCardSet;
import com.learn.app.Classes.FlashCards;
import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;


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
        FlashCardSet flashCardSet = addFlashCardSetInterface.findBySetID(SetID);
        model.addAttribute("user", user);
        model.addAttribute("flashCard", addFlashCardInterface.customFindBySetID(SetID));
        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        user.setUserName(LoggedUser.getUserName());
        user.setUserSurname(LoggedUser.getUserSurname());
        ArrayList slidecard = new ArrayList<FlashCards>();
        slidecard.addAll(addFlashCardInterface.customFindBySetID(SetID));
        Integer Learned = addFlashCardInterface.find_learnedFlashCards(SetID, true).size();
        Integer NotLearned = addFlashCardInterface.find_learnedFlashCards(SetID, false).size();
       // slidecard.addAll(addFlashCardInterface.customFindBySetIDAndTimeOut(SetID, 1));
        Date currentDate = java.sql.Date.valueOf(LocalDate.now());
        Iterator<FlashCards> iterator = slidecard.iterator();
        while (iterator.hasNext()) {
            flashCard = iterator.next();
            if ((flashCard.getLast_user_grade() == 4 && flashCard.getNext_rep().after(currentDate)) || (flashCard.getLast_user_grade() == 5 && flashCard.getNext_rep().after(currentDate))) {
                iterator.remove();
            }
        }

        model.addAttribute("slidecard", slidecard);
        model.addAttribute("user", user);
        model.addAttribute("slidecard", slidecard);
        model.addAttribute("Learned", Learned);
        model.addAttribute("NotLearned", NotLearned);
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

        //supermemo-----------------------------------




        //------------------------------------------------------

        Integer count_learned = Math.toIntExact(addFlashCardInterface.find_learnedFlashCards(SetID, true).stream().count());
        Integer count_set_size = Math.toIntExact(addFlashCardInterface.customFindBySetID(SetID).stream().count());
        double progres = (double) count_learned /count_set_size;
        flashCardSet.setProgression(progres);
        flashCard.setLearned(learned);
        addFlashCardSetInterface.save(flashCardSet);
        addFlashCardInterface.save(flashCard);

    }
    @RequestMapping(value = "/submit_grade", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void test2(HttpSession session,
                     @RequestParam ("flashCardId") Long flashCardId,
                     @RequestParam ("grade") byte grade
    ) {
        Long SetID = (Long) session.getAttribute("SetID");
        FlashCards flashCard = addFlashCardInterface.customFindByID(Long.valueOf(flashCardId));
        //  flashCard.setDefinition(flashCard.getDefinition());
        FlashCardSet flashCardSet = addFlashCardSetInterface.findBySetID(SetID);
        byte Q = grade;
        Float EF = (float) flashCard.getEF();
        Integer I = flashCard.getTime_out();
        Integer rep_Num = flashCard.getRep_Num();

        if(Q >=3){
            if(rep_Num ==0){
                I=1;
            } else if(rep_Num ==1){
                I=6;
            } else {
                I = (int) Math.round(I*EF);
            }
            rep_Num++;
            flashCard.setLearned(true);
        } else{
            rep_Num = 0;
            I=1;
            flashCard.setLearned(false);
        }
        EF = (float) (EF + (0.1 - (5-Q)*(0.08+(5-Q)*0.02)));
        if (EF < 1.3F){
            EF = 1.3F;
        }
        flashCard.setEF(EF);
        flashCard.setTime_out(I);
        flashCard.setRep_Num(rep_Num);
        flashCard.setNext_rep(LocalDate.now().plusDays(I));
        flashCard.setLast_user_grade(Q);

        Integer count_learned = Math.toIntExact(addFlashCardInterface.find_learnedFlashCards(SetID, true).stream().count());
        Integer count_set_size = Math.toIntExact(addFlashCardInterface.customFindBySetID(SetID).stream().count());
        double progres = (double) count_learned /count_set_size;
        flashCardSet.setProgression(progres);
        addFlashCardSetInterface.save(flashCardSet);
        addFlashCardInterface.save(flashCard);

            }
        @GetMapping("/getProgress")
        public ResponseEntity<Map<String, Integer>> getProgress(HttpSession session) {
            Long SetID = (Long) session.getAttribute("SetID");

            Integer Learned = addFlashCardInterface.find_learnedFlashCards(SetID, true).size();
            Integer NotLearned = addFlashCardInterface.find_learnedFlashCards(SetID, false).size();

            Map<String, Integer> response = new HashMap<>();
            response.put("Learned", Learned);
            response.put("NotLearned", NotLearned);

            return ResponseEntity.ok(response);
        }
        @PostMapping("/updateGrade")
        public ResponseEntity<Map<String, Integer>> updateGrade(@RequestParam Long flashCardId, @RequestParam Integer grade, HttpSession session) {
            Long SetID = (Long) session.getAttribute("SetID");

            Integer Learned = addFlashCardInterface.find_learnedFlashCards(SetID, true).size();
            Integer NotLearned = addFlashCardInterface.find_learnedFlashCards(SetID, false).size();

            Map<String, Integer> response = new HashMap<>();
            response.put("Learned", Learned);
            response.put("NotLearned", NotLearned);

            return ResponseEntity.ok(response);
        }
}
