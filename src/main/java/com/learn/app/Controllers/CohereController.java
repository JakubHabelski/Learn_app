package com.learn.app.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.learn.app.Services.CohereService;

@RestController
@RequestMapping("/api/cohere")
public class CohereController {

    private final CohereService cohereService;

    @Autowired
    public CohereController(CohereService cohereService) {
        this.cohereService = cohereService;
    }

    @GetMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam String prompt) {
        try {
            String response = cohereService.generateText(prompt);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
}
