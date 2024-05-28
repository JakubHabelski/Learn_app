package com.learn.app.Config;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class GoogleCloudConfig {

    @Bean
    public Storage googleCloudStorage() {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "learnapp-jh-klucz.json";
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", path);
        return StorageOptions.getDefaultInstance().getService();
    }
}
