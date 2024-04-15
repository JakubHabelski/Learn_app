package com.learn.app.Controllers;

import com.learn.app.Classes.UserData;
import com.learn.app.Classes.image;
import com.learn.app.Interfaces.UserInterface;
import com.learn.app.Interfaces.upload_Image_Interface;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Controller
public class EditUserProfileController {
    private final UserInterface userInterface;
    private final upload_Image_Interface upload_Image_Interface;

    public EditUserProfileController(UserInterface userInterface, upload_Image_Interface upload_Image_Interface) {
        this.userInterface = userInterface;
        this.upload_Image_Interface = upload_Image_Interface;
    }
    @GetMapping("/UserProfile")
    public String UserProfile(Model model, HttpSession session){
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");


        Upload_image upload_image = new Upload_image();
        try{
            String path = upload_Image_Interface.findPathByUserID(LoggedUser.getUserID());
            path = path.replace("src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "uploads" + File.separator, "");
            System.out.println("path: " + path);
            ResponseEntity<byte[]> imageResponse = upload_image.showImage(path);
            String imageBase64 = Base64.getEncoder().encodeToString(imageResponse.getBody());
            String imageUrl = "data:" + imageResponse.getHeaders().getContentType().toString() + ";base64," + imageBase64;
            model.addAttribute("image", imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(LoggedUser != null){
            model.addAttribute("user", LoggedUser);
            return "UserProfile";
        }


        return "redirect:/loginform";
    }

    @PostMapping("/EditUserProfile")
    public String EditUserProfile(HttpSession session,
                                  @Param("userName") String userName,
                                  @Param("userSurname") String userSurname,
                                  @Param("UserMail") String UserMail,
                                  @RequestPart("image") MultipartFile file){
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        if (userName != null &&  !userName.isEmpty()) {
            LoggedUser.setUserName(userName);
        }else{
            LoggedUser.setUserName(LoggedUser.getUserName());
        }
        if (userSurname != null &&  !userSurname.isEmpty()) {
            LoggedUser.setUserSurname(userSurname);
        } else {
            LoggedUser.setUserSurname(LoggedUser.getUserSurname());
        }
        if (UserMail != null && !UserMail.isEmpty()) {
            LoggedUser.setUserMail(UserMail);
        } else {
            LoggedUser.setUserMail(LoggedUser.getUserMail());
        }
        userInterface.save(LoggedUser);
        if(!file.isEmpty()){
            Upload_image upload_image = new Upload_image();
            Long id= Long.valueOf(upload_Image_Interface.findIdByUserID(LoggedUser.getUserID()));
            image uploadedImage = upload_image.upload_image(file);
            uploadedImage.setId(id);
            uploadedImage.setUserID(LoggedUser.getUserID());
            uploadedImage.setPath(uploadedImage.getPath());
            upload_Image_Interface.save(uploadedImage);
        }

        return "redirect:/UserProfile";
    }
}
