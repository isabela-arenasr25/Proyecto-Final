package com.parqueadero.controlador;

import com.parqueadero.modelo.Cliente;
import com.parqueadero.modelo.TipoVehiculo;
import com.parqueadero.modelo.Vehiculo;
import com.parqueadero.servicio.ClienteServicio;
import com.parqueadero.servicio.VehiculoServicio;

import java.util.List;
import java.util.Optional;

public class VehiculoControlador {
    private VehiculoServicio vehiculoServicio;
    private ClienteServicio clienteServicio; 

    public VehiculoControlador(VehiculoServicio vehiculoServicio, ClienteServicio clienteServicio) {
        this.vehiculoServicio = vehiculoServicio;
        this.clienteServicio = clienteServicio;
    }

    public Optional<Vehiculo> registrarVehiculo(String cedulaPropietario, String placa, TipoVehiculo tipo, String color, String modelo) {
        // Validación previa: El propietario debe existir
        if (!clienteServicio.buscarClientePorCedula(cedulaPropietario).isPresent()) {
            System.err.println("Controlador: Propietario con cédula " + cedulaPropietario + " no existe. No se puede registrar el vehículo.");
            return Optional.empty(); 
        }
        return vehiculoServicio.registrarVehiculo(cedulaPropietario, placa, tipo, color, modelo); 
    }

    public boolean actualizarVehiculo(String placaExistente, String nuevoColor, String nuevoModelo, String nuevaCedulaPropietario) {
        // Si se intenta cambiar el propietario, validar que el nuevo propietario exista
        if (nuevaCedulaPropietario != null && !nuevaCedulaPropietario.trim().isEmpty()) {
            if (!clienteServicio.buscarClientePorCedula(nuevaCedulaPropietario).isPresent()) {
                System.err.println("Controlador: Nuevo propietario con cédula " + nuevaCedulaPropietario + " no existe. No se cambiará el propietario.");
                return false;
            }
        }
        return vehiculoServicio.actualizarVehiculo(placaExistente, nuevoColor, nuevoModelo, nuevaCedulaPropietario); 
    }

    public boolean eliminarVehiculo(String placa) {
        return vehiculoServicio.eliminarVehiculo(placa);
    }

    public Optional<Vehiculo> buscarVehiculoPorPlaca(String placa) {
        return vehiculoServicio.buscarVehiculoPorPlaca(placa);
    }

    public List<Vehiculo> buscarVehiculosPorTipo(TipoVehiculo tipo) {
        return vehiculoServicio.buscarVehiculosPorTipo(tipo);
    }

    public List<Vehiculo> buscarVehiculosPorPropietario(String cedulaPropietario) {
        return vehiculoServicio.buscarVehiculosPorPropietario(cedulaPropietario);
    }

    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return vehiculoServicio.obtenerTodosLosVehiculos();
    }
    
    public Optional<Cliente> obtenerPropietarioPorCedula(String cedula) {
        return clienteServicio.buscarClientePorCedula(cedula);
    }
}