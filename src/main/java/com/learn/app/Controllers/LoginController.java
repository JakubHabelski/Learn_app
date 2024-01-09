package com.learn.app.Controllers;

<<<<<<< HEAD
=======
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import com.learn.app.Classes.UserData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpSession;
>>>>>>> 89bc3243f3169d1e1deed8f9bc1ca0c68fd01927
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.AddFlashCardSetInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpSession;
=======
import org.springframework.web.bind.annotation.*;
>>>>>>> 89bc3243f3169d1e1deed8f9bc1ca0c68fd01927

@Controller
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private final AddFlashCardSetInterface addFlashCardSetInterface;
    public LoginController(AddFlashCardSetInterface addFlashCardSetInterface) {
        this.addFlashCardSetInterface = addFlashCardSetInterface;
    }
    
    @PersistenceContext
    @Autowired
    private EntityManager entityManager;
    UserData user = new UserData();



    @GetMapping(value = "/loginform" )
    public String loginform(Model model) {
             user.setUserLogin("");
             user.setUserPass("");
            model.addAttribute("user", user);
            return "LoginForm";
    }
    @PostMapping (value = "/userpanel" )
    public String userpanel(@RequestParam String UserLogin,
                            @RequestParam String UserPass,
                            Model model,
                            HttpSession session) {

        TypedQuery<UserData> query =
                entityManager.createQuery("SELECT u FROM UserData u", UserData.class);

        for (UserData userdata : query.getResultList()) {
            logger.warn(userdata.toString());
            if (userdata.getUserLogin().equals(UserLogin) && userdata.getUserPass().equals(UserPass)){
            
                user.setUserID(userdata.getUserID());
                user.setUserPass(UserPass);
                user.setUserLogin(UserLogin);
                session.setAttribute("LoggedUser", user);
                model.addAttribute("user", user);
                model.addAttribute("flashCardSets", addFlashCardSetInterface.findByUserID(user.getUserID()));
                model.addAttribute("method", "Post");
                // Redirect to "UserPanel"
                return "redirect:/userpanel";
            }
        }
       return "PError";
    }
    @GetMapping("/userpanel")
    public String userPanel(Model model, HttpSession session) {
        UserData loggedUser = (UserData) session.getAttribute("LoggedUser");

        // Check if the user is logged in
        if (loggedUser != null) {
            // Add user information to the model
            model.addAttribute("user", loggedUser);
            model.addAttribute("flashCardSets", addFlashCardSetInterface.findByUserID(loggedUser.getUserID()));

            // Add other attributes to the model as needed
            model.addAttribute("method", "Get");
            return "UserPanel";
        } else {
            // Redirect to login or handle not logged in scenario
            return "redirect:/loginform";
        }
    }

}

