package com.parqueadero.servicio;

import com.parqueadero.modelo.TipoVehiculo;
import com.parqueadero.modelo.Vehiculo;

import java.util.List;
import java.util.Optional;

public interface VehiculoServicio {

    /**
     * Registra un nuevo vehículo y lo asocia a un cliente existente.
     * @param cedulaPropietario Cédula del cliente propietario.
     * @param placa Placa única del vehículo. 
     * @param tipo Tipo de vehículo (Automóvil, Moto, Camión). 
     * @param color Color del vehículo. 
     * @param modelo Modelo del vehículo. 
     * @return El vehículo creado, o Optional.empty() si la placa ya existe, el propietario no se encuentra o los datos son inválidos.
     */
    Optional<Vehiculo> registrarVehiculo(String cedulaPropietario, String placa, TipoVehiculo tipo, String color, String modelo); // 

    /**
     * Busca un vehículo por su placa.
     * @param placa Placa del vehículo a buscar. 
     * @return Un Optional conteniendo el vehículo si se encuentra, o Optional.empty() si no.
     */
    Optional<Vehiculo> buscarVehiculoPorPlaca(String placa); // 

    /**
     * Busca vehículos por tipo.
     * @param tipo Tipo de vehículo a buscar. 
     * @return Una lista de vehículos que coinciden con el tipo.
     */
    List<Vehiculo> buscarVehiculosPorTipo(TipoVehiculo tipo); // 

    /**
     * Busca vehículos pertenecientes a un cliente específico (por cédula del cliente).
     * @param cedulaPropietario Cédula del propietario. 
     * @return Una lista de vehículos del cliente especificado.
     */
    List<Vehiculo> buscarVehiculosPorPropietario(String cedulaPropietario); // 

    /**
     * Actualiza la información de un vehículo existente.
     * La placa se usa para identificar el vehículo y no se puede modificar por este método.
     * @param placa Placa del vehículo a actualizar.
     * @param nuevoColor Nuevo color para el vehículo.
     * @param nuevoModelo Nuevo modelo para el vehículo.
     * @param nuevaCedulaPropietario (Opcional) Cédula del nuevo propietario. Si es null o vacía, no se cambia.
     * @return true si la actualización fue exitosa, false si el vehículo no se encontró.
     */
    boolean actualizarVehiculo(String placa, String nuevoColor, String nuevoModelo, String nuevaCedulaPropietario); // 
    
    /**
     * Elimina un vehículo del sistema usando su placa.
     * Considerar si se debe permitir eliminar un vehículo con membresía activa o que esté dentro del parqueadero.
     * @param placa Placa del vehículo a eliminar.
     * @return true si el vehículo fue eliminado, false en caso contrario.
     */
    boolean eliminarVehiculo(String placa);

    /**
     * Obtiene todos los vehículos registrados en el sistema.
     * @return Una lista de todos los vehículos.
     */
    List<Vehiculo> obtenerTodosLosVehiculos();
}