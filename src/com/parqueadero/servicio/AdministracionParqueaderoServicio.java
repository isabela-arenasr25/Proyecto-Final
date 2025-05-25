package com.parqueadero.servicio;

import com.parqueadero.modelo.Parqueadero;
import com.parqueadero.modelo.TipoVehiculo;
import com.parqueadero.modelo.Membresia;

public interface AdministracionParqueaderoServicio {

    /**
     * Define o actualiza los datos generales del parqueadero.
     * @param nombre 
     * @param direccion 
     * @param representante 
     * @param telefono 
     */
    void definirDatosParqueadero(String nombre, String direccion, String representante, String telefono); // 

    /**
     * Obtiene el objeto Parqueadero actual con su configuración.
     * @return El objeto Parqueadero.
     */
    Parqueadero obtenerDatosParqueadero();

    /**
     * Define la cantidad de espacios disponibles para un tipo específico de vehículo.
     * @param tipo Tipo de vehículo (Moto, Automóvil, Camión).
     * @param cantidad Número total de espacios para ese tipo.
     */
    void definirPuestosDisponibles(TipoVehiculo tipo, int cantidad); // 

    /**
     * Configura el precio por hora para un tipo específico de vehículo.
     * @param tipo Tipo de vehículo.
     * @param tarifa Precio por hora.
     */
    void configurarTarifaPorHora(TipoVehiculo tipo, double tarifa); // 

    /**
     * Configura el precio para un tipo de membresía y tipo de vehículo específicos.
     * Los clientes pueden pagar por 1 mes, 3 meses o 1 año máximo. 
     * @param tipoPeriodo El periodo de la membresía (MENSUAL, TRIMESTRAL, ANUAL).
     * @param tipoVehiculo El tipo de vehículo al que aplica la tarifa de membresía.
     * @param precio El costo de la membresía para ese periodo y tipo de vehículo.
     */
    void configurarTarifaMembresia(Membresia.TipoPeriodoMembresia tipoPeriodo, TipoVehiculo tipoVehiculo, double precio);
}