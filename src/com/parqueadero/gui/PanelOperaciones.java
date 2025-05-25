package com.parqueadero.gui;

import com.parqueadero.controlador.OperacionesParqueaderoControlador;
import com.parqueadero.modelo.RegistroEstacionamiento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PanelOperaciones extends JPanel {

    private OperacionesParqueaderoControlador operacionesControlador;

    private JTextField txtPlacaIngreso;
    private JButton btnRegistrarIngreso;

    private JTextField txtPlacaSalida;
    private JButton btnRegistrarSalida;
    private JTextArea areaFacturaSalida;

    private JTable tablaVehiculosActuales;
    private DefaultTableModel modeloTablaVehiculosActuales;
    private JButton btnActualizarListaActuales;

    public PanelOperaciones(OperacionesParqueaderoControlador operacionesControlador) {
        this.operacionesControlador = operacionesControlador;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelAccionesPrincipales = new JPanel(new GridLayout(1, 2, 20, 0)); 

        JPanel panelIngreso = new JPanel(new GridBagLayout());
        panelIngreso.setBorder(BorderFactory.createTitledBorder("Registrar Ingreso"));
        GridBagConstraints gbcIngreso = new GridBagConstraints();
        gbcIngreso.insets = new Insets(5, 5, 5, 5);
        gbcIngreso.fill = GridBagConstraints.HORIZONTAL;

        gbcIngreso.gridx = 0; gbcIngreso.gridy = 0; panelIngreso.add(new JLabel("Placa Vehículo:"), gbcIngreso);
        txtPlacaIngreso = new JTextField(15);
        gbcIngreso.gridx = 1; gbcIngreso.gridy = 0; panelIngreso.add(txtPlacaIngreso, gbcIngreso);

        btnRegistrarIngreso = new JButton("Registrar Ingreso");
        gbcIngreso.gridx = 0; gbcIngreso.gridy = 1; gbcIngreso.gridwidth = 2; gbcIngreso.fill = GridBagConstraints.NONE; gbcIngreso.anchor = GridBagConstraints.CENTER;
        panelIngreso.add(btnRegistrarIngreso, gbcIngreso);

        panelAccionesPrincipales.add(panelIngreso);

        JPanel panelSalida = new JPanel(new GridBagLayout());
        panelSalida.setBorder(BorderFactory.createTitledBorder("Registrar Salida y Factura"));
        GridBagConstraints gbcSalida = new GridBagConstraints();
        gbcSalida.insets = new Insets(5, 5, 5, 5);
        gbcSalida.fill = GridBagConstraints.HORIZONTAL;

        gbcSalida.gridx = 0; gbcSalida.gridy = 0; panelSalida.add(new JLabel("Placa Vehículo:"), gbcSalida);
        txtPlacaSalida = new JTextField(15);
        gbcSalida.gridx = 1; gbcSalida.gridy = 0; panelSalida.add(txtPlacaSalida, gbcSalida);

        btnRegistrarSalida = new JButton("Registrar Salida y Generar Factura");
        gbcSalida.gridx = 0; gbcSalida.gridy = 1; gbcSalida.gridwidth = 2; gbcSalida.fill = GridBagConstraints.NONE; gbcSalida.anchor = GridBagConstraints.CENTER;
        panelSalida.add(btnRegistrarSalida, gbcSalida);
        
        areaFacturaSalida = new JTextArea(8, 30);
        areaFacturaSalida.setEditable(false);
        areaFacturaSalida.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollFactura = new JScrollPane(areaFacturaSalida);
        gbcSalida.gridx = 0; gbcSalida.gridy = 2; gbcSalida.gridwidth = 2; gbcSalida.fill = GridBagConstraints.BOTH; gbcSalida.weighty = 1.0;
        panelSalida.add(scrollFactura, gbcSalida);

        panelAccionesPrincipales.add(panelSalida);
        add(panelAccionesPrincipales, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(0, 5));
        panelCentral.setBorder(BorderFactory.createTitledBorder("Vehículos Actualmente en el Parqueadero"));
        
        String[] columnasActuales = {"Placa", "Tipo Vehículo", "Fecha/Hora Ingreso", "Es Membresía"};
        modeloTablaVehiculosActuales = new DefaultTableModel(columnasActuales, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaVehiculosActuales = new JTable(modeloTablaVehiculosActuales);
        panelCentral.add(new JScrollPane(tablaVehiculosActuales), BorderLayout.CENTER);

        btnActualizarListaActuales = new JButton("Actualizar Lista");
        JPanel panelBotonActualizar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonActualizar.add(btnActualizarListaActuales);
        panelCentral.add(panelBotonActualizar, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);

        asignarActionListenersOperaciones();
        actualizarTablaVehiculosActuales();
    }

    private void asignarActionListenersOperaciones() {
        btnRegistrarIngreso.addActionListener(e -> registrarIngreso());
        btnRegistrarSalida.addActionListener(e -> registrarSalida());
        btnActualizarListaActuales.addActionListener(e -> actualizarTablaVehiculosActuales());
    }

    private void registrarIngreso() {
        String placa = txtPlacaIngreso.getText().trim().toUpperCase();
        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese la placa del vehículo.", "Error de Ingreso", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<RegistroEstacionamiento> registroOpt = operacionesControlador.registrarIngresoVehiculo(placa);

        if (registroOpt.isPresent()) {
            JOptionPane.showMessageDialog(this, "Ingreso registrado para el vehículo: " + placa, "Ingreso Exitoso", JOptionPane.INFORMATION_MESSAGE);
            txtPlacaIngreso.setText("");
            actualizarTablaVehiculosActuales();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el ingreso del vehículo " + placa + ".\nVerifique la placa, si el vehículo debe estar pre-registrado, si ya está dentro, o si hay espacio.", "Error de Ingreso", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarSalida() {
        String placa = txtPlacaSalida.getText().trim().toUpperCase();
        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese la placa del vehículo para la salida.", "Error de Salida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<RegistroEstacionamiento> registroOpt = operacionesControlador.registrarSalidaVehiculo(placa);

        if (registroOpt.isPresent()) {
            RegistroEstacionamiento registroCompletado = registroOpt.get();
            JOptionPane.showMessageDialog(this, "Salida registrada para el vehículo: " + placa, "Salida Exitosa", JOptionPane.INFORMATION_MESSAGE);
            txtPlacaSalida.setText("");
            actualizarTablaVehiculosActuales();

            if (!registroCompletado.fueConMembresia() && registroCompletado.getMontoPagado() >= 0) {
                String factura = operacionesControlador.generarFacturaIngresoSalida(registroCompletado);
                areaFacturaSalida.setText(factura);
            } else if (registroCompletado.fueConMembresia()){
                 areaFacturaSalida.setText("Vehículo con membresía.\nNo se genera factura de pago por hora en salida.");
            } else {
                areaFacturaSalida.setText("No se generó factura (posiblemente sin costo o error).");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar la salida del vehículo " + placa + ".\nVerifique si la placa es correcta y si el vehículo estaba ingresado.", "Error de Salida", JOptionPane.ERROR_MESSAGE);
            areaFacturaSalida.setText("");
        }
    }

    private void actualizarTablaVehiculosActuales() {
        modeloTablaVehiculosActuales.setRowCount(0); 
        List<RegistroEstacionamiento> actuales = operacionesControlador.obtenerVehiculosActualesEnParqueadero();
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (RegistroEstacionamiento reg : actuales) {
            modeloTablaVehiculosActuales.addRow(new Object[]{
                    reg.getPlacaVehiculo(),
                    reg.getTipoVehiculoAlIngreso().getDescripcion(),
                    reg.getFechaHoraIngreso().format(formateador),
                    reg.fueConMembresia() ? "Sí" : "No"
            });
        }
    }
}