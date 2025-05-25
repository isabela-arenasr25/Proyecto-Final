package com.parqueadero.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroEstacionamiento {
    private String placaVehiculo;
    private TipoVehiculo tipoVehiculoAlIngreso; 
    private LocalDateTime fechaHoraIngreso;
    private LocalDateTime fechaHoraSalida; // Será null si el vehículo aún está dentro
    private double montoPagado;
    private boolean fueConMembresia; // Para saber si el ingreso estuvo cubierto por membresía

    // Constructor para un nuevo ingreso
    public RegistroEstacionamiento(String placaVehiculo, TipoVehiculo tipoVehiculoAlIngreso, LocalDateTime fechaHoraIngreso, boolean fueConMembresia) {
        this.placaVehiculo = placaVehiculo;
        this.tipoVehiculoAlIngreso = tipoVehiculoAlIngreso;
        this.fechaHoraIngreso = fechaHoraIngreso;
        this.fueConMembresia = fueConMembresia;
        this.fechaHoraSalida = null; // Aún no ha salido
        this.montoPagado = 0.0;
    }

    // Getters
    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public TipoVehiculo getTipoVehiculoAlIngreso() {
        return tipoVehiculoAlIngreso;
    }

    public LocalDateTime getFechaHoraIngreso() {
        return fechaHoraIngreso;
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public boolean fueConMembresia() {
        return fueConMembresia;
    }

    // Setters para cuando el vehículo sale y paga
    public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public void setMontoPagado(double montoPagado) {
        this.montoPagado = montoPagado;
    }

    @Override
    public String toString() {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String salidaStr = (fechaHoraSalida != null) ? fechaHoraSalida.format(formateador) : "Aún en parqueadero";
        return "RegistroEstacionamiento {" +
               "placa='" + placaVehiculo + '\'' +
               ", tipo=" + tipoVehiculoAlIngreso +
               ", ingreso=" + fechaHoraIngreso.format(formateador) +
               ", salida=" + salidaStr +
               ", montoPagado=" + montoPagado +
               ", membresia=" + fueConMembresia +
               '}';
    }
}