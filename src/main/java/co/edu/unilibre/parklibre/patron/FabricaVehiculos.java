package co.edu.unilibre.parklibre.patron;

import co.edu.unilibre.parklibre.modelo.Vehiculo;
import co.edu.unilibre.parklibre.excepciones.*;
import java.time.LocalDateTime;
import java.util.Locale;

public class FabricaVehiculos {
    public static Vehiculo crear(String tipo, String identificador, String propietario,
                                 LocalDateTime ingreso, LocalDateTime salida, boolean lavado)
            throws IdentificadorInvalidoException, SalidaInvalidaException {
        CreadorVehiculo creador = switch (tipo.trim().toUpperCase(Locale.ROOT)) {
            case "CARRO" -> new CreadorCarro();
            case "MOTO" -> new CreadorMoto();
            case "BICICLETA" -> new CreadorBicicleta();
            default -> throw new IllegalArgumentException("Tipo de vehículo desconocido.");
        };
        return creador.crear(identificador, propietario, ingreso, salida, lavado);
    }
}
