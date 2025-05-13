package arsw.wherewe.back.arepalocations.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.project-id}")
    private String projectId;

    @Value("${firebase.private-key}")
    private String privateKey;

    @Value("${firebase.private-key-id}")
    private String privateKeyId;

    @Value("${firebase.client-email}")
    private String clientEmail;

    @Value("${firebase.client-id}")
    private String clientId;

    @Value("${firebase.auth-uri}")
    private String authUri;

    @Value("${firebase.token-uri}")
    private String tokenUri;

    @Value("${firebase.auth-provider-x509-cert-url}")
    private String authProviderX509CertUrl;

    @Value("${firebase.client-x509-cert-url}")
    private String clientX509CertUrl;

    @Value("${firebase.universe-domain}")
    private String universeDomain;

    @PostConstruct
    public void initialize() throws IOException {
        // Construct JSON string for service account
        String serviceAccountJson = String.format(
                "{\"type\": \"service_account\", \"project_id\": \"%s\", \"private_key_id\": \"%s\", \"private_key\": \"%s\", \"client_email\": \"%s\", \"client_id\": \"%s\", \"auth_uri\": \"%s\", \"token_uri\": \"%s\", \"auth_provider_x509_cert_url\": \"%s\", \"client_x509_cert_url\": \"%s\", \"universe_domain\": \"%s\"}",
                projectId, privateKeyId, privateKey.replace("\\n", "\n"), clientEmail, clientId, authUri, tokenUri, authProviderX509CertUrl, clientX509CertUrl, universeDomain
        );

        // Initialize Firebase with credentials
        GoogleCredentials credentials = null;
        try {
            credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(serviceAccountJson.getBytes(StandardCharsets.UTF_8))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}