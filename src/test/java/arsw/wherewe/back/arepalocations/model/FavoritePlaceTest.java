package arsw.wherewe.back.arepalocations.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FavoritePlaceTest {


    @Test
    void setters_updateFields() {
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.setPlaceName("Central Park");
        favoritePlace.setLatitude(40.785091);
        favoritePlace.setLongitude(-73.968285);
        favoritePlace.setGroupId("group123");
        favoritePlace.setRadius(100.0f);
        favoritePlace.setTimestamp(1696118400000L);

        assertEquals("Central Park", favoritePlace.getPlaceName());
        assertEquals(40.785091, favoritePlace.getLatitude());
        assertEquals(-73.968285, favoritePlace.getLongitude());
        assertEquals("group123", favoritePlace.getGroupId());
        assertEquals(100.0f, favoritePlace.getRadius());
        assertEquals(1696118400000L, favoritePlace.getTimestamp());
    }

    @Test
    void toString_returnsCorrectFormat() {
        FavoritePlace favoritePlace = new FavoritePlace("Central Park", 40.785091, -73.968285, "group123", 100.0f);
        favoritePlace.setId("507f1f77bcf86cd799439011");
        String expected = "FavoritePlace{id='507f1f77bcf86cd799439011', placeName='Central Park', latitude=40.785091, longitude=-73.968285, groupId='group123', radius=100.0, timestamp=" + favoritePlace.getTimestamp() + "}";
        assertEquals(expected, favoritePlace.toString());
    }
}