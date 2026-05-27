package ec.edu.epn.fis.rentajuegosasociacion.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
// "user" es una palabra reservada en PostgreSQL, por lo que nombramos la tabla "users"
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // El correo será nuestro identificador principal (username) para el login
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    // Guardaremos la contraseña encriptada (NUNCA en texto plano)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
