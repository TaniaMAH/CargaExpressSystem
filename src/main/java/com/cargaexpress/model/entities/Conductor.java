package com.cargaexpress.model.entities;

import com.cargaexpress.exceptions.ConductorException;
import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.model.enums.TipoLicencia;
import com.cargaexpress.model.enums.TipoVehiculo;
import com.cargaexpress.utils.Constants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Clase que representa un conductor del sistema de transporte.
 * Extiende la clase Persona e incluye funcionalidades específicas
 * como manejo de licencias, experiencia, bonificaciones y disponibilidad.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2024
 */
public class Conductor extends Persona {
    
    // ==================== ATRIBUTOS ESPECÍFICOS ====================
    
    /**
     * Número de licencia de conducción
     */
    private String licencia;
    
    /**
     * Tipo de licencia de conducción
     */
    private TipoLicencia tipoLicencia;
    
    /**
     * Años de experiencia como conductor
     */
    private int anosExperiencia;
    
    /**
     * Ruta de la foto del conductor
     */
    private String foto;
    
    /**
     * Salario base mensual del conductor
     */
    private double salarioBase;
    
    /**
     * Indica si el conductor está disponible para asignaciones
     */
    private boolean disponible;
    
    /**
     * Fecha de ingreso a la empresa
     */
    private LocalDate fechaIngreso;
    
    /**
     * Fecha de vencimiento de la licencia
     */
    private LocalDate vencimientoLicencia;
    
    /**
     * Número total de viajes realizados
     */
    private int viajesRealizados;
    
    /**
     * Calificación promedio del conductor (1-5)
     */
    private double calificacionPromedio;
    
    /**
     * Observaciones o notas sobre el conductor
     */
    private String observaciones;
    
    /**
     * Última fecha y hora de actividad
     */
    private LocalDateTime ultimaActividad;
    
    // ==================== PATRONES DE VALIDACIÓN ====================
    
    private static final Pattern LICENCIA_PATTERN = Pattern.compile("^[A-Z0-9]{8,15}$");
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor por defecto.
     * Inicializa un conductor con valores predeterminados.
     */
    public Conductor() {
        super();
        this.licencia = "";
        this.tipoLicencia = TipoLicencia.B1;
        this.anosExperiencia = 0;
        this.foto = "";
        this.salarioBase = 0.0;
        this.disponible = true;
        this.fechaIngreso = LocalDate.now();
        this.vencimientoLicencia = LocalDate.now().plusYears(5);
        this.viajesRealizados = 0;
        this.calificacionPromedio = 5.0;
        this.observaciones = "";
        this.ultimaActividad = LocalDateTime.now();
    }
    
    /**
     * Constructor básico para conductor.
     * 
     * @param cedula número de cédula del conductor
     * @param nombre nombre del conductor
     * @param apellido apellido del conductor
     * @param telefono teléfono de contacto
     * @param email correo electrónico
     * @param fechaNacimiento fecha de nacimiento
     * @param licencia número de licencia
     * @param tipoLicencia tipo de licencia
     * @param anosExperiencia años de experiencia
     * @throws ValidationException si los datos son inválidos
     */
    public Conductor(String cedula, String nombre, String apellido, String telefono,
                     String email, LocalDate fechaNacimiento, String licencia,
                     TipoLicencia tipoLicencia, int anosExperiencia) throws ValidationException {
        super(cedula, nombre, apellido, telefono, email, fechaNacimiento);
        setLicencia(licencia);
        setTipoLicencia(tipoLicencia);
        setAnosExperiencia(anosExperiencia);
        
        this.foto = "";
        this.salarioBase = Constants.BONIFICACION_POR_ANO * 20; // Salario base inicial
        this.disponible = true;
        this.fechaIngreso = LocalDate.now();
        this.vencimientoLicencia = LocalDate.now().plusYears(5);
        this.viajesRealizados = 0;
        this.calificacionPromedio = 5.0;
        this.observaciones = "";
        this.ultimaActividad = LocalDateTime.now();
    }
    
