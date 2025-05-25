package com.parqueadero.controlador;

import com.parqueadero.modelo.Cliente;
import com.parqueadero.modelo.Vehiculo;
import com.parqueadero.servicio.ClienteServicio; // Necesita el servicio de cliente

import java.util.List;
import java.util.Optional;

public class ClienteControlador {

    private ClienteServicio clienteServicio;

    public ClienteControlador(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    public Optional<Cliente> anadirCliente(String nombre, String cedula, String telefono, String correo) {
        return clienteServicio.anadirCliente(nombre, cedula, telefono, correo); 
    }

    public boolean actualizarCliente(String cedulaExistente, String nuevoNombre, String nuevoTelefono, String nuevoCorreo) {
        return clienteServicio.actualizarCliente(cedulaExistente, nuevoNombre, nuevoTelefono, nuevoCorreo);  
    }

    public boolean eliminarCliente(String cedula) {
        return clienteServicio.eliminarCliente(cedula); 
    }

    public Optional<Cliente> buscarClientePorCedula(String cedula) {
        return clienteServicio.buscarClientePorCedula(cedula);  
    }

    public List<Cliente> buscarClientesPorNombre(String nombre) {
        return clienteServicio.buscarClientesPorNombre(nombre);  
    }
    
    public Optional<Cliente> buscarClientePorTelefono(String telefono) {
        return clienteServicio.buscarClientePorTelefono(telefono); 
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteServicio.obtenerTodosLosClientes();
    }

    public List<Vehiculo> verVehiculosDelCliente(String cedulaCliente) {
        return clienteServicio.verVehiculosDelCliente(cedulaCliente);  
    }
}