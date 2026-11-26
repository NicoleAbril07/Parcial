package co.edu.unilibre.parklibre.modelo;

import co.edu.unilibre.parklibre.excepciones.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class Bicicleta extends Vehiculo {
    public Bicicleta(String identificador, String propietario, LocalDateTime ingreso,
                     LocalDateTime salida, boolean lavado)
            throws IdentificadorInvalidoException, SalidaInvalidaException {
        super(identificador, propietario, ingreso, salida, false);
    }

    @Override public String getTipo() { return "BICICLETA"; }
    @Override protected boolean validarIdentificador(String id) {
        return id.matches("BIC-[0-9]{4}");
    }
    @Override protected long calcularTarifa(LocalDateTime fin) {
        long minutos = Duration.between(getIngreso(), fin).toMinutes();
        if (minutos <= 30) return 0;
        return ((minutos + 59) / 60) * 500;
    }
}
