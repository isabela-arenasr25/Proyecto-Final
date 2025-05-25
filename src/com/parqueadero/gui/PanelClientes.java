package com.parqueadero.gui;

import com.parqueadero.controlador.ClienteControlador;
import com.parqueadero.modelo.Cliente;
import com.parqueadero.modelo.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

public class PanelClientes extends JPanel {

    private ClienteControlador clienteControlador;

    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JButton btnAnadirCliente;
    private JButton btnActualizarCliente;
    private JButton btnLimpiarFormulario;

    private JTextField txtTerminoBusqueda;
    private JComboBox<String> comboCriterioBusqueda;
    private JButton btnBuscarCliente;

    private JTable tablaClientes;
    private DefaultTableModel modeloTablaClientes;
    private JButton btnEliminarCliente;
    private JButton btnVerVehiculos;
    private JTextArea areaVehiculosCliente;

    private String cedulaClienteSeleccionadoParaActualizar = null;

    public PanelClientes(ClienteControlador clienteControlador) {
        this.clienteControlador = clienteControlador;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelSuperior = new JPanel(new GridLayout(1, 2, 10, 0));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(5, 5, 5, 5);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;

        gbcForm.gridx = 0; gbcForm.gridy = 0; panelFormulario.add(new JLabel("Cédula:"), gbcForm);
        txtCedula = new JTextField(15);
        gbcForm.gridx = 1; gbcForm.gridy = 0; panelFormulario.add(txtCedula, gbcForm);

        gbcForm.gridx = 0; gbcForm.gridy = 1; panelFormulario.add(new JLabel("Nombre:"), gbcForm);
        txtNombre = new JTextField(15);
        gbcForm.gridx = 1; gbcForm.gridy = 1; panelFormulario.add(txtNombre, gbcForm);

        gbcForm.gridx = 0; gbcForm.gridy = 2; panelFormulario.add(new JLabel("Teléfono:"), gbcForm);
        txtTelefono = new JTextField(15);
        gbcForm.gridx = 1; gbcForm.gridy = 2; panelFormulario.add(txtTelefono, gbcForm);

        gbcForm.gridx = 0; gbcForm.gridy = 3; panelFormulario.add(new JLabel("Correo:"), gbcForm);
        txtCorreo = new JTextField(15);
        gbcForm.gridx = 1; gbcForm.gridy = 3; panelFormulario.add(txtCorreo, gbcForm);

        JPanel panelBotonesForm = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAnadirCliente = new JButton("Añadir Cliente");
        btnActualizarCliente = new JButton("Actualizar Cliente");
        btnActualizarCliente.setEnabled(false); 
        btnLimpiarFormulario = new JButton("Limpiar");
        panelBotonesForm.add(btnAnadirCliente);
        panelBotonesForm.add(btnActualizarCliente);
        panelBotonesForm.add(btnLimpiarFormulario);
        gbcForm.gridx = 0; gbcForm.gridy = 4; gbcForm.gridwidth = 2; panelFormulario.add(panelBotonesForm, gbcForm);
        
        panelSuperior.add(panelFormulario);

        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda de Clientes"));
        GridBagConstraints gbcBusqueda = new GridBagConstraints();
        gbcBusqueda.insets = new Insets(5,5,5,5);
        gbcBusqueda.fill = GridBagConstraints.HORIZONTAL;

        gbcBusqueda.gridx = 0; gbcBusqueda.gridy = 0; panelBusqueda.add(new JLabel("Buscar por:"), gbcBusqueda);
        comboCriterioBusqueda = new JComboBox<>(new String[]{"Cédula", "Nombre", "Teléfono"});
        gbcBusqueda.gridx = 1; gbcBusqueda.gridy = 0; panelBusqueda.add(comboCriterioBusqueda, gbcBusqueda);

        gbcBusqueda.gridx = 0; gbcBusqueda.gridy = 1; panelBusqueda.add(new JLabel("Término:"), gbcBusqueda);
        txtTerminoBusqueda = new JTextField(15);
        gbcBusqueda.gridx = 1; gbcBusqueda.gridy = 1; panelBusqueda.add(txtTerminoBusqueda, gbcBusqueda);
        
        btnBuscarCliente = new JButton("Buscar");
        gbcBusqueda.gridx = 0; gbcBusqueda.gridy = 2; gbcBusqueda.gridwidth = 2; gbcBusqueda.fill = GridBagConstraints.NONE; gbcBusqueda.anchor = GridBagConstraints.CENTER;
        panelBusqueda.add(btnBuscarCliente, gbcBusqueda);

        areaVehiculosCliente = new JTextArea(5, 20);
        areaVehiculosCliente.setEditable(false);
        areaVehiculosCliente.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaVehiculosCliente.setBorder(BorderFactory.createTitledBorder("Vehículos del Cliente Seleccionado"));
        JScrollPane scrollVehiculos = new JScrollPane(areaVehiculosCliente);
        gbcBusqueda.gridx = 0; gbcBusqueda.gridy = 3; gbcBusqueda.gridwidth = 2; gbcBusqueda.fill = GridBagConstraints.BOTH; gbcBusqueda.weighty = 1.0;
        panelBusqueda.add(scrollVehiculos, gbcBusqueda);

        panelSuperior.add(panelBusqueda);
        add(panelSuperior, BorderLayout.NORTH);

        String[] columnas = {"Cédula", "Nombre", "Teléfono", "Correo", "N° Vehículos"};
        modeloTablaClientes = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tablaClientes = new JTable(modeloTablaClientes);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaClientes.getSelectedRow() != -1) {
                cargarClienteSeleccionadoEnFormulario();
            }
        });

        add(new JScrollPane(tablaClientes), BorderLayout.CENTER);

        JPanel panelAccionesTabla = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnEliminarCliente = new JButton("Eliminar Cliente Seleccionado");
        btnVerVehiculos = new JButton("Ver Vehículos del Cliente");
        panelAccionesTabla.add(btnEliminarCliente);
        panelAccionesTabla.add(btnVerVehiculos);
        add(panelAccionesTabla, BorderLayout.SOUTH);

        asignarActionListeners();
        cargarTodosLosClientesEnTabla();
    }

    private void asignarActionListeners() {
        btnAnadirCliente.addActionListener(e -> anadirNuevoCliente());
        btnActualizarCliente.addActionListener(e -> actualizarClienteExistente());
        btnLimpiarFormulario.addActionListener(e -> limpiarFormulario());
        btnBuscarCliente.addActionListener(e -> buscarClientes());
        btnEliminarCliente.addActionListener(e -> eliminarClienteSeleccionado());
        btnVerVehiculos.addActionListener(e -> verVehiculosClienteSeleccionado());
    }

    private void limpiarFormulario() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtCedula.setEditable(true);
        btnAnadirCliente.setEnabled(true);
        btnActualizarCliente.setEnabled(false);
        cedulaClienteSeleccionadoParaActualizar = null;
        tablaClientes.clearSelection();
        areaVehiculosCliente.setText("");
    }

    private void anadirNuevoCliente() {
        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (cedula.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cédula, Nombre y Teléfono son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<Cliente> clienteOpt = clienteControlador.anadirCliente(nombre, cedula, telefono, correo);
        
        if (clienteOpt.isPresent()) {
            JOptionPane.showMessageDialog(this, "Cliente añadido exitosamente: " + nombre);
            limpiarFormulario();
            cargarTodosLosClientesEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo añadir el cliente. Verifique los datos (ej. cédula duplicada).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarClienteExistente() {
        if (cedulaClienteSeleccionadoParaActualizar == null) {
            JOptionPane.showMessageDialog(this, "No hay un cliente seleccionado para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y Teléfono son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean actualizado = clienteControlador.actualizarCliente(cedulaClienteSeleccionadoParaActualizar, nombre, telefono, correo);
        
        if (actualizado) {
            JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente.");
            limpiarFormulario(); 
            cargarTodosLosClientesEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTodosLosClientesEnTabla() {
        modeloTablaClientes.setRowCount(0); 
        List<Cliente> clientes = clienteControlador.obtenerTodosLosClientes();
        for (Cliente cliente : clientes) {
            modeloTablaClientes.addRow(new Object[]{
                    cliente.getCedula(),
                    cliente.getNombre(),
                    cliente.getTelefono(),
                    cliente.getCorreo(),
                    cliente.getVehiculos().size() 
            });
        }
    }
    
    private void buscarClientes() {
        String termino = txtTerminoBusqueda.getText().trim();
        String criterio = (String) comboCriterioBusqueda.getSelectedItem();
        List<Cliente> clientesEncontrados = null;

        if (termino.isEmpty()) {
            cargarTodosLosClientesEnTabla(); 
            return;
        }

        if ("Cédula".equals(criterio)) {
            Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorCedula(termino);
            clientesEncontrados = clienteOpt.map(List::of).orElseGet(List::of);
        } else if ("Nombre".equals(criterio)) {
            clientesEncontrados = clienteControlador.buscarClientesPorNombre(termino);
        } else if ("Teléfono".equals(criterio)) {
             Optional<Cliente> clienteOpt = clienteControlador.buscarClientePorTelefono(termino);
             clientesEncontrados = clienteOpt.map(List::of).orElseGet(List::of);
        }

        modeloTablaClientes.setRowCount(0); 
        if (clientesEncontrados != null && !clientesEncontrados.isEmpty()) {
            for (Cliente cliente : clientesEncontrados) {
                modeloTablaClientes.addRow(new Object[]{
                        cliente.getCedula(),
                        cliente.getNombre(),
                        cliente.getTelefono(),
                        cliente.getCorreo(),
                        cliente.getVehiculos().size()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron clientes con ese criterio.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cargarClienteSeleccionadoEnFormulario() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada != -1) {
            cedulaClienteSeleccionadoParaActualizar = (String) modeloTablaClientes.getValueAt(filaSeleccionada, 0);
            txtCedula.setText((String) modeloTablaClientes.getValueAt(filaSeleccionada, 0));
            txtNombre.setText((String) modeloTablaClientes.getValueAt(filaSeleccionada, 1));
            txtTelefono.setText((String) modeloTablaClientes.getValueAt(filaSeleccionada, 2));
            txtCorreo.setText((String) modeloTablaClientes.getValueAt(filaSeleccionada, 3));

            txtCedula.setEditable(false); 
            btnAnadirCliente.setEnabled(false);
            btnActualizarCliente.setEnabled(true);
            areaVehiculosCliente.setText(""); 
        }
    }
    
    private void eliminarClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String cedula = (String) modeloTablaClientes.getValueAt(filaSeleccionada, 0);
        String nombre = (String) modeloTablaClientes.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar al cliente: " + nombre + " (Cédula: " + cedula + ")?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = clienteControlador.eliminarCliente(cedula);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.");
                cargarTodosLosClientesEnTabla();
                limpiarFormulario(); 
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el cliente (puede tener vehículos asociados, membresías activas, o estar actualmente en el parqueadero).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void verVehiculosClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente de la tabla para ver sus vehículos.", "Información", JOptionPane.INFORMATION_MESSAGE);
            areaVehiculosCliente.setText("");
            return;
        }
        String cedulaCliente = (String) modeloTablaClientes.getValueAt(filaSeleccionada, 0);
        
        List<Vehiculo> vehiculos = clienteControlador.verVehiculosDelCliente(cedulaCliente);
        
        if (vehiculos.isEmpty()) {
            areaVehiculosCliente.setText("El cliente no tiene vehículos registrados.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Vehículos de ").append(modeloTablaClientes.getValueAt(filaSeleccionada, 1)).append(":\n");
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
            areaVehiculosCliente.setText(sb.toString());
        }
    }
}