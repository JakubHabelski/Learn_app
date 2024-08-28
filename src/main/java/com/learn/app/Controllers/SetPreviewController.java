package com.learn.app.Controllers;

import com.learn.app.Classes.FlashCardSet;
import com.learn.app.Classes.FlashCards;
import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SetPreviewController {

    @Autowired
    private final AddFlashCardInterface addFlashCardInterface;
    private final AddFlashCardSetInterface addFlashCardSetInterface;

    public SetPreviewController(AddFlashCardInterface addFlashCardInterface, AddFlashCardSetInterface addFlashCardSetInterface) {
        this.addFlashCardInterface = addFlashCardInterface;
        this.addFlashCardSetInterface = addFlashCardSetInterface;
    }


    @GetMapping(value = "/SetPreview/{SetID}")
    public String EditFlashCardSet_GET(@PathVariable Long SetID,
                                       @RequestParam(value = "fromEdit", required = false, defaultValue = "false") boolean fromEdit,
                                       Model model,
                                       FlashCards flashCards,
                                       HttpSession session) throws Exception {
        UserData LoggedUser = (UserData) session.getAttribute("LoggedUser");
        Upload_image upload_image = new Upload_image();
        FlashCardSet flashCardSet = addFlashCardSetInterface.findBySetID(SetID);
        session.setAttribute("SetID", SetID);
        flashCards.setSetID(SetID);
        //model.addAttribute("user", user);
        model.addAttribute("userID", LoggedUser.getUserID());
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

                String displayUrl = TestImageUpload.getImageUrl2(flashCard.getPath());
                File file = new File(displayUrl);
                flashCard_Images.add(file);

            } else {
                // Jeśli ścieżka pliku jest "", dodaj pustą ścieżkę do listy
                imagePaths.add("");
                flashCard_Images.add(null);
            }
        }
        return "SetPreview";
    }

}

