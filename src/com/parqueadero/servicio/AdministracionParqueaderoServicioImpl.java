package com.parqueadero.servicio;

import com.parqueadero.modelo.Membresia;
import com.parqueadero.modelo.Parqueadero;
import com.parqueadero.modelo.TipoVehiculo;

public class AdministracionParqueaderoServicioImpl implements AdministracionParqueaderoServicio {

    private Parqueadero parqueadero;

    public AdministracionParqueaderoServicioImpl() {
        this.parqueadero = new Parqueadero("Parqueadero Principal", "Avenida Siempre Viva 742", "Admin General", "555-0100");
    }


    @Override
    public void definirDatosParqueadero(String nombre, String direccion, String representante, String telefono) { 
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.parqueadero.setNombre(nombre);
        }
        if (direccion != null && !direccion.trim().isEmpty()) {
            this.parqueadero.setDireccion(direccion);
        }
        if (representante != null && !representante.trim().isEmpty()) {
            this.parqueadero.setRepresentante(representante);
        }
        if (telefono != null && !telefono.trim().isEmpty()) {
            this.parqueadero.setTelefonoContacto(telefono);
        }
        System.out.println("Datos del parqueadero actualizados.");
    }

    @Override
    public Parqueadero obtenerDatosParqueadero() {
        return this.parqueadero;
    }

    @Override
    public void definirPuestosDisponibles(TipoVehiculo tipo, int cantidad) { 
        if (tipo == null) {
            System.err.println("Error: El tipo de vehículo no puede ser nulo.");
            return;
        }
        if (cantidad < 0) {
            System.err.println("Error: La cantidad de puestos no puede ser negativa.");
            return;
        }
        // La llamada al método del objeto parqueadero no cambia su firma
        this.parqueadero.definirCapacidad(tipo, cantidad);
        System.out.println("Capacidad para " + tipo.getDescripcion() + " definida a: " + cantidad + " puestos.");
    }

    @Override
    public void configurarTarifaPorHora(TipoVehiculo tipo, double tarifa) { 
        if (tipo == null) {
            System.err.println("Error: El tipo de vehículo no puede ser nulo.");
            return;
        }
        if (tarifa < 0) {
            System.err.println("Error: La tarifa no puede ser negativa.");
            return;
        }
        // La llamada al método del objeto parqueadero no cambia su firma
        this.parqueadero.configurarTarifaPorHora(tipo, tarifa);
        System.out.println("Tarifa por hora para " + tipo.getDescripcion() + " configurada a: " + tarifa);
    }

    @Override
    public void configurarTarifaMembresia(Membresia.TipoPeriodoMembresia tipoPeriodo, TipoVehiculo tipoVehiculo, double precio) {
        if (tipoPeriodo == null || tipoVehiculo == null) {
            System.err.println("Error: El tipo de periodo o tipo de vehículo no pueden ser nulos.");
            return;
        }
        if (precio < 0) {
            System.err.println("Error: El precio de la membresía no puede ser negativo.");
            return;
        }
        // La llamada al método del objeto parqueadero no cambia su firma
        this.parqueadero.configurarTarifaMembresia(tipoPeriodo, tipoVehiculo, precio);
        System.out.println("Tarifa para membresía " + tipoPeriodo.getDescripcion() +
                           " para " + tipoVehiculo.getDescripcion() + " configurada a: " + precio);
    }
}