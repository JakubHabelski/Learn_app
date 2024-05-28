package com.learn.app.Controllers;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.learn.app.Classes.image;
import com.learn.app.Interfaces.upload_Image_Interface;
import com.learn.app.Services.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Controller
public class TestImageUpload {

    @Autowired
    upload_Image_Interface Upload_Image_Interface;

    public static String getImageUrl(String bucketName, String objectName) throws IOException {
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream("src"+ File.separator+"main"+File.separator+"resources"+File.separator+"learnapp-jh-klucz.json")))
                .build()
                .getService();

        // Get BlobId of the image file
        BlobId blobId = BlobId.of(bucketName, objectName);

        // Get BlobInfo of the image file
        BlobInfo blobInfo = storage.get(blobId);

        // Generate signed URL for the image file
        URL signedUrl = storage.signUrl(blobInfo, 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());

        return signedUrl.toString();
    }
    @GetMapping(value = "/test_image_upload_get")
    public String test_image_upload() {
        return "TestImageUpload";
    }
    @PostMapping(value = "/test_image_upload_post")
    public String test_image_upload_post(@RequestPart(name = "file") MultipartFile file,
                                         Model model) throws IOException {

        ImageUploadService imageUploadService = new ImageUploadService();
        imageUploadService.uploadImage(file,"test");
        image image_obj = new image();

        image_obj.setPath(file.getOriginalFilename());
        Upload_Image_Interface.save(image_obj);
        return "redirect:/show_image";
    }
    @GetMapping(value="/show_image")

    public String show_image( Model model) throws IOException {


        String displayUrl = getImageUrl("learn-app-jh-bucket", "test/001.jpg");
        File file = new File(displayUrl);
        System.out.println(displayUrl);
        model.addAttribute("file", file);

        return "testshowimage";
    }

}
