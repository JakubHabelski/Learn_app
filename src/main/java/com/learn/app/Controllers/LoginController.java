package com.learn.app.Controllers;

import com.learn.app.Classes.UserData;
import com.learn.app.Config.UserService;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import com.learn.app.Interfaces.UserInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private final AddFlashCardSetInterface addFlashCardSetInterface;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserInterface userInterface; // Dodaj adnotację @Autowired

    public LoginController(AddFlashCardSetInterface addFlashCardSetInterface, PasswordEncoder passwordEncoder) {
        this.addFlashCardSetInterface = addFlashCardSetInterface;
        this.passwordEncoder = passwordEncoder;
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
    public String loginform(Model model) {
        user.setUserLogin("");
        user.setUserPass("");
        model.addAttribute("user", user);
        System.out.println("Login Form");

        return "LoginForm";
    }
    // LoginController.java
    @PostMapping(value = "/login")
    public String loginform(@RequestParam("UserLogin") String UserLogin,
                            @RequestParam("UserPass") String UserPass,
                            Model model,
                            HttpSession session) {
        System.out.println("Login Form Post");

        // Znajdź użytkownika w bazie danych
        UserData user = userInterface.findByUserLogin(UserLogin);
        if (user != null && passwordEncoder.matches(UserPass, user.getUserPass())) {
            // Jeśli użytkownik istnieje i hasło jest poprawne, dodaj go do sesji
            session.setAttribute("user", user);
        } else {
            // Jeśli użytkownik nie istnieje lub hasło jest niepoprawne, wyświetl komunikat o błędzie
            model.addAttribute("loginError", "Invalid username or password");
            return "LoginForm";
        }

    return "redirect:/userpanel";
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

         System.out.println("dupa");

        // Redirect to login or handle not logged in scenario
        return "redirect:/loginform";
    }



    @RequestMapping("/Logout")
    public String Logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginform";
    }




    @RequestMapping("/")

    public String Home(HttpSession httpSession, Model model, @Param("UserLogin") String UserLogin, @Param("UserPass") String UserPass) {

        UserData user = (UserData) httpSession.getAttribute("user");
        System.out.println(UserLogin + UserPass);
        /*
        if (user != null) {
            return user.getUserLogin();
        } else {
            // Handle the case where there is no user in the session
            return "No user in session";
        }

         */
        if(user != null){
            return "redirect:/userpanel";
        }else{
            return "redirect:/loginform";
        }

    }
}