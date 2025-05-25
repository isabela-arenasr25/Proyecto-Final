package com.parqueadero.modelo;

public enum TipoVehiculo {
    AUTOMOVIL("Automóvil"), 
    MOTO("Moto"),           
    CAMION("Camión");       

    private final String descripcion;

    TipoVehiculo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}