package com.learn.app.Controllers;


import com.detectlanguage.DetectLanguage;
import com.detectlanguage.Result;
import com.detectlanguage.errors.APIError;
import com.learn.app.Classes.FlashCardSet;
import com.learn.app.Classes.FlashCards;
import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import com.learn.app.Interfaces.upload_Image_Interface;
import com.learn.app.Services.ImageUploadService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;




@Controller
public class EditSetController {


   @Autowired
    private final AddFlashCardInterface addFlashCardInterface;
    private final AddFlashCardSetInterface addFlashCardSetInterface;
  //  private final upload_Image_Interface upload_Image_Interface;
    public EditSetController(AddFlashCardInterface addFlashCardInterface, AddFlashCardSetInterface addFlashCardSetInterface, upload_Image_Interface upload_Image_Interface) {
        this.addFlashCardInterface = addFlashCardInterface;
        this.addFlashCardSetInterface = addFlashCardSetInterface;
       // this.upload_Image_Interface = upload_Image_Interface;
    }

    UserData user = new UserData();



    @GetMapping (value = "/EditFlashCardSet/{SetID}")
    public String EditFlashCardSet_GET(@PathVariable Long SetID,
                                       Model model,
                                       FlashCards flashCards,
                                       HttpSession session) throws Exception {
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        Upload_image upload_image = new Upload_image();
        FlashCardSet flashCardSet = addFlashCardSetInterface.findBySetID(SetID);
        session.setAttribute("SetID", SetID);
      //  user.setUserLogin(LoggedUser.getUserLogin());
      //  user.setUserPass(LoggedUser.getUserPass());
      //  user.setUserID(LoggedUser.getUserID());
      //  user.setUserName(LoggedUser.getUserName());
      //  user.setUserSurname(LoggedUser.getUserSurname());
        flashCards.setSetID(SetID);
        model.addAttribute("user", user);
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


        model.addAttribute("learncard", learncard);

        model.addAttribute("imagePaths", flashCard_Images);
        model.addAttribute("flashCard", addFlashCardInterface.customFindBySetID(SetID));
        model.addAttribute("file", new ArrayList<MultipartFile>());
        return  ("EditFlashCardSet");

    }
    @PostMapping(value = "/AddCard")
    public String EditFlashCardSet_POST(@RequestParam Long SetID,
                                        @RequestParam ("Definition[]") List<String> Definition,
                                        @RequestParam ("Description[]") List<String> Description,
                                        @RequestParam (name="file[]", required = false) List<MultipartFile> files,
                                        HttpSession session,
                                        Model model) throws Exception {
        // Log the number of received files
        System.out.println("Received " + files.size() + " files.");

        // Log the names of the received files
        for (MultipartFile file : files) {
            System.out.println("Received file: " + file.getOriginalFilename());
        }
        ImageUploadService imageUploadService = new ImageUploadService();

        String image_obj_path = "test";
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
      //  user.setUserLogin(LoggedUser.getUserLogin());
      //  user.setUserPass(LoggedUser.getUserPass());
      //  user.setUserID(LoggedUser.getUserID());
        model.addAttribute("user" , user);
        model.addAttribute("flashCardSets", addFlashCardSetInterface.findByUserID(LoggedUser.getUserID()));

        for (int i = 0; i < Definition.size(); i++) {
            FlashCards flashCard = new FlashCards();
            flashCard.setSetID(SetID);
            flashCard.setDefinition(Definition.get(i));
            flashCard.setDescription(Description.get(i));
            String path;
            if (files != null && i < files.size() && !files.get(i).isEmpty()) {
                path = imageUploadService.uploadImage(files.get(i), image_obj_path);
            } else {
                path = "";
            }
            flashCard.setSetID(SetID);
            flashCard.setDefinition(Definition.get(i));
            flashCard.setDescription(Description.get(i));
            flashCard.setPath(path);
            flashCard.setTime_out(0);
            flashCard.setRep_Num(0);
            flashCard.setEF(2.5F);
            flashCard.setNext_rep(LocalDate.now());
            flashCard.setDef_lang(Detect_Lang(Definition.get(i)));
            flashCard.setDes_lang(Detect_Lang(Description.get(i)));
            addFlashCardInterface.save(flashCard);
            // Log the number of received files
            System.out.println("Received " + files.size() + " files.");

            // Log the names of the received files
            for (MultipartFile file : files) {
                System.out.println("Received file: " + file.getOriginalFilename());
            }
        }

        return "redirect:/EditFlashCardSet/" + SetID;
    }
    @RequestMapping("/deleteCard")
    public String deleteCard(@RequestParam String FlashCardId,
                             @RequestParam String SetID){
        FlashCards flashCard = addFlashCardInterface.customFindByID(Long.valueOf(FlashCardId));
        ImageUploadService imageUploadService = new ImageUploadService();
        imageUploadService.deleteImage(flashCard.getPath());
        addFlashCardInterface.deleteById(Long.valueOf(FlashCardId));

        return "redirect:/EditFlashCardSet/"  + SetID;
    }

