package arsw.wherewe.back.arepalocations.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            System.out.println("Inicializando Firebase...");
            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-service-account.json");
            if (serviceAccount == null) {
                throw new IOException("Archivo firebase-service-account.json no encontrado en el classpath");
            }
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase inicializado exitosamente");

                try {
                    FirebaseMessaging.getInstance();
                    System.out.println("FirebaseMessaging instanciado exitosamente");
                } catch (Exception e) {
                    System.err.println("Error al instanciar FirebaseMessaging: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Firebase ya estaba inicializado");
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar Firebase: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado al inicializar Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }
}