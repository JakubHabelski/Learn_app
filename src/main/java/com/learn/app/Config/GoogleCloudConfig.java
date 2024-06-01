package com.learn.app.Config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.nio.channels.Channels;

@Configuration
public class GoogleCloudConfig {

    @Bean
    public Storage googleCloudStorage() throws Exception {
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

        return storageWithCredentials;
    }
}
