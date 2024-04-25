package lk.ijse.dep11.edupanel;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lk.ijse.dep11.edupanel.converter.LecturerTypeConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.io.InputStream;


@SpringBootApplication
public class AppInitializer {

    public static void main(String[] args) {
        System.setProperty("application.title", "EduPanel");
        System.setProperty("application.version", "v1.0.0");

        SpringApplication.run(AppInitializer.class, args);
    }

    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }

    @Bean
    public Bucket defaultBucket(@Value("${application.firebase.storage.bucket}")String storageBucket) throws IOException {
        InputStream serviceAccount =
                new ClassPathResource("/edu-panel-574fb-firebase-adminsdk-49xdf-77c939bd52.json").getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(storageBucket)
                .build();

        FirebaseApp.initializeApp(options);
        return StorageClient.getInstance().bucket();
    }

}
