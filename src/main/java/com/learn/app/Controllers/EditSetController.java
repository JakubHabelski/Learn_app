package com.learn.app.Controllers;


import com.learn.app.Classes.FlashCardSet;
import com.learn.app.Classes.FlashCards;
import com.learn.app.Classes.UserData;
import com.learn.app.Classes.image;
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
import java.io.IOException;
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
                                       HttpSession session) throws IOException {
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        Upload_image upload_image = new Upload_image();
        FlashCardSet flashCardSet = addFlashCardSetInterface.findBySetID(SetID);
        session.setAttribute("SetID", SetID);
        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        user.setUserName(LoggedUser.getUserName());
        user.setUserSurname(LoggedUser.getUserSurname());
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

                    String displayUrl =  TestImageUpload.getImageUrl("learn-app-jh-bucket", flashCard.getPath());
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

        return  ("EditFlashCardSet");

    }
    @PostMapping(value = "/AddCard")
    public String EditFlashCardSet_POST(@RequestParam Long SetID,
                                        @RequestParam String Definition,
                                        @RequestParam String Description,
                                        @RequestParam (name="file", required = false) MultipartFile file,
                                        HttpSession session,
                                        Model model) throws Exception {
        ImageUploadService imageUploadService = new ImageUploadService();
        image image_obj = new image();
        String image_obj_path = "test";
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        user.setUserLogin(LoggedUser.getUserLogin());
        user.setUserPass(LoggedUser.getUserPass());
        user.setUserID(LoggedUser.getUserID());
        model.addAttribute("user" , user);
        model.addAttribute("flashCardSets", addFlashCardSetInterface.findByUserID(user.getUserID()));

        FlashCards flashCard = new FlashCards();
        flashCard.setSetID(SetID);
        flashCard.setDefinition(Definition);
        flashCard.setDescription(Description);
        //saving card image file to database

       // Upload_image upload_image = new Upload_image();
      //  image uploadedImage = upload_image.upload_image(file);
        String path;
        if ( file.isEmpty()) {
           path = "";
        } else {

           // uploadedImage.setUserID(null);
           // uploadedImage.setPath(path);
           // uploadedImage.setFlashCardId(flashCard.getFlashCardId());
            imageUploadService.uploadImage(file, image_obj_path);
           // path = imageUploadService.uploadImage(file, image_obj_path).toString();
            path = "test/" + file.getOriginalFilename();
           // image_obj.setPath(imageUploadService.uploadImage(file, image_obj_path));
          //  image_obj.setFlashCardId(flashCard.getFlashCardId());
          // image_obj.setId_of_flashCard(Long.valueOf(32));
           // upload_Image_Interface.save(image_obj);
        }
       // String fullPath = path;
      //  String fileName = fullPath.substring(fullPath.lastIndexOf(File.separator) + 1);
       // System.out.println(fileName);
        flashCard.setSetID(SetID);
        flashCard.setDefinition(Definition);
        flashCard.setDescription(Description);
        flashCard.setPath(path);
        flashCard.setTime_out(0);
        flashCard.setRep_Num(0);
        flashCard.setEF(2.5F);
        flashCard.setNext_rep(LocalDate.now());
        addFlashCardInterface.save(flashCard);
        if(!file.isEmpty()){
           // flashCard.setPath(imageUploadService.uploadImage(file, image_obj_path).toString());
        }
       // upload_Image_Interface.save(uploadedImage);

        System.out.println("FlashCardId: " + imageUploadService.uploadImage(file, image_obj_path).toString());
        //return "UserPanel";
        return "redirect:/EditFlashCardSet/" + SetID;
    }

    @RequestMapping("/deleteCard")
    public String deleteCard(@RequestParam String FlashCardId,
                             @RequestParam String SetID){
        addFlashCardInterface.deleteById(Long.valueOf(FlashCardId));
        return "redirect:/EditFlashCardSet/"  + SetID;
    }

    @PostMapping("/EditCard")
    public String EditCard(@RequestParam ("FlashCardId") Long FlashCardId,
                           @RequestParam ("SetID") Long SetID,
                           @RequestParam ("Definition") String Definition,
                           @RequestParam ("Description") String Description,
                           @RequestParam (name="file", required = false) MultipartFile file

    )throws Exception {
        FlashCards flashCard = addFlashCardInterface.customFindByID(FlashCardId);
        flashCard.setDefinition(Definition);
        flashCard.setDescription(Description);
       // Long old_img_id = upload_Image_Interface.findIdByFlashCardId(flashCard.getFlashCardId());
        Upload_image upload_image = new Upload_image();
        image uploadedImage = upload_image.upload_image(file);
        String path;
        if ( file.isEmpty()) {
            path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"uploads"+File.separator+"";
        } else {
            path = uploadedImage.getPath();
         //   uploadedImage.setId(old_img_id);
            uploadedImage.setUserID(null);
            uploadedImage.setPath(path);
            uploadedImage.setId_of_flashCard(flashCard.getFlashCardId());
        }
        String fullPath = path;
        String fileName = fullPath.substring(fullPath.lastIndexOf(File.separator) + 1);
        flashCard.setPath(fileName);
        addFlashCardInterface.save(flashCard);
      //  upload_Image_Interface.save(uploadedImage);
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
        return "redirect:/EditFlashCardSet/" + SetID;
    }


}
