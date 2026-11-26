package co.edu.unilibre.parklibre.persistencia;

import co.edu.unilibre.parklibre.modelo.Vehiculo;
import co.edu.unilibre.parklibre.patron.FabricaVehiculos;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class VehiculoDAOImpl implements VehiculoDAO {
    private Vehiculo mapear(ResultSet rs) throws SQLException {
        try {
            Timestamp salidaTs = rs.getTimestamp("salida");
            Vehiculo v = FabricaVehiculos.crear(
                    rs.getString("tipo"), rs.getString("identificador"),
                    rs.getString("propietario"), rs.getTimestamp("ingreso").toLocalDateTime(),
                    salidaTs == null ? null : salidaTs.toLocalDateTime(),
                    rs.getBoolean("lavado"));
            v.setId(rs.getInt("id"));
            return v;
        } catch (Exception e) {
            throw new SQLException("No se pudo reconstruir el vehículo: " + e.getMessage(), e);
        }
    }

    @Override public List<Vehiculo> listarTodos() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM estacionamientos ORDER BY ingreso DESC";
        try (Connection c = Conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    @Override public void insertar(Vehiculo v) throws SQLException {
        String sql = "INSERT INTO estacionamientos(identificador,propietario,tipo,ingreso,salida,lavado) VALUES(?,?,?,?,?,?)";
        try (Connection c = Conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.getIdentificador());
            ps.setString(2, v.getPropietario());
            ps.setString(3, v.getTipo());
            ps.setTimestamp(4, Timestamp.valueOf(v.getIngreso()));
            if (v.getSalida() == null) ps.setNull(5, Types.TIMESTAMP);
            else ps.setTimestamp(5, Timestamp.valueOf(v.getSalida()));
            ps.setBoolean(6, v.isLavado());
            ps.executeUpdate();
        }
    }

    @Override public void actualizar(Vehiculo v) throws SQLException {
        String sql = "UPDATE estacionamientos SET identificador=?, propietario=?, tipo=?, ingreso=?, salida=?, lavado=? WHERE id=?";
        try (Connection c = Conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.getIdentificador());
            ps.setString(2, v.getPropietario());
            ps.setString(3, v.getTipo());
            ps.setTimestamp(4, Timestamp.valueOf(v.getIngreso()));
            if (v.getSalida() == null) ps.setNull(5, Types.TIMESTAMP);
            else ps.setTimestamp(5, Timestamp.valueOf(v.getSalida()));
            ps.setBoolean(6, v.isLavado());
            ps.setInt(7, v.getId());
            ps.executeUpdate();
        }
    }

    @Override public void eliminar(int id) throws SQLException {
        try (Connection c = Conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement("DELETE FROM estacionamientos WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override public boolean existeActivo(String identificador) throws SQLException {
        String sql = "SELECT COUNT(*) FROM estacionamientos WHERE identificador=? AND salida IS NULL";
        try (Connection c = Conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, identificador);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    @Override public List<Vehiculo> listarDentro() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM estacionamientos WHERE salida IS NULL ORDER BY ingreso";
        try (Connection c = Conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    @Override public Map<String, Long> recaudoPorTipo(LocalDate fecha) throws SQLException {
        Map<String, Long> recaudo = new LinkedHashMap<>();
        recaudo.put("CARRO", 0L);
        recaudo.put("MOTO", 0L);
        recaudo.put("BICICLETA", 0L);
        String sql = "SELECT * FROM estacionamientos WHERE salida >= ? AND salida < ?";
        try (Connection c = Conexion.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fecha.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(fecha.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vehiculo v = mapear(rs);
                    recaudo.put(v.getTipo(), recaudo.get(v.getTipo()) + v.calcularTotal());
                }
            }
        }
        return recaudo;
    }
}