    /**
     * Constructor completo con todos los datos del conductor.
     * 
     * @param cedula número de cédula del conductor
     * @param nombre nombre del conductor
     * @param apellido apellido del conductor
     * @param telefono teléfono de contacto
     * @param email correo electrónico
     * @param fechaNacimiento fecha de nacimiento
     * @param licencia número de licencia
     * @param tipoLicencia tipo de licencia
     * @param anosExperiencia años de experiencia
     * @param salarioBase salario base mensual
     * @param vencimientoLicencia fecha de vencimiento de licencia
     * @throws ValidationException si los datos son inválidos
     */
    public Conductor(String cedula, String nombre, String apellido, String telefono,
                     String email, LocalDate fechaNacimiento, String licencia,
                     TipoLicencia tipoLicencia, int anosExperiencia, double salarioBase,
                     LocalDate vencimientoLicencia) throws ValidationException {
        this(cedula, nombre, apellido, telefono, email, fechaNacimiento, 
             licencia, tipoLicencia, anosExperiencia);
        setSalarioBase(salarioBase);
        setVencimientoLicencia(vencimientoLicencia);
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    /**
     * Obtiene el número de licencia.
     * 
     * @return número de licencia
     */
    public String getLicencia() {
        return licencia;
    }
    
    /**
     * Establece el número de licencia con validación.
     * 
     * @param licencia número de licencia
     * @throws ValidationException si la licencia es inválida
     */
    public void setLicencia(String licencia) throws ValidationException {
        if (licencia == null || licencia.trim().isEmpty()) {
            throw ValidationException.campoRequerido("licencia");
        }
        
        String licenciaLimpia = licencia.trim().toUpperCase();
        if (!LICENCIA_PATTERN.matcher(licenciaLimpia).matches()) {
            throw ValidationException.formatoInvalido("licencia", licenciaLimpia, 
                "8-15 caracteres alfanuméricos en mayúsculas");
        }
        
        this.licencia = licenciaLimpia;
    }
    
    /**
     * Obtiene el tipo de licencia.
     * 
     * @return tipo de licencia
     */
    public TipoLicencia getTipoLicencia() {
        return tipoLicencia;
    }
    
    /**
     * Establece el tipo de licencia.
     * 
     * @param tipoLicencia tipo de licencia
     */
    public void setTipoLicencia(TipoLicencia tipoLicencia) {
        this.tipoLicencia = (tipoLicencia == null) ? TipoLicencia.B1 : tipoLicencia;
    }
    
    /**
     * Obtiene los años de experiencia.
     * 
     * @return años de experiencia
     */
    public int getAnosExperiencia() {
        return anosExperiencia;
    }
    
    /**
     * Establece los años de experiencia con validación.
     * 
     * @param anosExperiencia años de experiencia
     * @throws ValidationException si los años son inválidos
     */
    public void setAnosExperiencia(int anosExperiencia) throws ValidationException {
        if (anosExperiencia < 0) {
            throw new ValidationException("años de experiencia", 
                String.valueOf(anosExperiencia), "No puede ser negativo");
        }
        
        if (anosExperiencia > Constants.MAX_ANOS_EXPERIENCIA) {
            throw new ValidationException("años de experiencia", 
                String.valueOf(anosExperiencia), 
                "Máximo permitido: " + Constants.MAX_ANOS_EXPERIENCIA + " años");
        }
        
        // Validar que la experiencia sea coherente con la edad
        int edad = calcularEdad();
        if (anosExperiencia > (edad - 18)) {
            throw new ValidationException("años de experiencia", 
                String.valueOf(anosExperiencia), 
                "No puede tener más experiencia que años desde los 18");
        }
        
        this.anosExperiencia = anosExperiencia;
    }
    
    /**
     * Obtiene la ruta de la foto.
     * 
     * @return ruta de la foto del conductor
     */
    public String getFoto() {
        return foto;
    }
    
    /**
     * Establece la ruta de la foto.
     * 
     * @param foto ruta del archivo de foto
     */
    public void setFoto(String foto) {
        this.foto = (foto == null) ? "" : foto.trim();
    }
    
    /**
     * Obtiene el salario base.
     * 
     * @return salario base mensual
     */
    public double getSalarioBase() {
        return salarioBase;
    }
    
    /**
     * Establece el salario base con validación.
     * 
     * @param salarioBase salario base mensual
     * @throws ValidationException si el salario es inválido
     */
    public void setSalarioBase(double salarioBase) throws ValidationException {
        if (salarioBase < 0) {
            throw new ValidationException("salario base", 
                String.valueOf(salarioBase), "No puede ser negativo");
        }
        
        this.salarioBase = salarioBase;
    }
    
    /**
     * Verifica si el conductor está disponible.
     * 
     * @return true si está disponible
     */
    public boolean isDisponible() {
        return disponible;
    }
    
    /**
     * Establece la disponibilidad del conductor.
     * 
     * @param disponible estado de disponibilidad
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
        if (disponible) {
            this.ultimaActividad = LocalDateTime.now();
        }
    }
    
    /**
     * Obtiene la fecha de ingreso.
     * 
     * @return fecha de ingreso a la empresa
     */
    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }
    
