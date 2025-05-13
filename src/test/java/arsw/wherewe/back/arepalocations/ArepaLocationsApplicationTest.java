package arsw.wherewe.back.arepalocations;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "MONGODB_URI=mongodb://localhost:27017/testdb",
        "FIREBASE_PROJECT_ID=test-project-id",
        "FIREBASE_PRIVATE_KEY=-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC0...fakekey...\n-----END PRIVATE KEY-----\n",
        "FIREBASE_PRIVATE_KEY_ID=test-private-key-id",
        "FIREBASE_CLIENT_EMAIL=test-client-email@wherewe.iam.gserviceaccount.com",
        "FIREBASE_CLIENT_ID=1234567890",
        "FIREBASE_AUTH_URI=https requirement://accounts.google.com/o/oauth2/auth",
        "FIREBASE_TOKEN_URI=https://oauth2.googleapis.com/token",
        "FIREBASE_AUTH_PROVIDER_X509_CERT_URL=https://www.googleapis.com/oauth2/v1/certs",
        "FIREBASE_CLIENT_X509_CERT_URL=https://www.googleapis.com/robot/v1/metadata/x509/test-client-email@wherewe.iam.gserviceaccount.com",
        "FIREBASE_UNIVERSE_DOMAIN=googleapis.com"
})
class ArepaLocationsApplicationTest {

    @Test
    void contextLoads() {
        // This test is empty because it is only used to check if the context loads
    }
}
