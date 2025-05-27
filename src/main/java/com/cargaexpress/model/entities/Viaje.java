package com.cargaexpress.model.entities;

import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.exceptions.ViajeException;
import com.cargaexpress.model.enums.EstadoViaje;
import com.cargaexpress.model.pricing.Tarifa;
import com.cargaexpress.model.pricing.TarifaEstandar;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

/**
 * Clase que representa un viaje en el sistema de transporte.
 * Es el núcleo del negocio, conecta clientes, conductores, vehículos y tarifas.
 * Maneja el ciclo completo del viaje desde programación hasta finalización.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class Viaje {
    
    // ==================== ATRIBUTOS ====================
    
    /**
     * Identificador único del viaje
     */
    private String id;
    
    /**
     * Ciudad o dirección de origen
     */
    private String origen;
    
    /**
     * Ciudad o dirección de destino
     */
    private String destino;
    
    /**
     * Fecha y hora programada del viaje
     */
    private LocalDateTime fechaViaje;
    
    /**
     * Distancia del viaje en kilómetros
     */
    private double distancia;
    
    /**
     * Duración estimada del viaje en minutos
     */
    private int duracionEstimada;
    
    /**
     * Estado actual del viaje
     */
    private EstadoViaje estado;
    
    /**
     * Observaciones del viaje
     */
    private String observaciones;
    
    /**
     * Tarifa total calculada para el viaje
     */
    private double tarifaTotal;
    
    /**
     * Cliente asignado al viaje
     */
    private Cliente cliente;
    
    /**
     * Conductor asignado al viaje
     */
    private Conductor conductor;
    
    /**
     * Vehículo asignado al viaje
     */
    private Vehiculo vehiculo;
    
    /**
     * Sistema de tarifa utilizado para el cálculo
     */
    private Tarifa sistemaTarifa;
    
    /**
     * Fecha y hora real de inicio del viaje
     */
    private LocalDateTime fechaInicio;
    
    /**
     * Fecha y hora real de finalización del viaje
     */
    private LocalDateTime fechaFinalizacion;
    
    /**
     * Kilometraje inicial del vehículo
     */
    private double kilometrajeInicial;
    
    /**
     * Kilometraje final del vehículo
     */
    private double kilometrajeFinal;
    
    /**
     * Calificación del servicio (1-5)
     */
    private double calificacion;
    
    /**
     * Comentarios del cliente sobre el viaje
     */
    private String comentariosCliente;
    
    /**
     * Indica si el viaje es urgente
     */
    private boolean esUrgente;
    
    /**
     * Indica si el viaje es en horario nocturno
     */
    private boolean esNocturno;
    
    /**
     * Costo adicional por servicios especiales
     */
    private double costoAdicional;
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor por defecto.
     * Inicializa un viaje con valores predeterminados.
     */
    public Viaje() {
        this.id = generarIdUnico();
        this.origen = "";
        this.destino = "";
        this.fechaViaje = LocalDateTime.now().plusHours(1);
        this.distancia = 0.0;
        this.duracionEstimada = 0;
        this.estado = EstadoViaje.PROGRAMADO;
        this.observaciones = "";
        this.tarifaTotal = 0.0;
        this.cliente = null;
        this.conductor = null;
        this.vehiculo = null;
        this.sistemaTarifa = new TarifaEstandar();
        this.fechaInicio = null;
        this.fechaFinalizacion = null;
        this.kilometrajeInicial = 0.0;
        this.kilometrajeFinal = 0.0;
        this.calificacion = 0.0;
        this.comentariosCliente = "";
        this.esUrgente = false;
        this.esNocturno = false;
        this.costoAdicional = 0.0;
    }
    
    /**
     * Constructor básico para crear un viaje.
     * 
     * @param origen ciudad o dirección de origen
     * @param destino ciudad o dirección de destino
     * @param fechaViaje fecha y hora del viaje
     * @param distancia distancia en kilómetros
     * @param cliente cliente del viaje
     * @throws ValidationException si los datos son inválidos
     */
    public Viaje(String origen, String destino, LocalDateTime fechaViaje, 
                 double distancia, Cliente cliente) throws ValidationException {
        this();
        setOrigen(origen);
        setDestino(destino);
        setFechaViaje(fechaViaje);
        setDistancia(distancia);
        setCliente(cliente);
        
        // Calcular duración estimada (60 km/h promedio)
        this.duracionEstimada = (int) Math.ceil(distancia / 1.0); // 1 km por minuto aprox
        
        // Determinar si es nocturno
        this.esNocturno = determinarSiEsNocturno(fechaViaje);
    }
    
    /**
     * Constructor completo para viaje con asignaciones.
     * 
     * @param origen origen del viaje
     * @param destino destino del viaje
     * @param fechaViaje fecha y hora del viaje
     * @param distancia distancia en kilómetros
     * @param cliente cliente del viaje
     * @param conductor conductor asignado
     * @param vehiculo vehículo asignado
     * @throws ValidationException si los datos son inválidos
     */
    public Viaje(String origen, String destino, LocalDateTime fechaViaje, 
                 double distancia, Cliente cliente, Conductor conductor, 
                 Vehiculo vehiculo) throws ValidationException {
        this(origen, destino, fechaViaje, distancia, cliente);
        setConductor(conductor);
        setVehiculo(vehiculo);
        
        // Calcular tarifa automáticamente
        calcularTarifa();
    }
    
    // ==================== GETTERS Y SETTERS CON VALIDACIÓN ====================
    
    /**
     * Obtiene el ID del viaje.
     * 
     * @return identificador único del viaje
     */
    public String getId() {
        return id;
    }
    
    /**
     * Establece el ID del viaje.
     * 
     * @param id identificador del viaje
     * @throws ValidationException si el ID es inválido
     */
    public void setId(String id) throws ValidationException {
        if (id == null || id.trim().isEmpty()) {
            throw ValidationException.campoRequerido("ID del viaje");
        }
        
        this.id = id.trim();
    }
    
    /**
     * Obtiene el origen del viaje.
     * 
     * @return origen del viaje
     */
    public String getOrigen() {
        return origen;
    }
    
    /**
     * Establece el origen del viaje.
     * 
     * @param origen origen del viaje
     * @throws ValidationException si el origen es inválido
     */
    public void setOrigen(String origen) throws ValidationException {
        if (origen == null || origen.trim().isEmpty()) {
            throw ValidationException.campoRequerido("origen");
        }
        
        String origenLimpio = origen.trim();
        if (origenLimpio.length() < 3 || origenLimpio.length() > 100) {
            throw ValidationException.longitudInvalida("origen", origenLimpio, 3, 100);
        }
        
        this.origen = origenLimpio;
    }
    
    /**
     * Obtiene el destino del viaje.
     * 
     * @return destino del viaje
     */
    public String getDestino() {
        return destino;
    }
    
    /**
     * Establece el destino del viaje.
     * 
     * @param destino destino del viaje
     * @throws ValidationException si el destino es inválido
     */
    public void setDestino(String destino) throws ValidationException {
        if (destino == null || destino.trim().isEmpty()) {
            throw ValidationException.campoRequerido("destino");
        }
        
        String destinoLimpio = destino.trim();
        if (destinoLimpio.length() < 3 || destinoLimpio.length() > 100) {
            throw ValidationException.longitudInvalida("destino", destinoLimpio, 3, 100);
        }
        
        if (destinoLimpio.equalsIgnoreCase(origen)) {
            throw new ValidationException("destino", destinoLimpio, 
                "El destino no puede ser igual al origen");
        }
        
        this.destino = destinoLimpio;
    }
    
    /**
     * Obtiene la fecha del viaje.
     * 
     * @return fecha y hora del viaje
     */
    public LocalDateTime getFechaViaje() {
        return fechaViaje;
    }
    
    /**
     * Establece la fecha del viaje.
     * 
     * @param fechaViaje fecha y hora del viaje
     * @throws ValidationException si la fecha es inválida
     */
    public void setFechaViaje(LocalDateTime fechaViaje) throws ValidationException {
        if (fechaViaje == null) {
            throw ValidationException.campoRequerido("fecha del viaje");
        }
        
        // Solo permitir fechas futuras para viajes nuevos
        if (estado == EstadoViaje.PROGRAMADO && fechaViaje.isBefore(LocalDateTime.now())) {
            throw new ValidationException("fecha del viaje", fechaViaje.toString(), 
                "No se pueden programar viajes en el pasado");
        }
        
        this.fechaViaje = fechaViaje;
        this.esNocturno = determinarSiEsNocturno(fechaViaje);
    }
    
    /**
     * Obtiene la distancia del viaje.
     * 
     * @return distancia en kilómetros
     */
    public double getDistancia() {
        return distancia;
    }
    
    /**
     * Establece la distancia del viaje.
     * 
     * @param distancia distancia en kilómetros
     * @throws ValidationException si la distancia es inválida
     */
    public void setDistancia(double distancia) throws ValidationException {
        if (distancia <= 0) {
            throw ViajeException.distanciaInvalida(distancia);
        }
        
        if (distancia > 2000) {
            throw new ValidationException("distancia", String.valueOf(distancia), 
                "Excede el límite máximo de 2000 km");
        }
        
        this.distancia = distancia;
        
        // Recalcular duración estimada
        this.duracionEstimada = (int) Math.ceil(distancia / 1.0);
    }
    
    /**
     * Obtiene la duración estimada.
     * 
     * @return duración estimada en minutos
     */
    public int getDuracionEstimada() {
        return duracionEstimada;
    }
    
    /**
     * Establece la duración estimada.
     * 
     * @param duracionEstimada duración en minutos
     * @throws ValidationException si la duración es inválida
     */
    public void setDuracionEstimada(int duracionEstimada) throws ValidationException {
        if (duracionEstimada < 0) {
            throw new ValidationException("duración estimada", String.valueOf(duracionEstimada), 
                "No puede ser negativa");
        }
        
        this.duracionEstimada = duracionEstimada;
    }
    
    /**
     * Obtiene el estado del viaje.
     * 
     * @return estado actual del viaje
     */
    public EstadoViaje getEstado() {
        return estado;
    }
    
    /**
     * Establece el estado del viaje.
     * 
     * @param estado nuevo estado del viaje
     */
    public void setEstado(EstadoViaje estado) {
        this.estado = (estado == null) ? EstadoViaje.PROGRAMADO : estado;
    }
    
    /**
     * Obtiene las observaciones.
     * 
     * @return observaciones del viaje
     */
    public String getObservaciones() {
        return observaciones;
    }
    
    /**
     * Establece las observaciones.
     * 
     * @param observaciones observaciones del viaje
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = (observaciones == null) ? "" : observaciones.trim();
    }
    
    /**
     * Obtiene la tarifa total.
     * 
     * @return tarifa total del viaje
     */
    public double getTarifaTotal() {
        return tarifaTotal;
    }
    
    /**
     * Establece la tarifa total.
     * 
     * @param tarifaTotal tarifa total del viaje
     * @throws ValidationException si la tarifa es inválida
     */
    public void setTarifaTotal(double tarifaTotal) throws ValidationException {
        if (tarifaTotal < 0) {
            throw new ValidationException("tarifa total", String.valueOf(tarifaTotal), 
                "No puede ser negativa");
        }
        
        this.tarifaTotal = tarifaTotal;
    }
    
    /**
     * Obtiene el cliente del viaje.
     * 
     * @return cliente asignado
     */
    public Cliente getCliente() {
        return cliente;
    }
    
    /**
     * Establece el cliente del viaje.
     * 
     * @param cliente cliente a asignar
     * @throws ValidationException si el cliente es inválido
     */
    public void setCliente(Cliente cliente) throws ValidationException {
        if (cliente == null) {
            throw ValidationException.campoRequerido("cliente");
        }
        
        if (!cliente.validarDatosCliente()) {
            throw new ValidationException("cliente", cliente.getCedula(), 
                "El cliente tiene datos incompletos o inválidos");
        }
        
        this.cliente = cliente;
    }
    
    /**
     * Obtiene el conductor del viaje.
     * 
     * @return conductor asignado
     */
    public Conductor getConductor() {
        return conductor;
    }
    
    /**
     * Establece el conductor del viaje.
     * 
     * @param conductor conductor a asignar
     * @throws ValidationException si el conductor es inválido
     */
    public void setConductor(Conductor conductor) throws ValidationException {
        if (conductor != null) {
            if (!conductor.validarDatosConductor()) {
                throw new ValidationException("conductor", conductor.getCedula(), 
                    "El conductor tiene datos incompletos o inválidos");
            }
            
            if (!conductor.isDisponible()) {
                throw new ValidationException("conductor", conductor.getCedula(), 
                    "El conductor no está disponible");
            }
        }
        
        this.conductor = conductor;
    }
    
    /**
     * Obtiene el vehículo del viaje.
     * 
     * @return vehículo asignado
     */
    public Vehiculo getVehiculo() {
        return vehiculo;
    }
    
    /**
     * Establece el vehículo del viaje.
     * 
     * @param vehiculo vehículo a asignar
     * @throws ValidationException si el vehículo es inválido
     */
    public void setVehiculo(Vehiculo vehiculo) throws ValidationException {
        if (vehiculo != null) {
            if (!vehiculo.validarDocumentacion()) {
                throw new ValidationException("vehículo", vehiculo.getPlaca(), 
                    "El vehículo tiene documentación vencida");
            }
            
            if (!vehiculo.isDisponible()) {
                throw new ValidationException("vehículo", vehiculo.getPlaca(), 
                    "El vehículo no está disponible");
            }
        }
        
        this.vehiculo = vehiculo;
    }
    
    /**
     * Obtiene el sistema de tarifa.
     * 
     * @return sistema de tarifa utilizado
     */
    public Tarifa getSistemaTarifa() {
        return sistemaTarifa;
    }
    
    /**
     * Establece el sistema de tarifa.
     * 
     * @param sistemaTarifa sistema de tarifa a utilizar
     */
    public void setSistemaTarifa(Tarifa sistemaTarifa) {
        this.sistemaTarifa = (sistemaTarifa == null) ? new TarifaEstandar() : sistemaTarifa;
    }
    
    // Getters y setters adicionales (fechas, kilometrajes, etc.)
    
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public LocalDateTime getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }
    
    public double getKilometrajeInicial() { return kilometrajeInicial; }
    public void setKilometrajeInicial(double kilometrajeInicial) { this.kilometrajeInicial = kilometrajeInicial; }
    
    public double getKilometrajeFinal() { return kilometrajeFinal; }
    public void setKilometrajeFinal(double kilometrajeFinal) { this.kilometrajeFinal = kilometrajeFinal; }
    
    public double getCalificacion() { return calificacion; }
    public void setCalificacion(double calificacion) throws ValidationException {
        if (calificacion < 0 || calificacion > 5) {
            throw new ValidationException("calificación", String.valueOf(calificacion), 
                "Debe estar entre 0 y 5");
        }
        this.calificacion = calificacion;
    }
    
    public String getComentariosCliente() { return comentariosCliente; }
    public void setComentariosCliente(String comentariosCliente) { 
        this.comentariosCliente = (comentariosCliente == null) ? "" : comentariosCliente.trim(); 
    }
    
    public boolean isEsUrgente() { return esUrgente; }
    public void setEsUrgente(boolean esUrgente) { this.esUrgente = esUrgente; }
    
    public boolean isEsNocturno() { return esNocturno; }
    
    public double getCostoAdicional() { return costoAdicional; }
    public void setCostoAdicional(double costoAdicional) throws ValidationException {
        if (costoAdicional < 0) {
            throw new ValidationException("costo adicional", String.valueOf(costoAdicional), 
                "No puede ser negativo");
        }
        this.costoAdicional = costoAdicional;
    }
    
    // ==================== MÉTODOS DE NEGOCIO ====================
    
    /**
     * Genera un ID único para el viaje.
     * 
     * @return ID único generado
     */
    private String generarIdUnico() {
        return "VJ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Determina si el viaje es en horario nocturno.
     * 
     * @param fechaHora fecha y hora del viaje
     * @return true si es nocturno (22:00 - 06:00)
     */
    private boolean determinarSiEsNocturno(LocalDateTime fechaHora) {
        int hora = fechaHora.getHour();
        return hora >= 22 || hora < 6;
    }
    
    /**
     * Calcula la tarifa total del viaje usando el sistema de tarifa asignado.
     * 
     * @return tarifa calculada
     * @throws ViajeException si no se puede calcular la tarifa
     */
    public double calcularTarifa() throws ViajeException {
        if (sistemaTarifa == null) {
            throw new ViajeException("Sistema de tarifa no configurado");
        }
        
        if (vehiculo == null) {
            throw ViajeException.recursoNoDisponible("vehículo");
        }
        
        try {
            double tarifaBase = sistemaTarifa.calcular(distancia, cliente, vehiculo);
            
            // Aplicar costo adicional
            tarifaBase += costoAdicional;
            
            // Aplicar recargos por condiciones especiales
            if (esUrgente) {
                tarifaBase *= 1.25; // 25% adicional por urgencia
            }
            
            if (esNocturno) {
                tarifaBase *= 1.20; // 20% adicional por horario nocturno
            }
            
            this.tarifaTotal = Math.round(tarifaBase / 100.0) * 100.0; // Redondear a centenas
            return tarifaTotal;
            
        } catch (Exception e) {
            throw new ViajeException("Error al calcular tarifa: " + e.getMessage(), e);
        }
    }
    
    /**
     * Inicia el viaje cambiando el estado y registrando la hora de inicio.
     * 
     * @throws ViajeException si el viaje no puede iniciarse
     */
    public void iniciarViaje() throws ViajeException {
        if (estado != EstadoViaje.PROGRAMADO && estado != EstadoViaje.CONFIRMADO) {
            throw new ViajeException("El viaje debe estar programado o confirmado para iniciarse");
        }
        
        if (conductor == null || vehiculo == null) {
            throw ViajeException.recursoNoDisponible("conductor o vehículo");
        }
        
        // Registrar inicio
        this.fechaInicio = LocalDateTime.now();
        this.estado = EstadoViaje.EN_CURSO;
        
        // Marcar recursos como ocupados
        conductor.setDisponible(false);
        vehiculo.setDisponible(false);
        
        // Registrar kilometraje inicial
        if (vehiculo != null) {
            this.kilometrajeInicial = vehiculo.getKilometraje();
        }
        
        // Incrementar contador de viajes del cliente
        if (cliente != null) {
            cliente.incrementarViajes();
        }
    }
    
    /**
     * Finaliza el viaje registrando la hora de finalización y liberando recursos.
     * 
     * @throws ViajeException si el viaje no puede finalizarse
     */
    public void finalizarViaje() throws ViajeException {
        if (estado != EstadoViaje.EN_CURSO) {
            throw new ViajeException("Solo se pueden finalizar viajes en curso");
        }
        
        // Registrar finalización
        this.fechaFinalizacion = LocalDateTime.now();
        this.estado = EstadoViaje.COMPLETADO;
        
        // Actualizar kilometraje del vehículo
        if (vehiculo != null) {
            this.kilometrajeFinal = vehiculo.getKilometraje() + distancia;
            try {
                vehiculo.actualizarKilometraje(distancia);
            } catch (ValidationException e) {
                // Log error pero no fallar el viaje
                System.err.println("Error actualizando kilometraje: " + e.getMessage());
            }
        }
        
        // Liberar recursos
        if (conductor != null) {
            conductor.liberarConductor(); // Esto también incrementa sus viajes realizados
        }
        
        if (vehiculo != null) {
            vehiculo.setDisponible(true);
        }
    }
    
    /**
     * Cancela el viaje.
     * 
     * @param motivo motivo de la cancelación
     * @throws ViajeException si el viaje no puede cancelarse
     */
    public void cancelarViaje(String motivo) throws ViajeException {
        if (estado == EstadoViaje.COMPLETADO) {
            throw new ViajeException("No se puede cancelar un viaje completado");
        }
        
        if (estado == EstadoViaje.CANCELADO) {
            throw new ViajeException("El viaje ya está cancelado");
        }
        
        // Liberar recursos si estaban asignados
        if (conductor != null && !conductor.isDisponible()) {
            conductor.setDisponible(true);
        }
        
        if (vehiculo != null && !vehiculo.isDisponible()) {
            vehiculo.setDisponible(true);
        }
        
        this.estado = EstadoViaje.CANCELADO;
        this.observaciones += (observaciones.isEmpty() ? "" : "\n") + 
                             "CANCELADO: " + (motivo == null ? "Sin motivo especificado" : motivo);
    }
    
    /**
     * Agrega una observación al viaje.
     * 
     * @param observacion observación a agregar
     */
    public void agregarObservacion(String observacion) {
        if (observacion != null && !observacion.trim().isEmpty()) {
            String nuevaObservacion = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ": " + observacion.trim();
            
            if (observaciones.isEmpty()) {
                observaciones = nuevaObservacion;
            } else {
                observaciones = observaciones + "\n" + nuevaObservacion;
            }
        }
    }
    
    /**
     * Calcula la duración real del viaje.
     * 
     * @return duración en minutos, -1 si no está finalizado
     */
    public long calcularDuracionReal() {
        if (fechaInicio == null) {
            return -1;
        }
        
        LocalDateTime fechaFin = (fechaFinalizacion != null) ? fechaFinalizacion : LocalDateTime.now();
        return ChronoUnit.MINUTES.between(fechaInicio, fechaFin);
    }
    
    /**
     * Verifica si el viaje está retrasado.
     * 
     * @return true si el viaje está retrasado
     */
    public boolean estaRetrasado() {
        if (estado == EstadoViaje.COMPLETADO || estado == EstadoViaje.CANCELADO) {
            return false;
        }
        
        return LocalDateTime.now().isAfter(fechaViaje.plusMinutes(15)); // 15 min de tolerancia
    }
    
    /**
     * Obtiene el tiempo restante hasta el viaje.
     * 
     * @return minutos hasta el viaje (negativo si ya pasó)
     */
    public long tiempoHastaViaje() {
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), fechaViaje);
    }
    
    /**
     * Valida que el viaje esté completo y listo para ejecutarse.
     * 
     * @return true si el viaje está listo
     * @throws ViajeException si faltan datos
     */
    public boolean validarViajeCompleto() throws ViajeException {
        if (cliente == null) {
            throw ViajeException.recursoNoDisponible("cliente");
        }
        
        if (conductor == null) {
            throw ViajeException.recursoNoDisponible("conductor");
        }
        
        if (vehiculo == null) {
            throw ViajeException.recursoNoDisponible("vehículo");
        }
        
        if (origen.isEmpty() || destino.isEmpty()) {
            throw new ViajeException("Origen y destino son requeridos");
        }
        
        if (distancia <= 0) {
            throw ViajeException.distanciaInvalida(distancia);
        }
        
        return true;
    }
    
    /**
     * Obtiene detalles del cálculo de la tarifa.
     * 
     * @return detalles del cálculo
     */
    public String obtenerDetallesTarifa() {
        if (sistemaTarifa == null) {
            return "Sistema de tarifa no configurado";
        }
        
        return sistemaTarifa.obtenerDetallesCalculo(distancia, cliente, vehiculo);
    }
    
    // ==================== MÉTODOS DE PRESENTACIÓN ====================
    
    /**
     * Obtiene información resumida del viaje.
     * 
     * @return información resumida
     */
    public String getInformacionResumida() {
        return String.format("%s: %s → %s | %.1f km | %s | %s", 
                           id, origen, destino, distancia, 
                           estado.getNombre(), 
                           (tarifaTotal > 0) ? String.format("$%.0f", tarifaTotal) : "Por calcular");
    }
    
    /**
     * Muestra información completa del viaje.
     * 
     * @return información detallada del viaje
     */
    public String mostrarInformacion() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder info = new StringBuilder();
        
        info.append("=== INFORMACIÓN DEL VIAJE ===\n");
        info.append("ID: ").append(id).append("\n");
        info.append("Origen: ").append(origen).append("\n");
        info.append("Destino: ").append(destino).append("\n");
        info.append("Fecha programada: ").append(fechaViaje.format(formatter)).append("\n");
        info.append("Distancia: ").append(String.format("%.1f", distancia)).append(" km\n");
        info.append("Duración estimada: ").append(duracionEstimada).append(" minutos\n");
        info.append("Estado: ").append(estado.getNombre()).append("\n");
        
        // Información de tarifa
        info.append("\n--- INFORMACIÓN ECONÓMICA ---\n");
        info.append("Tarifa total: ").append(String.format("$%.0f", tarifaTotal)).append("\n");
        if (costoAdicional > 0) {
            info.append("Costo adicional: ").append(String.format("$%.0f", costoAdicional)).append("\n");
        }
        info.append("Sistema de tarifa: ").append(sistemaTarifa.getNombreTarifa()).append("\n");
        
        // Información de asignaciones
        info.append("\n--- ASIGNACIONES ---\n");
        if (cliente != null) {
            info.append("Cliente: ").append(cliente.getNombreCompleto())
                 .append(" (").append(cliente.getCedula()).append(")\n");
        }
        if (conductor != null) {
            info.append("Conductor: ").append(conductor.getNombreCompleto())
                 .append(" (").append(conductor.getCedula()).append(")\n");
        }
        if (vehiculo != null) {
            info.append("Vehículo: ").append(vehiculo.getMarca()).append(" ")
                 .append(vehiculo.getModelo()).append(" (").append(vehiculo.getPlaca()).append(")\n");
        }
        
        // Información de ejecución
        if (fechaInicio != null) {
            info.append("\n--- EJECUCIÓN ---\n");
            info.append("Fecha inicio: ").append(fechaInicio.format(formatter)).append("\n");
            
            if (fechaFinalizacion != null) {
                info.append("Fecha finalización: ").append(fechaFinalizacion.format(formatter)).append("\n");
                info.append("Duración real: ").append(calcularDuracionReal()).append(" minutos\n");
            }
            
            if (kilometrajeInicial > 0) {
                info.append("Kilometraje inicial: ").append(String.format("%.0f", kilometrajeInicial)).append(" km\n");
            }
            if (kilometrajeFinal > 0) {
                info.append("Kilometraje final: ").append(String.format("%.0f", kilometrajeFinal)).append(" km\n");
            }
        }
        
        // Características especiales
        if (esUrgente || esNocturno) {
            info.append("\n--- CARACTERÍSTICAS ESPECIALES ---\n");
            if (esUrgente) info.append("✓ Viaje urgente\n");
            if (esNocturno) info.append("✓ Horario nocturno\n");
        }
        
        // Calificación y comentarios
        if (calificacion > 0) {
            info.append("\n--- EVALUACIÓN ---\n");
            info.append("Calificación: ").append(String.format("%.1f", calificacion)).append("/5.0\n");
            if (!comentariosCliente.isEmpty()) {
                info.append("Comentarios: ").append(comentariosCliente).append("\n");
            }
        }
        
        // Observaciones
        if (!observaciones.isEmpty()) {
            info.append("\n--- OBSERVACIONES ---\n");
            info.append(observaciones).append("\n");
        }
        
        // Estado y alertas
        info.append("\n--- ESTADO ACTUAL ---\n");
        if (estaRetrasado()) {
            info.append("⚠️ VIAJE RETRASADO\n");
        }
        
        long tiempoRestante = tiempoHastaViaje();
        if (tiempoRestante > 0 && estado == EstadoViaje.PROGRAMADO) {
            info.append("⏰ Tiempo hasta viaje: ").append(tiempoRestante).append(" minutos\n");
        }
        
        return info.toString();
    }
    
    /**
     * Genera un reporte del viaje para facturación.
     * 
     * @return reporte de facturación
     */
    public String generarReporteFacturacion() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder reporte = new StringBuilder();
        
        reporte.append("=== REPORTE DE FACTURACIÓN ===\n");
        reporte.append("Viaje ID: ").append(id).append("\n");
        reporte.append("Fecha: ").append(fechaViaje.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        
        if (cliente != null) {
            reporte.append("Cliente: ").append(cliente.getNombreCompleto()).append("\n");
            reporte.append("Cédula/NIT: ").append(cliente.getCedula()).append("\n");
            reporte.append("Tipo cliente: ").append(cliente.getTipoCliente().getDescripcion()).append("\n");
        }
        
        reporte.append("Ruta: ").append(origen).append(" → ").append(destino).append("\n");
        reporte.append("Distancia: ").append(String.format("%.1f", distancia)).append(" km\n");
        
        if (vehiculo != null) {
            reporte.append("Vehículo: ").append(vehiculo.getTipoVehiculo().getNombre()).append("\n");
        }
        
        reporte.append("\n--- DESGLOSE DE COSTOS ---\n");
        reporte.append(obtenerDetallesTarifa()).append("\n");
        
        if (esUrgente) {
            reporte.append("Recargo urgente: 25%\n");
        }
        if (esNocturno) {
            reporte.append("Recargo nocturno: 20%\n");
        }
        
        reporte.append("\n--- TOTAL A PAGAR ---\n");
        reporte.append("TOTAL: ").append(String.format("$%.0f", tarifaTotal)).append("\n");
        
        reporte.append("\nEstado: ").append(estado.getNombre()).append("\n");
        reporte.append("Generado: ").append(LocalDateTime.now().format(formatter));
        
        return reporte.toString();
    }
    
    // ==================== MÉTODOS SOBRESCRITOS ====================
    
    /**
     * Compara dos viajes por su ID.
     * 
     * @param obj objeto a comparar
     * @return true si tienen el mismo ID
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Viaje viaje = (Viaje) obj;
        return Objects.equals(id, viaje.id);
    }
    
    /**
     * Genera código hash basado en el ID.
     * 
     * @return código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /**
     * Representación en cadena del viaje.
     * 
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return String.format("Viaje{id='%s', origen='%s', destino='%s', distancia=%.1f, estado='%s', tarifa=%.0f}", 
                           id, origen, destino, distancia, estado.getNombre(), tarifaTotal);
    }
}