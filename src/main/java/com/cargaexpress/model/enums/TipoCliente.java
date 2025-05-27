package com.cargaexpress.model.enums;

/**
 * Enumeración que define los tipos de cliente en el sistema.
 * Permite clasificar clientes para aplicar diferentes tarifas y descuentos.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public enum TipoCliente {
    
    /**
     * Cliente estándar sin descuentos especiales
     */
    ESTANDAR("Estándar", 0.0),
    
    /**
     * Cliente frecuente con descuentos por fidelidad
     */
    FRECUENTE("Frecuente", 0.15),
    
    /**
     * Cliente corporativo con tarifas preferenciales
     */
    CORPORATIVO("Corporativo", 0.20),
    
    /**
     * Cliente VIP con máximos beneficios
     */
    VIP("VIP", 0.25);
    
    private final String descripcion;
    private final double descuento;
    
    /**
     * Constructor del enum TipoCliente.
     * 
     * @param descripcion descripción legible del tipo de cliente
     * @param descuento porcentaje de descuento (0.0 a 1.0)
     */
    TipoCliente(String descripcion, double descuento) {
        this.descripcion = descripcion;
        this.descuento = descuento;
    }
    
    /**
     * Obtiene la descripción del tipo de cliente.
     * 
     * @return descripción del tipo de cliente
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Obtiene el porcentaje de descuento asociado al tipo de cliente.
     * 
     * @return porcentaje de descuento (0.0 a 1.0)
     */
    public double getDescuento() {
        return descuento;
    }
    
    /**
     * Determina el tipo de cliente basado en el número de viajes realizados.
     * 
     * @param viajesRealizados número de viajes del cliente
     * @return tipo de cliente correspondiente
     */
    public static TipoCliente determinarTipo(int viajesRealizados) {
        if (viajesRealizados >= 50) {
            return VIP;
        } else if (viajesRealizados >= 20) {
            return CORPORATIVO;
        } else if (viajesRealizados >= 5) {
            return FRECUENTE;
        } else {
            return ESTANDAR;
        }
    }
    
    @Override
    public String toString() {
        return descripcion + " (" + (descuento * 100) + "% descuento)";
    }
}