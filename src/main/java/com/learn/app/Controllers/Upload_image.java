package com.learn.app.Controllers;

import com.learn.app.Classes.image;
import com.learn.app.Interfaces.upload_Image_Interface;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;



@Controller
public class Upload_image {
    public Upload_image() {
    }
    
    private upload_Image_Interface upload_Image_Interface;

    @Autowired
    public Upload_image(upload_Image_Interface upload_Image_Interface) {
        this.upload_Image_Interface = upload_Image_Interface;
    }

    @GetMapping(value = "/upload_image_get")
    public String upload_image() {
        return "upload_image";
    }
    @PostMapping(value = "/upload_image_post")
    public image upload_image(@RequestPart(name = "file") MultipartFile file) {
        image image_obj = new image();
        final  String UploadPath= "src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"uploads"+File.separator;
        File uploadDirectory = new File(UploadPath);
        uploadDirectory.mkdirs();

        try {
            File formFile = new File(UploadPath + file.getOriginalFilename());
            OutputStream outputStream = new FileOutputStream(formFile);
            InputStream inputStream = file.getInputStream();
            IOUtils.copy(inputStream, outputStream);
            image_obj.setPath(formFile.getPath());

           // upload_Image_Interface.save(image_obj);
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
          //  return "Coś poszło  źle" + e.getMessage();
        }
        return image_obj;
    }
    @GetMapping("photo/{name}")
    public ResponseEntity showImage(@PathVariable String name) throws IOException {
        File file = new File("src"+File.separator+"main"+File.separator+"resources"+File.separator+"static"+File.separator+"uploads"+File.separator + name);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(URLConnection.guessContentTypeFromName(name)))
                .body(Files.readAllBytes(file.toPath()));
    }
    
    
}
