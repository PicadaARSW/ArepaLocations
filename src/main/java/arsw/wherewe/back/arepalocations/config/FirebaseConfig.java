package arsw.wherewe.back.arepalocations.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() throws IOException {
        Dotenv dotenv = Dotenv.load();
        String firebaseCredentials = dotenv.get("FIREBASE_CREDENTIALS");

        if (firebaseCredentials == null || firebaseCredentials.isEmpty()) {
            throw new IOException("FIREBASE_CREDENTIALS no está configurada en el archivo .env");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(
                        new ByteArrayInputStream(firebaseCredentials.getBytes(StandardCharsets.UTF_8))
                ))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
