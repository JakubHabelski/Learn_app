package com.learn.app.Controllers;

import com.learn.app.Classes.FlashCardSet;
import com.learn.app.Classes.FlashCards;
import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import com.learn.app.Interfaces.FlashCardTagsInterface;
import com.learn.app.Interfaces.TagsInterface;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SetPreviewController {

    @Autowired
    private final AddFlashCardInterface addFlashCardInterface;
    private final AddFlashCardSetInterface addFlashCardSetInterface;
    private final TagsInterface tagsInterface;
    private final FlashCardTagsInterface flashCardTagsInterface;
    // private final UserandSetInterface userandSetInterface;
    public SetPreviewController(AddFlashCardInterface addFlashCardInterface, AddFlashCardSetInterface addFlashCardSetInterface, TagsInterface tagsInterface, FlashCardTagsInterface flashCardTagsInterface) {
        this.addFlashCardInterface = addFlashCardInterface;
        this.addFlashCardSetInterface = addFlashCardSetInterface;
        this.tagsInterface = tagsInterface;
        this.flashCardTagsInterface = flashCardTagsInterface;
        //  this.userandSetInterface = userandSetInterface;
    }

    UserData user = new UserData();
    @GetMapping(value = "/SetPreview/{SetID}")
    public String EditFlashCardSet_GET(@PathVariable Long SetID,
                                       @RequestParam(value = "fromEdit", required = false, defaultValue = "false") boolean fromEdit,
                                       Model model,
                                       FlashCards flashCards,
                                       HttpSession session) throws Exception {
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        Upload_image upload_image = new Upload_image();
        FlashCardSet flashCardSet = addFlashCardSetInterface.findBySetID(SetID);
        session.setAttribute("SetID", SetID);
        flashCards.setSetID(SetID);
        model.addAttribute("user", user);
        model.addAttribute("userID", LoggedUser.getUserID());
        Integer count_learned = Math.toIntExact(addFlashCardInterface.find_learnedFlashCards(SetID, true).stream().count());
        Integer count_set_size = Math.toIntExact(addFlashCardInterface.customFindBySetID(SetID).stream().count());
        double progres = (double) count_learned /count_set_size;
        flashCardSet.setProgression(progres);
        flashCardSet.setLastvisit(LocalDate.now());
        //  addFlashCardSetInterface.save(flashCardSet);
        model.addAttribute("flashcard_set_progress", Math.round(flashCardSet.getProgression()*100));
        model.addAttribute("flashcard_set_lastvisit", flashCardSet.getLastvisit());
        model.addAttribute("count_learned", Math.toIntExact(addFlashCardInterface.find_learnedFlashCards(SetID, true).stream().count()));
        model.addAttribute("set_size", Math.toIntExact(addFlashCardInterface.customFindBySetID(SetID).stream().count()));
        // Tablica z obiektami FlashCards - "slidecard" - przekazuje do widoku EditFlashCardSet i kolejno do Quiz, FLaashCardGame
        ArrayList slidecard = new ArrayList<FlashCards>();
        slidecard.addAll(addFlashCardInterface.customFindBySetID(SetID));
        model.addAttribute("slidecard", slidecard);
        FlashCards flashCards2 = addFlashCardInterface.customFindByID(Long.valueOf(14902));
        model.addAttribute("flashCards2", flashCards2);



        List<FlashCards> flashCards_list = addFlashCardInterface.customFindBySetID(SetID);
        List<String> imagePaths = new ArrayList<>();
        List<File> flashCard_Images = new ArrayList<>();
        for (FlashCards flashCard : flashCards_list) {
            if (!flashCard.getPath().equals("")) {

                String displayUrl =  TestImageUpload.getImageUrl2( flashCard.getPath());
                File file = new File(displayUrl);
                flashCard_Images.add(file);

            } else {
                // Jeśli ścieżka pliku jest "", dodaj pustą ścieżkę do listy
                imagePaths.add("");
                flashCard_Images.add(null);
            }
        }


        ArrayList learncard = new ArrayList<FlashCards>();
        learncard.addAll(addFlashCardInterface.find_learnedFlashCards(SetID, true)); // pokazywanie tylko nauczonych fiszek
        System.out.println(learncard) ;
        System.out.println("FromEdit: " + fromEdit);
        if(fromEdit){
            model.addAttribute("fromEdit", true);
        } else {
            model.addAttribute("fromEdit", false);
        }
        VertexAI vertexAI = new VertexAI();

        // System.out.println("vertexAI: " + vertexAI.textInput("Basing on the flashcardsets ."+ flashCardSet.getSetName() + flashCardSet.getSetDescription()+ "generate maximum 10 single word tags for this set which suit the best"));
        // System.out.println(vertexAI.textInput("Generate 200 single word tags for flashcard sets which i will paste to mysql database INSERT INTO Tag (name) VALUES"));
        // System.out.println(vertexAI.textInput("Basing on:"+ tagsInterface.findAll()+ "generate maximum 10 single word tags for this set which suit the best"+ flashCardSet.getSetName() + flashCardSet.getSetDescription()));
        model.addAttribute("learncard", learncard);

        model.addAttribute("imagePaths", flashCard_Images);
        model.addAttribute("flashCard", addFlashCardInterface.customFindBySetID(SetID));
        model.addAttribute("file", new ArrayList<MultipartFile>());
        return "SetPreview";
    }
    @PostMapping(value = "/CloneSet/{SetID}/${userID}")
    public ResponseEntity<String> CloneSet(@PathVariable("SetID") Long SetID,@PathVariable("userID") Long userID, HttpSession session) {
        UserData LoggedUser = (UserData) session.getAttribute("LoggedUser");
        FlashCardSet flashCardSet_old = addFlashCardSetInterface.findBySetID(SetID);
        FlashCardSet flashCardSet_new = new FlashCardSet();
       // FlashCardSetController flashCardSetController = new FlashCardSetController(addFlashCardInterface, addFlashCardSetInterface, tagsInterface, flashCardTagsInterface);
       // flashCardSetController.AddCard(flashCardSet_new, new Model(), session, flashCardSet_old.getSetName(), flashCardSet_old.getSetDescription());
        for(FlashCards flashCard : addFlashCardInterface.customFindBySetID(SetID)){
            FlashCards flashCard_new = new FlashCards();
            flashCard_new.setSetID(flashCardSet_new.getSetID());
            flashCard_new.setDefinition(flashCard.getDefinition());
            flashCard_new.setDescription(flashCard.getDescription());
            flashCard_new.setLearned(false);
            flashCard_new.setPath(flashCard.getPath());
            flashCard_new.setRep_Num(0);
            flashCard_new.setNext_rep(null);
            flashCard_new.setTime_out(0);
            flashCard_new.setEF(2.5f);
            flashCard_new.setLast_user_grade((byte) 0);
            flashCard_new.setDef_lang(flashCard.getDef_lang());
            flashCard_new.setDes_lang(flashCard.getDes_lang());
            addFlashCardInterface.save(flashCard_new);
        }
        return ResponseEntity.ok("Set cloned successfully");
    }
}

