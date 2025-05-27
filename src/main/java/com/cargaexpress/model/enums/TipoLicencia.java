package com.cargaexpress.model.enums;

/**
 * Enumeración que define los tipos de licencia de conducción.
 * Determina qué tipo de vehículos puede manejar cada conductor.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public enum TipoLicencia {
    
    /**
     * Licencia A1 - Motocicletas hasta 125cc
     */
    A1("A1", "Motocicletas hasta 125cc", 1.0),
    
    /**
     * Licencia A2 - Motocicletas sin restricción de cilindraje
     */
    A2("A2", "Motocicletas sin restricción", 1.1),
    
    /**
     * Licencia B1 - Automóviles, camperos, camionetas
     */
    B1("B1", "Vehículos particulares", 1.2),
    
    /**
     * Licencia B2 - Camiones rígidos, buses
     */
    B2("B2", "Camiones y buses", 1.5),
    
    /**
     * Licencia B3 - Vehículos articulados
     */
    B3("B3", "Vehículos articulados", 1.8),
    
    /**
     * Licencia C1 - Servicio público individual
     */
    C1("C1", "Servicio público individual", 1.3),
    
    /**
     * Licencia C2 - Servicio público colectivo
     */
    C2("C2", "Servicio público colectivo", 1.6),
    
    /**
     * Licencia C3 - Servicio público masivo
     */
    C3("C3", "Servicio público masivo", 2.0);
    
    private final String codigo;
    private final String descripcion;
    private final double factorBonificacion;
    
    /**
     * Constructor del enum TipoLicencia.
     * 
     * @param codigo código de la licencia
     * @param descripcion descripción del tipo de vehículos permitidos
     * @param factorBonificacion factor multiplicador para bonificaciones
     */
    TipoLicencia(String codigo, String descripcion, double factorBonificacion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.factorBonificacion = factorBonificacion;
    }
    
    /**
     * Obtiene el código de la licencia.
     * 
     * @return código de la licencia
     */
    public String getCodigo() {
        return codigo;
    }
    
    /**
     * Obtiene la descripción de la licencia.
     * 
     * @return descripción de la licencia
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Obtiene el factor de bonificación para esta licencia.
     * 
     * @return factor de bonificación
     */
    public double getFactorBonificacion() {
        return factorBonificacion;
    }
    
    /**
     * Verifica si esta licencia permite manejar un tipo específico de vehículo.
     * 
     * @param tipoVehiculo tipo de vehículo a verificar
     * @return true si la licencia permite manejar el vehículo
     */
    public boolean puedeManear(TipoVehiculo tipoVehiculo) {
        switch (this) {
            case A1:
            case A2:
                return tipoVehiculo == TipoVehiculo.MOTOCICLETA;
            case B1:
                return tipoVehiculo == TipoVehiculo.AUTOMOVIL || 
                       tipoVehiculo == TipoVehiculo.CAMIONETA;
            case B2:
                return tipoVehiculo != TipoVehiculo.MOTOCICLETA;
            case B3:
                return true; // Puede manejar cualquier vehículo
            case C1:
                return tipoVehiculo == TipoVehiculo.TAXI || 
                       tipoVehiculo == TipoVehiculo.AUTOMOVIL;
            case C2:
            case C3:
                return tipoVehiculo == TipoVehiculo.BUS || 
                       tipoVehiculo == TipoVehiculo.CAMION ||
                       tipoVehiculo == TipoVehiculo.FURGON;
            default:
                return false;
        }
    }
    
    @Override
    public String toString() {
        return codigo + " - " + descripcion;
    }
}