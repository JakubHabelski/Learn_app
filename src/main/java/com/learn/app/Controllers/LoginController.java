package com.learn.app.Controllers;

import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.learn.app.Classes.UserData;
import com.learn.app.Config.MyMailSenderService;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import com.learn.app.Interfaces.UserInterface;
import com.learn.app.Interfaces.upload_Image_Interface;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

@Controller
public class LoginController {
   // Logger logger = LoggerFactory.getLogger(LoginController.class);

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





  //  @Autowired
  //  private EntityManager entityManager;

  //  @Autowired
  //  private UserService userService;


    UserData user = new UserData();

  //  boolean userfound = true;

    @PostConstruct
    public void init() {
        try {
            DetectorFactory.loadProfile("lang_profiles");
        } catch (LangDetectException e) {
            e.printStackTrace();
        }
    }
    @GetMapping(value = "/loginform")
    public String loginform(Model model, HttpSession session) {
        user.setUserLogin("");
        user.setUserPass("");
        model.addAttribute("user", user);
        System.out.println("Login Form");
        if(session.getAttribute("login_error")== "login_error"){
            model.addAttribute("login_error", "login_error");

        }
        /*
        try {
            com.cybozu.labs.langdetect.Detector detector = DetectorFactory.create();
            detector.append("Tekst do wykrycia języka");
            String language = detector.detect();
            model.addAttribute("language", language);
            System.out.println("Wykryty język: " + language);
        } catch (LangDetectException e) {
            e.printStackTrace();
        }
         */
        System.out.println(language("Chuj ci w dupe"));
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
        return "redirect:/loginform";
    }



    @RequestMapping("/Logout")
    public String Logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginform";
    }
    public String language(String text){
        LanguageDetector detector = LanguageDetectorBuilder.fromAllSpokenLanguages().build();
        Language language = detector.detectLanguageOf(text);
        return language.toString();
    }




}