    /**
     * Establece la fecha de ingreso.
     * 
     * @param fechaIngreso fecha de ingreso
     * @throws ValidationException si la fecha es inválida
     */
    public void setFechaIngreso(LocalDate fechaIngreso) throws ValidationException {
        if (fechaIngreso == null) {
            throw ValidationException.campoRequerido("fecha de ingreso");
        }
        
        if (fechaIngreso.isAfter(LocalDate.now())) {
            throw new ValidationException("fecha de ingreso", fechaIngreso.toString(), 
                "No puede ser fecha futura");
        }
        
        this.fechaIngreso = fechaIngreso;
    }
    
    /**
     * Obtiene la fecha de vencimiento de la licencia.
     * 
     * @return fecha de vencimiento de licencia
     */
    public LocalDate getVencimientoLicencia() {
        return vencimientoLicencia;
    }
    
    /**
     * Establece la fecha de vencimiento de la licencia.
     * 
     * @param vencimientoLicencia fecha de vencimiento
     * @throws ValidationException si la fecha es inválida
     */
    public void setVencimientoLicencia(LocalDate vencimientoLicencia) throws ValidationException {
        if (vencimientoLicencia == null) {
            throw ValidationException.campoRequerido("vencimiento de licencia");
        }
        
        if (vencimientoLicencia.isBefore(LocalDate.now())) {
            throw new ValidationException("vencimiento de licencia", 
                vencimientoLicencia.toString(), "La licencia está vencida");
        }
        
        this.vencimientoLicencia = vencimientoLicencia;
    }
    
    /**
     * Obtiene el número de viajes realizados.
     * 
     * @return número de viajes realizados
     */
    public int getViajesRealizados() {
        return viajesRealizados;
    }
    
    /**
     * Establece el número de viajes realizados.
     * 
     * @param viajesRealizados número de viajes
     * @throws ValidationException si el número es inválido
     */
    public void setViajesRealizados(int viajesRealizados) throws ValidationException {
        if (viajesRealizados < 0) {
            throw new ValidationException("viajes realizados", 
                String.valueOf(viajesRealizados), "No puede ser negativo");
        }
        
        this.viajesRealizados = viajesRealizados;
    }
    
    /**
     * Obtiene la calificación promedio.
     * 
     * @return calificación promedio (1.0 - 5.0)
     */
    public double getCalificacionPromedio() {
        return calificacionPromedio;
    }
    
    /**
     * Establece la calificación promedio.
     * 
     * @param calificacionPromedio calificación de 1.0 a 5.0
     * @throws ValidationException si la calificación es inválida
     */
    public void setCalificacionPromedio(double calificacionPromedio) throws ValidationException {
        if (calificacionPromedio < 1.0 || calificacionPromedio > 5.0) {
            throw new ValidationException("calificación", 
                String.valueOf(calificacionPromedio), "Debe estar entre 1.0 y 5.0");
        }
        
        this.calificacionPromedio = calificacionPromedio;
    }
    
    /**
     * Obtiene las observaciones.
     * 
     * @return observaciones sobre el conductor
     */
    public String getObservaciones() {
        return observaciones;
    }
    
    /**
     * Establece las observaciones.
     * 
     * @param observaciones observaciones sobre el conductor
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = (observaciones == null) ? "" : observaciones.trim();
    }
    
    /**
     * Obtiene la última actividad.
     * 
     * @return fecha y hora de última actividad
     */
    public LocalDateTime getUltimaActividad() {
        return ultimaActividad;
    }
    
    /**
     * Actualiza la última actividad al momento actual.
     */
    public void actualizarUltimaActividad() {
        this.ultimaActividad = LocalDateTime.now();
    }
    
    // ==================== MÉTODOS DE NEGOCIO ====================
    
    /**
     * Valida si la licencia del conductor está vigente.
     * 
     * @return true si la licencia está vigente
     */
    public boolean validarLicencia() {
        return licencia != null && 
               !licencia.trim().isEmpty() && 
               LICENCIA_PATTERN.matcher(licencia).matches() &&
               vencimientoLicencia != null &&
               vencimientoLicencia.isAfter(LocalDate.now());
    }
    
