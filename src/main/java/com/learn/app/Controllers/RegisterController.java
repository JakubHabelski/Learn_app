package com.learn.app.Controllers;


import com.learn.app.Classes.UserData;
import com.learn.app.Classes.image;
import com.learn.app.Config.MyMailSenderService;
import com.learn.app.Interfaces.UserInterface;
import com.learn.app.Interfaces.upload_Image_Interface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Controller
public class RegisterController {

    private final UserInterface userInterface;
    private final upload_Image_Interface upload_Image_Interface;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private MyMailSenderService myMailSenderService;

    public RegisterController(UserInterface userInterface, upload_Image_Interface upload_Image_Interface, PasswordEncoder passwordEncoder) {
        this.userInterface = userInterface;
        this.upload_Image_Interface = upload_Image_Interface;
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
       // user.setUserPass(passwordEncoder.encode(UserPass));
        userInterface.save(user);
        myMailSenderService.sendEmail(UserMail, "Potwierdzenie rejestracji", "http://localhost:8080/register_success/" + UserToken);
        //saving user image file to da
        Upload_image upload_image = new Upload_image();
        image uploadedImage = upload_image.upload_image(file);
        String path = uploadedImage.getPath();
       // image image_obj = new image();
        uploadedImage.setUserID(user.getUserID());
        uploadedImage.setPath(path);
        uploadedImage.setFlashCardId(null);
        System.out.println(uploadedImage.getPath());
        upload_Image_Interface.save(uploadedImage);
        return "redirect:/loginform";
    }
    @RequestMapping("/register_success/{UserToken}")
    @ResponseBody
    public UserData register_success( @PathVariable String UserToken){
        UserData user = userInterface.findByUserToken(UserToken);
        user.setUserActive(true);
        userInterface.save(user);
        return user;
    }
    @RequestMapping("/")
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

}
