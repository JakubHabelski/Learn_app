package com.learn.app.Controllers;

import com.detectlanguage.DetectLanguage;
import com.detectlanguage.Result;
import com.detectlanguage.errors.APIError;
import com.learn.app.Classes.FlashCardSet;
import com.learn.app.Classes.Tags;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping(value = "/loginform")
    public String loginform(Model model, HttpSession session) throws APIError {
        
        user.setUserLogin("");
        user.setUserPass("");
        model.addAttribute("user", user);
        System.out.println("Login Form");
        if(session.getAttribute("login_error")== "login_error"){
            model.addAttribute("login_error", "login_error");

        }
        // https://detectlanguage.com/
         DetectLanguage.apiKey = "a257d4911b341402d8a05ffc386dae17";
        //String language = DetectLanguage.simpleDetect("chuj");
       // System.out.println(language);
        Detect_Lang2("dupa");
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
            List<FlashCardSet> flashCardSets = addFlashCardSetInterface.findByUserID(LoggedUser.getUserID());
            List<Object[]> Tags = null;
            for (FlashCardSet flashCardSet : flashCardSets) {
                Tags = addFlashCardSetInterface.getTagsBySetID(flashCardSet.getSetID());                
            }
            model.addAttribute("Tags", Tags);
            return "UserPanel";

        }
        return "redirect:/loginform";
    }



    @RequestMapping("/Logout")
    public String Logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginform";
    }

    public void Detect_Lang2(String Text) throws APIError {
        List<Result> results = DetectLanguage.detect(Text);
        String lang_string = "";
        for(int i = 0; i < results.size(); i++){
            lang_string += results.get(i).language + ",";
        }
        System.out.println("Language: " + lang_string);
    }
    @GetMapping("/ForgotPass")
    public String ForgotPass(){
        return "ForgotPass";
    }
    @PostMapping("/ResetPass")
    public String ResetPass(@RequestParam ("UserLogin_or_Mail") String UserLogin_or_Mail){
        UserData user = userInterface.findByUserLogin(UserLogin_or_Mail);
        if(user == null){
            user = userInterface.findByUserMail(UserLogin_or_Mail);
        }
        if(user != null){
            //localhost:8080/
            myMailSenderService.sendEmail(user.getUserMail(), "Reset Password", "Click this link to reset your password: http://localhost:8080/reset_pass/" + user.getUserLogin());
            //https://project-jh-425111.ew.r.appspot.com/
            //myMailSenderService.sendEmail(user.getUserMail(), "Reset Password", "Click this link to reset your password: https://project-jh-425111.ew.r.appspot.com/reset_pass/" + user.getUserLogin());
            System.out.println("Email sent");
        }
        if (user == null){
            System.out.println("User not found");
        }else {
            System.out.println("User found");
        }
        return "redirect:/loginform";
    }
    @RequestMapping("/reset_pass/{UserLogin}")
    public String reset_pass(@PathVariable ("UserLogin") String UserLogin, Model model){
        System.out.println("UserLogin parameter: " + UserLogin);
        UserData user = userInterface.findByUserLogin(UserLogin);
        System.out.println(userInterface.findByUserLogin(UserLogin));
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new UserData()); // Add an empty user object to avoid null reference
        }
        return "ResetPass";
    }
    @PostMapping("/ChangePass")
    public String ChangePass(@RequestParam ("UserPass") String UserPass, @RequestParam ("UserLogin") String UserLogin){
        if(UserPass==null || UserLogin==null){
            return "PError";
        }

        UserData user = userInterface.findByUserLogin(UserLogin);
        user.setUserPass(passwordEncoder.encode(UserPass));
        userInterface.save(user);
        return "redirect:/loginform";
    }


}