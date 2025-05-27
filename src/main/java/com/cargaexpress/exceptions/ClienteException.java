package com.cargaexpress.exceptions;

/**
 * Excepción específica para errores relacionados con clientes.
 * Se lanza cuando hay problemas en operaciones de cliente.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class ClienteException extends CargaExpressException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor por defecto.
     */
    public ClienteException() {
        super("Error en operación de cliente");
    }
    
    /**
     * Constructor con mensaje de error.
     * 
     * @param mensaje mensaje descriptivo del error
     */
    public ClienteException(String mensaje) {
        super("Error de Cliente: " + mensaje);
    }
    
    /**
     * Constructor con mensaje y causa.
     * 
     * @param mensaje mensaje descriptivo del error
     * @param causa excepción que causó este error
     */
    public ClienteException(String mensaje, Throwable causa) {
        super("Error de Cliente: " + mensaje, causa);
    }
    
    /**
     * Excepción para cliente no encontrado.
     * 
     * @param cedula cédula del cliente no encontrado
     * @return nueva instancia de ClienteException
     */
    public static ClienteException clienteNoEncontrado(String cedula) {
        return new ClienteException("Cliente con cédula " + cedula + " no encontrado");
    }
    
    /**
     * Excepción para cliente ya existente.
     * 
     * @param cedula cédula del cliente que ya existe
     * @return nueva instancia de ClienteException
     */
    public static ClienteException clienteYaExiste(String cedula) {
        return new ClienteException("Ya existe un cliente con cédula " + cedula);
    }
    
    /**
     * Excepción para datos inválidos de cliente.
     * 
     * @param campo campo con datos inválidos
     * @param valor valor inválido
     * @return nueva instancia de ClienteException
     */
    public static ClienteException datosInvalidos(String campo, String valor) {
        return new ClienteException("Datos inválidos en campo '" + campo + "': " + valor);
    }
}