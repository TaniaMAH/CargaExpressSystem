package com.cargaexpress.utils;

/**
 * Clase de constantes del sistema Carga Express.
 * Contiene todas las constantes utilizadas en el sistema.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2024
 */
public final class Constants {
    
    // ==================== RUTAS DE ARCHIVOS ====================
    public static final String DATA_PATH = "src/main/resources/data/";
    public static final String IMAGES_PATH = "src/main/resources/images/";
    public static final String CONFIG_PATH = "src/main/resources/config/";
    
    // ==================== ARCHIVOS DE DATOS ====================
    public static final String CLIENTES_FILE = "clientes.txt";
    public static final String CONDUCTORES_FILE = "conductores.txt";
    public static final String VEHICULOS_FILE = "vehiculos.txt";
    public static final String VIAJES_FILE = "viajes.txt";
    public static final String EMPRESA_FILE = "empresa.txt";
    
    // ==================== VALIDACIONES ====================
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MIN_PHONE_LENGTH = 7;
    public static final int MAX_PHONE_LENGTH = 15;
    public static final int CEDULA_LENGTH = 10;
    public static final int PLACA_LENGTH = 6;
    
    // ==================== PATRONES DE VALIDACIÓN ====================
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String PHONE_PATTERN = "^[0-9+\\-\\s]+$";
    public static final String CEDULA_PATTERN = "^[0-9]{8,10}$";
    public static final String PLACA_PATTERN = "^[A-Z]{3}[0-9]{3}$";
    
    // ==================== VALORES DE NEGOCIO ====================
    public static final double TARIFA_BASE_CARGA = 50000.0;
    public static final double TARIFA_BASE_PASAJEROS = 30000.0;
    public static final double FACTOR_DISTANCIA = 1500.0;
    public static final double DESCUENTO_CLIENTE_FRECUENTE = 0.15;
    public static final int VIAJES_PARA_FRECUENTE = 5;
    public static final double BONIFICACION_POR_ANO = 50000.0;
    public static final int MAX_ANOS_EXPERIENCIA = 30;
    
    // ==================== MENSAJES DE ERROR ====================
    public static final String ERROR_INVALID_EMAIL = "Email inválido";
    public static final String ERROR_INVALID_PHONE = "Teléfono inválido";
    public static final String ERROR_INVALID_CEDULA = "Cédula inválida";
    public static final String ERROR_INVALID_PLACA = "Placa inválida";
    public static final String ERROR_NAME_TOO_SHORT = "Nombre demasiado corto";
    public static final String ERROR_NAME_TOO_LONG = "Nombre demasiado largo";
    public static final String ERROR_REQUIRED_FIELD = "Campo requerido";
    
    // ==================== MENSAJES DE ÉXITO ====================
    public static final String SUCCESS_CLIENTE_REGISTERED = "Cliente registrado exitosamente";
    public static final String SUCCESS_CONDUCTOR_REGISTERED = "Conductor registrado exitosamente";
    public static final String SUCCESS_VEHICULO_REGISTERED = "Vehículo registrado exitosamente";
    public static final String SUCCESS_VIAJE_CREATED = "Viaje creado exitosamente";
    
    // ==================== ESTADOS DEL SISTEMA ====================
    public static final String ESTADO_DISPONIBLE = "DISPONIBLE";
    public static final String ESTADO_OCUPADO = "OCUPADO";
    public static final String ESTADO_PROGRAMADO = "PROGRAMADO";
    public static final String ESTADO_EN_CURSO = "EN_CURSO";
    public static final String ESTADO_FINALIZADO = "FINALIZADO";
    public static final String ESTADO_CANCELADO = "CANCELADO";
    
    /**
     * Constructor privado para prevenir instanciación.
     * Esta es una clase de utilidades con solo métodos estáticos.
     */
    private Constants() {
        throw new UnsupportedOperationException("Esta es una clase de utilidades y no debe ser instanciada");
    }
}