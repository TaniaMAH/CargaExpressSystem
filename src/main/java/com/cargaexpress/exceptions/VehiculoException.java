package com.cargaexpress.exceptions;

/**
 * Excepción específica para errores relacionados con vehículos.
 * Se lanza cuando hay problemas en operaciones de vehículo.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class VehiculoException extends CargaExpressException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor por defecto.
     */
    public VehiculoException() {
        super("Error en operación de vehículo");
    }
    
    /**
     * Constructor con mensaje de error.
     * 
     * @param mensaje mensaje descriptivo del error
     */
    public VehiculoException(String mensaje) {
        super("Error de Vehículo: " + mensaje);
    }
    
    /**
     * Constructor con mensaje y causa.
     * 
     * @param mensaje mensaje descriptivo del error
     * @param causa excepción que causó este error
     */
    public VehiculoException(String mensaje, Throwable causa) {
        super("Error de Vehículo: " + mensaje, causa);
    }
    
    /**
     * Excepción para vehículo no encontrado.
     * 
     * @param placa placa del vehículo no encontrado
     * @return nueva instancia de VehiculoException
     */
    public static VehiculoException vehiculoNoEncontrado(String placa) {
        return new VehiculoException("Vehículo con placa " + placa + " no encontrado");
    }
    
    /**
     * Excepción para vehículo no disponible.
     * 
     * @param placa placa del vehículo no disponible
     * @return nueva instancia de VehiculoException
     */
    public static VehiculoException vehiculoNoDisponible(String placa) {
        return new VehiculoException("Vehículo con placa " + placa + " no está disponible");
    }
    
    /**
     * Excepción para placa inválida.
     * 
     * @param placa placa inválida
     * @return nueva instancia de VehiculoException
     */
    public static VehiculoException placaInvalida(String placa) {
        return new VehiculoException("Placa inválida: " + placa);
    }
    
    /**
     * Excepción para capacidad excedida.
     * 
     * @param capacidadRequerida capacidad requerida
     * @param capacidadDisponible capacidad disponible
     * @return nueva instancia de VehiculoException
     */
    public static VehiculoException capacidadExcedida(double capacidadRequerida, double capacidadDisponible) {
        return new VehiculoException(
            "Capacidad excedida: requerida " + capacidadRequerida + 
            ", disponible " + capacidadDisponible
        );
    }
}