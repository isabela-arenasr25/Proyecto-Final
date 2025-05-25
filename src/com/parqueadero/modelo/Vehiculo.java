package com.parqueadero.modelo;

import java.time.LocalDateTime; 

public abstract class Vehiculo { 
    private String placa;  
    private TipoVehiculo tipo;  
    private String color; 
    private String modelo; 
    private Cliente propietario; 
    private Membresia membresia; 

    // Constructor
    public Vehiculo(String placa, TipoVehiculo tipo, String color, String modelo, Cliente propietario) {
        this.placa = placa;
        this.tipo = tipo;
        this.color = color;
        this.modelo = modelo;
        this.propietario = propietario; 
        this.membresia = null; 
    }

    // Getters
    public String getPlaca() {
        return placa;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public String getColor() {
        return color;
    }

    public String getModelo() {
        return modelo;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public Membresia getMembresia() {
        return membresia;
    }

    // Setters
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public void setMembresia(Membresia membresia) {
        this.membresia = membresia;
    }


    public boolean tieneMembresiaActiva() {
        if (this.membresia != null && this.membresia.getFechaFin().isAfter(LocalDateTime.now())) {
            return true;
        }
        if (this.membresia != null && !this.membresia.getFechaFin().isAfter(LocalDateTime.now())) {
            this.membresia = null; // O manejar la notificación de expiración
        }
        return false;
    }
    
    @Override
    public String toString() {
        String membresiaInfo = (membresia != null) ? membresia.toString() : "Sin membresía";
        return "Vehiculo {" +
               "placa='" + placa + '\'' +
               ", tipo=" + tipo +
               ", color='" + color + '\'' +
               ", modelo='" + modelo + '\'' +
               ", propietario=" + (propietario != null ? propietario.getNombre() : "N/A") +
               ", " + membresiaInfo +
               '}';
    }
}