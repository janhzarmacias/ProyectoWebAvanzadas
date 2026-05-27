package ec.edu.epn.fis.rentajuegosasociacion.rental.web.mvc;

import ec.edu.epn.fis.rentajuegosasociacion.catalog.service.CatalogService;
import ec.edu.epn.fis.rentajuegosasociacion.rental.domain.Rental;
import ec.edu.epn.fis.rentajuegosasociacion.rental.domain.RentalStatus;
import ec.edu.epn.fis.rentajuegosasociacion.rental.dto.GameItemAvailabilityDTO;
import ec.edu.epn.fis.rentajuegosasociacion.rental.repository.RentalRepository;
import ec.edu.epn.fis.rentajuegosasociacion.rental.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/categoria/{categoryId}/disponibilidad")
@RequiredArgsConstructor
public class RentalController {

    private final CatalogService catalogService;
    private final RentalRepository rentalRepository;
    private final RentalService rentalService;

    @PreAuthorize("hasAnyRole('STUDENT', 'EMPLOYEE', 'ADMIN')")
    @GetMapping
    public String showAvailability(@PathVariable Long categoryId, Model model) {
        var items = catalogService.getAvailableItemsByCategory(categoryId);

        List<GameItemAvailabilityDTO> availabilityList = items.stream().map(item -> {
            Optional<Rental> activeRental = rentalRepository.findByGameItemIdAndStatus(item.id(), RentalStatus.ACTIVE);

            return new GameItemAvailabilityDTO(
                    item.id(),
                    item.identifier(),
                    activeRental.isPresent(),
                    activeRental.map(Rental::getEndTime).orElse(null),
                    activeRental.map(Rental::getId).orElse(null) // <-- MAPEAMOS EL ID DE LA RENTA
            );
        }).collect(Collectors.toList());

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("items", availabilityList);
        return "rental/availability";
    }

    // El candado asegura que solo el rol EMPLEADO invoque esta acción
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public String rentItem(
            @PathVariable Long categoryId,
            @RequestParam Long gameItemId,
            @RequestParam String studentEmail,
            RedirectAttributes redirectAttributes) {

        try {
            // Limpiamos el texto para evitar espacios vacíos o problemas de mayúsculas
            String cleanEmail = studentEmail.trim().toLowerCase();

            // Ejecutamos la renta usando el correo del estudiante que digitó el empleado
            rentalService.rentGameItem(gameItemId, cleanEmail, 30);

            redirectAttributes.addFlashAttribute("mensajeExito", "¡Juego asignado con éxito al estudiante: " + cleanEmail + "!");

        } catch (IllegalStateException | IllegalArgumentException e) {
            // Atrapamos las reglas de negocio (ej: Correo no institucional, o estudiante con juego ya activo)
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/categoria/" + categoryId + "/disponibilidad";
    }

    // ACCIÓN PARA FINALIZAR LA RENTA FÍSICA
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/finalizar")
    public String finishRental(
            @PathVariable Long categoryId,
            @RequestParam UUID rentalId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            // Como el controlador está protegido con @PreAuthorize("hasRole('EMPLOYEE')"),
            // sabemos con certeza que el usuario actual es un empleado autorizado.
            // Pasamos el correo del propio empleado (principal.getName()) para la auditoría del servicio.
            rentalService.finishRental(rentalId, principal.getName());

            redirectAttributes.addFlashAttribute("mensajeExito", "¡Renta finalizada! El artículo ahora está libre.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo finalizar la renta: " + e.getMessage());
        }

        return "redirect:/categoria/" + categoryId + "/disponibilidad";
    }
}
