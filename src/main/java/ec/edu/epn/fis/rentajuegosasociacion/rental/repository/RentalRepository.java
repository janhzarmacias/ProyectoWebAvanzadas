package ec.edu.epn.fis.rentajuegosasociacion.rental.repository;

import ec.edu.epn.fis.rentajuegosasociacion.rental.domain.Rental;
import ec.edu.epn.fis.rentajuegosasociacion.rental.domain.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RentalRepository extends JpaRepository<Rental, UUID> {

    // 1. La consulta crítica: ¿Existe ya una renta ACTIVA para este artículo?
    // Devuelve 'true' si el juego está ocupado, ideal para validaciones rápidas.
    boolean existsByGameItemIdAndStatus(Long gameItemId, RentalStatus status);

    // 2. Obtener la renta activa actual de un artículo específico
    // Útil para saber QUÉ estudiante lo tiene y HASTA qué hora estará ocupado.
    Optional<Rental> findByGameItemIdAndStatus(Long gameItemId, RentalStatus status);

    // 3. Regla de la Asociación: ¿El estudiante ya tiene algún juego rentado?
    // Evita que un mismo estudiante acapare múltiples juegos a la vez.
    List<Rental> findByStudentEmailAndStatus(String studentEmail, RentalStatus status);

    // 4. Alternativa con JPQL (Opcional, para reportes o consultas complejas)
    // Hace exactamente lo mismo que el método 2, pero te da control total del query.
    @Query("SELECT r FROM Rental r WHERE r.gameItem.id = :itemId AND r.status = :status")
    Optional<Rental> findActiveRentalByItemId(@Param("itemId") Long itemId, @Param("status") RentalStatus status);
}
