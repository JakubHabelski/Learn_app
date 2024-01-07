package com.learn.app;


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
    public String PostRegister(@RequestParam("UserName") String UserName,
                               @RequestParam("UserSurname") String UserSurname,
                               @RequestParam("UserLogin") String UserLogin,
                               @RequestParam("UserPass") String UserPass,
                               @RequestParam("UserMail") String UserMail
                                ) throws Exception{

        UserData user = new UserData(UserName, UserSurname, UserLogin, UserPass, UserMail);

        userInterface.save(user);
        return "redirect:/loginform";
    }
}
