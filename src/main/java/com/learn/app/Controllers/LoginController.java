package com.learn.app.Controllers;

import com.detectlanguage.DetectLanguage;
import com.detectlanguage.Result;
import com.detectlanguage.errors.APIError;
import com.learn.app.Classes.FlashCardSet;
import com.learn.app.Classes.UserData;
import com.learn.app.Config.MyMailSenderService;
import com.learn.app.Interfaces.AddFlashCardInterface;
import com.learn.app.Interfaces.AddFlashCardSetInterface;
import com.learn.app.Interfaces.UserInterface;
import com.learn.app.Services.TestDatabaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

@Controller
public class LoginController {
   // Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private final AddFlashCardSetInterface addFlashCardSetInterface;
    private final AddFlashCardInterface addFlashCardInterface;
    private final PasswordEncoder passwordEncoder;
    private final TestDatabaseService testDatabaseService;


    @Autowired
    private UserInterface userInterface; // Dodaj adnotację @Autowired
    @Autowired
    private MyMailSenderService myMailSenderService;

    public LoginController(AddFlashCardSetInterface addFlashCardSetInterface, PasswordEncoder passwordEncoder, MyMailSenderService myMailSenderService, AddFlashCardInterface addFlashCardInterface, TestDatabaseService testDatabaseService) {
        this.addFlashCardSetInterface = addFlashCardSetInterface;
        this.passwordEncoder = passwordEncoder;
        this.myMailSenderService = myMailSenderService;
        this.addFlashCardInterface = addFlashCardInterface;
        this.testDatabaseService = testDatabaseService;

    }





  //  @Autowired
  //  private EntityManager entityManager;

  //  @Autowired
  //  private UserService userService;


    UserData user = new UserData();

  //  boolean userfound = true;


    @GetMapping(value = "/loginform")
    public String loginform(Model model, HttpSession session) throws APIError {
        testDatabaseService.testConnection();
        user.setUserLogin("");
        user.setUserPass("");
        model.addAttribute("user", user);
        //System.out.println("Login Form");
        if(session.getAttribute("login_error")== "login_error"){
            model.addAttribute("login_error", "login_error");

        }
        // https://detectlanguage.com/
         DetectLanguage.apiKey = "a257d4911b341402d8a05ffc386dae17";
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
         if (LoggedUser == null) {
             System.out.println("User not logged in");
         }
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            // Znajdź użytkownika w bazie danych
            UserData user = userInterface.findByUserLogin(username);
            if (user != null) {
                if(!user.getUserActive()){
                    return "redirect:/loginform";
                }
                List<FlashCardSet> suggestedSets = addFlashCardSetInterface.getRecommendedSets(user.getUserID());
                List<FlashCardSet> flashCardSets = addFlashCardSetInterface.findByUserID(user.getUserID());

                Iterator<FlashCardSet> iterator = suggestedSets.iterator();
                    while (iterator.hasNext()) {
                        FlashCardSet suggestedSet = iterator.next();
                        for (FlashCardSet flashCardSet : flashCardSets) {
                            if (suggestedSet.getSetName().equals(flashCardSet.getSetName()) &&
                                suggestedSet.getSetDescription().equals(flashCardSet.getSetDescription())) {
                                iterator.remove();
                                break;
                            }
                        }
                    }

                    iterator = suggestedSets.iterator();
                    while (iterator.hasNext()) {
                        FlashCardSet suggestedSet = iterator.next();
                        if (addFlashCardInterface.customFindBySetID(suggestedSet.getSetID()).isEmpty()) {
                            iterator.remove();
                        }
}

                model.addAttribute("suggestedSets", suggestedSets);
                session.setAttribute("LoggedUser", user);
                model.addAttribute("user", user);
                model.addAttribute("flashCardSets", addFlashCardSetInterface.findByUserID(user.getUserID()));
                //model.addAttribute("flashCardSets", addFlashCardSetInterface.getSetsByUserID(user.getUserID()));
                /*List<FlashCardSet> flashCardSets = addFlashCardSetInterface.getSetsByUserID(user.getUserID());
                for (FlashCardSet flashCardSet : flashCardSets) {
                    System.out.println("getSetsByUserID: "+flashCardSet.getSetName());
                }
                 */
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
        StringBuilder lang_string = new StringBuilder();
        for (Result result : results) {
            lang_string.append(result.language).append(",");
        }
        System.out.println("Language: " + lang_string);
    }
    @GetMapping("/ForgotPass")
    public String ForgotPass(){
        return "ForgotPass";
    }
    @PostMapping("/ResetPass")
    public String ResetPass(@RequestParam("UserLogin_or_Mail") String UserLogin_or_Mail) {
        UserData user = userInterface.findByUserLogin(UserLogin_or_Mail);
        if (user == null) {
            user = userInterface.findByUserMail(UserLogin_or_Mail);
        }

        if (user != null) {
            // Debugowanie, aby upewnić się, że wartości nie są null
            String userLogin = user.getUserLogin();
            if (userLogin != null) {
                String resetLink = "https://project-jh-425111.ew.r.appspot.com/reset_pass/" + userLogin;
                myMailSenderService.sendEmail(user.getUserMail(), "Reset Password", "Click this link to reset your password: " + resetLink);
                System.out.println("Email sent to " + user.getUserMail() + " with reset link: " + resetLink);
            } else {
                System.out.println("User login is null for user: " + user.getUserMail());
            }
        } else {
            System.out.println("User not found");
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
    public String ChangePass(@RequestParam ("UserPass") String UserPass,
                             @RequestParam ("UserLogin") String UserLogin){
        if(UserPass==null || UserLogin==null){
            return "Error";
        }
        UserData user = userInterface.findByUserLogin(UserLogin);
        user.setUserPass(passwordEncoder.encode(UserPass));
        userInterface.save(user);
        return "redirect:/loginform";
    }


}