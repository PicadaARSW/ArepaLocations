package arsw.wherewe.back.arepalocations.service;

import arsw.wherewe.back.arepalocations.model.FavoritePlace;
import arsw.wherewe.back.arepalocations.model.LocationMessage;
import arsw.wherewe.back.arepalocations.model.PushToken;
import arsw.wherewe.back.arepalocations.repository.PushTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private PushTokenRepository pushTokenRepository;

    @Autowired
    private FavoritePlaceService favoritePlaceService;

    private final Map<String, Map<String, Boolean>> userPlaceStatus = new HashMap<>();

    public void checkFavoritePlaceProximity(LocationMessage location) {
        System.out.println("Buscando token para userId: " + location.getUserId());
        List<FavoritePlace> favoritePlaces = favoritePlaceService.getFavoritePlacesByGroupId(location.getGroupId());
        PushToken userToken = pushTokenRepository.findByUserId(location.getUserId()).orElse(null);
        System.out.println("Verificando proximidad para usuario " + location.getUserId() + " en grupo " + location.getGroupId() + ", token: " + (userToken != null ? userToken.getToken() : "ninguno"));

        if (userToken == null) return;

        for (FavoritePlace place : favoritePlaces) {
            double distance = calculateDistance(
                    location.getLatitude(), location.getLongitude(),
                    place.getLatitude(), place.getLongitude()
            );
            System.out.println("Distancia a " + place.getPlaceName() + ": " + distance + " metros (radio: " + place.getRadius() + ")");

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

    private void sendNotification(String groupId, String userId, FavoritePlace place, String action) {
        List<PushToken> tokens = pushTokenRepository.findByGroupId(groupId);
        String userName = getUserName(userId);

        String messageBody = String.format("%s acaba de %s %s", userName, action, place.getPlaceName());

        for (PushToken token : tokens) {
            if (!token.getUserId().equals(userId)) {
                try {
                    URL url = new URL("https://exp.host/--/api/v2/push/send");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    String payload = String.format(
                            "{\"to\": \"%s\", \"title\": \"Movimiento en el grupo\", \"body\": \"%s\"}",
                            token.getToken(), messageBody
                    );

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = payload.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    int responseCode = conn.getResponseCode();
                    System.out.println("Notificación enviada a " + token.getUserId() + ", código de respuesta: " + responseCode);
                } catch (Exception e) {
                    System.err.println("Error enviando notificación: " + e.getMessage());
                }
            }
        }
    }

    private String getUserName(String userId) {
        return "Usuario " + userId.substring(0, 5);
    }
}