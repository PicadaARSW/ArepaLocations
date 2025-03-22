package arsw.wherewe.back.arepalocations.controller;

import arsw.wherewe.back.arepalocations.model.LocationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class LocationController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/location") // Este es el punto de entrada para los mensajes STOMP
    public void sendLocation(LocationMessage location) {
        // Imprimir un mensaje de conexión para verificar que está llegando
        System.out.println("Location received: " + location.getUserId() + ": " + location.getStatus() +
                " at (" + location.getLatitude() + ", " + location.getLongitude() + ")");

        // Enviar el mensaje a todos los suscriptores del grupo
        simpMessagingTemplate.convertAndSend("/topic/location/" + location.getGroupId(), location);
    }
}
