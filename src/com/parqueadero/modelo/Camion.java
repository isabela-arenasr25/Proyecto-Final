package com.parqueadero.modelo;

public class Camion extends Vehiculo {
    public Camion(String placa, String color, String modelo, Cliente propietario) {
        super(placa, TipoVehiculo.CAMION, color, modelo, propietario);
    }
}