package ec.edu.epn.fis.rentajuegosasociacion.chat.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageResponse(
        UUID id,
        String sender,
        String content,
        LocalDateTime timestamp
) {}
