package ec.edu.epn.fis.rentajuegosasociacion.rental.dto;

import ec.edu.epn.fis.rentajuegosasociacion.rental.domain.RentalStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record RentalDTO(
        UUID id,
        Long gameItemId,
        String studentEmail,
        LocalDateTime startTime,
        LocalDateTime endTime,
        RentalStatus status
) {}