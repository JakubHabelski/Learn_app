package com.learn.app.Controllers;


import com.learn.app.Classes.UserData;
import com.learn.app.Classes.image;
import com.learn.app.Interfaces.UserInterface;
import com.learn.app.Interfaces.upload_Image_Interface;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class RegisterController {

    private final UserInterface userInterface;
    private final upload_Image_Interface upload_Image_Interface;

    private final PasswordEncoder passwordEncoder;

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
                               @RequestPart(name = "file") MultipartFile file
                                ) throws Exception{

        UserData user = new UserData(UserName, UserSurname, UserLogin, UserPass, UserMail);
       // user.setUserPass(passwordEncoder.encode(UserPass));
        userInterface.save(user);
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

}
