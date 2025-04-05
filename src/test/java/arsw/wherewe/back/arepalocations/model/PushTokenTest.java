package arsw.wherewe.back.arepalocations.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PushTokenTest {

    @Test
    void constructor_initializesFields() {
        PushToken pushToken = new PushToken("user123", "group123", "token123");
        assertEquals("user123", pushToken.getUserId());
        assertEquals("group123", pushToken.getGroupId());
        assertEquals("token123", pushToken.getToken());
    }

    @Test
    void setters_updateFields() {
        PushToken pushToken = new PushToken();
        pushToken.setUserId("user123");
        pushToken.setGroupId("group123");
        pushToken.setToken("token123");
        pushToken.setId("507f1f77bcf86cd799439011");

        assertEquals("user123", pushToken.getUserId());
        assertEquals("group123", pushToken.getGroupId());
        assertEquals("token123", pushToken.getToken());
        assertEquals("507f1f77bcf86cd799439011", pushToken.getId());
    }

    @Test
    void defaultConstructor_initializesEmptyFields() {
        PushToken pushToken = new PushToken();
        assertNotNull(pushToken);
    }
}