package com.parqueadero.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransaccionFinanciera {

    public enum TipoTransaccion {
        INGRESO_TEMPORAL,
        INGRESO_MEMBRESIA
    }

    private LocalDateTime fechaHora;
    private String descripcion;
    private double monto;
    private TipoTransaccion tipo;
    private String placaAsociada; 

    public TransaccionFinanciera(LocalDateTime fechaHora, String descripcion, double monto, TipoTransaccion tipo, String placaAsociada) {
        this.fechaHora = fechaHora;
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipo = tipo;
        this.placaAsociada = placaAsociada;
    }

    // Getters
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public String getPlacaAsociada() {
        return placaAsociada;
    }

    @Override
    public String toString() {
        return "TransaccionFinanciera {" +
               "fechaHora=" + fechaHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
               ", descripcion='" + descripcion + '\'' +
               ", monto=" + String.format("%.2f", monto) +
               ", tipo=" + tipo +
               (placaAsociada != null ? ", placa='" + placaAsociada + '\'' : "") +
               '}';
    }
}