    @PostMapping("/EditCard")
    public String EditCard(@RequestParam ("FlashCardId") Long FlashCardId,
                           @RequestParam ("SetID") Long SetID,
                           @RequestParam ("Definition") String Definition,
                           @RequestParam ("Description") String Description,
                           @RequestParam ("def_lang") String def_lang,
                           @RequestParam ("des_lang") String des_lang,
                           @RequestParam (name="file", required = false) MultipartFile file

    )throws Exception {
        ImageUploadService imageUploadService = new ImageUploadService();
        FlashCards flashCard = addFlashCardInterface.customFindByID(FlashCardId);
        String image_obj_path = "test";
        flashCard.setDefinition(Definition);
        flashCard.setDescription(Description);
        if(!(def_lang ==null)){
            flashCard.setDef_lang(def_lang);
        } else{
            flashCard.setDef_lang(flashCard.getDef_lang());
        }
        if(!(des_lang ==null)){
            flashCard.setDes_lang(des_lang);
        }else{
            flashCard.setDes_lang(flashCard.getDes_lang());
        }
        String path;
        if ( file.isEmpty()) {
            path = "";
        } else {
            imageUploadService.uploadImage(file, image_obj_path);
            path = imageUploadService.uploadImage(file, image_obj_path);
            flashCard.setPath(path);
        }

        addFlashCardInterface.save(flashCard);
        return "redirect:/EditFlashCardSet/" + SetID;
    }

    
    @PostMapping("/Return_UserPanel")
    public String Return_UserPanel( HttpSession session,
                                    Model model
                                    ){
        model.addAttribute("user" , user);
        return "redirect:/userpanel";
    }
    @RequestMapping("/reset_progress")
    public String reset_progress(HttpSession session){
        Long SetID = (Long) session.getAttribute("SetID");
        ArrayList slidecard = new ArrayList<FlashCards>();
        slidecard.addAll(addFlashCardInterface.customFindBySetID(SetID));
        for (int i = 0; i < slidecard.size(); i++) {
            FlashCards flashCard = (FlashCards) slidecard.get(i);
            flashCard.setLearned(false);
            flashCard.setNext_rep(LocalDate.now());
            flashCard.setRep_Num(0);
            flashCard.setEF(2.5F);
            flashCard.setTime_out(0);
            flashCard.setLast_user_grade((byte) 0);
            addFlashCardInterface.save(flashCard);
        }
        FlashCardSet flashCardSet = addFlashCardSetInterface.findBySetID(SetID);
        flashCardSet.setProgression(0);
        addFlashCardSetInterface.save(flashCardSet);
        return "redirect:/EditFlashCardSet/" + SetID;
    }
   public String Detect_Lang(String Text) throws APIError {
       // https://detectlanguage.com/
       DetectLanguage.apiKey = "a257d4911b341402d8a05ffc386dae17";
       return DetectLanguage.simpleDetect(Text);
   }
    public String Detect_Lang2(String Text) throws APIError {
        List<Result> results = DetectLanguage.detect(Text);
        String lang_string = "";
        for (int i = 0; i < results.size(); i++) {
            lang_string += results.get(i).language;
            if (i < results.size() - 1) {
                lang_string += ",";
            }
        }
        System.out.println("Language: " + lang_string);
        return lang_string;
    }
}
