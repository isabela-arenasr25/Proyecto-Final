package com.parqueadero.gui;

import com.parqueadero.controlador.AdministracionParqueaderoControlador;
import com.parqueadero.modelo.Membresia;
import com.parqueadero.modelo.Parqueadero;
import com.parqueadero.modelo.TipoVehiculo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelAdministracion extends JPanel {

    private AdministracionParqueaderoControlador controlador;
    private Parqueadero parqueadero; // Para mostrar datos actuales después de obtenerlos del controlador

    // Componentes de la GUI para datos del parqueadero
    private JTextField txtNombreParqueadero;
    private JTextField txtDireccion;
    private JTextField txtRepresentante;
    private JTextField txtTelefonoContacto;
    private JButton btnGuardarDatosParqueadero;

    // Componentes para capacidades
    private JComboBox<TipoVehiculo> comboTipoVehiculoCapacidad;
    private JTextField txtCantidadCapacidad;
    private JButton btnEstablecerCapacidad;
    private JTextArea areaInfoCapacidades;

    // Componentes para tarifas por hora
    private JComboBox<TipoVehiculo> comboTipoVehiculoTarifaHora;
    private JTextField txtTarifaPorHora;
    private JButton btnEstablecerTarifaHora;
    private JTextArea areaInfoTarifasHora;

    // Componentes para tarifas de membresía
    private JComboBox<Membresia.TipoPeriodoMembresia> comboPeriodoMembresia;
    private JComboBox<TipoVehiculo> comboTipoVehiculoTarifaMembresia;
    private JTextField txtPrecioMembresia;
    private JButton btnEstablecerTarifaMembresia;
    private JTextArea areaInfoTarifasMembresia;


    public PanelAdministracion(AdministracionParqueaderoControlador controlador) {
        this.controlador = controlador;
        this.parqueadero = controlador.obtenerDatosActualesParqueadero(); // Obtener el parqueadero inicial

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para la configuración general del parqueadero
        JPanel panelDatosGenerales = new JPanel(new GridBagLayout());
        panelDatosGenerales.setBorder(BorderFactory.createTitledBorder("Datos Generales del Parqueadero"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelDatosGenerales.add(new JLabel("Nombre:"), gbc);
        txtNombreParqueadero = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; panelDatosGenerales.add(txtNombreParqueadero, gbc);
        gbc.gridwidth = 1; 

        gbc.gridx = 0; gbc.gridy = 1; panelDatosGenerales.add(new JLabel("Dirección:"), gbc);
        txtDireccion = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; panelDatosGenerales.add(txtDireccion, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; panelDatosGenerales.add(new JLabel("Representante:"), gbc);
        txtRepresentante = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; panelDatosGenerales.add(txtRepresentante, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 3; panelDatosGenerales.add(new JLabel("Teléfono:"), gbc);
        txtTelefonoContacto = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; panelDatosGenerales.add(txtTelefonoContacto, gbc);
        gbc.gridwidth = 1;
        
        btnGuardarDatosParqueadero = new JButton("Guardar Datos");
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        panelDatosGenerales.add(btnGuardarDatosParqueadero, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL; 

        JTabbedPane tabbedPaneConfig = new JTabbedPane();

        JPanel panelCapacidades = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCap = new GridBagConstraints(); // Usar un nuevo GBC para este panel
        gbcCap.insets = new Insets(5,5,5,5);
        gbcCap.fill = GridBagConstraints.HORIZONTAL;

        gbcCap.gridx = 0; gbcCap.gridy = 0; panelCapacidades.add(new JLabel("Tipo Vehículo:"), gbcCap);
        comboTipoVehiculoCapacidad = new JComboBox<>(TipoVehiculo.values());
        gbcCap.gridx = 1; gbcCap.gridy = 0; panelCapacidades.add(comboTipoVehiculoCapacidad, gbcCap);

        gbcCap.gridx = 0; gbcCap.gridy = 1; panelCapacidades.add(new JLabel("Cantidad:"), gbcCap);
        txtCantidadCapacidad = new JTextField(5);
        gbcCap.gridx = 1; gbcCap.gridy = 1; panelCapacidades.add(txtCantidadCapacidad, gbcCap);

        btnEstablecerCapacidad = new JButton("Establecer Capacidad");
        gbcCap.gridx = 0; gbcCap.gridy = 2; gbcCap.gridwidth = 2; gbcCap.fill = GridBagConstraints.NONE; gbcCap.anchor = GridBagConstraints.CENTER;
        panelCapacidades.add(btnEstablecerCapacidad, gbcCap);
        gbcCap.gridwidth = 1; gbcCap.fill = GridBagConstraints.HORIZONTAL;

        areaInfoCapacidades = new JTextArea(5, 25);
        areaInfoCapacidades.setEditable(false);
        JScrollPane scrollCapacidades = new JScrollPane(areaInfoCapacidades);
        gbcCap.gridx = 0; gbcCap.gridy = 3; gbcCap.gridwidth = 2; gbcCap.weightx = 1.0; gbcCap.weighty = 1.0; gbcCap.fill = GridBagConstraints.BOTH;
        panelCapacidades.add(scrollCapacidades, gbcCap);
        
        tabbedPaneConfig.addTab("Capacidades", panelCapacidades);

        JPanel panelTarifasHora = new JPanel(new GridBagLayout());
        GridBagConstraints gbcTarHr = new GridBagConstraints(); // Nuevo GBC
        gbcTarHr.insets = new Insets(5,5,5,5);
        gbcTarHr.fill = GridBagConstraints.HORIZONTAL;

        gbcTarHr.gridx = 0; gbcTarHr.gridy = 0; panelTarifasHora.add(new JLabel("Tipo Vehículo:"), gbcTarHr);
        comboTipoVehiculoTarifaHora = new JComboBox<>(TipoVehiculo.values());
        gbcTarHr.gridx = 1; gbcTarHr.gridy = 0; panelTarifasHora.add(comboTipoVehiculoTarifaHora, gbcTarHr);

        gbcTarHr.gridx = 0; gbcTarHr.gridy = 1; panelTarifasHora.add(new JLabel("Tarifa por Hora:"), gbcTarHr);
        txtTarifaPorHora = new JTextField(5);
        gbcTarHr.gridx = 1; gbcTarHr.gridy = 1; panelTarifasHora.add(txtTarifaPorHora, gbcTarHr);

        btnEstablecerTarifaHora = new JButton("Establecer Tarifa Hora");
        gbcTarHr.gridx = 0; gbcTarHr.gridy = 2; gbcTarHr.gridwidth = 2; gbcTarHr.fill = GridBagConstraints.NONE; gbcTarHr.anchor = GridBagConstraints.CENTER;
        panelTarifasHora.add(btnEstablecerTarifaHora, gbcTarHr);
        gbcTarHr.gridwidth = 1; gbcTarHr.fill = GridBagConstraints.HORIZONTAL;

        areaInfoTarifasHora = new JTextArea(5, 25);
        areaInfoTarifasHora.setEditable(false);
        JScrollPane scrollTarifasHora = new JScrollPane(areaInfoTarifasHora);
        gbcTarHr.gridx = 0; gbcTarHr.gridy = 3; gbcTarHr.gridwidth = 2; gbcTarHr.weightx = 1.0; gbcTarHr.weighty = 1.0; gbcTarHr.fill = GridBagConstraints.BOTH;
        panelTarifasHora.add(scrollTarifasHora, gbcTarHr);

        tabbedPaneConfig.addTab("Tarifas por Hora", panelTarifasHora);

        JPanel panelTarifasMembresia = new JPanel(new GridBagLayout());
        GridBagConstraints gbcTarMem = new GridBagConstraints(); // Nuevo GBC
        gbcTarMem.insets = new Insets(5,5,5,5);
        gbcTarMem.fill = GridBagConstraints.HORIZONTAL;

        gbcTarMem.gridx = 0; gbcTarMem.gridy = 0; panelTarifasMembresia.add(new JLabel("Periodo Memb.:"), gbcTarMem);
        comboPeriodoMembresia = new JComboBox<>(Membresia.TipoPeriodoMembresia.values());
        gbcTarMem.gridx = 1; gbcTarMem.gridy = 0; panelTarifasMembresia.add(comboPeriodoMembresia, gbcTarMem);

        gbcTarMem.gridx = 0; gbcTarMem.gridy = 1; panelTarifasMembresia.add(new JLabel("Tipo Vehículo:"), gbcTarMem);
        comboTipoVehiculoTarifaMembresia = new JComboBox<>(TipoVehiculo.values());
        gbcTarMem.gridx = 1; gbcTarMem.gridy = 1; panelTarifasMembresia.add(comboTipoVehiculoTarifaMembresia, gbcTarMem);

        gbcTarMem.gridx = 0; gbcTarMem.gridy = 2; panelTarifasMembresia.add(new JLabel("Precio Memb.:"), gbcTarMem);
        txtPrecioMembresia = new JTextField(8);
        gbcTarMem.gridx = 1; gbcTarMem.gridy = 2; panelTarifasMembresia.add(txtPrecioMembresia, gbcTarMem);
        
        btnEstablecerTarifaMembresia = new JButton("Establecer Tarifa Memb.");
        gbcTarMem.gridx = 0; gbcTarMem.gridy = 3; gbcTarMem.gridwidth = 2; gbcTarMem.fill = GridBagConstraints.NONE; gbcTarMem.anchor = GridBagConstraints.CENTER;
        panelTarifasMembresia.add(btnEstablecerTarifaMembresia, gbcTarMem);
        gbcTarMem.gridwidth = 1; gbcTarMem.fill = GridBagConstraints.HORIZONTAL;

        areaInfoTarifasMembresia = new JTextArea(5, 25);
        areaInfoTarifasMembresia.setEditable(false);
        JScrollPane scrollTarifasMemb = new JScrollPane(areaInfoTarifasMembresia);
        gbcTarMem.gridx = 0; gbcTarMem.gridy = 4; gbcTarMem.gridwidth = 2; gbcTarMem.weightx = 1.0; gbcTarMem.weighty = 1.0; gbcTarMem.fill = GridBagConstraints.BOTH;
        panelTarifasMembresia.add(scrollTarifasMemb, gbcTarMem);

        tabbedPaneConfig.addTab("Tarifas Membresía", panelTarifasMembresia);

        add(panelDatosGenerales, BorderLayout.NORTH);
        add(tabbedPaneConfig, BorderLayout.CENTER);

        cargarDatosActualesParqueadero();
        asignarActionListeners();
    }

    private void cargarDatosActualesParqueadero() {
        this.parqueadero = controlador.obtenerDatosActualesParqueadero();
        
        txtNombreParqueadero.setText(parqueadero.getNombre());
        txtDireccion.setText(parqueadero.getDireccion());
        txtRepresentante.setText(parqueadero.getRepresentante());
        txtTelefonoContacto.setText(parqueadero.getTelefonoContacto());
        actualizarDisplayCapacidades();
        actualizarDisplayTarifasHora();
        actualizarDisplayTarifasMembresia();
    }

    private void actualizarDisplayCapacidades() {
        // Se necesita el objeto parqueadero actualizado para leer sus listas internas
        // this.parqueadero debe estar actualizado por cargarDatosActualesParqueadero()
        StringBuilder sb = new StringBuilder("Capacidades Actuales:\n");
        for (TipoVehiculo tipo : TipoVehiculo.values()) {
            sb.append(tipo.getDescripcion()).append(": ").append(parqueadero.obtenerCapacidad(tipo)).append(" puestos\n");
        }
        areaInfoCapacidades.setText(sb.toString());
    }
    
    private void actualizarDisplayTarifasHora() {
        StringBuilder sb = new StringBuilder("Tarifas por Hora Actuales:\n");
        for (TipoVehiculo tipo : TipoVehiculo.values()) {
            sb.append(tipo.getDescripcion()).append(": $").append(String.format("%.2f", parqueadero.obtenerTarifaPorHora(tipo))).append("\n");
        }
        areaInfoTarifasHora.setText(sb.toString());
    }

    private void actualizarDisplayTarifasMembresia() {
        StringBuilder sb = new StringBuilder("Tarifas de Membresía Actuales:\n");
        for (Membresia.TipoPeriodoMembresia periodo : Membresia.TipoPeriodoMembresia.values()) {
            sb.append("Periodo: ").append(periodo.getDescripcion()).append("\n");
            for (TipoVehiculo tv : TipoVehiculo.values()) {
                double precio = parqueadero.obtenerTarifaMembresia(periodo, tv);
                sb.append("  - ").append(tv.getDescripcion()).append(": $").append(String.format("%.2f", precio)).append("\n");
            }
        }
        areaInfoTarifasMembresia.setText(sb.toString());
    }

    private void asignarActionListeners() {
        btnGuardarDatosParqueadero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.guardarDatosParqueadero(
                        txtNombreParqueadero.getText(),
                        txtDireccion.getText(),
                        txtRepresentante.getText(),
                        txtTelefonoContacto.getText()
                );
                JOptionPane.showMessageDialog(PanelAdministracion.this, "Datos generales del parqueadero guardados.");
                cargarDatosActualesParqueadero(); 
            }
        });

        btnEstablecerCapacidad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TipoVehiculo tipo = (TipoVehiculo) comboTipoVehiculoCapacidad.getSelectedItem();
                    int cantidad = Integer.parseInt(txtCantidadCapacidad.getText());
                    
                    boolean exito = controlador.establecerCapacidad(tipo, cantidad);
                    if (exito) {
                        JOptionPane.showMessageDialog(PanelAdministracion.this, "Capacidad para " + tipo.getDescripcion() + " establecida a " + cantidad);
                        cargarDatosActualesParqueadero(); // Para refrescar los displays que leen de 'this.parqueadero'
                        txtCantidadCapacidad.setText("");
                    } else {
                         JOptionPane.showMessageDialog(PanelAdministracion.this, "La cantidad no puede ser negativa o el tipo es nulo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PanelAdministracion.this, "Por favor, ingrese un número válido para la cantidad.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEstablecerTarifaHora.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TipoVehiculo tipo = (TipoVehiculo) comboTipoVehiculoTarifaHora.getSelectedItem();
                    double tarifa = Double.parseDouble(txtTarifaPorHora.getText());

                    boolean exito = controlador.configurarTarifaHora(tipo, tarifa);
                    if (exito) {
                        JOptionPane.showMessageDialog(PanelAdministracion.this, "Tarifa por hora para " + tipo.getDescripcion() + " establecida a " + tarifa);
                        cargarDatosActualesParqueadero();
                        txtTarifaPorHora.setText("");
                    } else {
                        JOptionPane.showMessageDialog(PanelAdministracion.this, "La tarifa no puede ser negativa o el tipo es nulo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PanelAdministracion.this, "Por favor, ingrese un número válido para la tarifa.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnEstablecerTarifaMembresia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Membresia.TipoPeriodoMembresia periodo = (Membresia.TipoPeriodoMembresia) comboPeriodoMembresia.getSelectedItem();
                    TipoVehiculo tipoVehiculo = (TipoVehiculo) comboTipoVehiculoTarifaMembresia.getSelectedItem();
                    double precio = Double.parseDouble(txtPrecioMembresia.getText());

                    boolean exito = controlador.configurarTarifaMembresia(periodo, tipoVehiculo, precio);
                    if(exito) {
                        JOptionPane.showMessageDialog(PanelAdministracion.this, "Tarifa de membresía establecida.");
                        cargarDatosActualesParqueadero();
                        txtPrecioMembresia.setText("");
                    } else {
                         JOptionPane.showMessageDialog(PanelAdministracion.this, "Datos inválidos para tarifa de membresía.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PanelAdministracion.this, "Por favor, ingrese un número válido para el precio.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}