package com.parqueadero.servicio;

import com.parqueadero.modelo.Cliente;
import com.parqueadero.modelo.Vehiculo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClienteServicioImpl implements ClienteServicio {

    private List<Cliente> listaClientes;

    public ClienteServicioImpl() {
        this.listaClientes = new ArrayList<>(); 
    }

    @Override
    public Optional<Cliente> anadirCliente(String nombre, String cedula, String telefono, String correo) { 
        // Validar que la cédula no esté duplicada
        if (buscarClientePorCedula(cedula).isPresent()) {
            System.err.println("Error: Ya existe un cliente con la cédula " + cedula);
            return Optional.empty();
        }
        // Validaciones básicas
        if (nombre == null || nombre.trim().isEmpty() ||
            cedula == null || cedula.trim().isEmpty() ||
            telefono == null || telefono.trim().isEmpty()) {
            System.err.println("Error: Nombre, cédula y teléfono son campos obligatorios.");
            return Optional.empty();
        }

        Cliente nuevoCliente = new Cliente(nombre, cedula, telefono, correo);
        this.listaClientes.add(nuevoCliente);
        return Optional.of(nuevoCliente);
    }

    @Override
    public boolean eliminarCliente(String cedula) { 
        Optional<Cliente> clienteAEliminar = buscarClientePorCedula(cedula);
        if (clienteAEliminar.isPresent()) {
            if (!clienteAEliminar.get().getVehiculos().isEmpty()) {
                System.err.println("Advertencia: El cliente " + cedula + " tiene vehículos registrados. Estos no se eliminan automáticamente.");
            }
            return this.listaClientes.remove(clienteAEliminar.get());
        }
        return false;
    }

    @Override
    public Optional<Cliente> buscarClientePorCedula(String cedula) { 
        return this.listaClientes.stream()
                   .filter(cliente -> cliente.getCedula().equals(cedula))
                   .findFirst();
    }

    @Override
    public List<Cliente> buscarClientesPorNombre(String nombre) { 
        return this.listaClientes.stream()
                   .filter(cliente -> cliente.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                   .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Cliente> buscarClientePorTelefono(String telefono) { 
        return this.listaClientes.stream()
                   .filter(cliente -> cliente.getTelefono().equals(telefono))
                   .findFirst();
    }


    @Override
    public boolean actualizarCliente(String cedula, String nuevoNombre, String nuevoTelefono, String nuevoCorreo) { 
        Optional<Cliente> clienteOptional = buscarClientePorCedula(cedula);
        if (clienteOptional.isPresent()) {
            Cliente clienteAActualizar = clienteOptional.get();
            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                clienteAActualizar.setNombre(nuevoNombre);
            }
            if (nuevoTelefono != null && !nuevoTelefono.trim().isEmpty()) {
                clienteAActualizar.setTelefono(nuevoTelefono);
            }
            if (nuevoCorreo != null) { 
                clienteAActualizar.setCorreo(nuevoCorreo);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(this.listaClientes); // Devuelve una copia para evitar modificaciones externas
    }

    @Override
    public List<Vehiculo> verVehiculosDelCliente(String cedulaCliente) { 
        Optional<Cliente> clienteOptional = buscarClientePorCedula(cedulaCliente);
        if (clienteOptional.isPresent()) {
            return new ArrayList<>(clienteOptional.get().getVehiculos()); // Devuelve una copia
        }
        return new ArrayList<>(); // Cliente no encontrado o sin vehículos
    }
}