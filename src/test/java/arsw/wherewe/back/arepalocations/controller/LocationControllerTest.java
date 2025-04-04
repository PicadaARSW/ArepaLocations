package arsw.wherewe.back.arepalocations.controller;

import arsw.wherewe.back.arepalocations.model.FavoritePlace;
import arsw.wherewe.back.arepalocations.model.LocationMessage;
import arsw.wherewe.back.arepalocations.service.FavoritePlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LocationControllerTest {

    // Constants for test data
    private static final String GROUP_ID = "group123";
    private static final String PLACE_ID = "place456";
    private static final String USER_ID = "user789";
    private static final double LATITUDE = 40.785091;
    private static final double LONGITUDE = -73.968285;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private FavoritePlaceService favoritePlaceService;

    @InjectMocks
    private LocationController locationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void sendLocation_sendsMessageToGroup() {
//        // Arrange: Create a LocationMessage with test data
//        LocationMessage locationMessage = new LocationMessage();
//        locationMessage.setGroupId(GROUP_ID);
//        locationMessage.setUserId(USER_ID);
//        locationMessage.setLatitude(LATITUDE);
//        locationMessage.setLongitude(LONGITUDE);
//        locationMessage.setStatus("active");
//
//        // Act: Call the sendLocation method
//        locationController.sendLocation(locationMessage);
//
//        // Assert: Verify that the message is sent to the correct topic with the correct payload
//        verify(simpMessagingTemplate, times(1))
//                .convertAndSend("/topic/location/" + GROUP_ID, locationMessage);
//    }

    @Test
    void addFavoritePlace_savesAndSendsMessage() {
        // Arrange: Create a FavoritePlace with test data
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.setGroupId(GROUP_ID);
        favoritePlace.setPlaceName("Central Park");
        favoritePlace.setLatitude(LATITUDE);
        favoritePlace.setLongitude(LONGITUDE);
        favoritePlace.setRadius(100.0f);

        // Mock the service to return the saved place
        when(favoritePlaceService.saveFavoritePlace(any(FavoritePlace.class))).thenReturn(favoritePlace);

        // Act: Call the addFavoritePlace method
        locationController.addFavoritePlace(favoritePlace);

        // Assert: Verify that the place is saved and the message is sent
        verify(favoritePlaceService, times(1)).saveFavoritePlace(favoritePlace);
        verify(simpMessagingTemplate, times(1))
                .convertAndSend("/topic/favoritePlace/" + GROUP_ID, favoritePlace);
    }

    @Test
    void editFavoritePlace_editsAndSendsMessage() {
        // Arrange: Create a FavoritePlace with test data
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.setId(PLACE_ID);
        favoritePlace.setGroupId(GROUP_ID);
        favoritePlace.setPlaceName("Updated Park");
        favoritePlace.setLatitude(LATITUDE);
        favoritePlace.setLongitude(LONGITUDE);

        // Mock the service to return the edited place
        when(favoritePlaceService.editFavoritePlace(any(FavoritePlace.class))).thenReturn(favoritePlace);

        // Act: Call the editFavoritePlace method
        locationController.editFavoritePlace(favoritePlace);

        // Assert: Verify that the place is edited and the message is sent
        verify(favoritePlaceService, times(1)).editFavoritePlace(favoritePlace);
        verify(simpMessagingTemplate, times(1))
                .convertAndSend("/topic/favoritePlaceEdited/" + GROUP_ID, favoritePlace);
    }

    @Test
    void deleteFavoritePlace_deletesAndSendsMessage() {
        // Arrange: Create a FavoritePlace with test data
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.setId(PLACE_ID);
        favoritePlace.setGroupId(GROUP_ID);

        // Act: Call the deleteFavoritePlace method
        locationController.deleteFavoritePlace(favoritePlace);

        // Assert: Verify that the place is deleted and the message is sent
        verify(favoritePlaceService, times(1)).deleteFavoritePlace(PLACE_ID);
        verify(simpMessagingTemplate, times(1))
                .convertAndSend("/topic/favoritePlaceDeleted/" + GROUP_ID, favoritePlace);
    }

    @Test
    void getFavoritePlacesByGroupId_returnsFavoritePlaces() {
        // Arrange: Create a list of FavoritePlace objects
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.setId(PLACE_ID);
        favoritePlace.setGroupId(GROUP_ID);
        List<FavoritePlace> favoritePlaces = List.of(favoritePlace);

        // Mock the service to return the list
        when(favoritePlaceService.getFavoritePlacesByGroupId(GROUP_ID)).thenReturn(favoritePlaces);

        // Act: Call the getFavoritePlacesByGroupId method
        ResponseEntity<List<FavoritePlace>> response = locationController.getFavoritePlacesByGroupId(GROUP_ID);

        // Assert: Verify the response status and body
        assertEquals(200, response.getStatusCode().value());
        assertEquals(favoritePlaces, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(PLACE_ID, response.getBody().get(0).getId());
    }

    @Test
    void getFavoritePlacesByGroupId_returnsNoContentWhenEmpty() {
        // Arrange: Mock the service to return an empty list
        when(favoritePlaceService.getFavoritePlacesByGroupId(GROUP_ID)).thenReturn(Collections.emptyList());

        // Act: Call the getFavoritePlacesByGroupId method
        ResponseEntity<List<FavoritePlace>> response = locationController.getFavoritePlacesByGroupId(GROUP_ID);

        // Assert: Verify the response status is 204 (No Content)
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void addFavoritePlace_savesAndSendsMessage() {
        // Arrange: Create a FavoritePlace with test data
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.setGroupId(GROUP_ID);
        favoritePlace.setPlaceName("Central Park");
        favoritePlace.setLatitude(LATITUDE);
        favoritePlace.setLongitude(LONGITUDE);
        favoritePlace.setRadius(100.0f);

        // Mock the service to return the saved place
        when(favoritePlaceService.saveFavoritePlace(any(FavoritePlace.class))).thenReturn(favoritePlace);

        // Act: Call the addFavoritePlace method
        locationController.addFavoritePlace(favoritePlace);

        // Assert: Verify that the place is saved and the message is sent
        verify(favoritePlaceService, times(1)).saveFavoritePlace(favoritePlace);
        verify(simpMessagingTemplate, times(1))
                .convertAndSend("/topic/favoritePlace/" + GROUP_ID, favoritePlace);
    }

    @Test
    void editFavoritePlace_editsAndSendsMessage() {
        // Arrange: Create a FavoritePlace with test data
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.setId(PLACE_ID);
        favoritePlace.setGroupId(GROUP_ID);
        favoritePlace.setPlaceName("Updated Park");
        favoritePlace.setLatitude(LATITUDE);
        favoritePlace.setLongitude(LONGITUDE);

        // Mock the service to return the edited place
        when(favoritePlaceService.editFavoritePlace(any(FavoritePlace.class))).thenReturn(favoritePlace);

        // Act: Call the editFavoritePlace method
        locationController.editFavoritePlace(favoritePlace);

        // Assert: Verify that the place is edited and the message is sent
        verify(favoritePlaceService, times(1)).editFavoritePlace(favoritePlace);
        verify(simpMessagingTemplate, times(1))
                .convertAndSend("/topic/favoritePlaceEdited/" + GROUP_ID, favoritePlace);
    }

    @Test
    void deleteFavoritePlace_deletesAndSendsMessage() {
        // Arrange: Create a FavoritePlace with test data
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.setId(PLACE_ID);
        favoritePlace.setGroupId(GROUP_ID);

        // Act: Call the deleteFavoritePlace method
        locationController.deleteFavoritePlace(favoritePlace);

        // Assert: Verify that the place is deleted and the message is sent
        verify(favoritePlaceService, times(1)).deleteFavoritePlace(PLACE_ID);
        verify(simpMessagingTemplate, times(1))
                .convertAndSend("/topic/favoritePlaceDeleted/" + GROUP_ID, favoritePlace);
    }

    @Test
    void getFavoritePlacesByGroupId_returnsFavoritePlaces() {
        // Arrange: Create a list of FavoritePlace objects
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.setId(PLACE_ID);
        favoritePlace.setGroupId(GROUP_ID);
        List<FavoritePlace> favoritePlaces = List.of(favoritePlace);

        // Mock the service to return the list
        when(favoritePlaceService.getFavoritePlacesByGroupId(GROUP_ID)).thenReturn(favoritePlaces);

        // Act: Call the getFavoritePlacesByGroupId method
        ResponseEntity<List<FavoritePlace>> response = locationController.getFavoritePlacesByGroupId(GROUP_ID);

        // Assert: Verify the response status and body
        assertEquals(200, response.getStatusCode().value());
        assertEquals(favoritePlaces, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(PLACE_ID, response.getBody().get(0).getId());
    }

    @Test
    void getFavoritePlacesByGroupId_returnsNoContentWhenEmpty() {
        // Arrange: Mock the service to return an empty list
        when(favoritePlaceService.getFavoritePlacesByGroupId(GROUP_ID)).thenReturn(Collections.emptyList());

        // Act: Call the getFavoritePlacesByGroupId method
        ResponseEntity<List<FavoritePlace>> response = locationController.getFavoritePlacesByGroupId(GROUP_ID);

        // Assert: Verify the response status is 204 (No Content)
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void editFavoritePlacee_editsFavoritePlaceSuccessfully() {
        // Arrange: Create a FavoritePlace with test data
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.setId(PLACE_ID);
        favoritePlace.setGroupId(GROUP_ID);
        favoritePlace.setPlaceName("Updated Park");

        // Mock the service to return the edited place
        when(favoritePlaceService.editFavoritePlace(any(FavoritePlace.class))).thenReturn(favoritePlace);

        // Act: Call the editFavoritePlacee method
        ResponseEntity<FavoritePlace> response = locationController.editFavoritePlacee(favoritePlace);

        // Assert: Verify the response status and body
        assertEquals(200, response.getStatusCode().value());
        assertEquals(favoritePlace, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(PLACE_ID, response.getBody().getId());
        verify(favoritePlaceService, times(1)).editFavoritePlace(favoritePlace);
    }

    @Test
    void editFavoritePlacee_returnsNotFoundWhenPlaceDoesNotExist() {
        // Arrange: Mock the service to throw an exception (simulating a not-found scenario)
        FavoritePlace favoritePlace = new FavoritePlace();
        favoritePlace.setId(PLACE_ID);
        when(favoritePlaceService.editFavoritePlace(any(FavoritePlace.class)))
                .thenThrow(new IllegalArgumentException("Favorite place not found"));

        // Act & Assert: Verify that the exception is thrown
        assertThrows(IllegalArgumentException.class, () -> {
            locationController.editFavoritePlacee(favoritePlace);
        });
        verify(favoritePlaceService, times(1)).editFavoritePlace(favoritePlace);
    }

    @Test
    void deleteFavoritePlace_deletesFavoritePlaceSuccessfully() {
        // Act: Call the deleteFavoritePlace method
        ResponseEntity<Void> response = locationController.deleteFavoritePlace(PLACE_ID);

        // Assert: Verify the response status and service interaction
        assertEquals(200, response.getStatusCode().value());
        verify(favoritePlaceService, times(1)).deleteFavoritePlace(PLACE_ID);
    }

    @Test
    void deleteFavoritePlace_returnsNotFoundWhenPlaceDoesNotExist() {
        // Arrange: Mock the service to throw an exception (simulating a not-found scenario)
        doThrow(new IllegalArgumentException("Favorite place not found"))
                .when(favoritePlaceService).deleteFavoritePlace(PLACE_ID);

        // Act & Assert: Verify that the exception is thrown
        assertThrows(IllegalArgumentException.class, () -> {
            locationController.deleteFavoritePlace(PLACE_ID);
        });
        verify(favoritePlaceService, times(1)).deleteFavoritePlace(PLACE_ID);
    }
}