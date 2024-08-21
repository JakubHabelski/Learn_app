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

}
