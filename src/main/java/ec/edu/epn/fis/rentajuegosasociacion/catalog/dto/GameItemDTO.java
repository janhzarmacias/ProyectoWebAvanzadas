package ec.edu.epn.fis.rentajuegosasociacion.catalog.dto;

public record GameItemDTO(
        Long id,
        String identifier,
        boolean active,
        Long categoryId,
        String categoryName
) {}