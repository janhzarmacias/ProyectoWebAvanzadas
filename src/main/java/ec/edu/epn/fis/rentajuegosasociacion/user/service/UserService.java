package ec.edu.epn.fis.rentajuegosasociacion.user.service;

import ec.edu.epn.fis.rentajuegosasociacion.user.domain.Role;
import ec.edu.epn.fis.rentajuegosasociacion.user.domain.User;
import ec.edu.epn.fis.rentajuegosasociacion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Encriptador de Spring Security

    @Transactional
    public void registerNewStudent(String email, String rawPassword, String fullName) {
        // 1. Validar dominio institucional
        if (!email.toLowerCase().endsWith("@epn.edu.ec")) {
            throw new IllegalArgumentException("Solo se permiten correos institucionales de la EPN.");
        }

        // 2. Validar que no exista
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Este correo ya está registrado.");
        }

        // 3. Crear el usuario
        User newUser = new User();
        newUser.setEmail(email.toLowerCase());
        newUser.setFullName(fullName);
        // Encriptamos la contraseña inmediatamente
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setRole(Role.ROLE_STUDENT); // Rol por defecto al registrarse
        newUser.setActive(true);

        userRepository.save(newUser);
    }
}

