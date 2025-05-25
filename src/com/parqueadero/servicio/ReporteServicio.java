package com.parqueadero.servicio;

import com.parqueadero.modelo.Cliente;
import com.parqueadero.modelo.RegistroEstacionamiento;
import com.parqueadero.modelo.Vehiculo;

import java.time.LocalDate;
import java.util.List;

public interface ReporteServicio {

    /**
     * Muestra todos los vehículos asociados a un cliente, incluyendo el estado de su membresía.
     * (Requisito funcional 1.5 y 5.1) 
     * @param cedulaCliente Cédula del cliente.
     * @return Lista de vehículos del cliente.
     */
    List<Vehiculo> obtenerHistorialVehiculosPorCliente(String cedulaCliente);

    /**
     * Calcula el total de ganancias del parqueadero para un día específico.
     * (Requisito funcional 5.2) 
     * @param fecha Fecha para la cual calcular los ingresos.
     * @return El total de ingresos para ese día.
     */
    double calcularTotalIngresosPorDia(LocalDate fecha);

    /**
     * Calcula el total de ganancias del parqueadero para un mes específico de un año.
     * (Requisito funcional 5.2) 
     * @param anio Año.
     * @param mes Mes (1-12).
     * @return El total de ingresos para ese mes.
     */
    double calcularTotalIngresosPorMes(int anio, int mes);

    /**
     * Calcula el total de ganancias del parqueadero en lo transcurrido del año actual hasta la fecha.
     * (Requisito funcional 5.2) 
     * @param anio Año para el cual calcular los ingresos.
     * @return El total de ingresos para ese año.
     */
    double calcularTotalIngresosPorAnio(int anio);

    /**
     * Devuelve la lista de vehículos que están actualmente dentro del parqueadero.
     * (Requisito funcional 5.3) 
     * @return Lista de registros de estacionamiento de vehículos actualmente en el parqueadero.
     */
    List<RegistroEstacionamiento> obtenerListaVehiculosActuales();

    /**
     * Consulta los clientes con membresías activas.
     * (Requisito funcional 5.4) 
     * @return Lista de clientes que tienen al menos un vehículo con membresía activa.
     */
    List<Cliente> obtenerClientesConMembresiasActivas();

    /**
     * Consulta los clientes cuyas membresías están próximas a vencer (ej. en los próximos N días).
     * (Requisito funcional 5.4) 
     * @param diasProximos Número de días para considerar "próximo a vencer".
     * @return Lista de clientes con membresías próximas a vencer.
     */
    List<Cliente> obtenerClientesConMembresiasProximasAVencer(int diasProximos);
}