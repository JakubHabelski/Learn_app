package com.learn.app.Controllers;

import com.learn.app.Classes.UserData;
import com.learn.app.Interfaces.UserInterface;
import com.learn.app.Interfaces.upload_Image_Interface;
import com.learn.app.Services.ImageUploadService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class EditUserProfileController {
    private final UserInterface userInterface;
    private final upload_Image_Interface upload_Image_Interface;

    public EditUserProfileController(UserInterface userInterface, upload_Image_Interface upload_Image_Interface) {
        this.userInterface = userInterface;
        this.upload_Image_Interface = upload_Image_Interface;
    }
    @GetMapping("/UserProfile")
    public String UserProfile(Model model, HttpSession session) throws Exception{
        UserData  LoggedUser = (UserData) session.getAttribute("LoggedUser");
        if (LoggedUser != null) {
            Upload_image upload_image = new Upload_image();
            if (LoggedUser.getPath() != null && !LoggedUser.getPath().isEmpty()) {
                String displayUrl = TestImageUpload.getImageUrl2(LoggedUser.getPath());
                File file = new File(displayUrl);
                model.addAttribute("image", file);
            } else {
                model.addAttribute("image", "");
            }
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
                                  @RequestPart("image") MultipartFile file) throws IOException {
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

        String path;
        String image_obj_path = "UserPhoto";
        ImageUploadService imageUploadService = new ImageUploadService();
        if (! file.isEmpty()) {
            imageUploadService.deleteImage(LoggedUser.getPath());
            path = imageUploadService.uploadImage(file, image_obj_path);
            LoggedUser.setPath(path);
        }
        userInterface.save(LoggedUser);
        return "redirect:/UserProfile";
    }
}
