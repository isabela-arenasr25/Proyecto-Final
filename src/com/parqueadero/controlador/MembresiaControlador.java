package com.parqueadero.controlador;

import com.parqueadero.modelo.Cliente;
import com.parqueadero.modelo.Membresia;
import com.parqueadero.modelo.Parqueadero;
import com.parqueadero.modelo.Vehiculo;
import com.parqueadero.servicio.ClienteServicio;
import com.parqueadero.servicio.PagoServicio;
import com.parqueadero.servicio.VehiculoServicio;

import java.util.Optional;

public class MembresiaControlador {

    private PagoServicio pagoServicio;
    private ClienteServicio clienteServicio;
    private VehiculoServicio vehiculoServicio;
    private Parqueadero parqueadero;

    public MembresiaControlador(PagoServicio pagoServicio, ClienteServicio clienteServicio,
                                VehiculoServicio vehiculoServicio, Parqueadero parqueadero) {
        this.pagoServicio = pagoServicio;
        this.clienteServicio = clienteServicio;
        this.vehiculoServicio = vehiculoServicio;
        this.parqueadero = parqueadero;
    }

    /**
     * Calcula el precio de una membresía basado en el tipo de vehículo y periodo.
     * También valida la existencia del cliente y vehículo, y su asociación.
     * @return Objeto PrecioMembresiaInfo con el precio y un mensaje de estado/error.
     */

    public PrecioMembresiaInfo calcularPrecioMembresia(String cedulaCliente, String placaVehiculo, 
                                                       Membresia.TipoPeriodoMembresia tipoPeriodo) {
        if (cedulaCliente.isEmpty() || placaVehiculo.isEmpty() || tipoPeriodo == null) {
            return new PrecioMembresiaInfo(-1, "Cédula, Placa y Periodo son requeridos.");
        }

        Optional<Cliente> clienteOpt = clienteServicio.buscarClientePorCedula(cedulaCliente);
        if (!clienteOpt.isPresent()) {
            return new PrecioMembresiaInfo(-1, "Cliente con cédula " + cedulaCliente + " no encontrado.");
        }

        Optional<Vehiculo> vehiculoOpt = vehiculoServicio.buscarVehiculoPorPlaca(placaVehiculo);
        if (!vehiculoOpt.isPresent()) {
            return new PrecioMembresiaInfo(-1, "Vehículo con placa " + placaVehiculo + " no encontrado.");
        }
        
        Vehiculo vehiculo = vehiculoOpt.get();
        if (vehiculo.getPropietario() == null || !vehiculo.getPropietario().getCedula().equals(cedulaCliente)) {
             return new PrecioMembresiaInfo(-1, "El vehículo " + placaVehiculo + " no pertenece al cliente " + cedulaCliente + ".");
        }

        double precio = parqueadero.obtenerTarifaMembresia(tipoPeriodo, vehiculo.getTipo());
        if (precio <= 0) {
            return new PrecioMembresiaInfo(-1, "Tarifa no configurada para " + vehiculo.getTipo().getDescripcion() + " en periodo " + tipoPeriodo.getDescripcion() + ".");
        }
        return new PrecioMembresiaInfo(precio, String.format("Precio calculado: $%.2f", precio));
    }

    public Optional<Membresia> registrarPagoMembresia(String cedulaCliente, String placaVehiculo, 
                                                     Membresia.TipoPeriodoMembresia tipoPeriodo) {
        // Las validaciones de existencia de cliente, vehículo y tarifa se hacen indirectamente
        // al llamar a calcularPrecioMembresia o se repiten en el servicio de pago.
        // Aquí se asegura de que la tarifa exista antes de proceder.
        PrecioMembresiaInfo precioInfo = calcularPrecioMembresia(cedulaCliente, placaVehiculo, tipoPeriodo);
        if (precioInfo.getPrecio() <= 0) {
            System.err.println("Controlador: " + precioInfo.getMensaje()); 
            return Optional.empty(); 
        }
        
        return pagoServicio.registrarPagoMembresia(cedulaCliente, placaVehiculo, tipoPeriodo);
    }

    public String generarFacturaMembresia(Membresia membresia) {
        return pagoServicio.generarFacturaMembresia(membresia);
    }

    
    // Clase auxiliar para devolver el precio y un mensaje.
     
    public static class PrecioMembresiaInfo {
        private final double precio;
        private final String mensaje;

        public PrecioMembresiaInfo(double precio, String mensaje) {
            this.precio = precio;
            this.mensaje = mensaje;
        }

        public double getPrecio() {
            return precio;
        }

        public String getMensaje() {
            return mensaje;
        }
    }
}