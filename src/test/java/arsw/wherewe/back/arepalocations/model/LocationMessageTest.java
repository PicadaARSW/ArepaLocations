package arsw.wherewe.back.arepalocations.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationMessageTest {

    @Test
    void constructor_withParameters_initializesFields() {
        LocationMessage locationMessage = new LocationMessage("user123", 40.785091, -73.968285, "group123");
        assertEquals("user123", locationMessage.getUserId());
        assertEquals(40.785091, locationMessage.getLatitude());
        assertEquals(-73.968285, locationMessage.getLongitude());
        assertEquals("group123", locationMessage.getGroupId());
        assertEquals("active", locationMessage.getStatus());
    }

    @Test
    void setters_updateFields() {
        LocationMessage locationMessage = new LocationMessage();
        locationMessage.setUserId("user123");
        locationMessage.setLatitude(40.785091);
        locationMessage.setLongitude(-73.968285);
        locationMessage.setGroupId("group123");
        locationMessage.setStatus("inactive");
        locationMessage.setAccuracy(10.0f);
        locationMessage.setSpeed(1.5f);
        locationMessage.setHeading(90.0f);
        locationMessage.setAltitude(50.0f);
        locationMessage.setTimestamp(1696118400000L);

        assertEquals("user123", locationMessage.getUserId());
        assertEquals(40.785091, locationMessage.getLatitude());
        assertEquals(-73.968285, locationMessage.getLongitude());
        assertEquals("group123", locationMessage.getGroupId());
        assertEquals("inactive", locationMessage.getStatus());
        assertEquals(10.0f, locationMessage.getAccuracy());
        assertEquals(1.5f, locationMessage.getSpeed());
        assertEquals(90.0f, locationMessage.getHeading());
        assertEquals(50.0f, locationMessage.getAltitude());
        assertEquals(1696118400000L, locationMessage.getTimestamp());
    }

    @Test
    void toString_returnsCorrectFormat() {
        LocationMessage locationMessage = new LocationMessage("user123", 40.785091, -73.968285, "group123");
        locationMessage.setStatus("active");
        locationMessage.setAccuracy(10.0f);
        locationMessage.setSpeed(1.5f);
        locationMessage.setHeading(90.0f);
        locationMessage.setAltitude(50.0f);
        locationMessage.setTimestamp(1696118400000L);
        String expected = "LocationMessage{userId='user123', latitude=40.785091, longitude=-73.968285, groupId='group123', status='active', accuracy=10.0, speed=1.5, heading=90.0, altitude=50.0, timestamp=1696118400000}";
        assertEquals(expected, locationMessage.toString());
    }
}