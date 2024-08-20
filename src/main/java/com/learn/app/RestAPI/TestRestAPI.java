package com.learn.app.RestAPI;


import com.learn.app.Interfaces.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestRestAPI {

    @Autowired
    UserInterface userInterface;


    @CrossOrigin(origins = "http://localhost:58547")
    @GetMapping("/testRestAPI")
    public ResponseEntity test(@RequestParam String test_var){
        boolean isTaken = userInterface.findByUserLogin(test_var) != null;
        Map<String, String>  response= new HashMap<>();
        response.put("message", test_var);
        if(userInterface.findByUserLogin(test_var)==null){
            response.put("message", "User not found");
        } else {
            response.put("message", "User found");
        }

        return ResponseEntity.ok(isTaken);
    }
}
