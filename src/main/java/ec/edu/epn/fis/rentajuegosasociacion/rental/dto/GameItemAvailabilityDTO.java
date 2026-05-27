package ec.edu.epn.fis.rentajuegosasociacion.rental.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record GameItemAvailabilityDTO(
        Long gameItemId,
        String identifier,
        boolean isOccupied,
        LocalDateTime endTime,
        UUID rentalId
) {}