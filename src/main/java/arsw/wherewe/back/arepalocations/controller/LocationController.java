package arsw.wherewe.back.arepalocations.controller;

import arsw.wherewe.back.arepalocations.model.FavoritePlace;
import arsw.wherewe.back.arepalocations.model.LocationMessage;
import arsw.wherewe.back.arepalocations.model.PushToken;
import arsw.wherewe.back.arepalocations.repository.PushTokenRepository;
import arsw.wherewe.back.arepalocations.service.FavoritePlaceService;
import arsw.wherewe.back.arepalocations.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LocationController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private FavoritePlaceService favoritePlaceService;
    @Autowired
    private PushTokenRepository pushTokenRepository;
    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/location") // Este es el punto de entrada para los mensajes STOMP
    public void sendLocation(LocationMessage location) {
        System.out.println("Ubicación recibida: " + location.getUserId() + ": " + location.getStatus() +
                " en (" + location.getLatitude() + ", " + location.getLongitude() + ")");

        // Enviar la actualización de ubicación a los suscriptores vía STOMP
        simpMessagingTemplate.convertAndSend("/topic/location/" + location.getGroupId(), location);

        // Delegar la lógica de proximidad y notificaciones al servicio
        notificationService.checkFavoritePlaceProximity(location);
    }

    @MessageMapping("/addFavoritePlace")
    public void addFavoritePlace(FavoritePlace location) {
        System.out.println("Favorite place received: " + location.getPlaceName() + ": " +
                " at (" + location.getLatitude() + ", " + location.getLongitude() + ") for group " + location.getGroupId());

        // Save to MongoDB
        FavoritePlace savedPlace = favoritePlaceService.saveFavoritePlace(location);

        simpMessagingTemplate.convertAndSend("/topic/favoritePlace/" + location.getGroupId(), location);
    }

    @MessageMapping("/editFavoritePlace")
    public void editFavoritePlace(FavoritePlace location) {
        System.out.println("Editing favorite place: " + location.getPlaceName() + ": " +
                " at (" + location.getLatitude() + ", " + location.getLongitude() + ") for group " + location.getGroupId());

        FavoritePlace editedPlace = favoritePlaceService.editFavoritePlace(location);
        simpMessagingTemplate.convertAndSend("/topic/favoritePlaceEdited/" + location.getGroupId(), editedPlace);
    }

    @MessageMapping("/deleteFavoritePlace")
    public void deleteFavoritePlace(FavoritePlace location) {
        System.out.println("Deleting favorite place: " + location.getId() + " for group " + location.getGroupId());

        favoritePlaceService.deleteFavoritePlace(location.getId());
        simpMessagingTemplate.convertAndSend("/topic/favoritePlaceDeleted/" + location.getGroupId(), location);
    }

    @GetMapping("/api/v1/favoritePlaces/{groupId}")
    public ResponseEntity<List<FavoritePlace>> getFavoritePlacesByGroupId(@PathVariable("groupId") String groupId) {
        List<FavoritePlace> favoritePlaces = favoritePlaceService.getFavoritePlacesByGroupId(groupId);
        if(favoritePlaces.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(favoritePlaces);
    }

    @PutMapping("/api/v1/favoritePlaces")
    public ResponseEntity<FavoritePlace> editFavoritePlacee(FavoritePlace favoritePlace) {
        FavoritePlace editedFavoritePlace = favoritePlaceService.editFavoritePlace(favoritePlace);
        return ResponseEntity.ok(editedFavoritePlace);
    }

    @DeleteMapping("/api/v1/favoritePlaces/{id}")
    public ResponseEntity<?> deleteFavoritePlace(@PathVariable("id") String id) {
        favoritePlaceService.deleteFavoritePlace(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/users/push-token")
    public ResponseEntity<?> savePushToken(@RequestBody PushToken pushToken) {
        pushTokenRepository.save(pushToken);
        return ResponseEntity.ok().build();
    }
}
