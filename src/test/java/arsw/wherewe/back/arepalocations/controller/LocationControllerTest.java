package arsw.wherewe.back.arepalocations.controller;

import arsw.wherewe.back.arepalocations.dto.FavoritePlaceDTO;
import arsw.wherewe.back.arepalocations.dto.PushTokenDTO;
import arsw.wherewe.back.arepalocations.service.FavoritePlaceService;
import arsw.wherewe.back.arepalocations.service.NotificationService;
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

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private LocationController locationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFavoritePlace_savesAndSendsMessage() {
        FavoritePlaceDTO favoritePlaceDTO = new FavoritePlaceDTO();
        favoritePlaceDTO.setGroupId(GROUP_ID);
        favoritePlaceDTO.setPlaceName("Central Park");
        favoritePlaceDTO.setLatitude(LATITUDE);
        favoritePlaceDTO.setLongitude(LONGITUDE);
        favoritePlaceDTO.setRadius(100.0f);

        when(favoritePlaceService.saveFavoritePlace(any(FavoritePlaceDTO.class))).thenReturn(favoritePlaceDTO);

        locationController.addFavoritePlace(favoritePlaceDTO);

        verify(favoritePlaceService, times(1)).saveFavoritePlace(favoritePlaceDTO);
        verify(simpMessagingTemplate, times(1))
                .convertAndSend("/topic/favoritePlace/" + GROUP_ID, favoritePlaceDTO);
    }

    @Test
    void editFavoritePlace_editsAndSendsMessage() {
        FavoritePlaceDTO favoritePlaceDTO = new FavoritePlaceDTO();
        favoritePlaceDTO.setId(PLACE_ID);
        favoritePlaceDTO.setGroupId(GROUP_ID);
        favoritePlaceDTO.setPlaceName("Updated Park");
        favoritePlaceDTO.setLatitude(LATITUDE);
        favoritePlaceDTO.setLongitude(LONGITUDE);

        when(favoritePlaceService.editFavoritePlace(any(FavoritePlaceDTO.class))).thenReturn(favoritePlaceDTO);

        locationController.editFavoritePlace(favoritePlaceDTO);

        verify(favoritePlaceService, times(1)).editFavoritePlace(favoritePlaceDTO);
        verify(simpMessagingTemplate, times(1))
                .convertAndSend("/topic/favoritePlaceEdited/" + GROUP_ID, favoritePlaceDTO);
    }

    @Test
    void deleteFavoritePlace_deletesAndSendsMessage() {
        FavoritePlaceDTO favoritePlaceDTO = new FavoritePlaceDTO();
        favoritePlaceDTO.setId(PLACE_ID);
        favoritePlaceDTO.setGroupId(GROUP_ID);

        locationController.deleteFavoritePlace(favoritePlaceDTO);

        verify(favoritePlaceService, times(1)).deleteFavoritePlace(PLACE_ID);
        verify(simpMessagingTemplate, times(1))
                .convertAndSend("/topic/favoritePlaceDeleted/" + GROUP_ID, favoritePlaceDTO);
    }

    @Test
    void getFavoritePlacesByGroupId_returnsFavoritePlaces() {
        FavoritePlaceDTO favoritePlaceDTO = new FavoritePlaceDTO();
        favoritePlaceDTO.setId(PLACE_ID);
        favoritePlaceDTO.setGroupId(GROUP_ID);
        List<FavoritePlaceDTO> favoritePlaces = List.of(favoritePlaceDTO);

        when(favoritePlaceService.getFavoritePlacesByGroupId(GROUP_ID)).thenReturn(favoritePlaces);

        ResponseEntity<List<FavoritePlaceDTO>> response = locationController.getFavoritePlacesByGroupId(GROUP_ID);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(favoritePlaces, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(PLACE_ID, response.getBody().get(0).getId());
    }

    @Test
    void getFavoritePlacesByGroupId_returnsNoContentWhenEmpty() {
        when(favoritePlaceService.getFavoritePlacesByGroupId(GROUP_ID)).thenReturn(Collections.emptyList());

        ResponseEntity<List<FavoritePlaceDTO>> response = locationController.getFavoritePlacesByGroupId(GROUP_ID);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void editFavoritePlacee_editsFavoritePlaceSuccessfully() {
        FavoritePlaceDTO favoritePlaceDTO = new FavoritePlaceDTO();
        favoritePlaceDTO.setId(PLACE_ID);
        favoritePlaceDTO.setGroupId(GROUP_ID);
        favoritePlaceDTO.setPlaceName("Updated Park");

        when(favoritePlaceService.editFavoritePlace(any(FavoritePlaceDTO.class))).thenReturn(favoritePlaceDTO);

        ResponseEntity<FavoritePlaceDTO> response = locationController.editFavoritePlacee(favoritePlaceDTO);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(favoritePlaceDTO, response.getBody());
        assertNotNull(response.getBody());
        assertEquals(PLACE_ID, response.getBody().getId());
        verify(favoritePlaceService, times(1)).editFavoritePlace(favoritePlaceDTO);
    }

    @Test
    void editFavoritePlacee_returnsNotFoundWhenPlaceDoesNotExist() {
        FavoritePlaceDTO favoritePlaceDTO = new FavoritePlaceDTO();
        favoritePlaceDTO.setId(PLACE_ID);
        when(favoritePlaceService.editFavoritePlace(any(FavoritePlaceDTO.class)))
                .thenThrow(new IllegalArgumentException("Favorite place not found"));

        assertThrows(IllegalArgumentException.class, () -> {
            locationController.editFavoritePlacee(favoritePlaceDTO);
        });
        verify(favoritePlaceService, times(1)).editFavoritePlace(favoritePlaceDTO);
    }

    @Test
    void deleteFavoritePlace_deletesFavoritePlaceSuccessfully() {
        ResponseEntity<Void> response = locationController.deleteFavoritePlace(PLACE_ID);

        assertEquals(200, response.getStatusCode().value());
        verify(favoritePlaceService, times(1)).deleteFavoritePlace(PLACE_ID);
    }

    @Test
    void deleteFavoritePlace_returnsNotFoundWhenPlaceDoesNotExist() {
        doThrow(new IllegalArgumentException("Favorite place not found"))
                .when(favoritePlaceService).deleteFavoritePlace(PLACE_ID);

        assertThrows(IllegalArgumentException.class, () -> {
            locationController.deleteFavoritePlace(PLACE_ID);
        });
        verify(favoritePlaceService, times(1)).deleteFavoritePlace(PLACE_ID);
    }

    @Test
    void savePushToken_savesSuccessfully() {
        PushTokenDTO pushTokenDTO = new PushTokenDTO();
        pushTokenDTO.setUserId(USER_ID);
        pushTokenDTO.setGroupId(GROUP_ID);
        pushTokenDTO.setToken("token123");

        ResponseEntity<String> response = locationController.savePushToken(pushTokenDTO);

        assertEquals(200, response.getStatusCode().value());
        verify(notificationService, times(1)).deletePushToken(USER_ID);
        verify(notificationService, times(1)).savePushToken(pushTokenDTO);
    }

    @Test
    void savePushToken_handlesException() {
        PushTokenDTO pushTokenDTO = new PushTokenDTO();
        pushTokenDTO.setUserId(USER_ID);
        pushTokenDTO.setGroupId(GROUP_ID);
        pushTokenDTO.setToken("token123");

        doThrow(new RuntimeException("Error saving token")).when(notificationService).deletePushToken(USER_ID);

        ResponseEntity<String> response = locationController.savePushToken(pushTokenDTO);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Error al guardar el token push", response.getBody());
        verify(notificationService, times(1)).deletePushToken(USER_ID);
    }
}