package com.cargaexpress.exceptions;

/**
 * Excepción específica para errores relacionados con viajes.
 * Se lanza cuando hay problemas en operaciones de viaje.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class ViajeException extends CargaExpressException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor por defecto.
     */
    public ViajeException() {
        super("Error en operación de viaje");
    }
    
    /**
     * Constructor con mensaje de error.
     * 
     * @param mensaje mensaje descriptivo del error
     */
    public ViajeException(String mensaje) {
        super("Error de Viaje: " + mensaje);
    }
    
    /**
     * Constructor con mensaje y causa.
     * 
     * @param mensaje mensaje descriptivo del error
     * @param causa excepción que causó este error
     */
    public ViajeException(String mensaje, Throwable causa) {
        super("Error de Viaje: " + mensaje, causa);
    }
    
    /**
     * Excepción para viaje no encontrado.
     * 
     * @param id ID del viaje no encontrado
     * @return nueva instancia de ViajeException
     */
    public static ViajeException viajeNoEncontrado(String id) {
        return new ViajeException("Viaje con ID " + id + " no encontrado");
    }
    
    /**
     * Excepción para fecha de viaje inválida.
     * 
     * @param fecha fecha inválida
     * @return nueva instancia de ViajeException
     */
    public static ViajeException fechaInvalida(String fecha) {
        return new ViajeException("Fecha de viaje inválida: " + fecha);
    }
    
    /**
     * Excepción para recursos no disponibles.
     * 
     * @param recurso recurso no disponible
     * @return nueva instancia de ViajeException
     */
    public static ViajeException recursoNoDisponible(String recurso) {
        return new ViajeException("Recurso no disponible para el viaje: " + recurso);
    }
    
    /**
     * Excepción para distancia inválida.
     * 
     * @param distancia distancia inválida
     * @return nueva instancia de ViajeException
     */
    public static ViajeException distanciaInvalida(double distancia) {
        return new ViajeException("Distancia inválida: " + distancia + " km");
    }
}