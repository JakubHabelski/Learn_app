package com.learn.app;

import com.learn.app.Interfaces.AddFlashCardSetInterface;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String userpanel(@RequestParam("UserLogin") String UserLogin,
                            @RequestParam("UserPass") String UserPass,
                            Model model,
                            HttpSession session) {

        TypedQuery<UserData> query =
                entityManager.createQuery("SELECT u FROM UserData u", UserData.class);

        for (UserData userdata : query.getResultList()) {
            logger.warn(userdata.toString());
            if (userdata.getUserLogin().equals(UserLogin) && userdata.getUserPass().equals(UserPass)){
             //   UserData user = new UserData(userdata.getUserID(),UserLogin, UserPass);
             ;
                user.setUserID(userdata.getUserID());
                user.setUserPass(UserPass);
                user.setUserLogin(UserLogin);
                model.addAttribute("user", user);
                session.setAttribute("LoggedUser", user);

                //List<FlashCardSet> flashCardSets = addFlashCardSetInterface.findAll();
                model.addAttribute("flashCardSets", addFlashCardSetInterface.findAll());

                return "UserPanel";
            }
        }
       return "PError";
    }
}

