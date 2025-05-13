package arsw.wherewe.back.arepalocations.controller;

import arsw.wherewe.back.arepalocations.dto.FavoritePlaceDTO;
import arsw.wherewe.back.arepalocations.dto.PushTokenDTO;
import arsw.wherewe.back.arepalocations.model.FavoritePlace;
import arsw.wherewe.back.arepalocations.model.LocationMessage;

import arsw.wherewe.back.arepalocations.service.FavoritePlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import arsw.wherewe.back.arepalocations.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@Tag(name = "Locations", description = "API for managing locations and favorite places, including WebSocket-based location sharing")

public class LocationController {

    private SimpMessagingTemplate simpMessagingTemplate;

    private FavoritePlaceService favoritePlaceService;

    private NotificationService notificationService;


    /**
     * Constructor for LocationController injecting SimpMessagingTemplate
     * @param simpMessagingTemplate SimpMessagingTemplate
     * @param favoritePlaceService FavoritePlaceService
     */
    @Autowired
    public LocationController(SimpMessagingTemplate simpMessagingTemplate, FavoritePlaceService favoritePlaceService, NotificationService notificationService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.favoritePlaceService = favoritePlaceService;
        this.notificationService = notificationService;
    }

    @MessageMapping("/location") // Este es el punto de entrada para los mensajes STOMP
    @Operation(summary = "Send location via WebSocket (STOMP)", description = "Sends a user's location to a group via WebSocket. This endpoint is not directly testable in Swagger as it uses STOMP over WebSocket. Clients should connect to '/ws' and send messages to '/app/location'. The message will be broadcast to '/topic/location/{groupId}'.")
    public void sendLocation(LocationMessage location) {
        // Enviar la actualización de ubicación a los suscriptores vía STOMP
        simpMessagingTemplate.convertAndSend("/topic/location/" + location.getGroupId(), location);
        // Delegar la lógica de proximidad y notificaciones al servicio
        notificationService.checkFavoritePlaceProximity(location);
    }


    @MessageMapping("/addFavoritePlace")
    @Operation(summary = "Add favorite place via WebSocket (STOMP)", description = "Adds a favorite place for a group via WebSocket. This endpoint is not directly testable in Swagger as it uses STOMP over WebSocket. Clients should connect to '/ws' and send messages to '/app/addFavoritePlace'. The message will be broadcast to '/topic/favoritePlace/{groupId}'.")
    public void addFavoritePlace(FavoritePlaceDTO location) {
        FavoritePlaceDTO savedLocation = favoritePlaceService.saveFavoritePlace(location);
        simpMessagingTemplate.convertAndSend("/topic/favoritePlace/" + location.getGroupId(), savedLocation);
    }

    @MessageMapping("/editFavoritePlace")
    @Operation(summary = "Edit favorite place via WebSocket (STOMP)", description = "Edits a favorite place for a group via WebSocket. This endpoint is not directly testable in Swagger as it uses STOMP over WebSocket. Clients should connect to '/ws' and send messages to '/app/editFavoritePlace'. The message will be broadcast to '/topic/favoritePlaceEdited/{groupId}'.")
    public void editFavoritePlace(FavoritePlaceDTO location) {
        FavoritePlaceDTO editedPlace = favoritePlaceService.editFavoritePlace(location);
        simpMessagingTemplate.convertAndSend("/topic/favoritePlaceEdited/" + location.getGroupId(), editedPlace);
    }

    @MessageMapping("/deleteFavoritePlace")
    @Operation(summary = "Delete favorite place via WebSocket (STOMP)", description = "Deletes a favorite place for a group via WebSocket. This endpoint is not directly testable in Swagger as it uses STOMP over WebSocket. Clients should connect to '/ws' and send messages to '/app/deleteFavoritePlace'. The message will be broadcast to '/topic/favoritePlaceDeleted/{groupId}'.")
    public void deleteFavoritePlace(FavoritePlaceDTO location) {
        favoritePlaceService.deleteFavoritePlace(location.getId());
        simpMessagingTemplate.convertAndSend("/topic/favoritePlaceDeleted/" + location.getGroupId(), location);
    }

    @GetMapping("/api/v1/favoritePlaces/{groupId}")
    @Operation(summary = "Get favorite places by group ID", description = "Retrieves all favorite places for a specific group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved favorite places",
                    content = @Content(schema = @Schema(implementation = FavoritePlace.class))),
            @ApiResponse(responseCode = "204", description = "No favorite places found for the group")
    })
    public ResponseEntity<List<FavoritePlaceDTO>> getFavoritePlacesByGroupId(@PathVariable("groupId") String groupId) {
        List<FavoritePlaceDTO> favoritePlaces = favoritePlaceService.getFavoritePlacesByGroupId(groupId);
        if (favoritePlaces.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(favoritePlaces);
    }


    @PutMapping("/api/v1/favoritePlaces")
    @Operation(summary = "Edit a favorite place via HTTP", description = "Edits an existing favorite place for a group using HTTP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite place updated successfully",
                    content = @Content(schema = @Schema(implementation = FavoritePlace.class))),
            @ApiResponse(responseCode = "404", description = "Favorite place not found")
    })
    public ResponseEntity<FavoritePlaceDTO> editFavoritePlacee(@RequestBody FavoritePlaceDTO favoritePlace) {
        FavoritePlaceDTO editedFavoritePlace = favoritePlaceService.editFavoritePlace(favoritePlace);
        return ResponseEntity.ok(editedFavoritePlace);
    }

    @DeleteMapping("/api/v1/favoritePlaces/{id}")
    @Operation(summary = "Delete a favorite place via HTTP", description = "Deletes a favorite place by its ID using HTTP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite place deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Favorite place not found")
    })
    public ResponseEntity<Void> deleteFavoritePlace(@PathVariable("id") String id) {
        favoritePlaceService.deleteFavoritePlace(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/users/push-token")
    @Operation(summary = "Save push token", description = "Saves a push notification token for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Push token saved successfully"),
            @ApiResponse(responseCode = "500", description = "Error saving push token")
    })
    public ResponseEntity<String> savePushToken(@RequestBody PushTokenDTO pushToken) {
        try {
            notificationService.deletePushToken(pushToken.getUserId());
            notificationService.savePushToken(pushToken);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el token push");
        }
    }
}
