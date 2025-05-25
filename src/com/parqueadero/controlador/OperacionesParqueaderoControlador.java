package com.parqueadero.controlador;

import com.parqueadero.modelo.RegistroEstacionamiento;
import com.parqueadero.servicio.PagoServicio;

import java.util.List;
import java.util.Optional;

public class OperacionesParqueaderoControlador {

    private PagoServicio pagoServicio;

    public OperacionesParqueaderoControlador(PagoServicio pagoServicio) {
        this.pagoServicio = pagoServicio;
    }

    public Optional<RegistroEstacionamiento> registrarIngresoVehiculo(String placa) {
        // El servicio PagoServicio se encarga de las validaciones de espacio,
        // si el vehículo existe (para miembros), o si ya está dentro.
        return pagoServicio.registrarIngresoVehiculo(placa);
    }

    public Optional<RegistroEstacionamiento> registrarSalidaVehiculo(String placa) {
        // El servicio PagoServicio calcula el monto, libera el puesto, etc.
        return pagoServicio.registrarSalidaVehiculo(placa);
    }

    public String generarFacturaIngresoSalida(RegistroEstacionamiento registro) {
        // Delegar la generación de la factura al servicio de pago.
        return pagoServicio.generarFacturaIngresoSalida(registro);
    }

    public List<RegistroEstacionamiento> obtenerVehiculosActualesEnParqueadero() {
        return pagoServicio.obtenerVehiculosActualesEnParqueadero();
    }
}