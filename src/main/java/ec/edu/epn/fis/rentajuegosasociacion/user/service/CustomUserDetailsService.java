package ec.edu.epn.fis.rentajuegosasociacion.user.service;

import ec.edu.epn.fis.rentajuegosasociacion.user.domain.User;
import ec.edu.epn.fis.rentajuegosasociacion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscamos al usuario por su correo institucional
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + email));

        // 2. Verificamos que no esté deshabilitado (borrado lógico)
        if (!user.isActive()) {
            throw new UsernameNotFoundException("El usuario está deshabilitado.");
        }

        // 3. Convertimos nuestra entidad User al objeto UserDetails que Spring Security entiende
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
