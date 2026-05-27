package ec.edu.epn.fis.rentajuegosasociacion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 1. El punto de entrada: El navegador de los estudiantes se conectará a esta URL inicial.
        // SockJS permite que funcione incluso en navegadores antiguos o redes restrictivas.
        registry.addEndpoint("/ws-asociacion").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 2. Rutas de salida: Si el backend quiere enviar un mensaje a una sala,
        // lo enviará a un canal que empiece con "/topic". (Ej. /topic/chat/1)
        registry.enableSimpleBroker("/topic");

        // 3. Rutas de entrada: Si un estudiante envía un mensaje desde su celular al servidor,
        // la ruta deberá empezar con "/app". (Ej. /app/chat/1)
        registry.setApplicationDestinationPrefixes("/app");
    }
}
