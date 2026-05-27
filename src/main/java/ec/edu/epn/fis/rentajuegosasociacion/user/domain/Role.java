package ec.edu.epn.fis.rentajuegosasociacion.user.domain;

public enum Role {
    ROLE_STUDENT,   // Solo lectura de catálogo y escritura en chat
    ROLE_EMPLOYEE,  // Puede aprobar rentas físicas
    ROLE_ADMIN      // Puede crear/deshabilitar juegos y categorías
}
