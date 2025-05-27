package com.cargaexpress.exceptions;

/**
 * Excepción base del sistema Carga Express.
 * Todas las excepciones personalizadas del sistema heredan de esta clase.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class CargaExpressException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor por defecto.
     */
    public CargaExpressException() {
        super();
    }
    
    /**
     * Constructor con mensaje de error.
     * 
     * @param mensaje mensaje descriptivo del error
     */
    public CargaExpressException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Constructor con mensaje y causa.
     * 
     * @param mensaje mensaje descriptivo del error
     * @param causa excepción que causó este error
     */
    public CargaExpressException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    /**
     * Constructor con causa.
     * 
     * @param causa excepción que causó este error
     */
    public CargaExpressException(Throwable causa) {
        super(causa);
    }
}