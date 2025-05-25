package com.parqueadero.modelo;

public class Automovil extends Vehiculo {
    public Automovil(String placa, String color, String modelo, Cliente propietario) {
        super(placa, TipoVehiculo.AUTOMOVIL, color, modelo, propietario);
    }
}