    /**
     * Calcula la bonificación del conductor basada en experiencia y tipo de licencia.
     * 
     * @return monto de bonificación mensual
     */
    public double calcularBonificacion() {
        double bonificacionBase = anosExperiencia * Constants.BONIFICACION_POR_ANO;
        double factorLicencia = tipoLicencia.getFactorBonificacion();
        double bonificacionCalificacion = (calificacionPromedio >= 4.5) ? bonificacionBase * 0.1 : 0;
        
        return bonificacionBase * factorLicencia + bonificacionCalificacion;
    }
    
    /**
     * Calcula el salario total incluyendo bonificaciones.
     * 
     * @return salario total mensual
     */
    public double calcularSalarioTotal() {
        return salarioBase + calcularBonificacion();
    }
    
    /**
     * Verifica si el conductor está disponible y puede tomar un viaje.
     * 
     * @return true si está disponible para trabajar
     * @throws ConductorException si no está disponible
     */
    public boolean estaDisponible() throws ConductorException {
        if (!disponible) {
            throw ConductorException.conductorNoDisponible(getCedula());
        }
        
        if (!validarLicencia()) {
            throw ConductorException.licenciaInvalida(licencia);
        }
        
        return true;
    }
    
    /**
     * Verifica si el conductor puede manejar un tipo específico de vehículo.
     * 
     * @param tipoVehiculo tipo de vehículo a verificar
     * @return true si puede manejar el vehículo
     * @throws ConductorException si no puede manejar el vehículo
     */
    public boolean puedeManearVehiculo(TipoVehiculo tipoVehiculo) throws ConductorException {
        if (!tipoLicencia.puedeManear(tipoVehiculo)) {
            throw ConductorException.sinAutorizacionVehiculo(
                tipoLicencia.getCodigo(), tipoVehiculo.getNombre());
        }
        
        return true;
    }
    
    /**
     * Asigna un viaje al conductor (cambia disponibilidad a false).
     */
    public void asignarViaje() {
        this.disponible = false;
        actualizarUltimaActividad();
    }
    
    /**
     * Libera al conductor después de completar un viaje.
     */
    public void liberarConductor() {
        this.disponible = true;
        this.viajesRealizados++;
        actualizarUltimaActividad();
    }
    
    /**
     * Verifica si el conductor es experimentado (más de 5 años).
     * 
     * @return true si es experimentado
     */
    public boolean esConductorExperimentado() {
        return anosExperiencia >= 5;
    }
    
    /**
     * Verifica si el conductor es nuevo (menos de 1 año de experiencia).
     * 
     * @return true si es conductor nuevo
     */
    public boolean esConductorNuevo() {
        return anosExperiencia < 1;
    }
    
    /**
     * Calcula los días trabajando en la empresa.
     * 
     * @return número de días desde el ingreso
     */
    public long diasEnEmpresa() {
        return java.time.temporal.ChronoUnit.DAYS.between(fechaIngreso, LocalDate.now());
    }
    
    /**
     * Verifica si la licencia está próxima a vencer (menos de 30 días).
     * 
     * @return true si está próxima a vencer
     */
    public boolean licenciaProximaVencer() {
        long diasParaVencer = java.time.temporal.ChronoUnit.DAYS.between(
            LocalDate.now(), vencimientoLicencia);
        return diasParaVencer <= 30;
    }
    
    /**
     * Obtiene el nivel de experiencia como texto.
     * 
     * @return nivel de experiencia descriptivo
     */
    public String getNivelExperiencia() {
        if (anosExperiencia < 1) {
            return "Principiante";
        } else if (anosExperiencia < 3) {
            return "Intermedio";
        } else if (anosExperiencia < 7) {
            return "Avanzado";
        } else if (anosExperiencia < 15) {
            return "Experto";
        } else {
            return "Maestro";
        }
    }
    
    /**
     * Calcula el promedio de viajes por año de experiencia.
     * 
     * @return promedio de viajes por año
     */
    public double promedioViajesPorAno() {
        if (anosExperiencia == 0) {
            return viajesRealizados;
        }
        return (double) viajesRealizados / anosExperiencia;
    }
    
