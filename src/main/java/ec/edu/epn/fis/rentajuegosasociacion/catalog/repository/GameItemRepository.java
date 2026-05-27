package ec.edu.epn.fis.rentajuegosasociacion.catalog.repository;

import ec.edu.epn.fis.rentajuegosasociacion.catalog.domain.GameItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GameItemRepository extends JpaRepository<GameItem, Long> {

    // Busca todos los artículos de una categoría específica (ej. todas las mesas de ping pong)
    List<GameItem> findByCategoryId(Long categoryId);

    // Busca solo los artículos que pertenezcan a una categoría Y que estén activos para jugar
    List<GameItem> findByCategoryIdAndActiveTrue(Long categoryId);
}