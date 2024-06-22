package com.learn.app.Services;

import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageUploadService {

    private Storage storage;
    private final String bucketName = "project-jh-storager";

    public ImageUploadService() {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public String uploadImage(MultipartFile file, String path) throws IOException {
        try {
            // Generate a unique identifier
            String uniqueID = UUID.randomUUID().toString();

            // Append the unique identifier to the file name
            String blobName = path + "/" + uniqueID + "_" + file.getOriginalFilename();
            BlobId blobId = BlobId.of(bucketName, blobName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
            storage.create(blobInfo, file.getInputStream());

            // Return the public URL of the uploaded file
            return path + "/" + uniqueID + "_" + file.getOriginalFilename();
        } catch (StorageException e) {
            e.printStackTrace();
            throw new IOException("Nie udało się przesłać pliku do Google Cloud Storage");
        }
    }
    public void deleteImage(String path) {
        BlobId blobId = BlobId.of(bucketName, path);
        storage.delete(blobId);
    }
}
