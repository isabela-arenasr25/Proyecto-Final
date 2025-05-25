package com.parqueadero.servicio;

import com.parqueadero.modelo.Cliente;
import com.parqueadero.modelo.RegistroEstacionamiento;
import com.parqueadero.modelo.TransaccionFinanciera;
import com.parqueadero.modelo.Vehiculo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class ReporteServicioImpl implements ReporteServicio {

    private final ClienteServicio clienteServicio;
    private final PagoServicio pagoServicio; // Se usa la interfaz
   
    private final List<TransaccionFinanciera> transaccionesFinancierasProvider;


    public ReporteServicioImpl(ClienteServicio clienteServicio, PagoServicio pagoServicio, PagoServicioImpl pagoServicioImplInstance) {
        this.clienteServicio = clienteServicio;
        this.pagoServicio = pagoServicio;
        // Esta es una forma de obtener las transacciones. 
        this.transaccionesFinancierasProvider = pagoServicioImplInstance.obtenerTodasLasTransaccionesFinancieras();
    }


    @Override
    public List<Vehiculo> obtenerHistorialVehiculosPorCliente(String cedulaCliente) {
        return clienteServicio.verVehiculosDelCliente(cedulaCliente);
    }

    @Override
    public double calcularTotalIngresosPorDia(LocalDate fecha) { 
        double total = 0.0;
        for (TransaccionFinanciera transaccion : transaccionesFinancierasProvider) {
            if (transaccion.getFechaHora().toLocalDate().isEqual(fecha)) {
                total += transaccion.getMonto();
            }
        }
        return total;
    }

    @Override
    public double calcularTotalIngresosPorMes(int anio, int mes) { 
        YearMonth anioMes = YearMonth.of(anio, mes);
        double total = 0.0;
        for (TransaccionFinanciera transaccion : transaccionesFinancierasProvider) {
            if (YearMonth.from(transaccion.getFechaHora().toLocalDate()).equals(anioMes)) {
                total += transaccion.getMonto();
            }
        }
        return total;
    }

    @Override
    public double calcularTotalIngresosPorAnio(int anio) { 
        double total = 0.0;
        for (TransaccionFinanciera transaccion : transaccionesFinancierasProvider) {
            if (transaccion.getFechaHora().getYear() == anio) {
                total += transaccion.getMonto();
            }
        }
        return total;
    }

    @Override
    public List<RegistroEstacionamiento> obtenerListaVehiculosActuales() { 
        return pagoServicio.obtenerVehiculosActualesEnParqueadero();
    }

    @Override
    public List<Cliente> obtenerClientesConMembresiasActivas() { 
        List<Cliente> clientesConMembresiaActiva = new ArrayList<>();
        List<Cliente> todosLosClientes = clienteServicio.obtenerTodosLosClientes();

        for (Cliente cliente : todosLosClientes) {
            boolean tieneMembresiaActiva = false;
            for (Vehiculo vehiculo : cliente.getVehiculos()) {
                if (vehiculo.tieneMembresiaActiva()) {
                    tieneMembresiaActiva = true;
                    break; 
                }
            }
            if (tieneMembresiaActiva) {
                if (!clientesConMembresiaActiva.contains(cliente)) { // Evitar duplicados si un cliente tiene varios vehículos con membresía
                    clientesConMembresiaActiva.add(cliente);
                }
            }
        }
        return clientesConMembresiaActiva;
    }

    @Override
    public List<Cliente> obtenerClientesConMembresiasProximasAVencer(int diasProximos) { 
        List<Cliente> clientesConMembresiaProximaAVencer = new ArrayList<>();
        List<Cliente> todosLosClientes = clienteServicio.obtenerTodosLosClientes();
        LocalDateTime hoy = LocalDateTime.now();
        LocalDateTime fechaLimite = hoy.plusDays(diasProximos);

        for (Cliente cliente : todosLosClientes) {
            boolean tieneMembresiaProxima = false;
            for (Vehiculo vehiculo : cliente.getVehiculos()) {
                if (vehiculo.getMembresia() != null && vehiculo.getMembresia().estaActiva()) {
                    LocalDateTime fechaFinMembresia = vehiculo.getMembresia().getFechaFin();
                    if (fechaFinMembresia.isAfter(hoy) && (fechaFinMembresia.isBefore(fechaLimite) || fechaFinMembresia.isEqual(fechaLimite))) {
                        tieneMembresiaProxima = true;
                        break;
                    }
                }
            }
            if (tieneMembresiaProxima) {
                 if (!clientesConMembresiaProximaAVencer.contains(cliente)) {
                    clientesConMembresiaProximaAVencer.add(cliente);
                }
            }
        }
        return clientesConMembresiaProximaAVencer;
    }
}