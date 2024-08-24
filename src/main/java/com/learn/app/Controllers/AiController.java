package com.learn.app.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.learn.app.Services.OpenAiService;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final OpenAiService openAiService;

    @Autowired
    public AiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat() {
        try {
            String response = openAiService.getChatResponse("Hello, how are you?");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Handle exceptions appropriately
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
}
