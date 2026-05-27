package ec.edu.epn.fis.rentajuegosasociacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Esto habilitará las anotaciones @PreAuthorize en el futuro
public class SecurityConfig {

    // 1. Definimos el algoritmo de encriptación (BCrypt genera un "salt" aleatorio automáticamente)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Definimos la cadena de filtros de seguridad
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Configuración de rutas
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas a las que cualquiera puede entrar sin iniciar sesión
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/registro").permitAll()

                        // Permitimos que Thymeleaf y SockJS inicien la conexión del WebSocket sin bloquear el handshake inicial
                        .requestMatchers("/ws-asociacion/**").permitAll()

                        // Por ahora, el catálogo y las disponibilidades requerirán estar autenticado
                        .requestMatchers("/catalogo/**", "/categoria/**").authenticated()

                        // Cualquier otra ruta no especificada requerirá autenticación por defecto
                        .anyRequest().authenticated()
                )

                // Configuración del Login
                .formLogin(form -> form
                        .loginPage("/login") // Nuestra página personalizada
                        .defaultSuccessUrl("/catalogo", true)
                        .permitAll()
                )

                // Configuración del Logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}
