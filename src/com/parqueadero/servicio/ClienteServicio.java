package com.parqueadero.servicio;

import com.parqueadero.modelo.Cliente;
import com.parqueadero.modelo.Vehiculo;

import java.util.List;
import java.util.Optional;

public interface ClienteServicio {

    /**
     * Registra un nuevo cliente en el sistema.
     * @param nombre Nombre del cliente. 
     * @param cedula Cédula única del cliente. 
     * @param telefono Teléfono de contacto. 
     * @param correo Correo electrónico. 
     * @return El cliente creado, o Optional.empty() si la cédula ya existe o los datos son inválidos.
     */
    Optional<Cliente> anadirCliente(String nombre, String cedula, String telefono, String correo); // 

    /**
     * Elimina un cliente del sistema usando su cédula.
     * @param cedula Cédula del cliente a eliminar. 
     * @return true si el cliente fue eliminado, false en caso contrario (ej. no encontrado).
     */
    boolean eliminarCliente(String cedula); // 

    /**
     * Busca un cliente por su cédula.
     * @param cedula Cédula del cliente a buscar. 
     * @return Un Optional conteniendo al cliente si se encuentra, o Optional.empty() si no.
     */
    Optional<Cliente> buscarClientePorCedula(String cedula); // 

    /**
     * Busca clientes por nombre (puede devolver múltiples coincidencias).
     * @param nombre Nombre o parte del nombre a buscar. 
     * @return Una lista de clientes que coinciden con el nombre.
     */
    List<Cliente> buscarClientesPorNombre(String nombre); // 
    
    /**
     * Busca un cliente por su número de teléfono.
     * @param telefono Número de teléfono del cliente a buscar. 
     * @return Un Optional conteniendo al cliente si se encuentra, o Optional.empty() si no.
     */
    Optional<Cliente> buscarClientePorTelefono(String telefono); // 

    /**
     * Actualiza la información de un cliente existente.
     * La cédula se usa para identificar al cliente y no se puede modificar por este método.
     * @param cedula Cédula del cliente a actualizar.
     * @param nuevoNombre Nuevo nombre para el cliente.
     * @param nuevoTelefono Nuevo teléfono para el cliente.
     * @param nuevoCorreo Nuevo correo para el cliente.
     * @return true si la actualización fue exitosa, false si el cliente no se encontró.
     */
    boolean actualizarCliente(String cedula, String nuevoNombre, String nuevoTelefono, String nuevoCorreo); // 

    /**
     * Obtiene todos los clientes registrados.
     * @return Una lista de todos los clientes.
     */
    List<Cliente> obtenerTodosLosClientes();

    /**
     * Muestra todos los vehículos asociados a un cliente y el estado de su membresía.
     * @param cedulaCliente Cédula del cliente. 
     * @return Una lista de vehículos del cliente, o una lista vacía si no tiene o no se encuentra el cliente.
     */
    List<Vehiculo> verVehiculosDelCliente(String cedulaCliente); // 
}