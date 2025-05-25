package com.parqueadero.gui;

import com.parqueadero.controlador.MembresiaControlador;
import com.parqueadero.modelo.Membresia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class PanelMembresias extends JPanel {

    private MembresiaControlador membresiaControlador;

    private JTextField txtCedulaClienteMembresia;
    private JTextField txtPlacaVehiculoMembresia;
    private JComboBox<Membresia.TipoPeriodoMembresia> comboTipoPeriodoMembresia;
    private JButton btnVerificarPrecio; 
    private JLabel lblPrecioCalculado;   
    private JButton btnRegistrarPagoMembresia;
    private JTextArea areaConfirmacionMembresia;

    public PanelMembresias(MembresiaControlador membresiaControlador) {
        this.membresiaControlador = membresiaControlador;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Gestión de Membresías"));
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; panelFormulario.add(new JLabel("Cédula Cliente:"), gbc);
        txtCedulaClienteMembresia = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; panelFormulario.add(txtCedulaClienteMembresia, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; panelFormulario.add(new JLabel("Placa Vehículo:"), gbc);
        txtPlacaVehiculoMembresia = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; panelFormulario.add(txtPlacaVehiculoMembresia, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; panelFormulario.add(new JLabel("Periodo Membresía:"), gbc);
        comboTipoPeriodoMembresia = new JComboBox<>(Membresia.TipoPeriodoMembresia.values());
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2; panelFormulario.add(comboTipoPeriodoMembresia, gbc);
        gbc.gridwidth = 1;
        
        btnVerificarPrecio = new JButton("Calcular Precio");
        gbc.gridx = 0; gbc.gridy = 3; panelFormulario.add(btnVerificarPrecio, gbc);
        lblPrecioCalculado = new JLabel("Precio: $0.00");
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2; panelFormulario.add(lblPrecioCalculado, gbc);
        gbc.gridwidth = 1;

        btnRegistrarPagoMembresia = new JButton("Registrar Pago de Membresía");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(btnRegistrarPagoMembresia, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        areaConfirmacionMembresia = new JTextArea(10, 40);
        areaConfirmacionMembresia.setEditable(false);
        areaConfirmacionMembresia.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollConfirmacion = new JScrollPane(areaConfirmacionMembresia);
        
        add(panelFormulario, BorderLayout.NORTH);
        add(scrollConfirmacion, BorderLayout.CENTER);

        asignarActionListenersMembresias();
    }

    private void asignarActionListenersMembresias() {
        btnVerificarPrecio.addActionListener(e -> calcularYMostrarPrecio());
        
        btnRegistrarPagoMembresia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarMembresia();
            }
        });
    }
    
    private void calcularYMostrarPrecio() {
        String cedulaCliente = txtCedulaClienteMembresia.getText().trim();
        String placaVehiculo = txtPlacaVehiculoMembresia.getText().trim().toUpperCase();
        Membresia.TipoPeriodoMembresia tipoPeriodo = (Membresia.TipoPeriodoMembresia) comboTipoPeriodoMembresia.getSelectedItem();

        MembresiaControlador.PrecioMembresiaInfo precioInfo = 
                membresiaControlador.calcularPrecioMembresia(cedulaCliente, placaVehiculo, tipoPeriodo);

        lblPrecioCalculado.setText(precioInfo.getMensaje());
        if (precioInfo.getPrecio() < 0 && !precioInfo.getMensaje().toLowerCase().contains("precio")) { 
            JOptionPane.showMessageDialog(this, precioInfo.getMensaje(), "Error de Cálculo", JOptionPane.ERROR_MESSAGE);
        } else if (precioInfo.getPrecio() <= 0 && precioInfo.getMensaje().toLowerCase().contains("tarifa no configurada")){ 
             JOptionPane.showMessageDialog(this, precioInfo.getMensaje(), "Tarifa no Encontrada", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void registrarMembresia() {
        String cedulaCliente = txtCedulaClienteMembresia.getText().trim();
        String placaVehiculo = txtPlacaVehiculoMembresia.getText().trim().toUpperCase();
        Membresia.TipoPeriodoMembresia tipoPeriodo = (Membresia.TipoPeriodoMembresia) comboTipoPeriodoMembresia.getSelectedItem();

        if (cedulaCliente.isEmpty() || placaVehiculo.isEmpty() || tipoPeriodo == null) {
            JOptionPane.showMessageDialog(this, "Cédula, Placa y Periodo son requeridos.", "Datos Incompletos", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Optional<Membresia> membresiaOpt = membresiaControlador.registrarPagoMembresia(cedulaCliente, placaVehiculo, tipoPeriodo);

        if (membresiaOpt.isPresent()) {
            Membresia membresiaRegistrada = membresiaOpt.get();
            JOptionPane.showMessageDialog(this, "Pago de membresía registrado exitosamente para el vehículo: " + placaVehiculo, "Membresía Registrada", JOptionPane.INFORMATION_MESSAGE);
            
            String facturaMembresia = membresiaControlador.generarFacturaMembresia(membresiaRegistrada);
            areaConfirmacionMembresia.setText(facturaMembresia);

            txtCedulaClienteMembresia.setText("");
            txtPlacaVehiculoMembresia.setText("");
            comboTipoPeriodoMembresia.setSelectedIndex(0);
            lblPrecioCalculado.setText("Precio: $0.00");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el pago de la membresía.\nConsulte la consola para más detalles o verifique la configuración (tarifas, espacios, asociación cliente-vehículo).", "Error en Registro de Membresía", JOptionPane.ERROR_MESSAGE);
            areaConfirmacionMembresia.setText("Error al registrar la membresía.");
        }
    }
}