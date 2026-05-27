package ec.edu.epn.fis.rentajuegosasociacion.rental.domain;

import ec.edu.epn.fis.rentajuegosasociacion.catalog.domain.GameItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
public class Rental {

    // Usamos UUID en lugar de un ID secuencial por seguridad.
    // Evita que alguien adivine cuántas rentas se hacen al día mirando el ID.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Relación con el módulo de catálogo.
    // FetchType.LAZY es obligatorio para no cargar todo el catálogo cada vez que consultes una renta.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_item_id", nullable = false)
    private GameItem gameItem;

    // Para mantenerlo simple y funcional ahora mismo, guardaremos el correo institucional del estudiante.
    // Cuando integres el login definitivo, esto servirá como identificador único.
    @Column(name = "student_email", nullable = false, updatable = false)
    private String studentEmail;

    // updatable = false garantiza que, una vez insertada la hora de inicio,
    // ni siquiera por error en el código se pueda modificar en la base de datos.
    @Column(name = "start_time", nullable = false, updatable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    // Le decimos a JPA que guarde el nombre del Enum (texto) y no su posición numérica (0, 1, 2)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RentalStatus status;
}
