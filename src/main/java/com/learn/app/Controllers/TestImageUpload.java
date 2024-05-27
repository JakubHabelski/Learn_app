package com.learn.app.Controllers;

import com.learn.app.Services.ImageUploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class TestImageUpload {

    @GetMapping(value = "/test_image_upload_get")
    public String test_image_upload() {
        return "TestImageUpload";
    }
    @PostMapping(value = "/test_image_upload_post")
    public String test_image_upload_post(@RequestPart(name = "file") MultipartFile file,
                                         Model model) throws IOException {
        ImageUploadService imageUploadService = new ImageUploadService();
        imageUploadService.uploadImage(file);
        String fileUrl = imageUploadService.uploadImage(file);

        System.out.println(file.getName());
        model.addAttribute("fileUrl", fileUrl);

        return "redirect:/test_image_upload_get";
    }

}
