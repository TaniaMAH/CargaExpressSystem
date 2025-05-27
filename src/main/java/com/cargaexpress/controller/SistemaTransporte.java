package com.cargaexpress.controller;

import com.cargaexpress.exceptions.*;
import com.cargaexpress.model.entities.*;
import com.cargaexpress.model.enums.*;
import com.cargaexpress.model.pricing.*;
import com.cargaexpress.persistence.dao.*;
import com.cargaexpress.persistence.file.FileManager;
import com.cargaexpress.utils.Constants;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador principal del sistema de transporte.
 * Maneja todas las operaciones principales del negocio y coordina
 * la interacción entre todas las entidades del sistema.
 * Implementa el patrón Facade para simplificar el acceso al sistema.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class SistemaTransporte {
    
    // ==================== ATRIBUTOS - COLECCIONES PRINCIPALES ====================
    
    /**
     * Lista de todos los clientes registrados en el sistema
     */
    private ArrayList<Cliente> clientes;
    
    /**
     * Lista de todos los conductores registrados en el sistema
     */
    private ArrayList<Conductor> conductores;
    
    /**
     * Lista de todos los vehículos registrados en el sistema
     */
    private ArrayList<Vehiculo> vehiculos;
    
    /**
     * Lista de todos los viajes programados y realizados
     */
    private ArrayList<Viaje> viajes;
    
    /**
     * Información de la empresa
     */
    private Empresa empresa;
    
    /**
     * Sistema de tarifas por defecto
     */
    private Tarifa tarifaEstandar;
    
    /**
     * Sistema de tarifas para clientes frecuentes
     */
    private Tarifa tarifaFrecuente;
    
    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor por defecto.
     * Inicializa todas las colecciones y configura los sistemas de tarifa.
     */
    public SistemaTransporte() {
        // Inicializar colecciones
        this.clientes = new ArrayList<>();
        this.conductores = new ArrayList<>();
        this.vehiculos = new ArrayList<>();
        this.viajes = new ArrayList<>();
        
        // Configurar empresa por defecto
        try {
            this.empresa = new Empresa();
        } catch (Exception e) {
            this.empresa = new Empresa(); // Fallback con constructor vacío
        }
        
        // Configurar sistemas de tarifa
        try {
            this.tarifaEstandar = new TarifaEstandar();
            this.tarifaFrecuente = new TarifaFrecuente();
        } catch (ValidationException e) {
            System.err.println("Error configurando tarifas: " + e.getMessage());
            // Usar tarifas por defecto
            this.tarifaEstandar = new TarifaEstandar();
            this.tarifaFrecuente = new TarifaFrecuente();
        }
        
        System.out.println("✅ Sistema de Transporte inicializado correctamente");
    }
    
    /**
     * Constructor con empresa específica.
     * 
     * @param empresa información de la empresa
     */
    public SistemaTransporte(Empresa empresa) {
        this();
        this.empresa = (empresa != null) ? empresa : this.empresa;
    }
    
    // ==================== GETTERS PARA ACCESO A COLECCIONES ====================
    
    /**
     * Obtiene la lista de clientes.
     * 
     * @return lista de clientes (copia para evitar modificaciones externas)
     */
    public ArrayList<Cliente> getClientes() {
        return new ArrayList<>(clientes);
    }
    
    /**
     * Obtiene la lista de conductores.
     * 
     * @return lista de conductores
     */
    public ArrayList<Conductor> getConductores() {
        return new ArrayList<>(conductores);
    }
    
    /**
     * Obtiene la lista de vehículos.
     * 
     * @return lista de vehículos
     */
    public ArrayList<Vehiculo> getVehiculos() {
        return new ArrayList<>(vehiculos);
    }
    
    /**
     * Obtiene la lista de viajes.
     * 
     * @return lista de viajes
     */
    public ArrayList<Viaje> getViajes() {
        return new ArrayList<>(viajes);
    }
    
    /**
     * Obtiene la información de la empresa.
     * 
     * @return información de la empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }
    
    /**
     * Establece la información de la empresa.
     * 
     * @param empresa nueva información de la empresa
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = (empresa != null) ? empresa : this.empresa;
    }
    
    // ==================== MÉTODOS DE REGISTRO - CLIENTES ====================
    
    /**
     * Registra un nuevo cliente en el sistema.
     * 
     * @param cliente cliente a registrar
     * @return true si se registró exitosamente
     * @throws ClienteException si hay problemas con el registro
     */
    public boolean registrarCliente(Cliente cliente) throws ClienteException {
        if (cliente == null) {
            throw new ClienteException("El cliente no puede ser nulo");
        }
        
        // Verificar que no exista ya un cliente con la misma cédula
        if (buscarClientePorCedula(cliente.getCedula()) != null) {
            throw ClienteException.clienteYaExiste(cliente.getCedula());
        }
        
        // Validar datos del cliente
        if (!cliente.validarDatosCliente()) {
            throw ClienteException.datosInvalidos("datos generales", "incompletos o inválidos");
        }
        
        // Agregar a la lista
        boolean agregado = clientes.add(cliente);
        
        if (agregado) {
            System.out.println("✅ Cliente registrado: " + cliente.getNombreCompleto() + " (" + cliente.getCedula() + ")");
        }
        
        return agregado;
    }
    
    /**
     * Busca un cliente por su número de cédula.
     * 
     * @param cedula cédula del cliente a buscar
     * @return cliente encontrado o null si no existe
     */
    public Cliente buscarClientePorCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return null;
        }
        
        return clientes.stream()
                .filter(cliente -> cliente.getCedula().equals(cedula.trim()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Obtiene lista de clientes filtrada por tipo.
     * 
     * @param tipoCliente tipo de cliente a filtrar
     * @return lista de clientes del tipo especificado
     */
    public ArrayList<Cliente> obtenerClientesPorTipo(TipoCliente tipoCliente) {
        if (tipoCliente == null) {
            return new ArrayList<>(clientes);
        }
        
        return clientes.stream()
                .filter(cliente -> cliente.getTipoCliente() == tipoCliente)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    // ==================== MÉTODOS DE REGISTRO - CONDUCTORES ====================
    
    /**
     * Registra un nuevo conductor en el sistema.
     * 
     * @param conductor conductor a registrar
     * @return true si se registró exitosamente
     * @throws ConductorException si hay problemas con el registro
     */
    public boolean registrarConductor(Conductor conductor) throws ConductorException {
        if (conductor == null) {
            throw new ConductorException("El conductor no puede ser nulo");
        }
        
        // Verificar que no exista ya un conductor con la misma cédula
        if (buscarConductorPorCedula(conductor.getCedula()) != null) {
            throw new ConductorException("Ya existe un conductor con cédula " + conductor.getCedula());
        }
        
        // Validar datos del conductor
        if (!conductor.validarDatosConductor()) {
            throw new ConductorException("Datos del conductor incompletos o inválidos");
        }
        
        // Verificar licencia vigente
        if (!conductor.validarLicencia()) {
            throw ConductorException.licenciaInvalida(conductor.getLicencia());
        }
        
        // Agregar a la lista
        boolean agregado = conductores.add(conductor);
        
        if (agregado) {
            System.out.println("✅ Conductor registrado: " + conductor.getNombreCompleto() + " (" + conductor.getCedula() + ")");
        }
        
        return agregado;
    }
    
    /**
     * Busca un conductor por su número de cédula.
     * 
     * @param cedula cédula del conductor a buscar
     * @return conductor encontrado o null si no existe
     */
    public Conductor buscarConductorPorCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return null;
        }
        
        return conductores.stream()
                .filter(conductor -> conductor.getCedula().equals(cedula.trim()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Obtiene un conductor disponible que pueda manejar el tipo de vehículo especificado.
     * 
     * @param tipoVehiculo tipo de vehículo requerido
     * @return conductor disponible o null si no hay ninguno
     */
    public Conductor obtenerConductorDisponible(TipoVehiculo tipoVehiculo) {
        return conductores.stream()
                .filter(conductor -> {
                    try {
                        return conductor.estaDisponible() && 
                               conductor.puedeManearVehiculo(tipoVehiculo);
                    } catch (ConductorException e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Obtiene lista de conductores disponibles.
     * 
     * @return lista de conductores disponibles
     */
    public ArrayList<Conductor> obtenerConductoresDisponibles() {
        return conductores.stream()
                .filter(conductor -> {
                    try {
                        return conductor.estaDisponible();
                    } catch (ConductorException e) {
                        return false;
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    // ==================== MÉTODOS DE REGISTRO - VEHÍCULOS ====================
    
    /**
     * Registra un nuevo vehículo en el sistema.
     * 
     * @param vehiculo vehículo a registrar
     * @return true si se registró exitosamente
     * @throws VehiculoException si hay problemas con el registro
     */
    public boolean registrarVehiculo(Vehiculo vehiculo) throws VehiculoException {
        if (vehiculo == null) {
            throw new VehiculoException("El vehículo no puede ser nulo");
        }
        
        // Verificar que no exista ya un vehículo con la misma placa
        if (buscarVehiculoPorPlaca(vehiculo.getPlaca()) != null) {
            throw new VehiculoException("Ya existe un vehículo con placa " + vehiculo.getPlaca());
        }
        
        // Validar documentación
        if (!vehiculo.validarDocumentacion()) {
            throw new VehiculoException("Vehículo con documentación vencida o inválida");
        }
        
        // Agregar a la lista
        boolean agregado = vehiculos.add(vehiculo);
        
        if (agregado) {
            System.out.println("✅ Vehículo registrado: " + vehiculo.getMarca() + " " + 
                             vehiculo.getModelo() + " (" + vehiculo.getPlaca() + ")");
        }
        
        return agregado;
    }
    
    /**
     * Busca un vehículo por su placa.
     * 
     * @param placa placa del vehículo a buscar
     * @return vehículo encontrado o null si no existe
     */
    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }
        
        return vehiculos.stream()
                .filter(vehiculo -> vehiculo.getPlaca().equalsIgnoreCase(placa.trim()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Obtiene un vehículo disponible del tipo especificado.
     * 
     * @param tipoVehiculo tipo de vehículo requerido
     * @return vehículo disponible o null si no hay ninguno
     */
    public Vehiculo obtenerVehiculoDisponible(TipoVehiculo tipoVehiculo) {
        return vehiculos.stream()
                .filter(vehiculo -> {
                    try {
                        return vehiculo.estaDisponible() && 
                               vehiculo.getTipoVehiculo() == tipoVehiculo;
                    } catch (VehiculoException e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Obtiene lista de vehículos disponibles.
     * 
     * @return lista de vehículos disponibles
     */
    public ArrayList<Vehiculo> obtenerVehiculosDisponibles() {
        return vehiculos.stream()
                .filter(vehiculo -> {
                    try {
                        return vehiculo.estaDisponible();
                    } catch (VehiculoException e) {
                        return false;
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    // ==================== MÉTODOS DE GESTIÓN DE VIAJES ====================
    
    /**
     * Programa un nuevo viaje en el sistema.
     * 
     * @param viaje viaje a programar
     * @return true si se programó exitosamente
     * @throws ViajeException si hay problemas con la programación
     */
    public boolean programarViaje(Viaje viaje) throws ViajeException {
        if (viaje == null) {
            throw new ViajeException("El viaje no puede ser nulo");
        }
        
        // Validar que el viaje esté completo
        viaje.validarViajeCompleto();
        
        // Verificar disponibilidad de recursos
        if (!viaje.getConductor().isDisponible()) {
            throw ViajeException.recursoNoDisponible("conductor");
        }
        
        if (!viaje.getVehiculo().isDisponible()) {
            throw ViajeException.recursoNoDisponible("vehículo");
        }
        
        // Asignar sistema de tarifa apropiado
        Cliente cliente = viaje.getCliente();
        if (cliente != null && cliente.esClienteFrecuente()) {
            viaje.setSistemaTarifa(tarifaFrecuente);
        } else {
            viaje.setSistemaTarifa(tarifaEstandar);
        }
        
        // Calcular tarifa
        viaje.calcularTarifa();
        
        // Agregar a la lista
        boolean agregado = viajes.add(viaje);
        
        if (agregado) {
            System.out.println("✅ Viaje programado: " + viaje.getId() + " | " + 
                             viaje.getOrigen() + " → " + viaje.getDestino());
        }
        
        return agregado;
    }
    
    /**
     * Busca un viaje por su ID.
     * 
     * @param id ID del viaje a buscar
     * @return viaje encontrado o null si no existe
     */
    public Viaje buscarViajePorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        
        return viajes.stream()
                .filter(viaje -> viaje.getId().equals(id.trim()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Obtiene todos los viajes de un conductor específico.
     * 
     * @param conductor conductor cuyos viajes se quieren obtener
     * @return lista de viajes del conductor
     */
    public ArrayList<Viaje> obtenerViajesPorConductor(Conductor conductor) {
        if (conductor == null) {
            return new ArrayList<>();
        }
        
        return viajes.stream()
                .filter(viaje -> viaje.getConductor() != null && 
                               viaje.getConductor().equals(conductor))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Obtiene todos los viajes de un cliente específico.
     * 
     * @param cliente cliente cuyos viajes se quieren obtener
     * @return lista de viajes del cliente
     */
    public ArrayList<Viaje> obtenerViajesPorCliente(Cliente cliente) {
        if (cliente == null) {
            return new ArrayList<>();
        }
        
        return viajes.stream()
                .filter(viaje -> viaje.getCliente() != null && 
                               viaje.getCliente().equals(cliente))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Obtiene viajes filtrados por estado.
     * 
     * @param estado estado de los viajes a obtener
     * @return lista de viajes con el estado especificado
     */
    public ArrayList<Viaje> obtenerViajesPorEstado(EstadoViaje estado) {
        if (estado == null) {
            return new ArrayList<>(viajes);
        }
        
        return viajes.stream()
                .filter(viaje -> viaje.getEstado() == estado)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    // ==================== MÉTODOS DE ANÁLISIS Y REPORTES ====================
    
    /**
     * Obtiene los destinos más frecuentes.
     * 
     * @return lista de destinos ordenados por frecuencia
     */
    public ArrayList<String> obtenerDestinosFrecuentes() {
        Map<String, Long> destinosCount = viajes.stream()
                .collect(Collectors.groupingBy(
                    Viaje::getDestino,
                    Collectors.counting()
                ));
        
        return destinosCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Calcula el total de ingresos del sistema.
     * 
     * @return total de ingresos
     */
    public double calcularTotalIngresos() {
        return viajes.stream()
                .filter(viaje -> viaje.getEstado() == EstadoViaje.COMPLETADO)
                .mapToDouble(Viaje::getTarifaTotal)
                .sum();
    }
    
    /**
     * Obtiene estadísticas generales del sistema.
     * 
     * @return mapa con estadísticas del sistema
     */
    public Map<String, Object> obtenerEstadisticasGenerales() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        estadisticas.put("totalClientes", clientes.size());
        estadisticas.put("totalConductores", conductores.size());
        estadisticas.put("totalVehiculos", vehiculos.size());
        estadisticas.put("totalViajes", viajes.size());
        
        estadisticas.put("conductoresDisponibles", obtenerConductoresDisponibles().size());
        estadisticas.put("vehiculosDisponibles", obtenerVehiculosDisponibles().size());
        
        estadisticas.put("viajesCompletados", obtenerViajesPorEstado(EstadoViaje.COMPLETADO).size());
        estadisticas.put("viajesEnCurso", obtenerViajesPorEstado(EstadoViaje.EN_CURSO).size());
        estadisticas.put("viajesProgramados", obtenerViajesPorEstado(EstadoViaje.PROGRAMADO).size());
        
        estadisticas.put("totalIngresos", calcularTotalIngresos());
        
        return estadisticas;
    }
    
    /**
     * Genera un reporte completo del sistema.
     * 
     * @return reporte detallado del sistema
     */
    public String generarReporte() {
        StringBuilder reporte = new StringBuilder();
        Map<String, Object> stats = obtenerEstadisticasGenerales();
        
        reporte.append("=".repeat(60)).append("\n");
        reporte.append("REPORTE GENERAL DEL SISTEMA DE TRANSPORTE\n");
        reporte.append("=".repeat(60)).append("\n");
        
        // Información de la empresa
        reporte.append("EMPRESA: ").append(empresa.getNombre()).append("\n");
        reporte.append("NIT: ").append(empresa.getNit()).append("\n");
        reporte.append("Fecha del reporte: ").append(LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n\n");
        
        // Estadísticas generales
        reporte.append("--- ESTADÍSTICAS GENERALES ---\n");
        reporte.append("Total de clientes: ").append(stats.get("totalClientes")).append("\n");
        reporte.append("Total de conductores: ").append(stats.get("totalConductores")).append("\n");
        reporte.append("Total de vehículos: ").append(stats.get("totalVehiculos")).append("\n");
        reporte.append("Total de viajes: ").append(stats.get("totalViajes")).append("\n\n");
        
        // Disponibilidad de recursos
        reporte.append("--- DISPONIBILIDAD DE RECURSOS ---\n");
        reporte.append("Conductores disponibles: ").append(stats.get("conductoresDisponibles")).append("\n");
        reporte.append("Vehículos disponibles: ").append(stats.get("vehiculosDisponibles")).append("\n\n");
        
        // Estado de viajes
        reporte.append("--- ESTADO DE VIAJES ---\n");
        reporte.append("Viajes completados: ").append(stats.get("viajesCompletados")).append("\n");
        reporte.append("Viajes en curso: ").append(stats.get("viajesEnCurso")).append("\n");
        reporte.append("Viajes programados: ").append(stats.get("viajesProgramados")).append("\n\n");
        
        // Información financiera
        reporte.append("--- INFORMACIÓN FINANCIERA ---\n");
        reporte.append("Total de ingresos: $").append(String.format("%.0f", (Double) stats.get("totalIngresos"))).append("\n\n");
        
        // Destinos más frecuentes
        ArrayList<String> destinosFrecuentes = obtenerDestinosFrecuentes();
        reporte.append("--- DESTINOS MÁS FRECUENTES ---\n");
        for (int i = 0; i < Math.min(5, destinosFrecuentes.size()); i++) {
            reporte.append((i + 1)).append(". ").append(destinosFrecuentes.get(i)).append("\n");
        }
        
        reporte.append("\n").append("=".repeat(60));
        
        return reporte.toString();
    }
    
    // ==================== MÉTODOS DE BÚSQUEDA AVANZADA ====================
    
    /**
     * Busca conductores por años de experiencia mínima.
     * 
     * @param anosMinimos años mínimos de experiencia
     * @return lista de conductores que cumplen el criterio
     */
    public ArrayList<Conductor> buscarConductoresPorExperiencia(int anosMinimos) {
        return conductores.stream()
                .filter(conductor -> conductor.getAnosExperiencia() >= anosMinimos)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Busca vehículos por tipo específico.
     * 
     * @param tipoVehiculo tipo de vehículo a buscar
     * @return lista de vehículos del tipo especificado
     */
    public ArrayList<Vehiculo> buscarVehiculosPorTipo(TipoVehiculo tipoVehiculo) {
        if (tipoVehiculo == null) {
            return new ArrayList<>(vehiculos);
        }
        
        return vehiculos.stream()
                .filter(vehiculo -> vehiculo.getTipoVehiculo() == tipoVehiculo)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    /**
     * Busca viajes por rango de fechas.
     * 
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin fecha de fin del rango
     * @return lista de viajes en el rango especificado
     */
    public ArrayList<Viaje> buscarViajesPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return new ArrayList<>();
        }
        
        return viajes.stream()
                .filter(viaje -> {
                    LocalDateTime fechaViaje = viaje.getFechaViaje();
                    return fechaViaje != null && 
                           !fechaViaje.isBefore(fechaInicio) && 
                           !fechaViaje.isAfter(fechaFin);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    // ==================== MÉTODOS DE VALIDACIÓN Y MANTENIMIENTO ====================
    
    /**
     * Valida la integridad de todos los datos del sistema.
     * 
     * @return lista de problemas encontrados
     */
    public ArrayList<String> validarIntegridadSistema() {
        ArrayList<String> problemas = new ArrayList<>();
        
        // Validar clientes
        for (Cliente cliente : clientes) {
            if (!cliente.validarDatosCliente()) {
                problemas.add("Cliente con datos inválidos: " + cliente.getCedula());
            }
        }
        
        // Validar conductores
        for (Conductor conductor : conductores) {
            if (!conductor.validarDatosConductor()) {
                problemas.add("Conductor con datos inválidos: " + conductor.getCedula());
            }
            if (!conductor.validarLicencia()) {
                problemas.add("Conductor con licencia vencida: " + conductor.getCedula());
            }
        }
        
        // Validar vehículos
        for (Vehiculo vehiculo : vehiculos) {
            if (!vehiculo.validarDocumentacion()) {
                problemas.add("Vehículo con documentación vencida: " + vehiculo.getPlaca());
            }
        }
        
        // Validar viajes
        for (Viaje viaje : viajes) {
            if (viaje.getCliente() == null) {
                problemas.add("Viaje sin cliente asignado: " + viaje.getId());
            }
            if (viaje.getConductor() == null && viaje.getEstado() != EstadoViaje.PROGRAMADO) {
                problemas.add("Viaje sin conductor asignado: " + viaje.getId());
            }
        }
        
        return problemas;
    }
    
    /**
     * Limpia datos obsoletos del sistema (viajes muy antiguos, etc.).
     * 
     * @param diasAntiguedad días de antigüedad para considerar obsoleto
     * @return número de registros eliminados
     */
    public int limpiarDatosObsoletos(int diasAntiguedad) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasAntiguedad);
        int eliminados = 0;
        
        // Eliminar viajes muy antiguos que estén cancelados o completados
        Iterator<Viaje> iterator = viajes.iterator();
        while (iterator.hasNext()) {
            Viaje viaje = iterator.next();
            if (viaje.getFechaViaje().isBefore(fechaLimite) && 
                (viaje.getEstado() == EstadoViaje.CANCELADO || 
                 viaje.getEstado() == EstadoViaje.COMPLETADO)) {
                iterator.remove();
                eliminados++;
            }
        }
        
        System.out.println("🗑️ Limpieza completada: " + eliminados + " registros eliminados");
        return eliminados;
    }
    
    // ==================== MÉTODOS DE PERSISTENCIA - FASE 6 ====================
    
    /**
     * Guarda todos los datos del sistema en archivos.
     * 
     * @throws CargaExpressException si hay error al guardar
     */
    public void guardarDatos() throws CargaExpressException {
        try {
            System.out.println("💾 Guardando datos del sistema...");
            
            // Crear respaldos antes de guardar
            crearRespaldos();
            
            // Guardar cada tipo de datos
            ClienteDAO.guardarClientes(clientes);
            ConductorDAO.guardarConductores(conductores);
            VehiculoDAO.guardarVehiculos(vehiculos);
            ViajeDAO.guardarViajes(viajes);
            
            System.out.println("✅ Datos guardados exitosamente");
            System.out.println("   - " + clientes.size() + " clientes guardados");
            System.out.println("   - " + conductores.size() + " conductores guardados");
            System.out.println("   - " + vehiculos.size() + " vehículos guardados");
            System.out.println("   - " + viajes.size() + " viajes guardados");
            
        } catch (Exception e) {
            throw new CargaExpressException("Error guardando datos del sistema", e);
        }
    }
    
    /**
     * Carga todos los datos del sistema desde archivos.
     * 
     * @throws CargaExpressException si hay error al cargar
     */
    public void cargarDatos() throws CargaExpressException {
        try {
            System.out.println("📂 Cargando datos del sistema...");
            
            // Limpiar colecciones actuales
            clientes.clear();
            conductores.clear();
            vehiculos.clear();
            viajes.clear();
            
            // Cargar cada tipo de datos
            List<Cliente> clientesCargados = ClienteDAO.leerClientes();
            List<Conductor> conductoresCargados = ConductorDAO.leerConductores();
            List<Vehiculo> vehiculosCargados = VehiculoDAO.leerVehiculos();
            List<Viaje> viajesCargados = ViajeDAO.leerViajes();
            
            // Agregar a las colecciones
            clientes.addAll(clientesCargados);
            conductores.addAll(conductoresCargados);
            vehiculos.addAll(vehiculosCargados);
            
            // Para viajes, necesitamos reconstruir las referencias
            for (Viaje viajeData : viajesCargados) {
                try {
                    Viaje viaje = reconstruirViaje(viajeData);
                    if (viaje != null) {
                        viajes.add(viaje);
                    }
                } catch (Exception e) {
                    System.err.println("Error reconstruyendo viaje " + viajeData.getId() + ": " + e.getMessage());
                }
            }
            
            System.out.println("✅ Datos cargados exitosamente");
            System.out.println("   - " + clientes.size() + " clientes cargados");
            System.out.println("   - " + conductores.size() + " conductores cargados");
            System.out.println("   - " + vehiculos.size() + " vehículos cargados");
            System.out.println("   - " + viajes.size() + " viajes cargados");
            
        } catch (Exception e) {
            throw new CargaExpressException("Error cargando datos del sistema", e);
        }
    }
    
    /**
     * Reconstruye un viaje vinculando las referencias a objetos existentes.
     * 
     * @param viajeData datos del viaje cargados del archivo
     * @return viaje reconstruido con referencias correctas
     */
    private Viaje reconstruirViaje(Viaje viajeData) {
        // El viaje ya tiene los datos básicos, solo necesitamos vincular referencias
        // Si los IDs están guardados en el viaje, buscar los objetos correspondientes
        
        // Para simplificar, retornamos el viaje tal como está
        // En una implementación más compleja, buscaríamos cliente, conductor y vehículo por ID
        return viajeData;
    }
    
    /**
     * Crea respaldos de todos los archivos de datos.
     */
    private void crearRespaldos() {
        try {
            FileManager.crearRespaldo(Constants.CLIENTES_FILE);
            FileManager.crearRespaldo(Constants.CONDUCTORES_FILE);
            FileManager.crearRespaldo(Constants.VEHICULOS_FILE);
            FileManager.crearRespaldo(Constants.VIAJES_FILE);
            System.out.println("📋 Respaldos creados exitosamente");
        } catch (Exception e) {
            System.err.println("⚠️ Error creando respaldos: " + e.getMessage());
        }
    }
    
    /**
     * Exporta datos a formato CSV para análisis externo.
     * 
     * @param tipoExportacion tipo de datos a exportar ("clientes", "conductores", "vehiculos", "viajes")
     * @throws CargaExpressException si hay error en la exportación
     */
    public void exportarDatos(String tipoExportacion) throws CargaExpressException {
        try {
            String nombreArchivo = "export_" + tipoExportacion + "_" + 
                                  System.currentTimeMillis() + ".csv";
            List<String> lineasCSV = new ArrayList<>();
            
            switch (tipoExportacion.toLowerCase()) {
                case "clientes":
                    lineasCSV.add("Cedula,Nombre,Apellido,Email,Telefono,TipoCliente,ViajesRealizados,Activo");
                    for (Cliente cliente : clientes) {
                        lineasCSV.add(String.format("%s,%s,%s,%s,%s,%s,%d,%s",
                            cliente.getCedula(), cliente.getNombre(), cliente.getApellido(),
                            cliente.getEmail(), cliente.getTelefono(), cliente.getTipoCliente(),
                            cliente.getViajesRealizados(), cliente.isActivo()));
                    }
                    break;
                    
                case "conductores":
                    lineasCSV.add("Cedula,Nombre,Apellido,Licencia,TipoLicencia,ExperienciaAños,SalarioBase,Disponible");
                    for (Conductor conductor : conductores) {
                        lineasCSV.add(String.format("%s,%s,%s,%s,%s,%d,%.0f,%s",
                            conductor.getCedula(), conductor.getNombre(), conductor.getApellido(),
                            conductor.getLicencia(), conductor.getTipoLicencia(), conductor.getAnosExperiencia(),
                            conductor.getSalarioBase(), conductor.isDisponible()));
                    }
                    break;
                    
                case "vehiculos":
                    lineasCSV.add("Placa,Marca,Modelo,Año,TipoVehiculo,Kilometraje,Disponible,Estado");
                    for (Vehiculo vehiculo : vehiculos) {
                        lineasCSV.add(String.format("%s,%s,%s,%d,%s,%.0f,%s,%s",
                            vehiculo.getPlaca(), vehiculo.getMarca(), vehiculo.getModelo(),
                            vehiculo.getAno(), vehiculo.getTipoVehiculo(), vehiculo.getKilometraje(),
                            vehiculo.isDisponible(), vehiculo.getEstado()));
                    }
                    break;
                    
                case "viajes":
                    lineasCSV.add("ID,Origen,Destino,Distancia,Estado,TarifaTotal,FechaViaje");
                    for (Viaje viaje : viajes) {
                        lineasCSV.add(String.format("%s,%s,%s,%.1f,%s,%.0f,%s",
                            viaje.getId(), viaje.getOrigen(), viaje.getDestino(),
                            viaje.getDistancia(), viaje.getEstado(), viaje.getTarifaTotal(),
                            viaje.getFechaViaje()));
                    }
                    break;
                    
                default:
                    throw new CargaExpressException("Tipo de exportación no válido: " + tipoExportacion);
            }
            
            FileManager.guardarArchivo(nombreArchivo, lineasCSV);
            System.out.println("📊 Exportación completada: " + nombreArchivo);
            
        } catch (Exception e) {
            throw new CargaExpressException("Error exportando datos", e);
        }
    }
    
    /**
     * Limpia archivos de datos (para testing o reset completo).
     * 
     * @throws CargaExpressException si hay error al limpiar
     */
    public void limpiarArchivos() throws CargaExpressException {
        try {
            FileManager.eliminarArchivo(Constants.CLIENTES_FILE);
            FileManager.eliminarArchivo(Constants.CONDUCTORES_FILE);
            FileManager.eliminarArchivo(Constants.VEHICULOS_FILE);
            FileManager.eliminarArchivo(Constants.VIAJES_FILE);
            
            // Limpiar colecciones en memoria
            clientes.clear();
            conductores.clear();
            vehiculos.clear();
            viajes.clear();
            
            System.out.println("🗑️ Archivos de datos eliminados exitosamente");
            
        } catch (Exception e) {
            throw new CargaExpressException("Error limpiando archivos", e);
        }
    }

    /**
     * Método principal para demostrar el funcionamiento del sistema con persistencia.
     * 
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        System.out.println("🚛 INICIANDO SISTEMA CARGA EXPRESS S.A.S CON PERSISTENCIA");
        System.out.println("=" .repeat(70));
        
        try {
            // Crear instancia del sistema
            SistemaTransporte sistema = new SistemaTransporte();
            
            // Configurar empresa
            Empresa empresa = new Empresa("Carga Express S.A.S", "900123456-7", 
                                        "Calle 100 #15-25, Bogotá", "601-2345678", 
                                        "info@cargaexpress.com");
            empresa.setRepresentanteLegal("Pedro Pablo Ramirez");
            sistema.setEmpresa(empresa);
            
            // Intentar cargar datos existentes
            System.out.println("\n📂 Intentando cargar datos existentes...");
            try {
                sistema.cargarDatos();
                System.out.println("✅ Datos cargados desde archivos");
            } catch (Exception e) {
                System.out.println("ℹ️ No hay datos existentes, cargando datos de prueba...");
                sistema.cargarDatosPrueba();
            }
            
            // Mostrar resumen
            sistema.mostrarResumenSistema();
            
            // DEMOSTRAR PERSISTENCIA
            System.out.println("\n💾 DEMOSTRANDO PERSISTENCIA:");
            
            // 1. Guardar datos actuales
            sistema.guardarDatos();
            
            // 2. Agregar un cliente nuevo para demostrar
            try {
                Cliente nuevoCliente = new Cliente("9999888777", "Laura", "Martínez", 
                                                 "3159999888", "laura.martinez@email.com", 
                                                 java.time.LocalDate.of(1992, 5, 10));
                sistema.registrarCliente(nuevoCliente);
                System.out.println("✅ Nuevo cliente agregado: " + nuevoCliente.getNombreCompleto());
            } catch (Exception e) {
                System.err.println("Cliente ya existe o error: " + e.getMessage());
            }
            
            // 3. Guardar datos actualizados
            sistema.guardarDatos();
            
            // 4. Demostrar exportación
            System.out.println("\n📊 DEMOSTRANDO EXPORTACIÓN:");
            sistema.exportarDatos("clientes");
            sistema.exportarDatos("conductores");
            
            // 5. Mostrar funcionalidades adicionales
            System.out.println("\n🔍 FUNCIONALIDADES DEL SISTEMA:");
            
            // Buscar cliente
            Cliente cliente = sistema.buscarClientePorCedula("1234567890");
            if (cliente != null) {
                System.out.println("\n📋 Cliente encontrado: " + cliente.getNombreCompleto());
            }
            
            // Mostrar conductores disponibles
            System.out.println("\n🚗 Conductores disponibles: " + 
                             sistema.obtenerConductoresDisponibles().size());
            
            // Mostrar destinos frecuentes
            ArrayList<String> destinos = sistema.obtenerDestinosFrecuentes();
            System.out.println("📍 Destinos registrados: " + destinos.size());
            
            // Validar integridad
            ArrayList<String> problemas = sistema.validarIntegridadSistema();
            if (problemas.isEmpty()) {
                System.out.println("✅ Validación de integridad: SISTEMA CORRECTO");
            } else {
                System.out.println("⚠️ Problemas encontrados: " + problemas.size());
            }
            
            System.out.println("\n" + "=".repeat(70));
            System.out.println("🎉 DEMOSTRACIÓN CON PERSISTENCIA COMPLETADA EXITOSAMENTE");
            System.out.println("📁 Datos guardados en: " + Constants.DATA_PATH);
            System.out.println("   - Clientes: " + Constants.CLIENTES_FILE);
            System.out.println("   - Conductores: " + Constants.CONDUCTORES_FILE);
            System.out.println("   - Vehículos: " + Constants.VEHICULOS_FILE);
            System.out.println("   - Viajes: " + Constants.VIAJES_FILE);
            
        } catch (Exception e) {
            System.err.println("❌ Error en la demostración: " + e.getMessage());
            e.printStackTrace();
        }
    }
}