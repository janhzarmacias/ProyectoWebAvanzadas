package ec.edu.epn.fis.rentajuegosasociacion.chat.web;

import ec.edu.epn.fis.rentajuegosasociacion.chat.dto.ChatMessageRequest;
import ec.edu.epn.fis.rentajuegosasociacion.chat.dto.ChatMessageResponse;
import ec.edu.epn.fis.rentajuegosasociacion.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;

    /**
     * El cliente envía el mensaje a: /app/chat/{categoryId}
     * Spring lo procesa y lo retransmite a TODOS los suscritos en: /topic/chat/{categoryId}
     */
    @MessageMapping("/chat/{categoryId}")
    @SendTo("/topic/chat/{categoryId}")
    public ChatMessageResponse processMessage(
            @DestinationVariable Long categoryId,
            @Payload ChatMessageRequest request,
            Principal principal) {

        // Identificamos quién envía el mensaje.
        // Si no hay login aún, ponemos un nombre genérico para poder probar.
        String sender = (principal != null) ? principal.getName() : "Estudiante Anónimo";

        // Guardamos en la base de datos y retornamos el DTO.
        // Lo que retorne este método, Spring se lo inyectará instantáneamente a la pantalla
        // de todos los estudiantes conectados a esa categoría.
        return chatService.saveMessage(categoryId, sender, request.getContent());
    }
}
