package com.cargaexpress.model.entities;

import com.cargaexpress.exceptions.ClienteException;
import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.model.enums.TipoCliente;
import com.cargaexpress.utils.Constants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un cliente del sistema de transporte.
 * Extiende la clase Persona e incluye funcionalidades específicas
 * como manejo de descuentos, tipo de cliente y historial de viajes.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class Cliente extends Persona {
    
    // ==================== ATRIBUTOS ESPECÍFICOS ====================
    
    /**
     * Tipo de cliente (ESTANDAR, FRECUENTE, CORPORATIVO, VIP)
     */
    private TipoCliente tipoCliente;
    
    /**
     * Fecha de registro del cliente en el sistema
     */
    private LocalDate fechaRegistro;
    
    /**
     * Número total de viajes realizados por el cliente
     */
    private int viajesRealizados;
    
    /**
     * Porcentaje de descuento aplicable al cliente
     */
    private double descuentoFrecuente;
    
    /**
     * Dirección física del cliente
     */
    private String direccion;
    
    /**
     * Empresa o razón social (para clientes corporativos)
     */
    private String empresa;
    
    /**
     * Indica si el cliente está activo en el sistema
     */
    private boolean activo;
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor por defecto.
     * Inicializa un cliente con valores predeterminados.
     */
    public Cliente() {
        super();
        this.tipoCliente = TipoCliente.ESTANDAR;
        this.fechaRegistro = LocalDate.now();
        this.viajesRealizados = 0;
        this.descuentoFrecuente = 0.0;
        this.direccion = "";
        this.empresa = "";
        this.activo = true;
    }
    
    /**
     * Constructor básico para cliente estándar.
     * 
     * @param cedula número de cédula del cliente
     * @param nombre nombre del cliente
     * @param apellido apellido del cliente
     * @param telefono teléfono de contacto
     * @param email correo electrónico
     * @param fechaNacimiento fecha de nacimiento
     * @throws ValidationException si los datos son inválidos
     */
    public Cliente(String cedula, String nombre, String apellido, String telefono, 
                   String email, LocalDate fechaNacimiento) throws ValidationException {
        super(cedula, nombre, apellido, telefono, email, fechaNacimiento);
        this.tipoCliente = TipoCliente.ESTANDAR;
        this.fechaRegistro = LocalDate.now();
        this.viajesRealizados = 0;
        this.descuentoFrecuente = TipoCliente.ESTANDAR.getDescuento();
        this.direccion = "";
        this.empresa = "";
        this.activo = true;
    }
    
    /**
     * Constructor completo con todos los datos del cliente.
     * 
     * @param cedula número de cédula del cliente
     * @param nombre nombre del cliente
     * @param apellido apellido del cliente
     * @param telefono teléfono de contacto
     * @param email correo electrónico
     * @param fechaNacimiento fecha de nacimiento
     * @param direccion dirección física
     * @param empresa empresa o razón social
     * @throws ValidationException si los datos son inválidos
     */
    public Cliente(String cedula, String nombre, String apellido, String telefono, 
                   String email, LocalDate fechaNacimiento, String direccion, 
                   String empresa) throws ValidationException {
        this(cedula, nombre, apellido, telefono, email, fechaNacimiento);
        setDireccion(direccion);
        setEmpresa(empresa);
        
        // Determinar tipo de cliente basado en si tiene empresa
        if (empresa != null && !empresa.trim().isEmpty()) {
            this.tipoCliente = TipoCliente.CORPORATIVO;
            this.descuentoFrecuente = TipoCliente.CORPORATIVO.getDescuento();
        }
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    /**
     * Obtiene el tipo de cliente.
     * 
     * @return tipo de cliente
     */
    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }
    
    /**
     * Establece el tipo de cliente y actualiza el descuento correspondiente.
     * 
     * @param tipoCliente nuevo tipo de cliente
     */
    public void setTipoCliente(TipoCliente tipoCliente) {
        if (tipoCliente == null) {
            this.tipoCliente = TipoCliente.ESTANDAR;
        } else {
            this.tipoCliente = tipoCliente;
        }
        this.descuentoFrecuente = this.tipoCliente.getDescuento();
    }
    
    /**
     * Obtiene la fecha de registro.
     * 
     * @return fecha de registro del cliente
     */
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
    
    /**
     * Establece la fecha de registro.
     * 
     * @param fechaRegistro fecha de registro
     * @throws ValidationException si la fecha es inválida
     */
    public void setFechaRegistro(LocalDate fechaRegistro) throws ValidationException {
        if (fechaRegistro == null) {
            throw ValidationException.campoRequerido("fecha de registro");
        }
        
        if (fechaRegistro.isAfter(LocalDate.now())) {
            throw new ValidationException("fecha de registro", fechaRegistro.toString(), 
                "No puede ser fecha futura");
        }
        
        this.fechaRegistro = fechaRegistro;
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
        // Actualizar tipo de cliente basado en viajes
        actualizarTipoClientePorViajes();
    }
    
    /**
     * Obtiene el porcentaje de descuento.
     * 
     * @return porcentaje de descuento (0.0 a 1.0)
     */
    public double getDescuentoFrecuente() {
        return descuentoFrecuente;
    }
    
    /**
     * Establece manualmente el descuento (solo para casos especiales).
     * 
     * @param descuentoFrecuente porcentaje de descuento
     * @throws ValidationException si el descuento es inválido
     */
    public void setDescuentoFrecuente(double descuentoFrecuente) throws ValidationException {
        if (descuentoFrecuente < 0.0 || descuentoFrecuente > 1.0) {
            throw new ValidationException("descuento", 
                String.valueOf(descuentoFrecuente), "Debe estar entre 0.0 y 1.0");
        }
        
        this.descuentoFrecuente = descuentoFrecuente;
    }
    
    /**
     * Obtiene la dirección del cliente.
     * 
     * @return dirección física
     */
    public String getDireccion() {
        return direccion;
    }
    
    /**
     * Establece la dirección del cliente.
     * 
     * @param direccion dirección física
     * @throws ValidationException si la dirección es inválida
     */
    public void setDireccion(String direccion) throws ValidationException {
        if (direccion == null) {
            this.direccion = "";
            return;
        }
        
        String direccionLimpia = direccion.trim();
        if (direccionLimpia.length() > 200) {
            throw ValidationException.longitudInvalida("dirección", direccionLimpia, 0, 200);
        }
        
        this.direccion = direccionLimpia;
    }
    
    /**
     * Obtiene la empresa del cliente.
     * 
     * @return nombre de la empresa
     */
    public String getEmpresa() {
        return empresa;
    }
    
    /**
     * Establece la empresa del cliente.
     * 
     * @param empresa nombre de la empresa
     * @throws ValidationException si el nombre es inválido
     */
    public void setEmpresa(String empresa) throws ValidationException {
        if (empresa == null) {
            this.empresa = "";
            return;
        }
        
        String empresaLimpia = empresa.trim();
        if (empresaLimpia.length() > 100) {
            throw ValidationException.longitudInvalida("empresa", empresaLimpia, 0, 100);
        }
        
        this.empresa = empresaLimpia;
        
        // Si tiene empresa, debe ser al menos corporativo
        if (!empresaLimpia.isEmpty() && tipoCliente == TipoCliente.ESTANDAR) {
            setTipoCliente(TipoCliente.CORPORATIVO);
        }
    }
    
    /**
     * Verifica si el cliente está activo.
     * 
     * @return true si el cliente está activo
     */
    public boolean isActivo() {
        return activo;
    }
    
    /**
     * Establece el estado de actividad del cliente.
     * 
     * @param activo estado de actividad
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    // ==================== MÉTODOS DE NEGOCIO ====================
    
    /**
     * Verifica si el cliente es frecuente basado en número de viajes.
     * 
     * @return true si es cliente frecuente o superior
     */
    public boolean esClienteFrecuente() {
        return viajesRealizados >= Constants.VIAJES_PARA_FRECUENTE || 
               tipoCliente != TipoCliente.ESTANDAR;
    }
    
    /**
     * Calcula el descuento aplicable para una tarifa base.
     * 
     * @param tarifaBase tarifa base del viaje
     * @return monto del descuento a aplicar
     */
    public double aplicarDescuento(double tarifaBase) {
        if (tarifaBase <= 0) {
            return 0.0;
        }
        
        return tarifaBase * descuentoFrecuente;
    }
    
    /**
     * Calcula la tarifa final después de aplicar descuentos.
     * 
     * @param tarifaBase tarifa base del viaje
     * @return tarifa final con descuento aplicado
     */
    public double calcularTarifaFinal(double tarifaBase) {
        double descuento = aplicarDescuento(tarifaBase);
        return tarifaBase - descuento;
    }
    
    /**
     * Incrementa el contador de viajes realizados.
     * Actualiza automáticamente el tipo de cliente si es necesario.
     */
    public void incrementarViajes() {
        this.viajesRealizados++;
        actualizarTipoClientePorViajes();
    }
    
    /**
     * Registra múltiples viajes realizados.
     * 
     * @param numeroViajes número de viajes a agregar
     * @throws ClienteException si el número es inválido
     */
    public void registrarViajes(int numeroViajes) throws ClienteException {
        if (numeroViajes < 0) {
            throw new ClienteException("El número de viajes no puede ser negativo");
        }
        
        this.viajesRealizados += numeroViajes;
        actualizarTipoClientePorViajes();
    }
    
    /**
     * Actualiza el tipo de cliente basado en el número de viajes realizados.
     * Solo actualiza si el nuevo tipo es superior al actual.
     */
    private void actualizarTipoClientePorViajes() {
        TipoCliente nuevoTipo = TipoCliente.determinarTipo(viajesRealizados);
        
        // Solo actualizar si el nuevo tipo es superior (no degradar)
        if (nuevoTipo.ordinal() > tipoCliente.ordinal()) {
            setTipoCliente(nuevoTipo);
        }
    }
    
    /**
     * Calcula los días desde el registro del cliente.
     * 
     * @return número de días desde el registro
     */
    public long diasDesdeRegistro() {
        return java.time.temporal.ChronoUnit.DAYS.between(fechaRegistro, LocalDate.now());
    }
    
    /**
     * Verifica si el cliente es nuevo (registrado hace menos de 30 días).
     * 
     * @return true si es cliente nuevo
     */
    public boolean esClienteNuevo() {
        return diasDesdeRegistro() <= 30;
    }
    
    /**
     * Obtiene información del cliente como empresa o particular.
     * 
     * @return "Empresa" si tiene empresa, "Particular" si no
     */
    public String getTipoPersona() {
        return (empresa != null && !empresa.trim().isEmpty()) ? "Empresa" : "Particular";
    }
    
    /**
     * Genera un resumen estadístico del cliente.
     * 
     * @return cadena con estadísticas del cliente
     */
    public String generarResumenEstadistico() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN ESTADÍSTICO DEL CLIENTE ===\n");
        resumen.append("Nombre: ").append(getNombreCompleto()).append("\n");
        resumen.append("Tipo: ").append(tipoCliente.getDescripcion()).append("\n");
        resumen.append("Viajes realizados: ").append(viajesRealizados).append("\n");
        resumen.append("Descuento actual: ").append(String.format("%.1f%%", descuentoFrecuente * 100)).append("\n");
        resumen.append("Días desde registro: ").append(diasDesdeRegistro()).append("\n");
        resumen.append("Cliente nuevo: ").append(esClienteNuevo() ? "Sí" : "No").append("\n");
        resumen.append("Tipo persona: ").append(getTipoPersona()).append("\n");
        resumen.append("Estado: ").append(activo ? "Activo" : "Inactivo").append("\n");
        
        return resumen.toString();
    }
    
    // ==================== VALIDACIONES ESPECÍFICAS ====================
    
    /**
     * Valida todos los datos específicos del cliente.
     * 
     * @return true si todos los datos del cliente son válidos
     */
    public boolean validarDatosCliente() {
        return validarDatos() && // Validaciones de Persona
               tipoCliente != null &&
               fechaRegistro != null &&
               viajesRealizados >= 0 &&
               descuentoFrecuente >= 0.0 && descuentoFrecuente <= 1.0;
    }
    
    /**
     * Valida si el cliente puede realizar un nuevo viaje.
     * 
     * @return true si puede realizar viajes
     * @throws ClienteException si no puede realizar viajes
     */
    public boolean puedeRealizarViaje() throws ClienteException {
        if (!activo) {
            throw new ClienteException("Cliente inactivo no puede realizar viajes");
        }
        
        if (!validarDatosCliente()) {
            throw new ClienteException("Datos del cliente incompletos o inválidos");
        }
        
        return true;
    }
    
    // ==================== MÉTODOS DE FORMATO Y PRESENTACIÓN ====================
    
    /**
     * Obtiene información básica del cliente para mostrar en listas.
     * 
     * @return información resumida del cliente
     */
    public String getInformacionResumida() {
        return String.format("%s - %s (%s) - %d viajes", 
                           getCedula(), 
                           getNombreCompleto(), 
                           tipoCliente.getDescripcion(),
                           viajesRealizados);
    }
    
    /**
     * Formatea la información del cliente para reportes.
     * 
     * @return información formateada del cliente
     */
    public String formatearParaReporte() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        return String.format(
            "Cliente: %s | Cédula: %s | Tipo: %s | Viajes: %d | Registro: %s | Estado: %s",
            getNombreCompleto(),
            getCedula(),
            tipoCliente.getDescripcion(),
            viajesRealizados,
            fechaRegistro.format(formatter),
            activo ? "Activo" : "Inactivo"
        );
    }
    
    // ==================== IMPLEMENTACIÓN DEL MÉTODO ABSTRACTO ====================
    
    /**
     * Implementación del método abstracto de Persona.
     * Muestra información completa y específica del cliente.
     * 
     * @return información detallada del cliente
     */
    @Override
    public String mostrarInformacion() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder info = new StringBuilder();
        
        info.append("=== INFORMACIÓN DEL CLIENTE ===\n");
        info.append("Cédula: ").append(getCedula()).append("\n");
        info.append("Nombre completo: ").append(getNombreCompleto()).append("\n");
        info.append("Email: ").append(getEmail()).append("\n");
        info.append("Teléfono: ").append(getTelefono()).append("\n");
        info.append("Fecha nacimiento: ").append(getFechaNacimiento().format(formatter)).append("\n");
        info.append("Edad: ").append(calcularEdad()).append(" años\n");
        
        if (!direccion.isEmpty()) {
            info.append("Dirección: ").append(direccion).append("\n");
        }
        
        if (!empresa.isEmpty()) {
            info.append("Empresa: ").append(empresa).append("\n");
        }
        
        info.append("Tipo de cliente: ").append(tipoCliente.getDescripcion()).append("\n");
        info.append("Descuento aplicable: ").append(String.format("%.1f%%", descuentoFrecuente * 100)).append("\n");
        info.append("Viajes realizados: ").append(viajesRealizados).append("\n");
        info.append("Fecha de registro: ").append(fechaRegistro.format(formatter)).append("\n");
        info.append("Días como cliente: ").append(diasDesdeRegistro()).append("\n");
        info.append("Estado: ").append(activo ? "Activo" : "Inactivo").append("\n");
        
        return info.toString();
    }
    
    // ==================== MÉTODOS SOBRESCRITOS ====================
    
    /**
     * Representación en cadena del cliente.
     * 
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return String.format("Cliente{cedula='%s', nombre='%s', tipo='%s', viajes=%d, activo=%s}", 
                           getCedula(), getNombreCompleto(), tipoCliente.getDescripcion(), 
                           viajesRealizados, activo);
    }
    
    /**
     * Crea una copia de los datos básicos del cliente para transferencia.
     * Útil para operaciones que no requieren modificar el cliente original.
     * 
     * @return nueva instancia de Cliente con los mismos datos
     * @throws ValidationException si hay error al crear la copia
     */
    public Cliente crearCopia() throws ValidationException {
        Cliente copia = new Cliente(getCedula(), getNombre(), getApellido(), 
                                  getTelefono(), getEmail(), getFechaNacimiento(),
                                  direccion, empresa);
        copia.setTipoCliente(tipoCliente);
        copia.setFechaRegistro(fechaRegistro);
        copia.setViajesRealizados(viajesRealizados);
        copia.setDescuentoFrecuente(descuentoFrecuente);
        copia.setActivo(activo);
        
        return copia;
    }
}