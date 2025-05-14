package arsw.wherewe.back.arepalocations;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"MONGODB_URI=mongodb://localhost:27017/testdb"})
class ArepaLocationsApplicationTest {

    @Test
    void contextLoads() {
        // This test is empty because it is only used to check if the context loads
    }
}
