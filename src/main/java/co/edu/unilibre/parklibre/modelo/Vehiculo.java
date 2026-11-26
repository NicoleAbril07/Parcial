package co.edu.unilibre.parklibre.modelo;

import co.edu.unilibre.parklibre.excepciones.IdentificadorInvalidoException;
import co.edu.unilibre.parklibre.excepciones.SalidaInvalidaException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

public abstract class Vehiculo {
    private int id;
    private String identificador;
    private String propietario;
    private LocalDateTime ingreso;
    private LocalDateTime salida;
    private boolean lavado;

    protected Vehiculo(String identificador, String propietario,
                       LocalDateTime ingreso, LocalDateTime salida, boolean lavado)
            throws IdentificadorInvalidoException, SalidaInvalidaException {
        setIdentificador(identificador);
        setPropietario(propietario);
        setIngreso(ingreso);
        setSalida(salida);
        this.lavado = lavado;
    }

    public abstract String getTipo();
    protected abstract long calcularTarifa(LocalDateTime fin);

    protected long horasCobrables(LocalDateTime fin) {
        long minutos = Duration.between(ingreso, fin).toMinutes();
        return Math.max(0, (minutos + 59) / 60);
    }

    public long calcularParqueo() {
        if (salida == null) return 0;
        return calcularTarifa(salida);
    }

    protected long costoLavado() { return 0; }

    public long calcularTotal() {
        return calcularParqueo() + (lavado ? costoLavado() : 0);
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return id; }
    public String getIdentificador() { return identificador; }
    public String getPropietario() { return propietario; }
    public LocalDateTime getIngreso() { return ingreso; }
    public LocalDateTime getSalida() { return salida; }
    public boolean isLavado() { return lavado; }

    public void setIdentificador(String identificador) throws IdentificadorInvalidoException {
        String normalizado = identificador == null ? "" : identificador.trim().toUpperCase(Locale.ROOT);
        if (!validarIdentificador(normalizado)) {
            throw new IdentificadorInvalidoException("Identificador inválido para " + getTipo()
                    + ". Verifica el formato requerido.");
        }
        this.identificador = normalizado;
    }

    protected abstract boolean validarIdentificador(String identificador);

    public void setPropietario(String propietario) {
        if (propietario == null || propietario.trim().isEmpty())
            throw new IllegalArgumentException("El propietario es obligatorio.");
        this.propietario = propietario.trim();
    }

    public void setIngreso(LocalDateTime ingreso) {
        if (ingreso == null) throw new IllegalArgumentException("La fecha de ingreso es obligatoria.");
        this.ingreso = ingreso;
    }

    public void setSalida(LocalDateTime salida) throws SalidaInvalidaException {
        if (salida != null && ingreso != null && !salida.isAfter(ingreso))
            throw new SalidaInvalidaException("La salida debe ser posterior al ingreso.");
        this.salida = salida;
    }

    public void setLavado(boolean lavado) {
        if (lavado && !(this instanceof Lavable))
            throw new IllegalArgumentException("Las bicicletas no tienen servicio de lavado.");
        this.lavado = lavado;
    }
}
