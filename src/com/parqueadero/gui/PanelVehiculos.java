package com.parqueadero.gui;

import com.parqueadero.controlador.VehiculoControlador;
import com.parqueadero.modelo.Cliente;
import com.parqueadero.modelo.TipoVehiculo;
import com.parqueadero.modelo.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

public class PanelVehiculos extends JPanel {

    private VehiculoControlador vehiculoControlador;

    private JTextField txtPlaca;
    private JComboBox<TipoVehiculo> comboTipoVehiculo;
    private JTextField txtColor;
    private JTextField txtModelo;
    private JTextField txtCedulaPropietario; 
    private JLabel lblNombrePropietarioActual; 
    private JButton btnRegistrarVehiculo;
    private JButton btnActualizarVehiculo;
    private JButton btnLimpiarFormularioVehiculo;

    private JTextField txtTerminoBusquedaVehiculo;
    private JComboBox<String> comboCriterioBusquedaVehiculo;
    private JButton btnBuscarVehiculo;

    private JTable tablaVehiculos;
    private DefaultTableModel modeloTablaVehiculos;
    private JButton btnEliminarVehiculo;

    private String placaVehiculoSeleccionadoParaActualizar = null;

    public PanelVehiculos(VehiculoControlador vehiculoControlador) {
        this.vehiculoControlador = vehiculoControlador;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelSuperiorVehiculo = new JPanel(new GridLayout(1, 2, 10, 0));

        JPanel panelFormularioVehiculo = new JPanel(new GridBagLayout());
        panelFormularioVehiculo.setBorder(BorderFactory.createTitledBorder("Datos del Vehículo"));
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(5, 5, 5, 5);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;

        gbcForm.gridx = 0; gbcForm.gridy = 0; panelFormularioVehiculo.add(new JLabel("Placa:"), gbcForm);
        txtPlaca = new JTextField(15);
        gbcForm.gridx = 1; gbcForm.gridy = 0; panelFormularioVehiculo.add(txtPlaca, gbcForm);

        gbcForm.gridx = 0; gbcForm.gridy = 1; panelFormularioVehiculo.add(new JLabel("Tipo:"), gbcForm);
        comboTipoVehiculo = new JComboBox<>(TipoVehiculo.values());
        gbcForm.gridx = 1; gbcForm.gridy = 1; panelFormularioVehiculo.add(comboTipoVehiculo, gbcForm);

        gbcForm.gridx = 0; gbcForm.gridy = 2; panelFormularioVehiculo.add(new JLabel("Color:"), gbcForm);
        txtColor = new JTextField(15);
        gbcForm.gridx = 1; gbcForm.gridy = 2; panelFormularioVehiculo.add(txtColor, gbcForm);

        gbcForm.gridx = 0; gbcForm.gridy = 3; panelFormularioVehiculo.add(new JLabel("Modelo:"), gbcForm);
        txtModelo = new JTextField(15);
        gbcForm.gridx = 1; gbcForm.gridy = 3; panelFormularioVehiculo.add(txtModelo, gbcForm);

        gbcForm.gridx = 0; gbcForm.gridy = 4; panelFormularioVehiculo.add(new JLabel("Cédula Propietario:"), gbcForm);
        txtCedulaPropietario = new JTextField(15);
        gbcForm.gridx = 1; gbcForm.gridy = 4; panelFormularioVehiculo.add(txtCedulaPropietario, gbcForm);
        
        lblNombrePropietarioActual = new JLabel("(Propietario: N/A)");
        gbcForm.gridx = 0; gbcForm.gridy = 5; gbcForm.gridwidth=2; panelFormularioVehiculo.add(lblNombrePropietarioActual, gbcForm);
        gbcForm.gridwidth=1;

        JPanel panelBotonesFormVehiculo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRegistrarVehiculo = new JButton("Registrar Vehículo");
        btnActualizarVehiculo = new JButton("Actualizar Vehículo");
        btnActualizarVehiculo.setEnabled(false);
        btnLimpiarFormularioVehiculo = new JButton("Limpiar");
        panelBotonesFormVehiculo.add(btnRegistrarVehiculo);
        panelBotonesFormVehiculo.add(btnActualizarVehiculo);
        panelBotonesFormVehiculo.add(btnLimpiarFormularioVehiculo);
        gbcForm.gridx = 0; gbcForm.gridy = 6; gbcForm.gridwidth = 2; panelFormularioVehiculo.add(panelBotonesFormVehiculo, gbcForm);
        
        panelSuperiorVehiculo.add(panelFormularioVehiculo);

        JPanel panelBusquedaVehiculo = new JPanel(new GridBagLayout());
        panelBusquedaVehiculo.setBorder(BorderFactory.createTitledBorder("Búsqueda de Vehículos"));
        GridBagConstraints gbcBusqueda = new GridBagConstraints();
        gbcBusqueda.insets = new Insets(5,5,5,5);
        gbcBusqueda.fill = GridBagConstraints.HORIZONTAL;

        gbcBusqueda.gridx = 0; gbcBusqueda.gridy = 0; panelBusquedaVehiculo.add(new JLabel("Buscar por:"), gbcBusqueda);
        comboCriterioBusquedaVehiculo = new JComboBox<>(new String[]{"Placa", "Tipo de Vehículo", "Cédula del Propietario"});
        gbcBusqueda.gridx = 1; gbcBusqueda.gridy = 0; panelBusquedaVehiculo.add(comboCriterioBusquedaVehiculo, gbcBusqueda);

        gbcBusqueda.gridx = 0; gbcBusqueda.gridy = 1; panelBusquedaVehiculo.add(new JLabel("Término:"), gbcBusqueda);
        txtTerminoBusquedaVehiculo = new JTextField(15);
        gbcBusqueda.gridx = 1; gbcBusqueda.gridy = 1; panelBusquedaVehiculo.add(txtTerminoBusquedaVehiculo, gbcBusqueda);
        
        btnBuscarVehiculo = new JButton("Buscar Vehículo");
        gbcBusqueda.gridx = 0; gbcBusqueda.gridy = 2; gbcBusqueda.gridwidth = 2; gbcBusqueda.fill = GridBagConstraints.NONE; gbcBusqueda.anchor = GridBagConstraints.CENTER;
        panelBusquedaVehiculo.add(btnBuscarVehiculo, gbcBusqueda);
        
        gbcBusqueda.gridx = 0; gbcBusqueda.gridy = 3; gbcBusqueda.gridwidth = 2; gbcBusqueda.weighty = 1.0; 
        panelBusquedaVehiculo.add(new JLabel(), gbcBusqueda); // Espacio para empujar

        panelSuperiorVehiculo.add(panelBusquedaVehiculo);
        add(panelSuperiorVehiculo, BorderLayout.NORTH);

        String[] columnasVehiculos = {"Placa", "Tipo", "Color", "Modelo", "Propietario (Cédula)", "Propietario (Nombre)", "Membresía"};
        modeloTablaVehiculos = new DefaultTableModel(columnasVehiculos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaVehiculos = new JTable(modeloTablaVehiculos);
        tablaVehiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVehiculos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaVehiculos.getSelectedRow() != -1) {
                cargarVehiculoSeleccionadoEnFormulario();
            }
        });
        add(new JScrollPane(tablaVehiculos), BorderLayout.CENTER);

        JPanel panelAccionesTablaVehiculo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnEliminarVehiculo = new JButton("Eliminar Vehículo Seleccionado");
        panelAccionesTablaVehiculo.add(btnEliminarVehiculo);
        add(panelAccionesTablaVehiculo, BorderLayout.SOUTH);

        asignarActionListenersVehiculo();
        cargarTodosLosVehiculosEnTabla();
    }

    private void asignarActionListenersVehiculo() {
        btnRegistrarVehiculo.addActionListener(e -> registrarNuevoVehiculo());
        btnActualizarVehiculo.addActionListener(e -> actualizarVehiculoExistente());
        btnLimpiarFormularioVehiculo.addActionListener(e -> limpiarFormularioVehiculo());
        btnBuscarVehiculo.addActionListener(e -> buscarVehiculos());
        btnEliminarVehiculo.addActionListener(e -> eliminarVehiculoSeleccionado());
    }

    private void limpiarFormularioVehiculo() {
        txtPlaca.setText("");
        comboTipoVehiculo.setSelectedIndex(0);
        comboTipoVehiculo.setEnabled(true);
        txtColor.setText("");
        txtModelo.setText("");
        txtCedulaPropietario.setText("");
        lblNombrePropietarioActual.setText("(Propietario: N/A)");
        txtPlaca.setEditable(true);
        btnRegistrarVehiculo.setEnabled(true);
        btnActualizarVehiculo.setEnabled(false);
        placaVehiculoSeleccionadoParaActualizar = null;
        tablaVehiculos.clearSelection();
    }

    private void registrarNuevoVehiculo() {
        String placa = txtPlaca.getText().trim().toUpperCase();
        TipoVehiculo tipo = (TipoVehiculo) comboTipoVehiculo.getSelectedItem();
        String color = txtColor.getText().trim();
        String modelo = txtModelo.getText().trim();
        String cedulaPropietario = txtCedulaPropietario.getText().trim();

        if (placa.isEmpty() || color.isEmpty() || modelo.isEmpty() || cedulaPropietario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Placa, Color, Modelo y Cédula del Propietario son obligatorios.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<Vehiculo> vehiculoOpt = vehiculoControlador.registrarVehiculo(cedulaPropietario, placa, tipo, color, modelo);

        if (vehiculoOpt.isPresent()) {
            JOptionPane.showMessageDialog(this, "Vehículo registrado exitosamente: " + placa);
            limpiarFormularioVehiculo();
            cargarTodosLosVehiculosEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el vehículo. Verifique los datos (ej. placa duplicada, propietario no existe).", "Error de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarVehiculoExistente() {
        if (placaVehiculoSeleccionadoParaActualizar == null) {
            JOptionPane.showMessageDialog(this, "No hay un vehículo seleccionado para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nuevoColor = txtColor.getText().trim();
        String nuevoModelo = txtModelo.getText().trim();
        String nuevaCedulaPropietario = txtCedulaPropietario.getText().trim(); 

        if (nuevoColor.isEmpty() || nuevoModelo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Color y Modelo son obligatorios.", "Error de Actualización", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean actualizado = vehiculoControlador.actualizarVehiculo(placaVehiculoSeleccionadoParaActualizar, nuevoColor, nuevoModelo, nuevaCedulaPropietario.isEmpty() ? null : nuevaCedulaPropietario);
        if (actualizado) {
            JOptionPane.showMessageDialog(this, "Vehículo actualizado exitosamente.");
            limpiarFormularioVehiculo();
            cargarTodosLosVehiculosEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el vehículo (verifique si la nueva cédula de propietario es válida).", "Error de Actualización", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTodosLosVehiculosEnTabla() {
        modeloTablaVehiculos.setRowCount(0); 
        List<Vehiculo> vehiculos = vehiculoControlador.obtenerTodosLosVehiculos();
        for (Vehiculo vehiculo : vehiculos) {
            Cliente propietario = vehiculo.getPropietario();
            String cedulaProp = propietario != null ? propietario.getCedula() : "N/A";
            String nombreProp = propietario != null ? propietario.getNombre() : "N/A";
            String infoMembresia = vehiculo.tieneMembresiaActiva() ? 
                                   "Activa (" + vehiculo.getMembresia().getFechaFin().toLocalDate() + ")" : 
                                   "No";
            modeloTablaVehiculos.addRow(new Object[]{
                    vehiculo.getPlaca(),
                    vehiculo.getTipo().getDescripcion(),
                    vehiculo.getColor(),
                    vehiculo.getModelo(),
                    cedulaProp,
                    nombreProp,
                    infoMembresia
            });
        }
    }

    private void buscarVehiculos() {
        String termino = txtTerminoBusquedaVehiculo.getText().trim();
        String criterio = (String) comboCriterioBusquedaVehiculo.getSelectedItem();
        List<Vehiculo> vehiculosEncontrados = null;

        if (termino.isEmpty()) {
            cargarTodosLosVehiculosEnTabla();
            return;
        }

        switch (criterio) {
            case "Placa":
                Optional<Vehiculo> vehiculoOpt = vehiculoControlador.buscarVehiculoPorPlaca(termino.toUpperCase());
                vehiculosEncontrados = vehiculoOpt.map(List::of).orElseGet(List::of);
                break;
            case "Tipo de Vehículo":
                try {
                    TipoVehiculo tipoSeleccionado = null;
                    for(TipoVehiculo tv : TipoVehiculo.values()){
                        if(tv.getDescripcion().equalsIgnoreCase(termino) || tv.name().equalsIgnoreCase(termino)){
                            tipoSeleccionado = tv;
                            break;
                        }
                    }
                    if(tipoSeleccionado != null){
                        vehiculosEncontrados = vehiculoControlador.buscarVehiculosPorTipo(tipoSeleccionado);
                    } else {
                         JOptionPane.showMessageDialog(this, "Tipo de vehículo no reconocido: " + termino, "Búsqueda", JOptionPane.WARNING_MESSAGE);
                         vehiculosEncontrados = List.of();
                    }
                } catch (IllegalArgumentException e) {
                     JOptionPane.showMessageDialog(this, "Tipo de vehículo no válido: " + termino, "Búsqueda", JOptionPane.WARNING_MESSAGE);
                    vehiculosEncontrados = List.of();
                }
                break;
            case "Cédula del Propietario":
                vehiculosEncontrados = vehiculoControlador.buscarVehiculosPorPropietario(termino);
                break;
        }

        modeloTablaVehiculos.setRowCount(0);
        if (vehiculosEncontrados != null && !vehiculosEncontrados.isEmpty()) {
            for (Vehiculo vehiculo : vehiculosEncontrados) {
                Cliente propietario = vehiculo.getPropietario();
                String cedulaProp = propietario != null ? propietario.getCedula() : "N/A";
                String nombreProp = propietario != null ? propietario.getNombre() : "N/A";
                 String infoMembresia = vehiculo.tieneMembresiaActiva() ? 
                                   "Activa (" + vehiculo.getMembresia().getFechaFin().toLocalDate() + ")" : 
                                   "No";
                modeloTablaVehiculos.addRow(new Object[]{
                        vehiculo.getPlaca(),
                        vehiculo.getTipo().getDescripcion(),
                        vehiculo.getColor(),
                        vehiculo.getModelo(),
                        cedulaProp,
                        nombreProp,
                        infoMembresia
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron vehículos con ese criterio.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cargarVehiculoSeleccionadoEnFormulario() {
        int filaSeleccionada = tablaVehiculos.getSelectedRow();
        if (filaSeleccionada != -1) {
            placaVehiculoSeleccionadoParaActualizar = (String) modeloTablaVehiculos.getValueAt(filaSeleccionada, 0);
            txtPlaca.setText(placaVehiculoSeleccionadoParaActualizar);
            
            String tipoVehiculoStr = (String) modeloTablaVehiculos.getValueAt(filaSeleccionada, 1);
            for(TipoVehiculo tv : TipoVehiculo.values()){
                if(tv.getDescripcion().equals(tipoVehiculoStr)){
                    comboTipoVehiculo.setSelectedItem(tv);
                    break;
                }
            }
            
            txtColor.setText((String) modeloTablaVehiculos.getValueAt(filaSeleccionada, 2));
            txtModelo.setText((String) modeloTablaVehiculos.getValueAt(filaSeleccionada, 3));
            String cedulaPropietarioActual = (String) modeloTablaVehiculos.getValueAt(filaSeleccionada, 4);
            txtCedulaPropietario.setText(cedulaPropietarioActual);
            lblNombrePropietarioActual.setText("(Propietario: " + modeloTablaVehiculos.getValueAt(filaSeleccionada, 5) + ")");

            txtPlaca.setEditable(false); 
            comboTipoVehiculo.setEnabled(false); 
            btnRegistrarVehiculo.setEnabled(false);
            btnActualizarVehiculo.setEnabled(true);
        } else {
             limpiarFormularioVehiculo(); 
        }
    }
    
    private void eliminarVehiculoSeleccionado() {
        int filaSeleccionada = tablaVehiculos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un vehículo de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String placa = (String) modeloTablaVehiculos.getValueAt(filaSeleccionada, 0);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar el vehículo con placa: " + placa + "?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = vehiculoControlador.eliminarVehiculo(placa);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Vehículo eliminado exitosamente.");
                cargarTodosLosVehiculosEnTabla();
                limpiarFormularioVehiculo();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el vehículo (podría tener una membresía activa o estar actualmente en el parqueadero).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}