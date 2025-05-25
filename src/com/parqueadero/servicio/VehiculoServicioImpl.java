package com.parqueadero.servicio;

import com.parqueadero.modelo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VehiculoServicioImpl implements VehiculoServicio {

    private List<Vehiculo> listaVehiculos; 
    private ClienteServicio clienteServicio;

    public VehiculoServicioImpl(ClienteServicio clienteServicio) {
        this.listaVehiculos = new ArrayList<>(); 
        this.clienteServicio = clienteServicio; 
    }

    @Override
    public Optional<Vehiculo> registrarVehiculo(String cedulaPropietario, String placa, TipoVehiculo tipo, String color, String modelo) { // 
        Optional<Cliente> propietarioOpt = clienteServicio.buscarClientePorCedula(cedulaPropietario);
        if (!propietarioOpt.isPresent()) {
            System.err.println("Error: No se encontró el cliente propietario con cédula " + cedulaPropietario);
            return Optional.empty();
        }

        if (buscarVehiculoPorPlaca(placa).isPresent()) {
            System.err.println("Error: Ya existe un vehículo con la placa " + placa);
            return Optional.empty();
        }

        if (placa == null || placa.trim().isEmpty() || tipo == null) {
            System.err.println("Error: Placa y tipo son campos obligatorios para el vehículo.");
            return Optional.empty();
        }

        Cliente propietario = propietarioOpt.get();
        Vehiculo nuevoVehiculo;

        switch (tipo) { //  El tipo de vehículo se debe manejar con herencia
            case AUTOMOVIL:
                nuevoVehiculo = new Automovil(placa, color, modelo, propietario);
                break;
            case MOTO:
                nuevoVehiculo = new Moto(placa, color, modelo, propietario);
                break;
            case CAMION:
                nuevoVehiculo = new Camion(placa, color, modelo, propietario);
                break;
            default:
                System.err.println("Error: Tipo de vehículo no reconocido.");
                return Optional.empty();
        }

        this.listaVehiculos.add(nuevoVehiculo);
        propietario.agregarVehiculo(nuevoVehiculo); // Se asocia a la lista del cliente
        return Optional.of(nuevoVehiculo);
    }

    @Override
    public Optional<Vehiculo> buscarVehiculoPorPlaca(String placa) { 
        return this.listaVehiculos.stream()
                   .filter(vehiculo -> vehiculo.getPlaca().equalsIgnoreCase(placa))
                   .findFirst();
    }

    @Override
    public List<Vehiculo> buscarVehiculosPorTipo(TipoVehiculo tipo) { 
        return this.listaVehiculos.stream()
                   .filter(vehiculo -> vehiculo.getTipo() == tipo)
                   .collect(Collectors.toList());
    }

    @Override
    public List<Vehiculo> buscarVehiculosPorPropietario(String cedulaPropietario) { 
        Optional<Cliente> propietarioOpt = clienteServicio.buscarClientePorCedula(cedulaPropietario);
        if (propietarioOpt.isPresent()) {
            return this.listaVehiculos.stream()
                       .filter(vehiculo -> vehiculo.getPropietario() != null && vehiculo.getPropietario().getCedula().equals(cedulaPropietario))
                       .collect(Collectors.toList());
        }
        return new ArrayList<>(); // Propietario no encontrado
    }

    @Override
    public boolean actualizarVehiculo(String placa, String nuevoColor, String nuevoModelo, String nuevaCedulaPropietario) { 
        Optional<Vehiculo> vehiculoOpt = buscarVehiculoPorPlaca(placa);
        if (!vehiculoOpt.isPresent()) {
            System.err.println("Error: Vehículo con placa " + placa + " no encontrado.");
            return false;
        }

        Vehiculo vehiculoAActualizar = vehiculoOpt.get();
        if (nuevoColor != null && !nuevoColor.trim().isEmpty()) {
            vehiculoAActualizar.setColor(nuevoColor);
        }
        if (nuevoModelo != null && !nuevoModelo.trim().isEmpty()) {
            vehiculoAActualizar.setModelo(nuevoModelo);
        }

        if (nuevaCedulaPropietario != null && !nuevaCedulaPropietario.trim().isEmpty()) {
            Optional<Cliente> nuevoPropietarioOpt = clienteServicio.buscarClientePorCedula(nuevaCedulaPropietario);
            if (!nuevoPropietarioOpt.isPresent()) {
                System.err.println("Error: Nuevo propietario con cédula " + nuevaCedulaPropietario + " no encontrado. No se cambió el propietario.");
            } else {
                Cliente antiguoPropietario = vehiculoAActualizar.getPropietario();
                if (antiguoPropietario != null) {
                    antiguoPropietario.removerVehiculo(vehiculoAActualizar);
                }
                Cliente nuevoPropietario = nuevoPropietarioOpt.get();
                vehiculoAActualizar.setPropietario(nuevoPropietario);
                nuevoPropietario.agregarVehiculo(vehiculoAActualizar);
            }
        }
        return true;
    }
    
    @Override
    public boolean eliminarVehiculo(String placa) {
        Optional<Vehiculo> vehiculoOpt = buscarVehiculoPorPlaca(placa);
        if (vehiculoOpt.isPresent()) {
            Vehiculo vehiculoAEliminar = vehiculoOpt.get();
            if (vehiculoAEliminar.tieneMembresiaActiva()) {
                 System.err.println("Advertencia: El vehículo " + placa + " tiene una membresía activa.");
                 // Se podría requerir cancelar la membresía primero.
            }
            // Aquí también se debería verificar si está en el parqueadero (lo veremos en ParqueaderoServicio).
            Cliente propietario = vehiculoAEliminar.getPropietario();
            if (propietario != null) {
                propietario.removerVehiculo(vehiculoAEliminar);
            }
            return this.listaVehiculos.remove(vehiculoAEliminar);
        }
        return false;
    }


    @Override
    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return new ArrayList<>(this.listaVehiculos); // Devuelve una copia
    }
}