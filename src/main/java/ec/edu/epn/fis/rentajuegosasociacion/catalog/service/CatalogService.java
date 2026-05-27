package ec.edu.epn.fis.rentajuegosasociacion.catalog.service;

import ec.edu.epn.fis.rentajuegosasociacion.catalog.domain.Category;
import ec.edu.epn.fis.rentajuegosasociacion.catalog.domain.GameItem;
import ec.edu.epn.fis.rentajuegosasociacion.catalog.dto.CategoryDTO;
import ec.edu.epn.fis.rentajuegosasociacion.catalog.dto.GameItemDTO;
import ec.edu.epn.fis.rentajuegosasociacion.catalog.repository.CategoryRepository;
import ec.edu.epn.fis.rentajuegosasociacion.catalog.repository.GameItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CategoryRepository categoryRepository;
    private final GameItemRepository gameItemRepository;

    // 1. Obtener todas las categorías (Útil para construir el menú principal o los filtros)
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    // 2. Obtener solo los juegos activos y disponibles de una categoría específica
    @Transactional(readOnly = true)
    public List<GameItemDTO> getAvailableItemsByCategory(Long categoryId) {
        return gameItemRepository.findByCategoryIdAndActiveTrue(categoryId)
                .stream()
                .map(this::convertToGameItemDTO)
                .collect(Collectors.toList());
    }

    // 3. Método del mundo real: Crear una nueva categoría y asegurar que el chat dinámico funcione
    @Transactional
    public CategoryDTO createCategory(String name) {
        // Validación básica: evitar duplicados
        if (categoryRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("La categoría '" + name + "' ya existe.");
        }

        Category category = new Category();
        category.setName(name);
        Category savedCategory = categoryRepository.save(category);

        return new CategoryDTO(savedCategory.getId(), savedCategory.getName());
    }

    // 4. Método del mundo real: Agregar un nuevo artículo físico (ej: "Mesa 3") a una categoría
    @Transactional
    public GameItemDTO createGameItem(Long categoryId, String identifier) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + categoryId));

        GameItem item = new GameItem();
        item.setCategory(category);
        item.setIdentifier(identifier);
        item.setActive(true); // Disponible por defecto

        GameItem savedItem = gameItemRepository.save(item);
        return convertToGameItemDTO(savedItem);
    }

    // Método auxiliar privado para transformar la Entidad JPA a DTO
    private GameItemDTO convertToGameItemDTO(GameItem item) {
        return new GameItemDTO(
                item.getId(),
                item.getIdentifier(),
                item.isActive(),
                item.getCategory().getId(),
                item.getCategory().getName()
        );
    }

    @Transactional
    public void addGameItem(Long categoryId, String identifier) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        GameItem item = new GameItem();
        item.setCategory(category);
        item.setIdentifier(identifier);
        item.setActive(true);
        gameItemRepository.save(item);
    }

}