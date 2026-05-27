package ec.edu.epn.fis.rentajuegosasociacion.chat.service;

import ec.edu.epn.fis.rentajuegosasociacion.chat.domain.ChatMessage;
import ec.edu.epn.fis.rentajuegosasociacion.chat.dto.ChatMessageResponse;
import ec.edu.epn.fis.rentajuegosasociacion.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatRepository;

    // 1. Obtener el historial de la sala para cargarlo cuando un estudiante entra
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatHistory(Long categoryId) {
        return chatRepository.findByCategoryIdOrderByTimestampAsc(categoryId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 2. Guardar un nuevo mensaje y devolverlo listo para ser transmitido por WebSockets
    @Transactional
    public ChatMessageResponse saveMessage(Long categoryId, String sender, String content) {
        ChatMessage message = new ChatMessage();
        message.setCategoryId(categoryId);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        ChatMessage savedMessage = chatRepository.save(message);

        return convertToResponse(savedMessage);
    }

    private ChatMessageResponse convertToResponse(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getSender(),
                message.getContent(),
                message.getTimestamp()
        );
    }
}
