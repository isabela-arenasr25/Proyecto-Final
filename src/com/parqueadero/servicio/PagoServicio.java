package com.parqueadero.servicio;

import com.parqueadero.modelo.Membresia;
import com.parqueadero.modelo.RegistroEstacionamiento; // Importamos la nueva clase

import java.util.List;
import java.util.Optional;

public interface PagoServicio {

    /**
     * Registra el ingreso de un vehículo al parqueadero. 
     * Verifica si hay espacio disponible. 
     * Los vehículos con membresía activa ocupan un espacio permanente que no se libera hasta que venza. 
     * Los vehículos temporales ocupan un espacio hasta su salida. 
     * @param placa La placa del vehículo que ingresa.
     * @return Un Optional con el registro de estacionamiento si el ingreso fue exitoso, 
     * o Optional.empty() si no hay espacio, el vehículo no existe (para miembros) o ya está dentro.
     */
    Optional<RegistroEstacionamiento> registrarIngresoVehiculo(String placa);

    /**
     * Registra la salida de un vehículo del parqueadero.
     * Calcula el monto a pagar si es un vehículo temporal. 
     * Libera el espacio ocupado si es temporal.
     * @param placa La placa del vehículo que sale.
     * @return Un Optional con el registro de estacionamiento actualizado (incluyendo monto si aplica),
     * o Optional.empty() si el vehículo no se encontró dentro o ya había salido.
     */
    Optional<RegistroEstacionamiento> registrarSalidaVehiculo(String placa);

    /**
     * Calcula el monto a pagar para un vehículo que está actualmente en el parqueadero (temporal).
     * Se basa en la tarifa y las horas que lleva. 
     * @param placa Placa del vehículo.
     * @return El monto a pagar, o -1.0 si el vehículo no está o es de membresía.
     */
    double calcularMontoAPagarActual(String placa);

    /**
     * Registra el pago de una membresía para un vehículo.
     * Los clientes pueden pagar por 1 mes, 3 meses o 1 año máximo. 
     * Asigna un puesto fijo si la membresía es nueva y hay espacio.
     * @param cedulaCliente Cédula del cliente propietario.
     * @param placaVehiculo Placa del vehículo para la membresía.
     * @param tipoPeriodo Tipo de periodo de la membresía (MENSUAL, TRIMESTRAL, ANUAL).
     * @return Un Optional con la membresía creada si el pago fue exitoso, 
     * o Optional.empty() si el cliente/vehículo no existe, no hay tarifa configurada, o no hay espacio para puesto fijo.
     */
    Optional<Membresia> registrarPagoMembresia(String cedulaCliente, String placaVehiculo, Membresia.TipoPeriodoMembresia tipoPeriodo);

    /**
     * Genera una representación de la factura para un vehículo temporal que ha salido. 
     * @param registro El registro de estacionamiento completado.
     * @return Un String representando la factura, o un mensaje de error.
     */
    String generarFacturaIngresoSalida(RegistroEstacionamiento registro);

    /**
     * Genera una representación de la factura para una membresía pagada. 
     * @param membresia La membresía que fue pagada/creada.
     * @return Un String representando la factura, o un mensaje de error.
     */
    String generarFacturaMembresia(Membresia membresia);
    
    /**
     * Obtiene el historial de pagos/estacionamientos de un vehículo específico. 
     * @param placa Placa del vehículo.
     * @return Lista de registros de estacionamiento finalizados para ese vehículo.
     */
    List<RegistroEstacionamiento> obtenerHistorialPagosVehiculo(String placa);

    /**
     * Obtiene la lista de vehículos que están actualmente dentro del parqueadero. 
     * @return Lista de registros de estacionamiento activos (sin fecha de salida).
     */
    List<RegistroEstacionamiento> obtenerVehiculosActualesEnParqueadero();
}