package com.parqueadero.controlador;

import com.parqueadero.modelo.Cliente;
import com.parqueadero.modelo.RegistroEstacionamiento;
import com.parqueadero.modelo.Vehiculo;
import com.parqueadero.servicio.ReporteServicio;

import java.time.LocalDate;
import java.util.List;

public class ReporteControlador {

    private ReporteServicio reporteServicio;

    public ReporteControlador(ReporteServicio reporteServicio) {
        this.reporteServicio = reporteServicio;
    }

    public List<Vehiculo> obtenerHistorialVehiculosPorCliente(String cedulaCliente) {
        return reporteServicio.obtenerHistorialVehiculosPorCliente(cedulaCliente); // 
    }

    public double calcularTotalIngresosPorDia(LocalDate fecha) {
        return reporteServicio.calcularTotalIngresosPorDia(fecha); // 
    }

    public double calcularTotalIngresosPorMes(int anio, int mes) {
        return reporteServicio.calcularTotalIngresosPorMes(anio, mes); // 
    }

    public double calcularTotalIngresosPorAnio(int anio) {
        return reporteServicio.calcularTotalIngresosPorAnio(anio); // 
    }

    public List<RegistroEstacionamiento> obtenerListaVehiculosActuales() {
        return reporteServicio.obtenerListaVehiculosActuales(); // 
    }

    public List<Cliente> obtenerClientesConMembresiasActivas() {
        return reporteServicio.obtenerClientesConMembresiasActivas(); // 
    }

    public List<Cliente> obtenerClientesConMembresiasProximasAVencer(int diasProximos) {
        return reporteServicio.obtenerClientesConMembresiasProximasAVencer(diasProximos); // 
    }
}