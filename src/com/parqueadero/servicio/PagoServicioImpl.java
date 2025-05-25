package com.parqueadero.servicio;

import com.parqueadero.modelo.*; // Importa todos los modelos necesarios

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PagoServicioImpl implements PagoServicio {

    private final Parqueadero parqueadero; 
    private final VehiculoServicio vehiculoServicio;
    private final List<RegistroEstacionamiento> registrosActivos; // Vehículos actualmente DENTRO
    private final List<RegistroEstacionamiento> historialRegistros; // Historial de vehículos que YA SALIERON
    private final List<TransaccionFinanciera> transaccionesFinancieras;

    public PagoServicioImpl(Parqueadero parqueadero, VehiculoServicio vehiculoServicio) {
        this.parqueadero = parqueadero;
        this.vehiculoServicio = vehiculoServicio;
        this.registrosActivos = new ArrayList<>();
        this.historialRegistros = new ArrayList<>();
        this.transaccionesFinancieras = new ArrayList<>();
    }

    @Override
    public Optional<RegistroEstacionamiento> registrarIngresoVehiculo(String placa) {
        Optional<Vehiculo> vehiculoOpt = vehiculoServicio.buscarVehiculoPorPlaca(placa);
        boolean esMiembroConMembresiaActiva = false;
        TipoVehiculo tipoVehiculo;

        if (vehiculoOpt.isPresent()) {
            tipoVehiculo = vehiculoOpt.get().getTipo();
            if (vehiculoOpt.get().tieneMembresiaActiva()) {
                esMiembroConMembresiaActiva = true;
                // Se asume que si tiene membresía activa, tiene un puesto asignado.
            } else {
                // Si es un vehículo registrado pero SIN membresía activa, se trata como temporal
                if (!parqueadero.hayEspacio(tipoVehiculo)) {  
                    System.err.println("Error: No hay espacio disponible para " + tipoVehiculo.getDescripcion());
                    return Optional.empty();
                }
            }
        } else {
            // Vehículo no registrado (totalmente temporal) 
            // Necesitamos preguntar el tipo o tener una forma de asignarlo.
            // Por simplicidad, si no está registrado, no podemos determinar tarifa ni tipo fácilmente.
            // El proyecto indica "Los vehículos que solo ingresan por horas no se deben registrar, solo hacer su ingreso y salida." 
            // Esto implica que para un vehículo no registrado, debemos poder ingresarlo.
            // Pero ¿cómo sabemos su tipo para la tarifa y el espacio?
            // Asumamos por ahora que un vehículo DEBE estar registrado para ingresar,
            // o que la interfaz de usuario pedirá el tipo para vehículos no registrados.
            // Para simplificar la lógica aquí, requeriremos que el vehículo exista en el sistema.
            // O, si permitimos temporales no registrados, necesitaríamos pasar el TipoVehiculo como parámetro.
             System.err.println("Error: Vehículo con placa " + placa + " no encontrado en el sistema. Registre el vehículo primero o ingrese tipo.");
             return Optional.empty(); // MODIFICACIÓN: Simplificando. Si se quiere manejar temporales sin registro previo, se necesita otra lógica.
                                    // O podemos permitirlo y asumir un tipo por defecto o pedirlo.
                                    // Vamos a asumir que para la lógica de este servicio, el vehículo debe ser conocido.
        }

        // Verificar si el vehículo ya está dentro
        for (RegistroEstacionamiento reg : registrosActivos) {
            if (reg.getPlacaVehiculo().equalsIgnoreCase(placa)) {
                System.err.println("Error: El vehículo " + placa + " ya se encuentra dentro del parqueadero.");
                return Optional.empty();
            }
        }
        
        LocalDateTime fechaHoraIngreso = LocalDateTime.now();
        RegistroEstacionamiento nuevoRegistro = new RegistroEstacionamiento(placa, tipoVehiculo, fechaHoraIngreso, esMiembroConMembresiaActiva);

        if (!esMiembroConMembresiaActiva) {
            if (parqueadero.ocuparPuestoTemporal(tipoVehiculo)) { 
                registrosActivos.add(nuevoRegistro);
                System.out.println("Ingreso registrado para vehículo temporal: " + placa);
                return Optional.of(nuevoRegistro);
            } else {
                System.err.println("Error: No se pudo ocupar el puesto temporal para " + placa);
                return Optional.empty();
            }
        } else {
            // Para vehículos con membresía, solo registramos su entrada para control,
            // el puesto fijo ya está "contabilizado" en el Parqueadero.
            registrosActivos.add(nuevoRegistro);
            System.out.println("Ingreso registrado para vehículo con membresía: " + placa);
            return Optional.of(nuevoRegistro);
        }
    }

    @Override
    public Optional<RegistroEstacionamiento> registrarSalidaVehiculo(String placa) {
        Optional<RegistroEstacionamiento> registroOpt = Optional.empty();
        for (RegistroEstacionamiento reg : registrosActivos) {
            if (reg.getPlacaVehiculo().equalsIgnoreCase(placa)) {
                registroOpt = Optional.of(reg);
                break;
            }
        }

        if (!registroOpt.isPresent()) {
            System.err.println("Error: Vehículo con placa " + placa + " no encontrado dentro del parqueadero.");
            return Optional.empty();
        }

        RegistroEstacionamiento registroASalir = registroOpt.get();
        registroASalir.setFechaHoraSalida(LocalDateTime.now());

        if (!registroASalir.fueConMembresia()) {
            // Calcular monto para vehículos temporales
            Duration duracion = Duration.between(registroASalir.getFechaHoraIngreso(), registroASalir.getFechaHoraSalida());
            long horas = duracion.toHours();
            if (duracion.toMinutesPart() > 0 || duracion.toSecondsPart() > 0) { // Si hay minutos/segundos extras, se cobra la hora completa siguiente
                horas++;
            }
            if (horas == 0) horas = 1; // Mínimo 1 hora

            double tarifaPorHora = parqueadero.obtenerTarifaPorHora(registroASalir.getTipoVehiculoAlIngreso());
            double monto = horas * tarifaPorHora; 
            registroASalir.setMontoPagado(monto);
            parqueadero.liberarPuestoTemporal(registroASalir.getTipoVehiculoAlIngreso());
            System.out.println("Salida registrada para vehículo temporal: " + placa + ". Monto: " + monto);

            // Registar transaccion financiero por ingreso temporal
            if (monto > 0) {
                TransaccionFinanciera transaccion = new TransaccionFinanciera(
                    registroASalir.getFechaHoraSalida(),
                    "Pago estacionamiento temporal vehículo " + placa,
                    monto,
                    TransaccionFinanciera.TipoTransaccion.INGRESO_TEMPORAL,
                    placa
                );
                this.transaccionesFinancieras.add(transaccion);
            }
        } else {
            System.out.println("Salida registrada para vehículo con membresía: " + placa);
        }

        registrosActivos.remove(registroASalir);
        historialRegistros.add(registroASalir);

        return Optional.of(registroASalir);
    }

    @Override
    public double calcularMontoAPagarActual(String placa) { // 
        for (RegistroEstacionamiento reg : registrosActivos) {
            if (reg.getPlacaVehiculo().equalsIgnoreCase(placa) && !reg.fueConMembresia()) {
                Duration duracion = Duration.between(reg.getFechaHoraIngreso(), LocalDateTime.now());
                long horas = duracion.toHours();
                 if (duracion.toMinutesPart() > 0 || duracion.toSecondsPart() > 0) {
                    horas++;
                }
                if (horas == 0) horas = 1;

                double tarifaPorHora = parqueadero.obtenerTarifaPorHora(reg.getTipoVehiculoAlIngreso());
                return horas * tarifaPorHora;
            }
        }
        return -1.0; // Vehículo no encontrado, o es de membresía, o ya salió.
    }

    @Override
    public Optional<Membresia> registrarPagoMembresia(String cedulaCliente, String placaVehiculo, Membresia.TipoPeriodoMembresia tipoPeriodo) {
        Optional<Vehiculo> vehiculoOpt = vehiculoServicio.buscarVehiculoPorPlaca(placaVehiculo);
        if (!vehiculoOpt.isPresent()) {
            System.err.println("Error: Vehículo con placa " + placaVehiculo + " no encontrado.");
            return Optional.empty();
        }
        Vehiculo vehiculo = vehiculoOpt.get();
        if (vehiculo.getPropietario() == null || !vehiculo.getPropietario().getCedula().equals(cedulaCliente)) {
             System.err.println("Error: El vehículo " + placaVehiculo + " no pertenece al cliente " + cedulaCliente);
            return Optional.empty();
        }

        double precioMembresia = parqueadero.obtenerTarifaMembresia(tipoPeriodo, vehiculo.getTipo());
        if (precioMembresia <= 0) { // Asumiendo que 0 no es una tarifa válida
            System.err.println("Error: Tarifa para membresía " + tipoPeriodo.getDescripcion() +
                               " para " + vehiculo.getTipo().getDescripcion() + " no configurada o es inválida.");
            return Optional.empty();
        }

        // Si el vehículo ya tiene una membresía activa, ¿se permite renovar/extender o se debe esperar a que venza?
        // Por ahora, si ya tiene una activa, se podría impedir una nueva hasta que venza o esté próxima a vencer.
        // O la nueva podría iniciar cuando la actual termine.
        // Simplificación: si tiene una membresía, se reemplaza (o se avisa). Si no tiene puesto fijo aún, se intenta asignar.
        
        boolean teniaMembresiaAntes = vehiculo.tieneMembresiaActiva();
        boolean puestoFijoAsignadoPreviamente = false;
        // Si ya tenía membresía, se asume que ya tenía un puesto fijo asignado
        if (teniaMembresiaAntes) {
             // Si la membresía anterior era para este mismo vehículo, su puesto fijo ya estaba contado.
             // No necesitamos liberar y reasignar si es el mismo vehículo renovando.
             puestoFijoAsignadoPreviamente = true;
        }


        if (!puestoFijoAsignadoPreviamente) {
            if (!parqueadero.asignarPuestoFijoMembresia(vehiculo.getTipo())) { // 
                System.err.println("Error: No hay espacio general disponible para asignar un puesto fijo de membresía para " + vehiculo.getTipo().getDescripcion());
                return Optional.empty();
            }
        }
        // Si llegó aquí, se asignó un puesto fijo (si era necesario) o ya lo tenía.

        Membresia nuevaMembresia = new Membresia(vehiculo, tipoPeriodo, LocalDateTime.now());
        vehiculo.setMembresia(nuevaMembresia);
        // Registrar el pago de la membresía en algún historial de transacciones financieras (futuro)
        System.out.println("Pago de membresía (" + tipoPeriodo.getDescripcion() + 
                           ") registrado para vehículo " + placaVehiculo + ". Precio: " + precioMembresia);
        // Aquí se podría agregar el precioMembresia a un total de ingresos del parqueadero.

        // REGISTRAR TRANSACCIÓN FINANCIERA POR INGRESO DE MEMBRESÍA
        if (precioMembresia > 0) {
             TransaccionFinanciera transaccion = new TransaccionFinanciera(
                LocalDateTime.now(),
                "Pago membresía " + tipoPeriodo.getDescripcion() + " vehículo " + placaVehiculo,
                precioMembresia,
                TransaccionFinanciera.TipoTransaccion.INGRESO_MEMBRESIA,
                placaVehiculo
            );
            this.transaccionesFinancieras.add(transaccion);
        }

        return Optional.of(nuevaMembresia);
    }
    
    private String generarCabeceraFactura() {
        return "------------------------------------------\n" +
               "            FACTURA PARQUEADERO            \n" +
               "------------------------------------------\n" +
               "Parqueadero: " + parqueadero.getNombre() + "\n" + // 
               "Dirección: " + parqueadero.getDireccion() + "\n" + // 
               "Representante: " + parqueadero.getRepresentante() + "\n" + // 
               "Teléfono: " + parqueadero.getTelefonoContacto() + "\n" + // 
               "Fecha Emisión: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" +
               "------------------------------------------\n";
    }

    @Override
    public String generarFacturaIngresoSalida(RegistroEstacionamiento registro) { // 
        if (registro == null || registro.getFechaHoraSalida() == null) {
            return "Error: Registro inválido para generar factura.";
        }
        StringBuilder factura = new StringBuilder(generarCabeceraFactura());
        factura.append("Placa Vehículo: ").append(registro.getPlacaVehiculo()).append("\n"); // 
        factura.append("Tipo Vehículo: ").append(registro.getTipoVehiculoAlIngreso().getDescripcion()).append("\n");
        factura.append("Fecha Ingreso: ").append(registro.getFechaHoraIngreso().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n"); // 
        factura.append("Fecha Salida: ").append(registro.getFechaHoraSalida().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n"); // 
        
        Duration duracion = Duration.between(registro.getFechaHoraIngreso(), registro.getFechaHoraSalida());
        long horasEstadia = duracion.toHours();
        if (duracion.toMinutesPart() > 0 || duracion.toSecondsPart() > 0) horasEstadia++;
        if (horasEstadia == 0) horasEstadia = 1;

        factura.append("Tiempo Total: ").append(horasEstadia).append(" hora(s)\n");
        factura.append("Monto Pagado: $").append(String.format("%.2f", registro.getMontoPagado())).append("\n"); // 
        factura.append("------------------------------------------\n");
        factura.append("           ¡Gracias por su visita!         \n");
        factura.append("------------------------------------------\n");
        return factura.toString();
    }

    @Override
    public String generarFacturaMembresia(Membresia membresia) { // 
         if (membresia == null || membresia.getVehiculoAsociado() == null) {
            return "Error: Datos de membresía inválidos para generar factura.";
        }
        StringBuilder factura = new StringBuilder(generarCabeceraFactura());
        Vehiculo vehiculo = membresia.getVehiculoAsociado();
        factura.append("Cliente: ").append(vehiculo.getPropietario() != null ? vehiculo.getPropietario().getNombre() : "N/A").append("\n");
        factura.append("Cédula Cliente: ").append(vehiculo.getPropietario() != null ? vehiculo.getPropietario().getCedula() : "N/A").append("\n");
        factura.append("Placa Vehículo: ").append(vehiculo.getPlaca()).append("\n"); // 
        factura.append("Tipo Vehículo: ").append(vehiculo.getTipo().getDescripcion()).append("\n");
        factura.append("Tipo Membresía: ").append(membresia.getTipoPeriodo().getDescripcion()).append("\n");
        factura.append("Fecha Inscripción: ").append(membresia.getFechaInicio().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n"); // 
        factura.append("Fecha Terminación: ").append(membresia.getFechaFin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n"); // 
        
        double precioMembresia = parqueadero.obtenerTarifaMembresia(membresia.getTipoPeriodo(), vehiculo.getTipo());
        factura.append("Monto Pagado: $").append(String.format("%.2f", precioMembresia)).append("\n"); // 
        factura.append("------------------------------------------\n");
        factura.append("      ¡Gracias por adquirir su membresía!    \n");
        factura.append("------------------------------------------\n");
        return factura.toString();
    }
    
    @Override
    public List<RegistroEstacionamiento> obtenerHistorialPagosVehiculo(String placa) { // 
        return historialRegistros.stream()
                .filter(r -> r.getPlacaVehiculo().equalsIgnoreCase(placa))
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistroEstacionamiento> obtenerVehiculosActualesEnParqueadero() { // 
        return new ArrayList<>(registrosActivos); // Devuelve una copia
    }

    // NUEVO MÉTODO (no en la interfaz PagoServicio, pero usado por ReporteServicio)
    // O podría estar en la interfaz si se considera parte del contrato de PagoServicio.
    // Por ahora, lo dejamos aquí para que ReporteServicio lo use a través de la instancia.
    public List<TransaccionFinanciera> obtenerTodasLasTransaccionesFinancieras() {
        return new ArrayList<>(this.transaccionesFinancieras); // Devuelve una copia
    }
}