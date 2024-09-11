package com.learn.app.Controllers;


import com.learn.app.Classes.*;
import com.learn.app.Interfaces.*;
import com.learn.app.Services.ImageUploadService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Controller
public class FlashCardSetController {
    @Autowired
    private final AddFlashCardInterface addFlashCardInterface;
    private final AddFlashCardSetInterface addFlashCardSetInterface;
    private final TagsInterface tagsInterface;
    private final FlashCardTagsInterface flashCardTagsInterface;
   // private final UserandSetInterface userandSetInterface;
    public FlashCardSetController(AddFlashCardInterface addFlashCardInterface, AddFlashCardSetInterface addFlashCardSetInterface, TagsInterface tagsInterface, FlashCardTagsInterface flashCardTagsInterface) {
        this.addFlashCardInterface = addFlashCardInterface;
        this.addFlashCardSetInterface = addFlashCardSetInterface;
        this.tagsInterface = tagsInterface;
        this.flashCardTagsInterface = flashCardTagsInterface;
      //  this.userandSetInterface = userandSetInterface;
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
    public String AddCard(Model model,
                          HttpSession session,
                          @RequestParam String SetName,
                          @RequestParam String Description) throws Exception {
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        FlashCardSet flashCardSet1= new FlashCardSet();
        flashCardSet1.setSetName(SetName);
        flashCardSet1.setSetDescription(Description);
        flashCardSet1.setUserID(LoggedUser.getUserID());
        addFlashCardSetInterface.save(flashCardSet1);

        //pobiez id z flashCardSet
        Long flashCardSetId = flashCardSet1.getSetID();

        String tags;
        VertexAI vertexAI = new VertexAI();
        tags = vertexAI.textInput("Based on the following set name and description, generate a list of single-word tags, but maximum 5 tags. Provide only the tags, each separated by a newline:\n\n" +
                "Set Name: " + SetName + "\n" +
                "Description: " + Description);
        String[] tagsArray = tags.split("\\n");
        // Przypisz tagi do bazy danych i powiąż z zestawem fiszek
        for (String tag : tagsArray) {
            Tags tag1 = tagsInterface.findByTag(tag); // Pobierz istniejący tag lub stwórz nowy, jeśli nie istnieje
            if (tag1 == null) {
                tag1 = new Tags();
                tag1.setTag(tag);
                tagsInterface.save(tag1);
            }

            // Tworzenie i zapisywanie obiektów FlashCardTags
            FlashCardTags flashCardTags = new FlashCardTags();
            flashCardTags.setFlashCardSet(flashCardSet1);
            flashCardTags.setTags(tag1);
            flashCardTagsInterface.save(flashCardTags); // Zakładam, że masz repozytorium do zapisywania FlashCardTags
        }

        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        model.addAttribute("user" , user);
        model.addAttribute("flashCardSets", addFlashCardSetInterface.findByUserID(LoggedUser.getUserID()));
        // return "AddFlashCard";

            return "redirect:/userpanel";
       // return "redirect:/AddFlashCardSet/{userID}";

    }

    @RequestMapping(value="/DeleteFlashCardSet", method = { RequestMethod.GET, RequestMethod.POST } )
    public String deleteFlashCardSet(Model model, 
                                     HttpSession session,
                                     @RequestParam("SetID") Long SetID) {
        UserData loggedUser = (UserData) session.getAttribute("LoggedUser");
        flashCardTagsInterface.deleteBySetID(SetID);
        ArrayList FlashCardList = new ArrayList<FlashCards>();
        FlashCardList.addAll(addFlashCardInterface.customFindBySetID(SetID));
        ImageUploadService imageUploadService = new ImageUploadService();
        for (int i = 0; i < FlashCardList.size(); i++) {
            FlashCards flashCard = (FlashCards) FlashCardList.get(i);
            if (!flashCard.getPath().equals("")) {
                imageUploadService.deleteImage(flashCard.getPath());
            }
        }
       // imageUploadService.deleteImage(flashCard.getPath());
            // Tutaj dodaj logikę usuwania zestawu kart
            addFlashCardSetInterface.deleteById(SetID);

            // Aktualizacja danych do wyświetlenia w widoku UserPanel
            model.addAttribute("user", loggedUser);
            model.addAttribute("flashCardSets", addFlashCardSetInterface.findByUserID(loggedUser.getUserID()));
                                        
            return "redirect:/userpanel";
         
    }


    
    

}
