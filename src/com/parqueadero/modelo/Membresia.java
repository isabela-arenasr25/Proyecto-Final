package com.parqueadero.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Membresia {
    private Vehiculo vehiculoAsociado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private TipoPeriodoMembresia tipoPeriodo; // e.g., 1 month, 3 months, 1 year 

    public enum TipoPeriodoMembresia {
        MENSUAL(1, "1 Mes"),
        TRIMESTRAL(3, "3 Meses"),
        ANUAL(12, "1 Año");

        private final int meses;
        private final String descripcion;

        TipoPeriodoMembresia(int meses, String descripcion) {
            this.meses = meses;
            this.descripcion = descripcion;
        }

        public int getMeses() {
            return meses;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    // Constructor
    public Membresia(Vehiculo vehiculoAsociado, TipoPeriodoMembresia tipoPeriodo, LocalDateTime fechaInicio) {
        this.vehiculoAsociado = vehiculoAsociado;
        this.tipoPeriodo = tipoPeriodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = calcularFechaFin(fechaInicio, tipoPeriodo);
    }

    private LocalDateTime calcularFechaFin(LocalDateTime inicio, TipoPeriodoMembresia periodo) {
        return inicio.plusMonths(periodo.getMeses());
    }

    // Getters
    public Vehiculo getVehiculoAsociado() {
        return vehiculoAsociado;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public TipoPeriodoMembresia getTipoPeriodo() {
        return tipoPeriodo;
    }

    public boolean estaActiva() {
        return LocalDateTime.now().isBefore(fechaFin) && LocalDateTime.now().isAfter(fechaInicio.minusSeconds(1)); // Active if current date is before end date
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Membresia {" +
               "vehiculoPlaca=" + (vehiculoAsociado != null ? vehiculoAsociado.getPlaca() : "N/A") +
               ", tipo=" + tipoPeriodo.getDescripcion() +
               ", inicio=" + fechaInicio.format(formatter) +
               ", fin=" + fechaFin.format(formatter) +
               ", activa=" + estaActiva() +
               '}';
    }
}