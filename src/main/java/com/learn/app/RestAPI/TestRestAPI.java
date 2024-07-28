package com.learn.app.RestAPI;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestAPI {
    @CrossOrigin(origins = "http://localhost:58547")
    @GetMapping("/testRestAPI")
    public String test(){
        return "Hello World";
    }
}
