package com.parqueadero.gui;

import com.parqueadero.controlador.ReporteControlador;
import com.parqueadero.modelo.Cliente;
import com.parqueadero.modelo.RegistroEstacionamiento;
import com.parqueadero.modelo.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PanelReportes extends JPanel {

    private ReporteControlador reporteControlador;

    private JTextField txtDiaIngresos, txtMesIngresosDia, txtAnioIngresosDia;
    private JButton btnGenerarIngresosDia;
    private JLabel lblResultadoIngresosDia;

    private JComboBox<String> comboMesIngresosMes;
    private JTextField txtAnioIngresosMes;
    private JButton btnGenerarIngresosMes;
    private JLabel lblResultadoIngresosMes;

    private JTextField txtAnioIngresosAnio;
    private JButton btnGenerarIngresosAnio;
    private JLabel lblResultadoIngresosAnio;

    private JTextField txtCedulaHistorialVehiculos;
    private JButton btnGenerarHistorialVehiculos;
    private JTextArea areaResultadoHistorialVehiculos;

    private JButton btnMostrarVehiculosActuales;
    private JTable tablaVehiculosActualesReporte;
    private DefaultTableModel modeloTablaVehiculosActualesReporte;

    private JButton btnMostrarClientesActivos;
    private JTable tablaClientesActivos;
    private DefaultTableModel modeloTablaClientesActivos;

    private JTextField txtDiasProximosVencer;
    private JButton btnMostrarClientesProximosVencer;
    private JTable tablaClientesProximosVencer;
    private DefaultTableModel modeloTablaClientesProximosVencer;


    public PanelReportes(ReporteControlador reporteControlador) {
        this.reporteControlador = reporteControlador;
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panelIngresos = crearPanelIngresos();
        tabbedPane.addTab("Reportes de Ingresos", null, panelIngresos, "Visualiza los ingresos del parqueadero");

        JPanel panelClientesVehiculos = crearPanelClientesVehiculos();
        tabbedPane.addTab("Clientes y Vehículos", null, panelClientesVehiculos, "Reportes sobre clientes y vehículos");

        add(tabbedPane, BorderLayout.CENTER);
        asignarActionListenersReportes();
    }

    private JPanel crearPanelIngresos() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel panelDia = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDia.setBorder(BorderFactory.createTitledBorder("Ingresos por Día"));
        panelDia.add(new JLabel("Día (DD):"));
        txtDiaIngresos = new JTextField(2);
        panelDia.add(txtDiaIngresos);
        panelDia.add(new JLabel("Mes (MM):"));
        txtMesIngresosDia = new JTextField(2);
        panelDia.add(txtMesIngresosDia);
        panelDia.add(new JLabel("Año (YYYY):"));
        txtAnioIngresosDia = new JTextField(4);
        panelDia.add(txtAnioIngresosDia);
        btnGenerarIngresosDia = new JButton("Generar");
        panelDia.add(btnGenerarIngresosDia);
        lblResultadoIngresosDia = new JLabel("Total: $0.00");
        panelDia.add(lblResultadoIngresosDia);
        panel.add(panelDia);

        JPanel panelMes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMes.setBorder(BorderFactory.createTitledBorder("Ingresos por Mes"));
        panelMes.add(new JLabel("Mes:"));
        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        comboMesIngresosMes = new JComboBox<>(nombresMeses);
        panelMes.add(comboMesIngresosMes);
        panelMes.add(new JLabel("Año (YYYY):"));
        txtAnioIngresosMes = new JTextField(4);
        panelMes.add(txtAnioIngresosMes);
        btnGenerarIngresosMes = new JButton("Generar");
        panelMes.add(btnGenerarIngresosMes);
        lblResultadoIngresosMes = new JLabel("Total: $0.00");
        panelMes.add(lblResultadoIngresosMes);
        panel.add(panelMes);
        
        JPanel panelAnio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAnio.setBorder(BorderFactory.createTitledBorder("Ingresos por Año"));
        panelAnio.add(new JLabel("Año (YYYY):"));
        txtAnioIngresosAnio = new JTextField(4);
        panelAnio.add(txtAnioIngresosAnio);
        btnGenerarIngresosAnio = new JButton("Generar");
        panelAnio.add(btnGenerarIngresosAnio);
        lblResultadoIngresosAnio = new JLabel("Total: $0.00");
        panelAnio.add(lblResultadoIngresosAnio);
        panel.add(panelAnio);

        return panel;
    }

    private JPanel crearPanelClientesVehiculos() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH; 
        gbc.weightx = 1.0; 

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JPanel panelHistVeh = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelHistVeh.setBorder(BorderFactory.createTitledBorder("Historial de Vehículos por Cliente"));
        panelHistVeh.add(new JLabel("Cédula Cliente:"));
        txtCedulaHistorialVehiculos = new JTextField(10);
        panelHistVeh.add(txtCedulaHistorialVehiculos);
        btnGenerarHistorialVehiculos = new JButton("Mostrar");
        panelHistVeh.add(btnGenerarHistorialVehiculos);
        panel.add(panelHistVeh, gbc);
        
        gbc.gridy = 1; gbc.weighty = 0.3; 
        areaResultadoHistorialVehiculos = new JTextArea(6, 40);
        areaResultadoHistorialVehiculos.setEditable(false);
        areaResultadoHistorialVehiculos.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panel.add(new JScrollPane(areaResultadoHistorialVehiculos), gbc);
        gbc.weighty = 0; 

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel panelVehActuales = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelVehActuales.setBorder(BorderFactory.createTitledBorder("Vehículos Actuales en Parqueadero"));
        btnMostrarVehiculosActuales = new JButton("Mostrar/Actualizar Lista");
        panelVehActuales.add(btnMostrarVehiculosActuales);
        panel.add(panelVehActuales, gbc);

        gbc.gridy = 3; gbc.weighty = 0.3;
        modeloTablaVehiculosActualesReporte = new DefaultTableModel(new String[]{"Placa", "Tipo", "Ingreso", "Es Membresía"}, 0){
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaVehiculosActualesReporte = new JTable(modeloTablaVehiculosActualesReporte);
        panel.add(new JScrollPane(tablaVehiculosActualesReporte), gbc);
        gbc.weighty = 0;

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JPanel panelCliActivos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCliActivos.setBorder(BorderFactory.createTitledBorder("Clientes con Membresías Activas"));
        btnMostrarClientesActivos = new JButton("Mostrar Lista");
        panelCliActivos.add(btnMostrarClientesActivos);
        panel.add(panelCliActivos, gbc);

        gbc.gridy = 5; gbc.weighty = 0.2;
        modeloTablaClientesActivos = new DefaultTableModel(new String[]{"Cédula", "Nombre", "Teléfono"}, 0){
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaClientesActivos = new JTable(modeloTablaClientesActivos);
        panel.add(new JScrollPane(tablaClientesActivos), gbc);
        gbc.weighty = 0;

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JPanel panelCliVencer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCliVencer.setBorder(BorderFactory.createTitledBorder("Membresías Próximas a Vencer"));
        panelCliVencer.add(new JLabel("Días Próximos:"));
        txtDiasProximosVencer = new JTextField(3);
        panelCliVencer.add(txtDiasProximosVencer);
        btnMostrarClientesProximosVencer = new JButton("Mostrar Lista");
        panelCliVencer.add(btnMostrarClientesProximosVencer);
        panel.add(panelCliVencer, gbc);

        gbc.gridy = 7; gbc.weighty = 0.2;
        modeloTablaClientesProximosVencer = new DefaultTableModel(new String[]{"Cédula", "Nombre", "Vehículo (Placa)", "Fecha Vencimiento"}, 0){
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaClientesProximosVencer = new JTable(modeloTablaClientesProximosVencer);
        panel.add(new JScrollPane(tablaClientesProximosVencer), gbc);
        gbc.weighty = 0;
        
        return panel;
    }

    private void asignarActionListenersReportes() {
        btnGenerarIngresosDia.addActionListener(e -> generarReporteIngresosDia());
        btnGenerarIngresosMes.addActionListener(e -> generarReporteIngresosMes());
        btnGenerarIngresosAnio.addActionListener(e -> generarReporteIngresosAnio());
        btnGenerarHistorialVehiculos.addActionListener(e -> generarHistorialVehiculosCliente());
        btnMostrarVehiculosActuales.addActionListener(e -> mostrarVehiculosActuales());
        btnMostrarClientesActivos.addActionListener(e -> mostrarClientesConMembresiasActivas());
        btnMostrarClientesProximosVencer.addActionListener(e -> mostrarClientesConMembresiasProximasAVencer());
    }

    private void generarReporteIngresosDia() {
        try {
            int dia = Integer.parseInt(txtDiaIngresos.getText());
            int mes = Integer.parseInt(txtMesIngresosDia.getText());
            int anio = Integer.parseInt(txtAnioIngresosDia.getText());
            LocalDate fecha = LocalDate.of(anio, mes, dia);
            double total = reporteControlador.calcularTotalIngresosPorDia(fecha);
            lblResultadoIngresosDia.setText(String.format("Total: $%.2f", total));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos para día, mes y año.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
             JOptionPane.showMessageDialog(this, "Fecha inválida. Verifique el formato DD, MM, YYYY.", "Error de Fecha", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarReporteIngresosMes() {
        try {
            int mesSeleccionado = comboMesIngresosMes.getSelectedIndex() + 1; 
            int anio = Integer.parseInt(txtAnioIngresosMes.getText());
            if (anio < 1900 || anio > 2200) { // Validación simple de año
                 JOptionPane.showMessageDialog(this, "Por favor, ingrese un año realista.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            double total = reporteControlador.calcularTotalIngresosPorMes(anio, mesSeleccionado);
            lblResultadoIngresosMes.setText(String.format("Total: $%.2f", total));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un año válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generarReporteIngresosAnio() {
        try {
            int anio = Integer.parseInt(txtAnioIngresosAnio.getText());
             if (anio < 1900 || anio > 2200) {
                 JOptionPane.showMessageDialog(this, "Por favor, ingrese un año realista.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            double total = reporteControlador.calcularTotalIngresosPorAnio(anio);
            lblResultadoIngresosAnio.setText(String.format("Total: $%.2f", total));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un año válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarHistorialVehiculosCliente() {
        String cedula = txtCedulaHistorialVehiculos.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese la cédula del cliente.", "Dato Requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Vehiculo> vehiculos = reporteControlador.obtenerHistorialVehiculosPorCliente(cedula);
        StringBuilder sb = new StringBuilder();
        if (vehiculos.isEmpty()) {
            sb.append("No se encontraron vehículos para el cliente con cédula: ").append(cedula);
        } else {
            sb.append("Vehículos del cliente (Cédula: ").append(cedula).append("):\n");
            for (Vehiculo v : vehiculos) {
                sb.append(" - Placa: ").append(v.getPlaca())
                  .append(", Tipo: ").append(v.getTipo().getDescripcion())
                  .append(", Modelo: ").append(v.getModelo());
                if (v.tieneMembresiaActiva()) {
                    sb.append(" (Membresía activa hasta: ")
                      .append(v.getMembresia().getFechaFin().toLocalDate().toString()).append(")\n");
                } else {
                    sb.append(" (Sin membresía activa)\n");
                }
            }
        }
        areaResultadoHistorialVehiculos.setText(sb.toString());
    }

    private void mostrarVehiculosActuales() {
        List<RegistroEstacionamiento> actuales = reporteControlador.obtenerListaVehiculosActuales();
        modeloTablaVehiculosActualesReporte.setRowCount(0);
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (RegistroEstacionamiento reg : actuales) {
            modeloTablaVehiculosActualesReporte.addRow(new Object[]{
                    reg.getPlacaVehiculo(),
                    reg.getTipoVehiculoAlIngreso().getDescripcion(),
                    reg.getFechaHoraIngreso().format(formateador),
                    reg.fueConMembresia() ? "Sí" : "No"
            });
        }
         if(actuales.isEmpty()){
            JOptionPane.showMessageDialog(this, "No hay vehículos actualmente en el parqueadero.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void mostrarClientesConMembresiasActivas() {
        List<Cliente> clientes = reporteControlador.obtenerClientesConMembresiasActivas();
        modeloTablaClientesActivos.setRowCount(0);
        for (Cliente cliente : clientes) {
            modeloTablaClientesActivos.addRow(new Object[]{
                    cliente.getCedula(),
                    cliente.getNombre(),
                    cliente.getTelefono()
            });
        }
        if(clientes.isEmpty()){
            JOptionPane.showMessageDialog(this, "No hay clientes con membresías activas actualmente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void mostrarClientesConMembresiasProximasAVencer() {
        try {
            int dias = Integer.parseInt(txtDiasProximosVencer.getText().trim());
            if (dias <= 0) {
                 JOptionPane.showMessageDialog(this, "Ingrese un número de días positivo.", "Dato Inválido", JOptionPane.WARNING_MESSAGE);
                 return;
            }
            List<Cliente> clientes = reporteControlador.obtenerClientesConMembresiasProximasAVencer(dias);
            modeloTablaClientesProximosVencer.setRowCount(0);
            DateTimeFormatter formateadorFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            boolean encontrados = false;
            for (Cliente cliente : clientes) {
                for(Vehiculo v : cliente.getVehiculos()){
                    if(v.getMembresia() != null && v.getMembresia().estaActiva()){
                         LocalDateTime fechaFinMembresia = v.getMembresia().getFechaFin();
                         if (fechaFinMembresia.isAfter(LocalDateTime.now()) && 
                             fechaFinMembresia.isBefore(LocalDateTime.now().plusDays(dias).plusSeconds(1))) { // +1 segundo para incluir el día completo
                            modeloTablaClientesProximosVencer.addRow(new Object[]{
                                cliente.getCedula(),
                                cliente.getNombre(),
                                v.getPlaca(),
                                fechaFinMembresia.format(formateadorFecha)
                            });
                            encontrados = true;
                         }
                    }
                }
            }
             if(!encontrados){ // Si no se añadió ninguna fila
                JOptionPane.showMessageDialog(this, "No hay clientes con membresías próximas a vencer en los siguientes " + dias + " días.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido para los días.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}