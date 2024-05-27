package com.learn.app.Services;

import com.google.cloud.storage.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ImageUploadService {

    private Storage storage;
    private final String bucketName = "staging.learnapp-jh.appspot.com";

    public ImageUploadService() {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public String uploadImage(MultipartFile file) throws IOException {
        try {
            BlobId blobId = BlobId.of(bucketName, file.getOriginalFilename());
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            storage.create(blobInfo, file.getInputStream());

            // Zwracamy publiczny URL przesłanego pliku
            return "https://storage.googleapis.com/" + bucketName + "/" + file.getOriginalFilename();
        } catch (StorageException e) {
            e.printStackTrace();
            throw new IOException("Nie udało się przesłać pliku do Google Cloud Storage");
        }
    }
}
