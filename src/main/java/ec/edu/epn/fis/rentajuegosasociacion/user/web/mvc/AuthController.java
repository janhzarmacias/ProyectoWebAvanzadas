package ec.edu.epn.fis.rentajuegosasociacion.user.web.mvc;

import ec.edu.epn.fis.rentajuegosasociacion.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login"; // Thymeleaf buscará login.html
    }

    @GetMapping("/registro")
    public String showRegistrationForm() {
        return "register"; // Thymeleaf buscará register.html
    }

    @PostMapping("/registro")
    public String registerStudent(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String fullName,
            Model model) {
        try {
            userService.registerNewStudent(email, password, fullName);
            return "redirect:/login?registered"; // Redirige al login con mensaje de éxito
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
