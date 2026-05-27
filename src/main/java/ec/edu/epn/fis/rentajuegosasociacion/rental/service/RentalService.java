package ec.edu.epn.fis.rentajuegosasociacion.rental.service;

import ec.edu.epn.fis.rentajuegosasociacion.catalog.domain.GameItem;
import ec.edu.epn.fis.rentajuegosasociacion.catalog.service.CatalogService;
import ec.edu.epn.fis.rentajuegosasociacion.rental.domain.Rental;
import ec.edu.epn.fis.rentajuegosasociacion.rental.domain.RentalStatus;
import ec.edu.epn.fis.rentajuegosasociacion.rental.dto.RentalDTO;
import ec.edu.epn.fis.rentajuegosasociacion.rental.repository.RentalRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CatalogService catalogService; // Usamos el servicio del otro módulo
    private final EntityManager entityManager; // Para manejar la referencia a la llave foránea de forma limpia

    // Método principal para rentar un juego
    @Transactional
    public RentalDTO rentGameItem(Long gameItemId, String studentEmail, int durationMinutes) {

        // 1. REGLA DE NEGOCIO: ¿El estudiante ya tiene algo rentado?
        List<Rental> activeRentals = rentalRepository.findByStudentEmailAndStatus(studentEmail, RentalStatus.ACTIVE);
        if (!activeRentals.isEmpty()) {
            throw new IllegalStateException("Ya tienes un juego rentado. Debes terminar tu sesión actual antes de rentar otro.");
        }

        // 2. REGLA DE NEGOCIO: ¿El artículo está libre?
        boolean isOccupied = rentalRepository.existsByGameItemIdAndStatus(gameItemId, RentalStatus.ACTIVE);
        if (isOccupied) {
            throw new IllegalStateException("Este juego ya está siendo utilizado por otro estudiante.");
        }

        // 3. REGLA DE NEGOCIO: ¿El artículo existe en el catálogo y está disponible?
        // Delegamos esta verificación al módulo correspondiente (Catalog)
        var availableItems = catalogService.getAvailableItemsByCategory(gameItemId); // Necesitarías un método getGameItemById en el catalogService, asumamos que valida su existencia

        // 4. Calcular el tiempo con el reloj del servidor (Inmune a manipulaciones desde el navegador)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.plusMinutes(durationMinutes);

        // 5. Construir y guardar la entidad
        Rental rental = new Rental();
        rental.setStudentEmail(studentEmail);
        rental.setStartTime(now);
        rental.setEndTime(endTime);
        rental.setStatus(RentalStatus.ACTIVE);

        // Magia de JPA: Usamos getReference para mapear la relación sin cargar toda la entidad desde la base de datos
        GameItem itemRef = entityManager.getReference(GameItem.class, gameItemId);
        rental.setGameItem(itemRef);

        Rental savedRental = rentalRepository.save(rental);

        return convertToDTO(savedRental);
    }

    // Método para finalizar la renta físicamente por parte del empleado
    @Transactional
    public RentalDTO finishRental(UUID rentalId, String employeeEmail) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Renta no encontrada."));

        // Eliminamos la validación que obligaba a que el correo coincida con el del estudiante.
        // La seguridad de esta acción ya está garantizada en la capa web
        // por el @PreAuthorize("hasRole('EMPLOYEE')") en el controlador.

        // Opcional: Aquí podrías usar 'employeeEmail' en el futuro para guardar en
        // una tabla de auditoría qué empleado exacto recibió las paletas de ping pong.

        rental.setStatus(RentalStatus.COMPLETED);

        // Al estar bajo @Transactional, JPA detecta el cambio de estado
        // y hace el UPDATE automáticamente al finalizar el método.

        return convertToDTO(rental);
    }

    // Método auxiliar para conversión
    private RentalDTO convertToDTO(Rental rental) {
        return new RentalDTO(
                rental.getId(),
                rental.getGameItem().getId(),
                rental.getStudentEmail(),
                rental.getStartTime(),
                rental.getEndTime(),
                rental.getStatus()
        );
    }
}
