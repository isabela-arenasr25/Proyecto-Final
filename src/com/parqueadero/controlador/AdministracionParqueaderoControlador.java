package com.parqueadero.controlador;

import com.parqueadero.modelo.Membresia;
import com.parqueadero.modelo.Parqueadero;
import com.parqueadero.modelo.TipoVehiculo;
import com.parqueadero.servicio.AdministracionParqueaderoServicio;

public class AdministracionParqueaderoControlador {
    private AdministracionParqueaderoServicio adminServicio;

    public AdministracionParqueaderoControlador(AdministracionParqueaderoServicio adminServicio) {
        this.adminServicio = adminServicio;
    }

    public void guardarDatosParqueadero(String nombre, String direccion, String representante, String telefono) {
        adminServicio.definirDatosParqueadero(nombre, direccion, representante, telefono);
    }

    public Parqueadero obtenerDatosActualesParqueadero() {
        return adminServicio.obtenerDatosParqueadero();
    }

    public boolean establecerCapacidad(TipoVehiculo tipo, int cantidad) {
        if (tipo == null || cantidad < 0) {
            return false; // Valida un problema previo
        }
        adminServicio.definirPuestosDisponibles(tipo, cantidad);
        return true;
    }

    public boolean configurarTarifaHora(TipoVehiculo tipo, double tarifa) {
        if (tipo == null || tarifa < 0) {
            return false;
        }
        adminServicio.configurarTarifaPorHora(tipo, tarifa);
        return true;
    }

    public boolean configurarTarifaMembresia(Membresia.TipoPeriodoMembresia tipoPeriodo, TipoVehiculo tipoVehiculo, double precio) {
        if (tipoPeriodo == null || tipoVehiculo == null || precio < 0) {
            return false;
        }
        adminServicio.configurarTarifaMembresia(tipoPeriodo, tipoVehiculo, precio);
        return true;
    }
}