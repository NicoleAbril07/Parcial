package co.edu.unilibre.parklibre.vista;

import co.edu.unilibre.parklibre.modelo.Vehiculo;
import co.edu.unilibre.parklibre.patron.FabricaVehiculos;
import co.edu.unilibre.parklibre.persistencia.VehiculoDAOImpl;
import co.edu.unilibre.parklibre.servicio.ServicioParqueadero;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaPrincipal extends JFrame {
    private final ServicioParqueadero servicio = new ServicioParqueadero(new VehiculoDAOImpl());
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Identificador", "Propietario", "Tipo", "Ingreso", "Salida", "Lavado", "Total"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public VentanaPrincipal() {
        setTitle("ParkLibre - Gestión de parqueadero");
        setSize(1050, 600);
        setMinimumSize(new Dimension(850, 450));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        construirInterfaz();
        cargarTabla();
    }

    private void construirInterfaz() {
        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JLabel titulo = new JLabel("PARKLIBRE | Administración de parqueadero");
        titulo.setFont(new Font("Arial", Font.BOLD, 21));
        principal.add(titulo, BorderLayout.NORTH);
        tabla.setRowHeight(25);
        principal.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JButton registrar = new JButton("Registrar entrada");
        JButton salida = new JButton("Registrar salida");
        JButton editar = new JButton("Editar");
        JButton eliminar = new JButton("Eliminar");
        JButton dentro = new JButton("Vehículos dentro");
        JButton recaudo = new JButton("Recaudo por fecha");
        JButton actualizar = new JButton("Actualizar tabla");
        registrar.addActionListener(e -> registrar());
        salida.addActionListener(e -> registrarSalida());
        editar.addActionListener(e -> editar());
        eliminar.addActionListener(e -> eliminar());
        dentro.addActionListener(e -> consultarDentro());
        recaudo.addActionListener(e -> consultarRecaudo());
        actualizar.addActionListener(e -> cargarTabla());
        botones.add(registrar); botones.add(salida); botones.add(editar);
        botones.add(eliminar); botones.add(dentro); botones.add(recaudo); botones.add(actualizar);
        principal.add(botones, BorderLayout.SOUTH);
        setContentPane(principal);
    }

    private void cargarTabla() {
        try {
            modelo.setRowCount(0);
            for (Vehiculo v : servicio.listar()) {
                modelo.addRow(new Object[]{v.getId(), v.getIdentificador(), v.getPropietario(),
                        v.getTipo(), FORMATO.format(v.getIngreso()),
                        v.getSalida() == null ? "Dentro" : FORMATO.format(v.getSalida()),
                        v.isLavado() ? "Sí" : "No", "$" + v.calcularTotal()});
            }
        } catch (SQLException ex) { error(ex); }
    }

    private void registrar() {
        JTextField identificador = new JTextField();
        JTextField propietario = new JTextField();
        JComboBox<String> tipo = new JComboBox<>(new String[]{"CARRO", "MOTO", "BICICLETA"});
        JCheckBox lavado = new JCheckBox("Solicita lavado (solo carro/moto)");
        Object[] campos = {"Tipo:", tipo, "Identificador:", identificador, "Propietario:", propietario, lavado};
        if (JOptionPane.showConfirmDialog(this, campos, "Registrar entrada",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            boolean lavar = lavado.isSelected();
            Vehiculo v = FabricaVehiculos.crear((String) tipo.getSelectedItem(),
                    identificador.getText(), propietario.getText(), LocalDateTime.now(), null, lavar);
            servicio.registrarEntrada(v);
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Entrada registrada correctamente.");
        } catch (Exception ex) { error(ex); }
    }

    private void registrarSalida() {
        Vehiculo seleccionado = seleccionado();
        if (seleccionado == null) return;
        if (seleccionado.getSalida() != null) {
            JOptionPane.showMessageDialog(this, "Este registro ya tiene salida.");
            return;
        }
        String fecha = JOptionPane.showInputDialog(this,
                "Fecha y hora de salida (yyyy-MM-dd HH:mm):", LocalDateTime.now().format(FORMATO));
        if (fecha == null) return;
        try {
            LocalDateTime salida = LocalDateTime.parse(fecha.trim(), FORMATO);
            seleccionado.setSalida(salida);
            servicio.actualizar(seleccionado);
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Salida registrada. Total: $" + seleccionado.calcularTotal());
        } catch (Exception ex) { error(ex); }
    }

    private void editar() {
        Vehiculo v = seleccionado();
        if (v == null) return;
        JTextField id = new JTextField(v.getIdentificador());
        JTextField propietario = new JTextField(v.getPropietario());
        JTextField ingreso = new JTextField(v.getIngreso().format(FORMATO));
        JTextField salida = new JTextField(v.getSalida() == null ? "" : v.getSalida().format(FORMATO));
        JCheckBox lavado = new JCheckBox("Lavado", v.isLavado());
        Object[] campos = {"Identificador:", id, "Propietario:", propietario,
                "Ingreso (yyyy-MM-dd HH:mm):", ingreso, "Salida (vacío si sigue dentro):", salida, lavado};
        if (JOptionPane.showConfirmDialog(this, campos, "Editar registro",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            v.setIdentificador(id.getText());
            v.setPropietario(propietario.getText());
            v.setIngreso(LocalDateTime.parse(ingreso.getText().trim(), FORMATO));
            v.setSalida(salida.getText().trim().isEmpty() ? null : LocalDateTime.parse(salida.getText().trim(), FORMATO));
            v.setLavado(lavado.isSelected());
            servicio.actualizar(v);
            cargarTabla();
        } catch (Exception ex) { error(ex); }
    }

    private void eliminar() {
        Vehiculo v = seleccionado();
        if (v == null) return;
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el registro de " + v.getIdentificador() + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            try { servicio.eliminar(v.getId()); cargarTabla(); }
            catch (SQLException ex) { error(ex); }
        }
    }

    private void consultarDentro() {
        try {
            List<Vehiculo> lista = servicio.dentro();
            StringBuilder texto = new StringBuilder("VEHÍCULOS ACTUALMENTE DENTRO\n\n");
            for (Vehiculo v : lista)
                texto.append(v.getTipo()).append(" | ").append(v.getIdentificador())
                        .append(" | ").append(v.getPropietario()).append(" | ")
                        .append(FORMATO.format(v.getIngreso())).append("\n");
            if (lista.isEmpty()) texto.append("No hay vehículos dentro.");
            JOptionPane.showMessageDialog(this, texto.toString());
        } catch (SQLException ex) { error(ex); }
    }

    private void consultarRecaudo() {
        String fecha = JOptionPane.showInputDialog(this, "Fecha (yyyy-MM-dd):", LocalDate.now().toString());
        if (fecha == null) return;
        try {
            Map<String, Long> datos = servicio.recaudo(LocalDate.parse(fecha.trim()));
            String texto = "RECAUDO DEL " + fecha + "\n\n";
            for (Map.Entry<String, Long> e : datos.entrySet())
                texto += e.getKey() + ": $" + e.getValue() + "\n";
            JOptionPane.showMessageDialog(this, texto);
        } catch (Exception ex) { error(ex); }
    }

    private Vehiculo seleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro de la tabla.");
            return null;
        }
        int id = (int) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
        try {
            for (Vehiculo v : servicio.listar()) if (v.getId() == id) return v;
        } catch (SQLException ex) { error(ex); }
        return null;
    }

    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
