package com.cargaexpress.model.enums;

/**
 * Enumeración que define los tipos de vehículo disponibles en el sistema.
 * Determina las características y tarifas base de cada tipo.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public enum TipoVehiculo {
    
    /**
     * Motocicleta para servicios rápidos
     */
    MOTOCICLETA("Motocicleta", 15000.0, 2, true),
    
    /**
     * Automóvil para pasajeros
     */
    AUTOMOVIL("Automóvil", 25000.0, 5, true),
    
    /**
     * Camioneta para carga ligera
     */
    CAMIONETA("Camioneta", 35000.0, 7, false),
    
    /**
     * Taxi para servicio público
     */
    TAXI("Taxi", 20000.0, 4, true),
    
    /**
     * Furgón para carga mediana
     */
    FURGON("Furgón", 45000.0, 0, false),
    
    /**
     * Camión para carga pesada
     */
    CAMION("Camión", 80000.0, 0, false),
    
    /**
     * Bus para transporte masivo
     */
    BUS("Bus", 60000.0, 40, true);
    
    private final String nombre;
    private final double tarifaBase;
    private final int capacidadPasajeros;
    private final boolean transportaPasajeros;
    
    /**
     * Constructor del enum TipoVehiculo.
     * 
     * @param nombre nombre del tipo de vehículo
     * @param tarifaBase tarifa base por kilómetro
     * @param capacidadPasajeros número máximo de pasajeros
     * @param transportaPasajeros indica si transporta pasajeros
     */
    TipoVehiculo(String nombre, double tarifaBase, int capacidadPasajeros, boolean transportaPasajeros) {
        this.nombre = nombre;
        this.tarifaBase = tarifaBase;
        this.capacidadPasajeros = capacidadPasajeros;
        this.transportaPasajeros = transportaPasajeros;
    }
    
    /**
     * Obtiene el nombre del tipo de vehículo.
     * 
     * @return nombre del tipo de vehículo
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Obtiene la tarifa base por kilómetro.
     * 
     * @return tarifa base
     */
    public double getTarifaBase() {
        return tarifaBase;
    }
    
    /**
     * Obtiene la capacidad de pasajeros.
     * 
     * @return capacidad de pasajeros
     */
    public int getCapacidadPasajeros() {
        return capacidadPasajeros;
    }
    
    /**
     * Verifica si el vehículo transporta pasajeros.
     * 
     * @return true si transporta pasajeros
     */
    public boolean isTransportaPasajeros() {
        return transportaPasajeros;
    }
    
    /**
     * Verifica si el vehículo es de carga.
     * 
     * @return true si es vehículo de carga
     */
    public boolean isVehiculoCarga() {
        return !transportaPasajeros || capacidadPasajeros == 0;
    }
    
    @Override
    public String toString() {
        return nombre + " (Tarifa base: $" + String.format("%.0f", tarifaBase) + "/km)";
    }
}