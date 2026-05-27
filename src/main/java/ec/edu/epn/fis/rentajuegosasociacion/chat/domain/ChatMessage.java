package ec.edu.epn.fis.rentajuegosasociacion.chat.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Aquí guardamos el ID de la categoría (Ej. 1 para Ping Pong).
    // Esto es lo que nos permite tener salas dinámicas sin crear tablas nuevas.
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    // Temporalmente guardaremos el correo o nombre.
    // Cuando implementemos el módulo de roles, lo ataremos al usuario real.
    @Column(nullable = false)
    private String sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;
}
