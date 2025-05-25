package com.parqueadero.main;

import com.parqueadero.modelo.Parqueadero;
import com.parqueadero.servicio.*;
import com.parqueadero.controlador.*;
import com.parqueadero.gui.*;

import javax.swing.*;
import java.awt.*;

public class AplicacionGUI {

    // --- Servicios ---
    private static AdministracionParqueaderoServicio administracionServicio;
    private static ClienteServicio clienteServicio;
    private static VehiculoServicio vehiculoServicio;
    private static PagoServicio pagoServicio;
    private static ReporteServicio reporteServicio;

    // --- Controladores ---
    private static AdministracionParqueaderoControlador adminControlador;
    private static ClienteControlador clienteControlador;
    private static VehiculoControlador vehiculoControlador;
    private static OperacionesParqueaderoControlador operacionesControlador;
    private static MembresiaControlador membresiaControlador;
    private static ReporteControlador reporteControlador;

    // --- Objeto Modelo Principal del Parqueadero ---
    private static Parqueadero parqueaderoControlado;

    public static void main(String[] args) {
        // --- 1. Inicialización de Servicios ---
        // Es importante el orden si hay dependencias entre servicios.
        AdministracionParqueaderoServicioImpl adminServicioImpl = new AdministracionParqueaderoServicioImpl();
        administracionServicio = adminServicioImpl;
        parqueaderoControlado = adminServicioImpl.obtenerDatosParqueadero(); // Obtenemos la instancia central del parqueadero

        clienteServicio = new ClienteServicioImpl();
        vehiculoServicio = new VehiculoServicioImpl(clienteServicio); // VehiculoServicio depende de ClienteServicio

        // PagoServicioImpl depende de Parqueadero y VehiculoServicio
        PagoServicioImpl pagoServicioImpl = new PagoServicioImpl(parqueaderoControlado, vehiculoServicio);
        pagoServicio = pagoServicioImpl; // Guardamos la instancia concreta para pasarla a ReporteServicioImpl si es necesario

        // ReporteServicioImpl depende de ClienteServicio, PagoServicio (interfaz) y PagoServicioImpl (instancia)
        reporteServicio = new ReporteServicioImpl(clienteServicio, pagoServicio, pagoServicioImpl);


        // --- 2. Inicialización de Controladores ---
        // Se inyectan los servicios necesarios a cada controlador.
        adminControlador = new AdministracionParqueaderoControlador(administracionServicio);
        clienteControlador = new ClienteControlador(clienteServicio);
        vehiculoControlador = new VehiculoControlador(vehiculoServicio, clienteServicio);
        operacionesControlador = new OperacionesParqueaderoControlador(pagoServicio);
        membresiaControlador = new MembresiaControlador(pagoServicio, clienteServicio, vehiculoServicio, parqueaderoControlado);
        reporteControlador = new ReporteControlador(reporteServicio);
        

        // --- 3. Creación de la GUI en el Event Dispatch Thread ---
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                crearYMostrarGUI();
            }
        });
    }

    private static void crearYMostrarGUI() {
        // --- Configuración de la Ventana Principal (JFrame) ---
        JFrame frame = new JFrame("Sistema de Gestión de Parqueadero Del Quindío");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768); // Un tamaño generoso para los paneles
        frame.setLocationRelativeTo(null); // Centrar en pantalla

        // --- Panel Principal con CardLayout para Navegación entre Vistas ---
        JPanel panelPrincipalTarjetas = new JPanel(new CardLayout());

        // --- Instanciación de los Paneles de la GUI (Vistas) ---
        // A cada panel se le pasa su controlador correspondiente.
        PanelAdministracion panelAdmin = new PanelAdministracion(adminControlador);
        panelPrincipalTarjetas.add(panelAdmin, "PANEL_ADMIN");

        PanelClientes panelClientes = new PanelClientes(clienteControlador);
        panelPrincipalTarjetas.add(panelClientes, "PANEL_CLIENTES");

        PanelVehiculos panelVehiculos = new PanelVehiculos(vehiculoControlador);
        panelPrincipalTarjetas.add(panelVehiculos, "PANEL_VEHICULOS");

        PanelOperaciones panelOperaciones = new PanelOperaciones(operacionesControlador);
        panelPrincipalTarjetas.add(panelOperaciones, "PANEL_OPERACIONES");

        PanelMembresias panelMembresias = new PanelMembresias(membresiaControlador);
        panelPrincipalTarjetas.add(panelMembresias, "PANEL_MEMBRESIAS");
        
        PanelReportes panelReportes = new PanelReportes(reporteControlador);
        panelPrincipalTarjetas.add(panelReportes, "PANEL_REPORTES");

        // Panel de Bienvenida (Opcional)
        JPanel panelBienvenida = new JPanel(new GridBagLayout());
        JLabel lblBienvenida = new JLabel("<html><center><h1>Bienvenido al Sistema de Gestión de Parqueadero</h1><br>" +
                                          "Use el menú 'Módulos' para navegar entre las diferentes secciones.<br><br>" +
                                          "<b>Importante:</b> Comience configurando los datos del parqueadero en 'Administración'.</center></html>");
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        panelBienvenida.add(lblBienvenida);
        panelPrincipalTarjetas.add(panelBienvenida, "PANEL_BIENVENIDA");

        frame.add(panelPrincipalTarjetas, BorderLayout.CENTER);

        // --- Creación de la Barra de Menú para Navegación ---
        JMenuBar menuBar = new JMenuBar();
        JMenu menuNavegacion = new JMenu("Módulos");
        menuNavegacion.setMnemonic('M'); // Alt+M para abrir el menú

        // Items del Menú
        JMenuItem itemBienvenida = new JMenuItem("Bienvenida");
        itemBienvenida.addActionListener(e -> ((CardLayout)panelPrincipalTarjetas.getLayout()).show(panelPrincipalTarjetas, "PANEL_BIENVENIDA"));

        JMenuItem itemAdmin = new JMenuItem("Administración");
        itemAdmin.addActionListener(e -> ((CardLayout)panelPrincipalTarjetas.getLayout()).show(panelPrincipalTarjetas, "PANEL_ADMIN"));
        
        JMenuItem itemClientes = new JMenuItem("Clientes"); 
        itemClientes.addActionListener(e -> ((CardLayout)panelPrincipalTarjetas.getLayout()).show(panelPrincipalTarjetas, "PANEL_CLIENTES"));
        
        JMenuItem itemVehiculos = new JMenuItem("Vehículos"); 
        itemVehiculos.addActionListener(e -> ((CardLayout)panelPrincipalTarjetas.getLayout()).show(panelPrincipalTarjetas, "PANEL_VEHICULOS"));
        
        JMenuItem itemOperaciones = new JMenuItem("Operaciones Parqueadero"); 
        itemOperaciones.addActionListener(e -> ((CardLayout)panelPrincipalTarjetas.getLayout()).show(panelPrincipalTarjetas, "PANEL_OPERACIONES"));
        
        JMenuItem itemMembresias = new JMenuItem("Membresías"); 
        itemMembresias.addActionListener(e -> ((CardLayout)panelPrincipalTarjetas.getLayout()).show(panelPrincipalTarjetas, "PANEL_MEMBRESIAS"));
        
        JMenuItem itemReportes = new JMenuItem("Reportes"); 
        itemReportes.addActionListener(e -> ((CardLayout)panelPrincipalTarjetas.getLayout()).show(panelPrincipalTarjetas, "PANEL_REPORTES"));
        
        // Añadir items al menú de navegación
        menuNavegacion.add(itemBienvenida);
        menuNavegacion.addSeparator(); // Separador visual
        menuNavegacion.add(itemAdmin);
        menuNavegacion.add(itemClientes); 
        menuNavegacion.add(itemVehiculos); 
        menuNavegacion.addSeparator();
        menuNavegacion.add(itemOperaciones);
        menuNavegacion.add(itemMembresias); 
        menuNavegacion.addSeparator();
        menuNavegacion.add(itemReportes); 
        
        // Añadir menú a la barra de menú y la barra al frame
        menuBar.add(menuNavegacion);
        frame.setJMenuBar(menuBar);

        // --- Mostrar Panel Inicial y Hacer Visible la Ventana ---
        ((CardLayout)panelPrincipalTarjetas.getLayout()).show(panelPrincipalTarjetas, "PANEL_BIENVENIDA");
        frame.setVisible(true);
        
        // Mensaje de bienvenida y recordatorio de configuración inicial
        JOptionPane.showMessageDialog(frame, 
            "¡Bienvenido al Sistema de Gestión de Parqueadero!\n\n" +
            "Por favor, antes de realizar otras operaciones, diríjase al módulo 'Administración'\n" +
            "para configurar los datos del parqueadero, las capacidades y las tarifas.\n\n" +
            "¡Gracias!",
            "Configuración Inicial Requerida", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}