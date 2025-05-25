package com.parqueadero.modelo;

import java.util.ArrayList;
import java.util.List;

public class Parqueadero {
    private String nombre;             
    private String direccion;          
    private String representante;      
    private String telefonoContacto;   

    // Clases internas para manejar las estructuras de datos
    private static class InfoCapacidadPuestos {
        TipoVehiculo tipoVehiculo;
        int cantidad;

        InfoCapacidadPuestos(TipoVehiculo tipo, int cantidad) {
            this.tipoVehiculo = tipo;
            this.cantidad = cantidad;
        }
    }

    private static class InfoTarifaHora {
        TipoVehiculo tipoVehiculo;
        double tarifa;

        InfoTarifaHora(TipoVehiculo tipo, double tarifa) {
            this.tipoVehiculo = tipo;
            this.tarifa = tarifa;
        }
    }

    private static class InfoTarifaMembresia {
        Membresia.TipoPeriodoMembresia tipoPeriodo;
        TipoVehiculo tipoVehiculo;
        double precio;

        InfoTarifaMembresia(Membresia.TipoPeriodoMembresia tipoPeriodo, TipoVehiculo tipoVehiculo, double precio) {
            this.tipoPeriodo = tipoPeriodo;
            this.tipoVehiculo = tipoVehiculo;
            this.precio = precio;
        }
    }

    // Listas para almacenar la información 
    private List<InfoCapacidadPuestos> capacidadesPorTipo;
    private List<InfoCapacidadPuestos> puestosOcupadosConMembresia;
    private List<InfoCapacidadPuestos> puestosOcupadosTemporales;
    private List<InfoTarifaHora> tarifasPorHora;
    private List<InfoTarifaMembresia> configuracionesTarifasMembresia;

    public Parqueadero(String nombre, String direccion, String representante, String telefonoContacto) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.representante = representante;
        this.telefonoContacto = telefonoContacto;
        
        this.capacidadesPorTipo = new ArrayList<>();
        this.puestosOcupadosConMembresia = new ArrayList<>();
        this.puestosOcupadosTemporales = new ArrayList<>();
        this.tarifasPorHora = new ArrayList<>();
        this.configuracionesTarifasMembresia = new ArrayList<>();

