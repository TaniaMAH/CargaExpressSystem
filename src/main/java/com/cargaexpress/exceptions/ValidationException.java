package com.cargaexpress.exceptions;

/**
 * Excepción específica para errores de validación de datos.
 * Se lanza cuando los datos no cumplen con las reglas de validación.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class ValidationException extends CargaExpressException {
    
    private static final long serialVersionUID = 1L;
    
    private final String campo;
    private final String valor;
    private final String regla;
    
    /**
     * Constructor con información detallada de validación.
     * 
     * @param campo campo que falló la validación
     * @param valor valor que no pasó la validación
     * @param regla regla de validación violada
     */
    public ValidationException(String campo, String valor, String regla) {
        super("Validación fallida en campo '" + campo + "' con valor '" + valor + "': " + regla);
        this.campo = campo;
        this.valor = valor;
        this.regla = regla;
    }
    
    /**
     * Constructor con mensaje simple.
     * 
     * @param mensaje mensaje descriptivo del error de validación
     */
    public ValidationException(String mensaje) {
        super("Error de validación: " + mensaje);
        this.campo = null;
        this.valor = null;
        this.regla = mensaje;
    }
    
    /**
     * Obtiene el campo que falló la validación.
     * 
     * @return nombre del campo
     */
    public String getCampo() {
        return campo;
    }
    
    /**
     * Obtiene el valor que no pasó la validación.
     * 
     * @return valor inválido
     */
    public String getValor() {
        return valor;
    }
    
    /**
     * Obtiene la regla de validación violada.
     * 
     * @return regla de validación
     */
    public String getRegla() {
        return regla;
    }
    
    /**
     * Crea una excepción para campo requerido.
     * 
     * @param campo nombre del campo requerido
     * @return nueva instancia de ValidationException
     */
    public static ValidationException campoRequerido(String campo) {
        return new ValidationException(campo, "null/empty", "Campo requerido");
    }
    
    /**
     * Crea una excepción para formato inválido.
     * 
     * @param campo nombre del campo
     * @param valor valor con formato inválido
     * @param formatoEsperado formato esperado
     * @return nueva instancia de ValidationException
     */
    public static ValidationException formatoInvalido(String campo, String valor, String formatoEsperado) {
        return new ValidationException(campo, valor, "Formato esperado: " + formatoEsperado);
    }
    
    /**
     * Crea una excepción para longitud inválida.
     * 
     * @param campo nombre del campo
     * @param valor valor con longitud inválida
     * @param longitudMinima longitud mínima esperada
     * @param longitudMaxima longitud máxima esperada
     * @return nueva instancia de ValidationException
     */
    public static ValidationException longitudInvalida(String campo, String valor, int longitudMinima, int longitudMaxima) {
        return new ValidationException(
            campo, 
            valor, 
            "Longitud debe estar entre " + longitudMinima + " y " + longitudMaxima + " caracteres"
        );
    }
}