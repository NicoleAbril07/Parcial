package co.edu.unilibre.parklibre.persistencia;

import co.edu.unilibre.parklibre.modelo.Vehiculo;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface VehiculoDAO {
    List<Vehiculo> listarTodos() throws SQLException;
    void insertar(Vehiculo vehiculo) throws SQLException;
    void actualizar(Vehiculo vehiculo) throws SQLException;
    void eliminar(int id) throws SQLException;
    boolean existeActivo(String identificador) throws SQLException;
    List<Vehiculo> listarDentro() throws SQLException;
    Map<String, Long> recaudoPorTipo(LocalDate fecha) throws SQLException;
}