        // Inicializa las listas para cada tipo de vehículo con valores por defecto (0)
        for (TipoVehiculo tipo : TipoVehiculo.values()) {
            this.capacidadesPorTipo.add(new InfoCapacidadPuestos(tipo, 0));
            this.puestosOcupadosConMembresia.add(new InfoCapacidadPuestos(tipo, 0));
            this.puestosOcupadosTemporales.add(new InfoCapacidadPuestos(tipo, 0));
            this.tarifasPorHora.add(new InfoTarifaHora(tipo, 0.0));
        }
        // La inicialización de configuracionesTarifasMembresia se hará a medida que se configuren.
    }

    // Getters para la información básica del parqueadero
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getRepresentante() { return representante; }
    public String getTelefonoContacto() { return telefonoContacto; }

    // Setters para la información básica del parqueadero
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setRepresentante(String representante) { this.representante = representante; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    // Métodos para manejar capacidades
    public void definirCapacidad(TipoVehiculo tipo, int capacidad) {
        if (capacidad < 0) {
            System.err.println("Error: La capacidad no puede ser negativa.");
            return;
        }
        
        for (InfoCapacidadPuestos info : capacidadesPorTipo) {
            if (info.tipoVehiculo == tipo) {
                info.cantidad = capacidad;
                return;
            }
        }
    }

    public int obtenerCapacidad(TipoVehiculo tipo) {
        for (InfoCapacidadPuestos info : capacidadesPorTipo) {
            if (info.tipoVehiculo == tipo) {
                return info.cantidad;
            }
        }
        return 0; 
    }

    // Métodos para manejar tarifas por hora 
    public void configurarTarifaPorHora(TipoVehiculo tipo, double tarifa) {
        if (tarifa < 0) {
            System.err.println("Error: La tarifa no puede ser negativa.");
            return;
        }
        for (InfoTarifaHora info : tarifasPorHora) {
            if (info.tipoVehiculo == tipo) {
                info.tarifa = tarifa;
                return;
            }
        }
         // Si no existe, añadirla
        tarifasPorHora.add(new InfoTarifaHora(tipo, tarifa));
    }

    public double obtenerTarifaPorHora(TipoVehiculo tipo) {
        for (InfoTarifaHora info : tarifasPorHora) {
            if (info.tipoVehiculo == tipo) {
                return info.tarifa;
            }
        }
        return 0.0; 
    }

    // Métodos para manejar tarifas de membresía 
    public void configurarTarifaMembresia(Membresia.TipoPeriodoMembresia tipoPeriodo, TipoVehiculo tipoVehiculo, double precio) {
        if (precio < 0) {
            System.err.println("Error: El precio de la membresía no puede ser negativo.");
            return;
        }
        // Busca si ya existe una configuración para esta combinación y actualizarla
        for (InfoTarifaMembresia info : configuracionesTarifasMembresia) {
            if (info.tipoPeriodo == tipoPeriodo && info.tipoVehiculo == tipoVehiculo) {
                info.precio = precio;
                return;
            }
        }
        // Si no existe, añadirla 
        configuracionesTarifasMembresia.add(new InfoTarifaMembresia(tipoPeriodo, tipoVehiculo, precio));
    }

    public double obtenerTarifaMembresia(Membresia.TipoPeriodoMembresia tipoPeriodo, TipoVehiculo tipoVehiculo) {
        for (InfoTarifaMembresia info : configuracionesTarifasMembresia) {
            if (info.tipoPeriodo == tipoPeriodo && info.tipoVehiculo == tipoVehiculo) {
                return info.precio;
            }
        }
        return 0.0;
    }
    
    // Métodos para gestionar puestos ocupados (auxiliares) 
    private InfoCapacidadPuestos obtenerInfoPuestos(List<InfoCapacidadPuestos> listaPuestos, TipoVehiculo tipo) {
        for (InfoCapacidadPuestos info : listaPuestos) {
            if (info.tipoVehiculo == tipo) {
                return info;
            }
        }
        // Dado el caso de que no exista se inicializa una nueva capacidad para el tipo de vehiculo
        InfoCapacidadPuestos nuevaInfo = new InfoCapacidadPuestos(tipo, 0);
        listaPuestos.add(nuevaInfo);
        return nuevaInfo;
    }

    // Métodos para puestos disponibles y control de espacios
    public int getPuestosDisponibles(TipoVehiculo tipo) { 
        int capacidadTotal = obtenerCapacidad(tipo);
        int ocupadosMembresia = obtenerInfoPuestos(puestosOcupadosConMembresia, tipo).cantidad;
        int ocupadosTemporales = obtenerInfoPuestos(puestosOcupadosTemporales, tipo).cantidad;
        return Math.max(0, capacidadTotal - ocupadosMembresia - ocupadosTemporales);
    }

    public boolean hayEspacio(TipoVehiculo tipo) { 
        return getPuestosDisponibles(tipo) > 0;
    }

    public boolean asignarPuestoFijoMembresia(TipoVehiculo tipo) { 
        if (getPuestosDisponibles(tipo) > 0) {
            InfoCapacidadPuestos info = obtenerInfoPuestos(puestosOcupadosConMembresia, tipo);
            info.cantidad++;
            return true;
        }
        return false;
    }

    public void liberarPuestoFijoMembresia(TipoVehiculo tipo) { 
        InfoCapacidadPuestos info = obtenerInfoPuestos(puestosOcupadosConMembresia, tipo);
        info.cantidad = Math.max(0, info.cantidad - 1);
    }

    public boolean ocuparPuestoTemporal(TipoVehiculo tipo) { 
        if (getPuestosDisponibles(tipo) > 0) {
            InfoCapacidadPuestos info = obtenerInfoPuestos(puestosOcupadosTemporales, tipo);
            info.cantidad++;
            return true;
        }
        return false;
    }

    public void liberarPuestoTemporal(TipoVehiculo tipo) { 
        InfoCapacidadPuestos info = obtenerInfoPuestos(puestosOcupadosTemporales, tipo);
        info.cantidad = Math.max(0, info.cantidad - 1);
    }
    
    public List<InfoCapacidadPuestos> getCapacidadesPorTipo() { return capacidadesPorTipo; }
    public List<InfoTarifaHora> getTarifasPorHora() { return tarifasPorHora; }
    public List<InfoTarifaMembresia> getConfiguracionesTarifasMembresia() { return configuracionesTarifasMembresia; }


    @Override
    public String toString() {
        return "Parqueadero {" +
               "nombre='" + nombre + '\'' +
               ", direccion='" + direccion + '\'' +
               '}';
    }
}