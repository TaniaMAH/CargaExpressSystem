package com.cargaexpress.exceptions;

/**
 * Excepción específica para errores relacionados con conductores.
 * Se lanza cuando hay problemas en operaciones de conductor.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class ConductorException extends CargaExpressException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor por defecto.
     */
    public ConductorException() {
        super("Error en operación de conductor");
    }
    
    /**
     * Constructor con mensaje de error.
     * 
     * @param mensaje mensaje descriptivo del error
     */
    public ConductorException(String mensaje) {
        super("Error de Conductor: " + mensaje);
    }
    
    /**
     * Constructor con mensaje y causa.
     * 
     * @param mensaje mensaje descriptivo del error
     * @param causa excepción que causó este error
     */
    public ConductorException(String mensaje, Throwable causa) {
        super("Error de Conductor: " + mensaje, causa);
    }
    
    /**
     * Excepción para conductor no encontrado.
     * 
     * @param cedula cédula del conductor no encontrado
     * @return nueva instancia de ConductorException
     */
    public static ConductorException conductorNoEncontrado(String cedula) {
        return new ConductorException("Conductor con cédula " + cedula + " no encontrado");
    }
    
    /**
     * Excepción para conductor no disponible.
     * 
     * @param cedula cédula del conductor no disponible
     * @return nueva instancia de ConductorException
     */
    public static ConductorException conductorNoDisponible(String cedula) {
        return new ConductorException("Conductor con cédula " + cedula + " no está disponible");
    }
    
    /**
     * Excepción para licencia inválida.
     * 
     * @param licencia número de licencia inválida
     * @return nueva instancia de ConductorException
     */
    public static ConductorException licenciaInvalida(String licencia) {
        return new ConductorException("Licencia inválida: " + licencia);
    }
    
    /**
     * Excepción para conductor sin autorización para vehículo.
     * 
     * @param tipoLicencia tipo de licencia del conductor
     * @param tipoVehiculo tipo de vehículo
     * @return nueva instancia de ConductorException
     */
    public static ConductorException sinAutorizacionVehiculo(String tipoLicencia, String tipoVehiculo) {
        return new ConductorException(
            "Conductor con licencia " + tipoLicencia + 
            " no está autorizado para manejar vehículo tipo " + tipoVehiculo
        );
    }
}