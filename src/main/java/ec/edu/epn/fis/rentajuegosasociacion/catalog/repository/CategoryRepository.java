package ec.edu.epn.fis.rentajuegosasociacion.catalog.repository;

import ec.edu.epn.fis.rentajuegosasociacion.catalog.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Método personalizado útil para validar que no se dupliquen nombres
    Optional<Category> findByName(String name);
}