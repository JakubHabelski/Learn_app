package com.learn.app.RestAPI;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestAPI {

    @GetMapping("/test")
    public String test(){
        return "Hello World";
    }
}
