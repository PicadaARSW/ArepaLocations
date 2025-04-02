package arsw.wherewe.back.arepalocations.controller;

import arsw.wherewe.back.arepalocations.model.FavoritePlace;
import arsw.wherewe.back.arepalocations.model.LocationMessage;
import arsw.wherewe.back.arepalocations.service.FavoritePlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Constructor for LocationController injecting SimpMessagingTemplate
     * @param simpMessagingTemplate SimpMessagingTemplate
     * @param favoritePlaceService FavoritePlaceService
     */
    @Autowired
    public LocationController(SimpMessagingTemplate simpMessagingTemplate, FavoritePlaceService favoritePlaceService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.favoritePlaceService = favoritePlaceService;
    }

    @MessageMapping("/location") // Este es el punto de entrada para los mensajes STOMP
    @Operation(summary = "Send location via WebSocket (STOMP)", description = "Sends a user's location to a group via WebSocket. This endpoint is not directly testable in Swagger as it uses STOMP over WebSocket. Clients should connect to '/ws' and send messages to '/app/location'. The message will be broadcast to '/topic/location/{groupId}'.")
    public void sendLocation(LocationMessage location) {
        // Enviar el mensaje a todos los suscriptores del grupo
        simpMessagingTemplate.convertAndSend("/topic/location/" + location.getGroupId(), location);
    }

    @MessageMapping("/addFavoritePlace")
    @Operation(summary = "Add favorite place via WebSocket (STOMP)", description = "Adds a favorite place for a group via WebSocket. This endpoint is not directly testable in Swagger as it uses STOMP over WebSocket. Clients should connect to '/ws' and send messages to '/app/addFavoritePlace'. The message will be broadcast to '/topic/favoritePlace/{groupId}'.")
    public void addFavoritePlace(FavoritePlace location) {
        // Save to MongoDB
        favoritePlaceService.saveFavoritePlace(location);

        simpMessagingTemplate.convertAndSend("/topic/favoritePlace/" + location.getGroupId(), location);
    }

    @MessageMapping("/editFavoritePlace")
    @Operation(summary = "Edit favorite place via WebSocket (STOMP)", description = "Edits a favorite place for a group via WebSocket. This endpoint is not directly testable in Swagger as it uses STOMP over WebSocket. Clients should connect to '/ws' and send messages to '/app/editFavoritePlace'. The message will be broadcast to '/topic/favoritePlaceEdited/{groupId}'.")
    public void editFavoritePlace(FavoritePlace location) {
        FavoritePlace editedPlace = favoritePlaceService.editFavoritePlace(location);
        simpMessagingTemplate.convertAndSend("/topic/favoritePlaceEdited/" + location.getGroupId(), editedPlace);
    }

    @MessageMapping("/deleteFavoritePlace")
    @Operation(summary = "Delete favorite place via WebSocket (STOMP)", description = "Deletes a favorite place for a group via WebSocket. This endpoint is not directly testable in Swagger as it uses STOMP over WebSocket. Clients should connect to '/ws' and send messages to '/app/deleteFavoritePlace'. The message will be broadcast to '/topic/favoritePlaceDeleted/{groupId}'.")
    public void deleteFavoritePlace(FavoritePlace location) {
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
    public ResponseEntity<List<FavoritePlace>> getFavoritePlacesByGroupId(@PathVariable("groupId") String groupId) {
        List<FavoritePlace> favoritePlaces = favoritePlaceService.getFavoritePlacesByGroupId(groupId);
        if(favoritePlaces.isEmpty()){
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
    public ResponseEntity<FavoritePlace> editFavoritePlacee(FavoritePlace favoritePlace) {
        FavoritePlace editedFavoritePlace = favoritePlaceService.editFavoritePlace(favoritePlace);
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

}
