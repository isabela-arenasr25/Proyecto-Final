package com.parqueadero.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nombre; 
    private String cedula; 
    private String telefono; 
    private String correo;
    private List<Vehiculo> vehiculos; 

    // Constructor
    public Cliente(String nombre, String cedula, String telefono, String correo) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.correo = correo;
        this.vehiculos = new ArrayList<>(); 
    }

    // Getters 
    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    // Setters 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            System.err.println("Error: La cédula no puede estar vacía.");
            return;
        }
        this.cedula = cedula;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // Metodo para agregar un vehiculo a un cliente
    public void agregarVehiculo(Vehiculo vehiculo) {
        if (vehiculo != null) {
            this.vehiculos.add(vehiculo);
        }
    }

    // Metodo para retirar un vehiculo de un cliente
    public boolean removerVehiculo(Vehiculo vehiculo) {
        return this.vehiculos.remove(vehiculo);
    }
    
    @Override
    public String toString() {
        return "Cliente {" +
               "nombre='" + nombre + '\'' +
               ", cedula='" + cedula + '\'' +
               ", telefono='" + telefono + '\'' +
               ", correo='" + correo + '\'' +
               ", numeroDeVehiculos=" + vehiculos.size() +
               '}';
    }
}