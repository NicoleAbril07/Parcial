package co.edu.unilibre.parklibre.modelo;

import co.edu.unilibre.parklibre.excepciones.*;
import java.time.LocalDateTime;

public class Carro extends Vehiculo implements Lavable {
    private static final long TARIFA_HORA = 3000;
    private static final long TOPE_DIA = 25000;

    public Carro(String identificador, String propietario, LocalDateTime ingreso,
                 LocalDateTime salida, boolean lavado)
            throws IdentificadorInvalidoException, SalidaInvalidaException {
        super(identificador, propietario, ingreso, salida, lavado);
    }

    @Override public String getTipo() { return "CARRO"; }
    @Override protected boolean validarIdentificador(String id) {
        return id.matches("[A-Z]{3}[0-9]{3}");
    }
    @Override protected long calcularTarifa(LocalDateTime fin) {
        long minutos = java.time.Duration.between(getIngreso(), fin).toMinutes();
        long bloques24 = minutos / (24 * 60);
        long resto = minutos % (24 * 60);
        long horasResto = (resto + 59) / 60;
        return bloques24 * TOPE_DIA + Math.min(horasResto * TARIFA_HORA, TOPE_DIA);
    }
    @Override public long calcularCostoLavado() { return 15000; }
    @Override protected long costoLavado() { return calcularCostoLavado(); }
}
