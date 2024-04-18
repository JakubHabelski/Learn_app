package com.learn.app.Controllers;


import com.learn.app.Classes.FlashCards;
import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.AddFlashCardInterface;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class SearchController {

    private final AddFlashCardInterface addFlashCardInterface;

    public SearchController(AddFlashCardInterface addFlashCardInterface) {
        this.addFlashCardInterface = addFlashCardInterface;
    }

    @GetMapping("/search")

    public String getSearchSuggestions(@RequestParam String term, Model model, HttpSession session) {
        UserData LoggedUser = (UserData) session.getAttribute("LoggedUser");
        model.addAttribute("user", LoggedUser);
        List<FlashCards> flashCards = addFlashCardInterface.getSearchSuggestions(term);
        model.addAttribute("flashCards", flashCards);
        List<FlashCards> flashCards_list = addFlashCardInterface.getSearchSuggestions(term);
        List<String> imagePaths = new ArrayList<>();
        Upload_image upload_image = new Upload_image();
        for (FlashCards flashCard : flashCards_list) {
            if (!flashCard.getPath().equals("empty.jpg")) {
                try {
                    ResponseEntity<byte[]> imageResponse = upload_image.showImage(flashCard.getPath());
                    String imageBase64 = Base64.getEncoder().encodeToString(imageResponse.getBody());
                    String imageUrl = "data:" + imageResponse.getHeaders().getContentType().toString() + ";base64," + imageBase64;
                    imagePaths.add(imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Jeśli ścieżka pliku jest "empty.jpg", dodaj pustą ścieżkę do listy
                imagePaths.add("");
            }
        }
        model.addAttribute("imagePaths", imagePaths);
      //  return  addFlashCardInterface.getSearchSuggestions(term);
        return "Search";

    }
}
