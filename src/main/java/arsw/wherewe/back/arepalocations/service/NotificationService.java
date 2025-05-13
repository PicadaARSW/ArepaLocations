package arsw.wherewe.back.arepalocations.service;

import arsw.wherewe.back.arepalocations.dto.FavoritePlaceDTO;
import arsw.wherewe.back.arepalocations.dto.PushTokenDTO;
import arsw.wherewe.back.arepalocations.model.LocationMessage;
import arsw.wherewe.back.arepalocations.model.PushToken;
import arsw.wherewe.back.arepalocations.repository.PushTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URI;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private PushTokenRepository pushTokenRepository;

    private FavoritePlaceService favoritePlaceService;

    private final Map<String, Map<String, Boolean>> userPlaceStatus = new HashMap<>();

    @Autowired
    public NotificationService(PushTokenRepository pushTokenRepository, FavoritePlaceService favoritePlaceService) {
        this.pushTokenRepository = pushTokenRepository;
        this.favoritePlaceService = favoritePlaceService;
    }
    
    private PushToken toPushToken(PushTokenDTO pushTokenDTO) {
        PushToken pushToken = new PushToken(
                pushTokenDTO.getUserId(),
                pushTokenDTO.getGroupId(),
                pushTokenDTO.getToken()
        );
        pushToken.setId(pushTokenDTO.getId());
        return pushToken;
    }


    public void savePushToken(PushTokenDTO pushTokenDTO) {
        PushToken pushToken = toPushToken(pushTokenDTO);
        pushTokenRepository.save(pushToken);
    }

    public void deletePushToken(String userId) {
        pushTokenRepository.deleteByUserId(userId);
    }

    public void checkFavoritePlaceProximity(LocationMessage location) {
        List<FavoritePlaceDTO> favoritePlaces = favoritePlaceService.getFavoritePlacesByGroupId(location.getGroupId());
        PushToken userToken = pushTokenRepository.findByUserId(location.getUserId()).orElse(null);

        if (userToken == null) {
            return;
        }

        for (FavoritePlaceDTO place : favoritePlaces) {
            double distance = calculateDistance(
                    location.getLatitude(), location.getLongitude(),
                    place.getLatitude(), place.getLongitude()
            );

            boolean isInside = distance <= place.getRadius();
            boolean wasInside = wasUserInsidePlace(location.getUserId(), place.getId());

            if (isInside && !wasInside) {
                sendNotification(location.getGroupId(), location.getUserId(), place, "entrar a");
                updateUserPlaceStatus(location.getUserId(), place.getId(), true);
            } else if (!isInside && wasInside) {
                sendNotification(location.getGroupId(), location.getUserId(), place, "salir de");
                updateUserPlaceStatus(location.getUserId(), place.getId(), false);
            }
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371e3;
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private boolean wasUserInsidePlace(String userId, String placeId) {
        return userPlaceStatus.getOrDefault(userId, new HashMap<>()).getOrDefault(placeId, false);
    }

    private void updateUserPlaceStatus(String userId, String placeId, boolean isInside) {
        userPlaceStatus.computeIfAbsent(userId, k -> new HashMap<>()).put(placeId, isInside);
    }

    private void sendNotification(String groupId, String userId, FavoritePlaceDTO place, String action) {
        List<PushToken> tokens = pushTokenRepository.findByGroupId(groupId);
        String userName = getUserName(userId);
        String messageBodyOthers = String.format("%s acaba de %s %s", userName, action, place.getPlaceName());
        String messageBodySelf = String.format("Has %s %s", action.equals("entrar a") ? "entrado a" : "salido de", place.getPlaceName());
        System.out.println("Intentando enviar notificación a " + tokens.size() + " usuarios en el grupo " + groupId);
        for (PushToken token : tokens) {
            try {
                String messageBody = token.getUserId().equals(userId) ? messageBodySelf : messageBodyOthers;
                Message message = Message.builder()
                        .setNotification(Notification.builder()
                                .setTitle("Movimiento en el grupo")
                                .setBody(messageBody)
                                .build())
                        .setToken(token.getToken())
                        .putData("sound", "default")
                        .build();
                String response = FirebaseMessaging.getInstance().send(message);
                System.out.println("Notificación enviada a: " + token.getToken() + ", Respuesta: " + response);
            } catch (Exception e) {
                System.err.println("Error enviando notificación a " + token.getToken() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String getUserName(String userId) {
        return "Usuario " + userId.substring(0, 5);
    }
}