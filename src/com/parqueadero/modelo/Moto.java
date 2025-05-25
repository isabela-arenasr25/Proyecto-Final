package com.parqueadero.modelo;

public class Moto extends Vehiculo {
    public Moto(String placa, String color, String modelo, Cliente propietario) {
        super(placa, TipoVehiculo.MOTO, color, modelo, propietario);
    }
}