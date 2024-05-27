package com.learn.app.Controllers;

import com.learn.app.Classes.UserData;
import com.learn.app.Config.MyMailSenderService;
import com.learn.app.Config.UserService;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import com.learn.app.Interfaces.UserInterface;
import com.learn.app.Interfaces.upload_Image_Interface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private final AddFlashCardSetInterface addFlashCardSetInterface;
    private final PasswordEncoder passwordEncoder;
    private final upload_Image_Interface Upload_Image_Interface;

    @Autowired
    private UserInterface userInterface; // Dodaj adnotację @Autowired
    @Autowired
    private MyMailSenderService myMailSenderService;

    public LoginController(AddFlashCardSetInterface addFlashCardSetInterface, PasswordEncoder passwordEncoder, MyMailSenderService myMailSenderService, upload_Image_Interface Upload_Image_Interface) {
        this.addFlashCardSetInterface = addFlashCardSetInterface;
        this.passwordEncoder = passwordEncoder;
        this.myMailSenderService = myMailSenderService;
        this.Upload_Image_Interface = Upload_Image_Interface;
    }


    // Reszta Twojego kodu...


    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserService userService;


    UserData user = new UserData();

    boolean userfound = true;


    @GetMapping(value = "/loginform")
    public String loginform(Model model, HttpSession session) {


        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            

        } catch (Exception e) {
            e.printStackTrace();
        }

        user.setUserLogin("");
        user.setUserPass("");
        model.addAttribute("user", user);
        System.out.println("Login Form");
        if(session.getAttribute("login_error")== "login_error"){
            model.addAttribute("login_error", "login_error");

        }

        return "LoginForm";
    }
    // LoginController.java
    @GetMapping("/loginform_error")
    public String loginformError(Model model, HttpSession session) {
        user.setUserLogin("");
        user.setUserPass("");
        model.addAttribute("user", user);
        System.out.println("Login Form Error");
        session.setAttribute("login_error", "login_error");
        return "redirect:/loginform";
    }


     @GetMapping(value = "/userpanel")
    public String userpanel(Model model, Authentication authentication, HttpSession session){
        System.out.println("User Panel");
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            // Znajdź użytkownika w bazie danych
            UserData user = userInterface.findByUserLogin(username);
            if (user != null) {
                if( user.getUserActive()==false){
                    return "redirect:/loginform";
                }
                System.out.println("-------------------------------------------");
                System.out.println(user.getUserLogin() +" "+ user.getUserPass());
                System.out.println("-------------------------------------------");
                session.setAttribute("LoggedUser", user);
                model.addAttribute("user", user);
                model.addAttribute("flashCardSets", addFlashCardSetInterface.findByUserID(user.getUserID()));
                return "UserPanel";
            }
        } else if (LoggedUser != null) {
            model.addAttribute("user", LoggedUser);
            model.addAttribute("flashCardSets", addFlashCardSetInterface.findByUserID(LoggedUser.getUserID()));
            return "UserPanel";

        }



        // Redirect to login or handle not logged in scenario
        return "redirect:/loginform";
    }



    @RequestMapping("/Logout")
    public String Logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginform";
    }





}