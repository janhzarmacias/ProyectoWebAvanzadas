package ec.edu.epn.fis.rentajuegosasociacion.catalog.web.mvc;

import ec.edu.epn.fis.rentajuegosasociacion.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize; // IMPORTANTE IMPORTAR ESTO
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/catalogo")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    // Cualquier rol (Estudiante, Empleado, Admin) puede ver esto
    @PreAuthorize("hasAnyRole('STUDENT', 'EMPLOYEE', 'ADMIN')")
    @GetMapping
    public String showCatalog(Model model) {
        var categories = catalogService.getAllCategories();
        model.addAttribute("categories", categories);
        return "catalog/index";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categorias/nueva")
    public String saveCategory(@RequestParam String name) {
        catalogService.createCategory(name);
        return "redirect:/catalogo";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categorias/{id}/items/nuevo")
    public String saveItem(@PathVariable Long id, @RequestParam String identifier) {
        catalogService.addGameItem(id, identifier);
        return "redirect:/categoria/" + id + "/disponibilidad";
    }
}
