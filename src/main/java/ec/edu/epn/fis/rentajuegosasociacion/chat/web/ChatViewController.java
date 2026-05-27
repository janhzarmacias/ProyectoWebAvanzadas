package ec.edu.epn.fis.rentajuegosasociacion.chat.web;

import ec.edu.epn.fis.rentajuegosasociacion.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChatViewController {

    private final ChatService chatService;

    // Cuando el usuario haga clic en "Abrir Chat de esta Categoría"
    @PreAuthorize("hasAnyRole('STUDENT', 'EMPLOYEE', 'ADMIN')")
    @GetMapping("/categoria/{categoryId}/chat")
    public String showChatRoom(@PathVariable Long categoryId, Model model) {
        // Cargamos el historial de mensajes desde PostgreSQL
        var history = chatService.getChatHistory(categoryId);

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("history", history);

        // Retornaremos una vista de Thymeleaf dedicada al chat
        return "chat/room";
    }
}
