package arsw.wherewe.back.arepalocations.service;

import arsw.wherewe.back.arepalocations.model.FavoritePlace;
import arsw.wherewe.back.arepalocations.model.LocationMessage;
import arsw.wherewe.back.arepalocations.model.PushToken;
import arsw.wherewe.back.arepalocations.repository.PushTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private PushTokenRepository pushTokenRepository;

    @Autowired
    private FavoritePlaceService favoritePlaceService;

    // Mapa en memoria para rastrear el estado de los usuarios (entró/salió)
    // Considera usar MongoDB para persistencia en producción
    private final Map<String, Map<String, Boolean>> userPlaceStatus = new HashMap<>();

    // Verificar la proximidad a lugares favoritos y enviar notificaciones si es necesario
    public void checkFavoritePlaceProximity(LocationMessage location) {
        List<FavoritePlace> favoritePlaces = favoritePlaceService.getFavoritePlacesByGroupId(location.getGroupId());
        PushToken userToken = pushTokenRepository.findByUserId(location.getUserId()).orElse(null);

        if (userToken == null) return; // Si el usuario no tiene un token registrado, no enviar notificaciones

        for (FavoritePlace place : favoritePlaces) {
            double distance = calculateDistance(
                    location.getLatitude(), location.getLongitude(),
                    place.getLatitude(), place.getLongitude()
            );

            boolean isInside = distance <= place.getRadius();
            boolean wasInside = wasUserInsidePlace(location.getUserId(), place.getId());

            if (isInside && !wasInside) {
                sendNotification(location.getGroupId(), location.getUserId(), place, "entró a");
                updateUserPlaceStatus(location.getUserId(), place.getId(), true);
            } else if (!isInside && wasInside) {
                sendNotification(location.getGroupId(), location.getUserId(), place, "salió de");
                updateUserPlaceStatus(location.getUserId(), place.getId(), false);
            }
        }
    }

    // Calcular la distancia entre dos puntos (fórmula de Haversine)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371e3; // Radio de la Tierra en metros
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

    // Verificar si el usuario estaba dentro de un lugar
    private boolean wasUserInsidePlace(String userId, String placeId) {
        return userPlaceStatus.getOrDefault(userId, new HashMap<>()).getOrDefault(placeId, false);
    }

    // Actualizar el estado de entrada/salida del usuario
    private void updateUserPlaceStatus(String userId, String placeId, boolean isInside) {
        userPlaceStatus.computeIfAbsent(userId, k -> new HashMap<>()).put(placeId, isInside);
    }

    // Enviar notificaciones push a los miembros del grupo
    private void sendNotification(String groupId, String userId, FavoritePlace place, String action) {
        List<PushToken> tokens = pushTokenRepository.findByGroupId(groupId);
        String userName = getUserName(userId);

        String messageBody = String.format("%s acaba de %s %s", userName, action, place.getPlaceName());

        for (PushToken token : tokens) {
            if (!token.getUserId().equals(userId)) { // No notificar al usuario que desencadenó el evento
                Message message = Message.builder()
                        .setNotification(Notification.builder()
                                .setTitle("Movimiento en el grupo")
                                .setBody(messageBody)
                                .build())
                        .setToken(token.getToken())
                        .build();

                try {
                    FirebaseMessaging.getInstance().send(message);
                    System.out.println("Notificación enviada a " + token.getUserId());
                } catch (Exception e) {
                    System.err.println("Error enviando notificación: " + e.getMessage());
                }
            }
        }
    }

    // Obtener el nombre del usuario (placeholder)
    private String getUserName(String userId) {
        return "Usuario " + userId.substring(0, 5); // Placeholder
    }
}
