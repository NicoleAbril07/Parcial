package co.edu.unilibre.parklibre.patron;

import co.edu.unilibre.parklibre.modelo.Vehiculo;
import co.edu.unilibre.parklibre.excepciones.*;
import java.time.LocalDateTime;

public abstract class CreadorVehiculo {
    public abstract Vehiculo crear(String identificador, String propietario,
                                   LocalDateTime ingreso, LocalDateTime salida, boolean lavado)
            throws IdentificadorInvalidoException, SalidaInvalidaException;
}
