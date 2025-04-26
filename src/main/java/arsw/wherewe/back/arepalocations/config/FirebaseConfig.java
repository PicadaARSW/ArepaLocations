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

        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        String serviceAccountJson = String.format("""
            {
              "type": "service_account",
              "project_id": "%s",
              "private_key_id": "%s",
              "private_key": "%s",
              "client_email": "%s",
              "client_id": "%s",
              "auth_uri": "%s",
              "token_uri": "%s",
              "auth_provider_x509_cert_url": "%s",
              "client_x509_cert_url": "%s",
              "universe_domain": "%s"
            }
            """,
                require(dotenv, "FIREBASE_PROJECT_ID"),
                require(dotenv, "FIREBASE_PRIVATE_KEY_ID"),
                formatPrivateKey(require(dotenv, "FIREBASE_PRIVATE_KEY")),
                require(dotenv, "FIREBASE_CLIENT_EMAIL"),
                require(dotenv, "FIREBASE_CLIENT_ID"),
                require(dotenv, "FIREBASE_AUTH_URI"),
                require(dotenv, "FIREBASE_TOKEN_URI"),
                require(dotenv, "FIREBASE_AUTH_PROVIDER_X509_CERT_URL"),
                require(dotenv, "FIREBASE_CLIENT_X509_CERT_URL"),
                require(dotenv, "FIREBASE_UNIVERSE_DOMAIN")
        );

        ByteArrayInputStream stream = new ByteArrayInputStream(serviceAccountJson.getBytes(StandardCharsets.UTF_8));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(stream))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    private String require(Dotenv dotenv, String key) {
        return dotenv.get(key);
    }

    private String formatPrivateKey(String privateKey) {
        return privateKey.replace("\\n", "\n");
    }
}
