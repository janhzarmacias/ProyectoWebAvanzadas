package ec.edu.epn.fis.rentajuegosasociacion.catalog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "game_items")
@Getter
@Setter
@NoArgsConstructor
public class GameItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación Muchos a Uno: Muchos GameItems pertenecen a una Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 100)
    private String identifier;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}