package com.learn.app.Controllers;


import com.learn.app.Classes.FlashCardSet;
import com.learn.app.Classes.FlashCards;
import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Controller
public class SearchController {

    private final AddFlashCardInterface addFlashCardInterface;
    private final AddFlashCardSetInterface addFlashCardSetInterface;

    public SearchController(AddFlashCardInterface addFlashCardInterface, AddFlashCardSetInterface addFlashCardSetInterface) {
        this.addFlashCardInterface = addFlashCardInterface;
        this.addFlashCardSetInterface = addFlashCardSetInterface;
    }

    @GetMapping("/search")

    public String getSearchSuggestions(@RequestParam String term, Model model, HttpSession session)throws Exception  {
        UserData LoggedUser = (UserData) session.getAttribute("LoggedUser");
        model.addAttribute("user", LoggedUser);
        List<FlashCards> flashCards = addFlashCardInterface.getSearchSuggestions(term);
        Set<FlashCards> distinctFlashCards = new HashSet<>(flashCards);
        List<FlashCards> uniqueFlashCardsList = new ArrayList<>(distinctFlashCards);
        model.addAttribute("flashCards", uniqueFlashCardsList);
        List<FlashCards> flashCards_list = addFlashCardInterface.getSearchSuggestions(term);
        Set<FlashCards> distinctFlashCards_list = new HashSet<>(flashCards_list);
        List<FlashCards> uniqueFlashCardsList_list = new ArrayList<>(distinctFlashCards_list);
        List<String> imagePaths = new ArrayList<>();
        Upload_image upload_image = new Upload_image();
        List<File> flashCard_Images = new ArrayList<>();
        for (FlashCards flashCard : uniqueFlashCardsList_list) {
            if (!flashCard.getPath().equals("")) {
                try {
                    String displayUrl =  TestImageUpload.getImageUrl2( flashCard.getPath());
                    File file = new File(displayUrl);
                    flashCard_Images.add(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Jeśli ścieżka pliku jest "empty.jpg", dodaj pustą ścieżkę do listy
                imagePaths.add("");
                flashCard_Images.add(null);
            }
        }
        model.addAttribute("imagePaths", flashCard_Images);
        model.addAttribute("Sets", addFlashCardSetInterface.findByUserID(LoggedUser.getUserID()));
      //  return  addFlashCardInterface.getSearchSuggestions(term);
        return "Search";

    }
    @GetMapping("/search_one_card/{FlashCardId}")
    public String search_one_card(Model model, HttpSession session, @PathVariable String FlashCardId) throws Exception {
        UserData LoggedUser = (UserData) session.getAttribute("LoggedUser");
        model.addAttribute("user", LoggedUser);
        FlashCards flashCard = addFlashCardInterface.customFindByID(Long.valueOf(FlashCardId));
        model.addAttribute("flashCards", flashCard);
        if (!flashCard.getPath().equals("")) {
            String displayUrl = TestImageUpload.getImageUrl2(flashCard.getPath());
            File file = new File(displayUrl);
            model.addAttribute("imagePaths", file);
            System.out.println(file);
        } else {
            model.addAttribute("imagePaths", "");
        }
        model.addAttribute("Sets", addFlashCardSetInterface.findByUserID(LoggedUser.getUserID()));
        return "Search";

    }

    @RequestMapping(value = "/CloneCard", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void test3(@RequestParam ("SetSelect") String SetSelect,
                      @RequestParam ("FlashCardId") Long FlashCardId){
        System.out.println(FlashCardId + " " +SetSelect);
        FlashCards flashCard = addFlashCardInterface.customFindByID(FlashCardId);
        FlashCards flashCardClone = new FlashCards();
        flashCardClone.setSetID(Long.parseLong(SetSelect));
        flashCardClone.setDefinition(flashCard.getDefinition());
        flashCardClone.setDescription(flashCard.getDescription());
        flashCardClone.setPath(flashCard.getPath());
        flashCardClone.setLearned(false);
        flashCardClone.setNext_rep(LocalDate.now());
        flashCardClone.setTime_out(0);
        flashCardClone.setRep_Num(0);
        addFlashCardInterface.save(flashCardClone);
    }
    @GetMapping("/search2")
    public ResponseEntity<List<FlashCards>> getSearchSuggestions2(@RequestParam String term) {
        if (term.length() < 2) { // Minimalna długość wyszukiwania, np. 2 znaki
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(addFlashCardInterface.getSearchSuggestions(term), HttpStatus.OK);
    }
    @GetMapping("/search3")
    public ResponseEntity<List<FlashCardSet>> getSearchSuggestions3(@RequestParam String term, HttpSession session) {
        UserData LoggedUser = (UserData) session.getAttribute("LoggedUser");
        if (term.length() < 2) { // Minimalna długość wyszukiwania, np. 2 znaki
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        List <FlashCardSet> flashCardSet = addFlashCardSetInterface.getFlashCardSetSuggestions(term);
        Iterator<FlashCardSet> iterator = flashCardSet.iterator();
        while (iterator.hasNext()) {
            FlashCardSet suggestedSet = iterator.next();
            if (addFlashCardInterface.customFindBySetID(suggestedSet.getSetID()).isEmpty()) {
                iterator.remove();
            }
        }
        iterator = flashCardSet.iterator();
        List<FlashCardSet> UserSets = addFlashCardSetInterface.findByUserID(LoggedUser.getUserID());
        while (iterator.hasNext()) {
            FlashCardSet suggestedSet = iterator.next();
            for (FlashCardSet UserSet : UserSets) {
                if (suggestedSet.getUserID().equals(UserSet.getUserID())) {
                    iterator.remove();
                    break;
                }
            }
        }
        return new ResponseEntity<>(flashCardSet, HttpStatus.OK);
    }

    @GetMapping("/search_one_set/{setID}")
    public String search_one_set(Model model, HttpSession session, @PathVariable String setID) throws Exception {
        UserData LoggedUser = (UserData) session.getAttribute("LoggedUser");
        model.addAttribute("user", LoggedUser);

        FlashCardSet flashCardSet = addFlashCardSetInterface.findBySetID(Long.valueOf(setID));
        if (flashCardSet == null) {
            // Handle the case where the set is not found
            model.addAttribute("error", "Set not found");
            return "error"; // Return an error page or handle accordingly
        }


        model.addAttribute("Sets", flashCardSet);
        return "Search";
    }

    @RequestMapping(value = "/CloneSet", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void CloneSet(@RequestParam ("SetSelect") String SetSelect,
                      @RequestParam ("FlashCardId") Long FlashCardId){
        System.out.println(FlashCardId + " " +SetSelect);
        FlashCards flashCard = addFlashCardInterface.customFindByID(FlashCardId);
        FlashCards flashCardClone = new FlashCards();
        flashCardClone.setSetID(Long.parseLong(SetSelect));
        flashCardClone.setDefinition(flashCard.getDefinition());
        flashCardClone.setDescription(flashCard.getDescription());
        flashCardClone.setPath(flashCard.getPath());
        flashCardClone.setLearned(false);
        flashCardClone.setNext_rep(LocalDate.now());
        flashCardClone.setTime_out(0);
        flashCardClone.setRep_Num(0);
        addFlashCardInterface.save(flashCardClone);
    }
}
