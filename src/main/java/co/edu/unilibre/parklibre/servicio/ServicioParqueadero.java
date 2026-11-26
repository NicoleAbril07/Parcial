package co.edu.unilibre.parklibre.servicio;

import co.edu.unilibre.parklibre.modelo.Vehiculo;
import co.edu.unilibre.parklibre.persistencia.VehiculoDAO;
import co.edu.unilibre.parklibre.excepciones.VehiculoYaDentroException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ServicioParqueadero {
    private final VehiculoDAO dao;
    public ServicioParqueadero(VehiculoDAO dao) { this.dao = dao; }

    public void registrarEntrada(Vehiculo v) throws SQLException, VehiculoYaDentroException {
        if (dao.existeActivo(v.getIdentificador()))
            throw new VehiculoYaDentroException("El vehículo " + v.getIdentificador() + " ya está dentro.");
        dao.insertar(v);
    }
    public void actualizar(Vehiculo v) throws SQLException { dao.actualizar(v); }
    public void eliminar(int id) throws SQLException { dao.eliminar(id); }
    public List<Vehiculo> listar() throws SQLException { return dao.listarTodos(); }
    public List<Vehiculo> dentro() throws SQLException { return dao.listarDentro(); }
    public Map<String, Long> recaudo(LocalDate fecha) throws SQLException { return dao.recaudoPorTipo(fecha); }
}
