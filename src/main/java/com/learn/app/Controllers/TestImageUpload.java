package com.learn.app.Controllers;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import com.learn.app.Classes.image;
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
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.concurrent.TimeUnit;

@Controller
public class TestImageUpload {

    @Autowired
    com.learn.app.Interfaces.Upload_Image_Interface Upload_Image_Interface;

    public static String getImageUrl(String bucketName, String objectName) throws IOException {
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream( "src" + File.separator + "main" + File.separator + "resources" + File.separator +"learnapp-jh-klucz.json")))
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
  //  public static final String BASE_URL = "https://storage.googleapis.com/";


    private static final String BASE_URL = "https://storage.googleapis.com/";

    public static String getImageUrl2(String objectName) throws Exception {
        // Initialize a Cloud Storage client
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // Get the blob of the JSON file from Cloud Storage
        Blob jsonBlob = storage.get(BlobId.of("project-jh-storager", "project-jh-425111-da29a7a8eed2.json"));

        // Get a ReadChannel for the JSON blob
        ReadChannel jsonBlobChannel = jsonBlob.reader();

        // Convert the ReadChannel to an InputStream
        InputStream jsonBlobInputStream = Channels.newInputStream(jsonBlobChannel);

        // Use the InputStream with fromStream()
        Storage storageWithCredentials = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(jsonBlobInputStream))
                .build()
                .getService();

        // Get BlobId of the image file
        BlobId blobId = BlobId.of("project-jh-storager", objectName);

        // Generate signed URL for the image file
        URL signedUrl = storageWithCredentials.signUrl(
                BlobInfo.newBuilder(blobId).build(),
                15, TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature()
        );

        // Return the signed URL as a string
        return signedUrl.toString();
    }

    // private static final String BASE_URL = "https://storage.googleapis.com/";
    private static final String BUCKET_NAME = "lproject-jh-storager";
    private static final String CREDENTIALS_FILE_PATH = "project-jh-425111-da29a7a8eed2.json";

    public static String getImageUrl3(String objectName) throws Exception {
        // Initialize a Cloud Storage client
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH)))
                .build()
                .getService();

        // Get BlobId of the image file
        BlobId blobId = BlobId.of(BUCKET_NAME, objectName);

        // Generate signed URL for the image file
        URL signedUrl = storage.signUrl(BlobInfo.newBuilder(blobId).build(), 15, TimeUnit.MINUTES, Storage.SignUrlOption.withV4Signature());

        // Return the signed URL as a string
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

    public String show_image( Model model) throws Exception {


        String displayUrl = getImageUrl2( "test/DFT.jpg");
        File file = new File(displayUrl);
        System.out.println(displayUrl);
        model.addAttribute("file", file);

        return "testshowimage";
    }

}
