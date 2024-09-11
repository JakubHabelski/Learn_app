package com.learn.app.Controllers;


import com.learn.app.Classes.UserData;
import com.learn.app.Config.MyMailSenderService;
import com.learn.app.Interfaces.UserInterface;
import com.learn.app.Services.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Controller
public class RegisterController {

    private final UserInterface userInterface;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private MyMailSenderService myMailSenderService;
    @Autowired
    private ImageUploadService imageUploadService;


    public RegisterController(UserInterface userInterface, PasswordEncoder passwordEncoder) {
        this.userInterface = userInterface;
        this.passwordEncoder = passwordEncoder;
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
                               @RequestParam String UserMail,
                               @RequestPart(name = "file") MultipartFile file,
                               Model model
                                ) throws Exception{
        UUID uuid = UUID.randomUUID();
        String UserToken = uuid.toString();
        if(userInterface.findByUserLogin(UserLogin) != null){
            model.addAttribute("login_error", "login_error");
            return "redirect:/GetRegister";
        }
        UserData user = new UserData(UserName, UserSurname, UserLogin, UserPass, UserMail, UserToken, false);
        user.setUserPass(passwordEncoder.encode(UserPass));
        userInterface.save(user);
        myMailSenderService.sendEmail(UserMail, "Potwierdzenie rejestracji", "https://project-jh-425111.ew.r.appspot.com/register_success/" + UserToken);
        ImageUploadService imageUploadService = new ImageUploadService();
        String path;
        String image_obj_path = "UserPhoto";
        if ( file.isEmpty()) {
            path = "";
            user.setPath(path);
        } else {
            imageUploadService.uploadImage(file, image_obj_path);
            path = "UserPhoto/" + file.getOriginalFilename();
            user.setPath(path);
        }
        return "redirect:/loginform";
    }
    @RequestMapping("/register_success/{UserToken}")

    public String register_success( @PathVariable String UserToken){
        UserData user = userInterface.findByUserToken(UserToken);
        user.setUserActive(true);
        userInterface.save(user);
        return "redirect:/loginform";
    }
    @RequestMapping("/jkj")
    @ResponseBody
    public UserData home(){
        System.out.println("-------------------------------------------------");
        UserData user = userInterface.findByUserToken("20857c76-304c-41b1-a100-df1dbb97bd2e");
        System.out.println(user.getUserActive());
        user.setUserActive(true);
        System.out.println(user.getUserActive());
        userInterface.save(user);
        System.out.println("-------------------------------------------------");
        return user;
    }
    @GetMapping("/checkLogin")
    public ResponseEntity<Boolean> checkLogin(@RequestParam String UserLogin){
        boolean isTaken = userInterface.findByUserLogin(UserLogin) != null;
        return ResponseEntity.ok(isTaken);
    }
    @GetMapping("/checkMail")
    public ResponseEntity<Boolean> checkMail(@RequestParam String UserMail){
        boolean isTaken = userInterface.findByUserMail(UserMail) != null;
        return ResponseEntity.ok(isTaken);
    }

}
