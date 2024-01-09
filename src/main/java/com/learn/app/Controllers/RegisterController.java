package com.learn.app.Controllers;


import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.UserInterface;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final UserInterface userInterface;

    public RegisterController(UserInterface userInterface) {
        this.userInterface = userInterface;
    }
    @GetMapping("/GetRegister")
    public String GetRegister(){
        return "RegisterForm";
    }
    @PostMapping("/PostRegister")
    public String PostRegister(@RequestParam String UserName,
                               @RequestParam String UserSurname,
                               @RequestParam String UserLogin,
                               @RequestParam String UserPass,
                               @RequestParam String UserMail
                                ) throws Exception{

        UserData user = new UserData(UserName, UserSurname, UserLogin, UserPass, UserMail);

        userInterface.save(user);
        return "redirect:/loginform";
    }
}
