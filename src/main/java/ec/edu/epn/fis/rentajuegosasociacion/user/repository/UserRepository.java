package ec.edu.epn.fis.rentajuegosasociacion.user.repository;

import ec.edu.epn.fis.rentajuegosasociacion.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Este método es crítico para el Login
    Optional<User> findByEmail(String email);

    // Útil para validar si alguien ya se registró antes de intentar crear un duplicado
    boolean existsByEmail(String email);
}

