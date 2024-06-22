package com.learn.app.Controllers;


import com.learn.app.Classes.FlashCards;
import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                   // ResponseEntity<byte[]> imageResponse = upload_image.showImage(flashCard.getPath());
                   // String imageBase64 = Base64.getEncoder().encodeToString(imageResponse.getBody());
                   // String imageUrl = "data:" + imageResponse.getHeaders().getContentType().toString() + ";base64," + imageBase64;
                  //  imagePaths.add(imageUrl);
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
}
