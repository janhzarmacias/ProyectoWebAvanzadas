package ec.edu.epn.fis.rentajuegosasociacion.user.service;


import ec.edu.epn.fis.rentajuegosasociacion.user.domain.Role;
import ec.edu.epn.fis.rentajuegosasociacion.user.domain.User;
import ec.edu.epn.fis.rentajuegosasociacion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificamos si ya existen usuarios para no duplicar datos cada vez que se reinicie el servidor
        if (userRepository.count() == 0) {

            // 1. Crear Usuario ADMINISTRADOR (Presidente de la Asociación)
            User admin = new User();
            admin.setEmail("admin.asociacion@epn.edu.ec");
            admin.setFullName("Administrador de la Asociación FIS");
            admin.setPassword(passwordEncoder.encode("admin123")); // Contraseña encriptada segura
            admin.setRole(Role.ROLE_ADMIN);
            admin.setActive(true);
            userRepository.save(admin);

            // 2. Crear Usuario EMPLEADO (El encargado de atender y realizar las rentas físicas)
            User employee = new User();
            employee.setEmail("atencion.asociacion@epn.edu.ec");
            employee.setFullName("Asistente de Turno FIS");
            employee.setPassword(passwordEncoder.encode("empleado123"));
            employee.setRole(Role.ROLE_EMPLOYEE);
            employee.setActive(true);
            userRepository.save(employee);

            // 3. Crear Usuario ESTUDIANTE (Un alumno común que usará los chats y verá tiempos)
            User student = new User();
            student.setEmail("estudiante.prueba@epn.edu.ec");
            student.setFullName("Estudiante de Ingeniería de Sistemas");
            student.setPassword(passwordEncoder.encode("estudiante123"));
            student.setRole(Role.ROLE_STUDENT);
            student.setActive(true);
            userRepository.save(student);

            System.out.println(">> Base de datos inicializada: Usuarios de prueba creados exitosamente.");
        } else {
            System.out.println(">> La base de datos ya contiene usuarios. Saltando inicialización.");
        }
    }
}
