package co.edu.unilibre.parklibre.patron;

import co.edu.unilibre.parklibre.modelo.*;
import co.edu.unilibre.parklibre.excepciones.*;
import java.time.LocalDateTime;

public class CreadorMoto extends CreadorVehiculo {
    @Override public Vehiculo crear(String identificador, String propietario,
                                    LocalDateTime ingreso, LocalDateTime salida, boolean lavado)
            throws IdentificadorInvalidoException, SalidaInvalidaException {
        return new Moto(identificador, propietario, ingreso, salida, lavado);
    }
}
