package ec.edu.epn.fis.rentajuegosasociacion.rental.domain;

public enum RentalStatus {
    ACTIVE,      // El juego está siendo usado en este momento
    COMPLETED,   // El tiempo terminó y el juego fue devuelto correctamente
    CANCELLED    // El estudiante o el administrador canceló la renta antes de tiempo
}