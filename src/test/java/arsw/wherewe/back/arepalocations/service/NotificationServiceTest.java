package arsw.wherewe.back.arepalocations.service;

import arsw.wherewe.back.arepalocations.dto.FavoritePlaceDTO;
import arsw.wherewe.back.arepalocations.dto.PushTokenDTO;
import arsw.wherewe.back.arepalocations.model.LocationMessage;
import arsw.wherewe.back.arepalocations.model.PushToken;
import arsw.wherewe.back.arepalocations.repository.PushTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private PushTokenRepository pushTokenRepository;

    @Mock
    private FavoritePlaceService favoritePlaceService;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deletePushToken_deletesByUserId() {
        String userId = "user123";

        notificationService.deletePushToken(userId);

        verify(pushTokenRepository, times(1)).deleteByUserId(userId);
    }

    @Test
    void savePushToken_savesPushToken() {
        PushTokenDTO pushTokenDTO = new PushTokenDTO();
        pushTokenDTO.setUserId("user123");
        pushTokenDTO.setGroupId("group123");
        pushTokenDTO.setToken("token123");

        notificationService.savePushToken(pushTokenDTO);

        verify(pushTokenRepository, times(1)).save(any(PushToken.class));
    }

    @Test
    void checkFavoritePlaceProximity_noPushToken_doesNothing() {
        LocationMessage location = new LocationMessage();
        location.setUserId("user123");
        location.setGroupId("group123");
        location.setLatitude(40.785091);
        location.setLongitude(-73.968285);

        when(pushTokenRepository.findByUserId("user123")).thenReturn(Optional.empty());

        notificationService.checkFavoritePlaceProximity(location);

        verify(favoritePlaceService, times(1)).getFavoritePlacesByGroupId("group123");
        verify(pushTokenRepository, times(1)).findByUserId("user123");
        verifyNoMoreInteractions(pushTokenRepository);
    }

    @Test
    void checkFavoritePlaceProximity_userEntersPlace_sendsNotification() {
        LocationMessage location = new LocationMessage();
        location.setUserId("user123");
        location.setGroupId("group123");
        location.setLatitude(40.785091);
        location.setLongitude(-73.968285);

        FavoritePlaceDTO favoritePlaceDTO = new FavoritePlaceDTO();
        favoritePlaceDTO.setId("place456");
        favoritePlaceDTO.setGroupId("group123");
        favoritePlaceDTO.setPlaceName("Central Park");
        favoritePlaceDTO.setLatitude(40.785091);
        favoritePlaceDTO.setLongitude(-73.968285);
        favoritePlaceDTO.setRadius(100.0f);

        PushToken pushToken = new PushToken();
        pushToken.setUserId("user123");
        pushToken.setGroupId("group123");
        pushToken.setToken("token123");

        PushToken otherUserToken = new PushToken();
        otherUserToken.setUserId("user456");
        otherUserToken.setGroupId("group123");
        otherUserToken.setToken("token456");

        when(favoritePlaceService.getFavoritePlacesByGroupId("group123")).thenReturn(List.of(favoritePlaceDTO));
        when(pushTokenRepository.findByUserId("user123")).thenReturn(Optional.of(pushToken));
        when(pushTokenRepository.findByGroupId("group123")).thenReturn(List.of(pushToken, otherUserToken));

        notificationService.checkFavoritePlaceProximity(location);

        verify(favoritePlaceService, times(1)).getFavoritePlacesByGroupId("group123");
        verify(pushTokenRepository, times(1)).findByUserId("user123");
        verify(pushTokenRepository, times(1)).findByGroupId("group123");
    }

    @Test
    void checkFavoritePlaceProximity_userLeavesPlace_sendsNotification() {
        LocationMessage location = new LocationMessage();
        location.setUserId("user123");
        location.setGroupId("group123");
        location.setLatitude(41.0); // Far from the place
        location.setLongitude(-74.0);

        FavoritePlaceDTO favoritePlaceDTO = new FavoritePlaceDTO();
        favoritePlaceDTO.setId("place456");
        favoritePlaceDTO.setGroupId("group123");
        favoritePlaceDTO.setPlaceName("Central Park");
        favoritePlaceDTO.setLatitude(40.785091);
        favoritePlaceDTO.setLongitude(-73.968285);
        favoritePlaceDTO.setRadius(100.0f);

        PushToken pushToken = new PushToken();
        pushToken.setUserId("user123");
        pushToken.setGroupId("group123");
        pushToken.setToken("token123");

        PushToken otherUserToken = new PushToken();
        otherUserToken.setUserId("user456");
        otherUserToken.setGroupId("group123");
        otherUserToken.setToken("token456");

        when(favoritePlaceService.getFavoritePlacesByGroupId("group123")).thenReturn(List.of(favoritePlaceDTO));
        when(pushTokenRepository.findByUserId("user123")).thenReturn(Optional.of(pushToken));
        when(pushTokenRepository.findByGroupId("group123")).thenReturn(List.of(pushToken, otherUserToken));

        // Simulate the user was previously inside the place
        notificationService.checkFavoritePlaceProximity(new LocationMessage("user123", 40.785091, -73.968285, "group123"));
        notificationService.checkFavoritePlaceProximity(location);

        verify(favoritePlaceService, times(2)).getFavoritePlacesByGroupId("group123");
        verify(pushTokenRepository, times(2)).findByUserId("user123");
        verify(pushTokenRepository, times(2)).findByGroupId("group123");
    }
}