    /**
     * Genera estadísticas del conductor.
     * 
     * @return cadena con estadísticas detalladas
     */
    public String generarEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DEL CONDUCTOR ===\n");
        stats.append("Nombre: ").append(getNombreCompleto()).append("\n");
        stats.append("Nivel de experiencia: ").append(getNivelExperiencia()).append("\n");
        stats.append("Años de experiencia: ").append(anosExperiencia).append("\n");
        stats.append("Viajes realizados: ").append(viajesRealizados).append("\n");
        stats.append("Promedio viajes/año: ").append(String.format("%.1f", promedioViajesPorAno())).append("\n");
        stats.append("Calificación promedio: ").append(String.format("%.1f/5.0", calificacionPromedio)).append("\n");
        stats.append("Salario base: $").append(String.format("%.0f", salarioBase)).append("\n");
        stats.append("Bonificación: $").append(String.format("%.0f", calcularBonificacion())).append("\n");
        stats.append("Salario total: $").append(String.format("%.0f", calcularSalarioTotal())).append("\n");
        stats.append("Días en empresa: ").append(diasEnEmpresa()).append("\n");
        stats.append("Estado: ").append(disponible ? "Disponible" : "Ocupado").append("\n");
        
        return stats.toString();
    }
    
    // ==================== VALIDACIONES ESPECÍFICAS ====================
    
    /**
     * Valida todos los datos específicos del conductor.
     * 
     * @return true si todos los datos del conductor son válidos
     */
    public boolean validarDatosConductor() {
        return validarDatos() && // Validaciones de Persona
               validarLicencia() &&
               tipoLicencia != null &&
               anosExperiencia >= 0 &&
               salarioBase >= 0 &&
               fechaIngreso != null &&
               vencimientoLicencia != null &&
               calificacionPromedio >= 1.0 && calificacionPromedio <= 5.0;
    }
    
    /**
     * Verifica si el conductor puede trabajar (validaciones completas).
     * 
     * @return true si puede trabajar
     * @throws ConductorException si no puede trabajar
     */
    public boolean puedeTrabajar() throws ConductorException {
        if (!validarDatosConductor()) {
            throw new ConductorException("Datos del conductor incompletos o inválidos");
        }
        
        if (!validarLicencia()) {
            throw ConductorException.licenciaInvalida(licencia);
        }
        
        if (!disponible) {
            throw ConductorException.conductorNoDisponible(getCedula());
        }
        
        return true;
    }
    
    // ==================== MÉTODOS DE FORMATO Y PRESENTACIÓN ====================
    
    /**
     * Obtiene información resumida del conductor para listas.
     * 
     * @return información resumida del conductor
     */
    public String getInformacionResumida() {
        return String.format("%s - %s (%s) - %d años exp. - %s", 
                           getCedula(), 
                           getNombreCompleto(), 
                           tipoLicencia.getCodigo(),
                           anosExperiencia,
                           disponible ? "Disponible" : "Ocupado");
    }
    
    /**
     * Formatea la información del conductor para reportes.
     * 
     * @return información formateada del conductor
     */
    public String formatearParaReporte() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        return String.format(
            "Conductor: %s | Cédula: %s | Licencia: %s | Experiencia: %d años | " +
            "Viajes: %d | Calificación: %.1f | Estado: %s | Ingreso: %s",
            getNombreCompleto(),
            getCedula(),
            tipoLicencia.getCodigo(),
            anosExperiencia,
            viajesRealizados,
            calificacionPromedio,
            disponible ? "Disponible" : "Ocupado",
            fechaIngreso.format(formatter)
        );
    }
    
    /**
     * Genera un resumen de alertas sobre el conductor.
     * 
     * @return cadena con alertas importantes
     */
    public String generarAlertas() {
        StringBuilder alertas = new StringBuilder();
        
        if (!validarLicencia()) {
            alertas.append("⚠️ LICENCIA VENCIDA O INVÁLIDA\n");
        } else if (licenciaProximaVencer()) {
            alertas.append("⚠️ Licencia próxima a vencer (").append(
                java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), vencimientoLicencia)
            ).append(" días)\n");
        }
        
        if (calificacionPromedio < 3.0) {
            alertas.append("⚠️ Calificación baja (").append(String.format("%.1f", calificacionPromedio)).append("/5.0)\n");
        }
        
        if (esConductorNuevo() && viajesRealizados == 0) {
            alertas.append("ℹ️ Conductor nuevo sin viajes realizados\n");
        }
        
        if (diasEnEmpresa() < 30) {
            alertas.append("ℹ️ Empleado reciente (").append(diasEnEmpresa()).append(" días en empresa)\n");
        }
        
        long horasSinActividad = java.time.temporal.ChronoUnit.HOURS.between(ultimaActividad, LocalDateTime.now());
        if (horasSinActividad > 48) {
            alertas.append("⚠️ Sin actividad por ").append(horasSinActividad).append(" horas\n");
        }
        
        if (alertas.length() == 0) {
            alertas.append("✅ Sin alertas - Conductor en buen estado");
        }
        
        return alertas.toString();
    }
    
    // ==================== IMPLEMENTACIÓN DEL MÉTODO ABSTRACTO ====================
    
    /**
     * Implementación del método abstracto de Persona.
     * Muestra información completa y específica del conductor.
     * 
     * @return información detallada del conductor
     */
    @Override
    public String mostrarInformacion() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder info = new StringBuilder();
        
        info.append("=== INFORMACIÓN DEL CONDUCTOR ===\n");
        info.append("Cédula: ").append(getCedula()).append("\n");
        info.append("Nombre completo: ").append(getNombreCompleto()).append("\n");
        info.append("Email: ").append(getEmail()).append("\n");
        info.append("Teléfono: ").append(getTelefono()).append("\n");
        info.append("Fecha nacimiento: ").append(getFechaNacimiento().format(dateFormatter)).append("\n");
        info.append("Edad: ").append(calcularEdad()).append(" años\n");
        
        info.append("\n--- DATOS PROFESIONALES ---\n");
        info.append("Licencia: ").append(licencia).append("\n");
        info.append("Tipo de licencia: ").append(tipoLicencia.toString()).append("\n");
        info.append("Vencimiento licencia: ").append(vencimientoLicencia.format(dateFormatter)).append("\n");
        info.append("Años de experiencia: ").append(anosExperiencia).append(" (").append(getNivelExperiencia()).append(")\n");
        info.append("Fecha de ingreso: ").append(fechaIngreso.format(dateFormatter)).append("\n");
        info.append("Días en empresa: ").append(diasEnEmpresa()).append("\n");
        
        info.append("\n--- DESEMPEÑO ---\n");
        info.append("Viajes realizados: ").append(viajesRealizados).append("\n");
        info.append("Promedio viajes/año: ").append(String.format("%.1f", promedioViajesPorAno())).append("\n");
        info.append("Calificación promedio: ").append(String.format("%.1f/5.0", calificacionPromedio)).append("\n");
        
        info.append("\n--- INFORMACIÓN ECONÓMICA ---\n");
        info.append("Salario base: $").append(String.format("%.0f", salarioBase)).append("\n");
        info.append("Bonificación: $").append(String.format("%.0f", calcularBonificacion())).append("\n");
        info.append("Salario total: $").append(String.format("%.0f", calcularSalarioTotal())).append("\n");
        
        info.append("\n--- ESTADO ACTUAL ---\n");
        info.append("Disponible: ").append(disponible ? "Sí" : "No").append("\n");
        info.append("Última actividad: ").append(ultimaActividad.format(dateTimeFormatter)).append("\n");
        
        if (!foto.isEmpty()) {
            info.append("Foto: ").append(foto).append("\n");
        }
        
        if (!observaciones.isEmpty()) {
            info.append("Observaciones: ").append(observaciones).append("\n");
        }
        
        info.append("\n--- ALERTAS ---\n");
        info.append(generarAlertas());
        
        return info.toString();
    }
    
    // ==================== MÉTODOS SOBRESCRITOS ====================
    
    /**
     * Representación en cadena del conductor.
     * 
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return String.format("Conductor{cedula='%s', nombre='%s', licencia='%s', experiencia=%d años, disponible=%s}", 
                           getCedula(), getNombreCompleto(), tipoLicencia.getCodigo(), 
                           anosExperiencia, disponible);
    }
    
    /**
     * Crea una copia de los datos del conductor para transferencia.
     * Útil para operaciones que no requieren modificar el conductor original.
     * 
     * @return nueva instancia de Conductor con los mismos datos
     * @throws ValidationException si hay error al crear la copia
     */
    public Conductor crearCopia() throws ValidationException {
        Conductor copia = new Conductor(getCedula(), getNombre(), getApellido(), 
                                      getTelefono(), getEmail(), getFechaNacimiento(),
                                      licencia, tipoLicencia, anosExperiencia, 
                                      salarioBase, vencimientoLicencia);
        copia.setFoto(foto);
        copia.setDisponible(disponible);
        copia.setFechaIngreso(fechaIngreso);
        copia.setViajesRealizados(viajesRealizados);
        copia.setCalificacionPromedio(calificacionPromedio);
        copia.setObservaciones(observaciones);
        copia.ultimaActividad = ultimaActividad;
        
        return copia;
    }
}