package com.cargaexpress.model.pricing;

import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.model.entities.Cliente;
import com.cargaexpress.model.entities.Vehiculo;

/**
 * Clase abstracta que define la estructura base para el cálculo de tarifas.
 * Implementa el patrón Strategy para diferentes tipos de cálculo de precios.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public abstract class Tarifa {
    
    // ==================== ATRIBUTOS PROTEGIDOS ====================
    
    /**
     * Tarifa base por kilómetro
     */
    protected double tarifaBase;
    
    /**
     * Factor multiplicador por distancia
     */
    protected double factorDistancia;
    
    /**
     * Nombre descriptivo del tipo de tarifa
     */
    protected String nombreTarifa;
    
    /**
     * Descripción de la tarifa
     */
    protected String descripcion;
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor por defecto.
     */
    public Tarifa() {
        this.tarifaBase = 1000.0; // $1000 por km por defecto
        this.factorDistancia = 1.0;
        this.nombreTarifa = "Tarifa Base";
        this.descripcion = "Tarifa estándar del sistema";
    }
    
    /**
     * Constructor con parámetros básicos.
     * 
     * @param tarifaBase tarifa base por kilómetro
     * @param factorDistancia factor multiplicador por distancia
     * @throws ValidationException si los valores son inválidos
     */
    public Tarifa(double tarifaBase, double factorDistancia) throws ValidationException {
        setTarifaBase(tarifaBase);
        setFactorDistancia(factorDistancia);
        this.nombreTarifa = "Tarifa Base";
        this.descripcion = "Tarifa estándar del sistema";
    }
    
    /**
     * Constructor completo.
     * 
     * @param tarifaBase tarifa base por kilómetro
     * @param factorDistancia factor multiplicador por distancia
     * @param nombreTarifa nombre descriptivo
     * @param descripcion descripción de la tarifa
     * @throws ValidationException si los valores son inválidos
     */
    public Tarifa(double tarifaBase, double factorDistancia, String nombreTarifa, 
                  String descripcion) throws ValidationException {
        this(tarifaBase, factorDistancia);
        setNombreTarifa(nombreTarifa);
        setDescripcion(descripcion);
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    /**
     * Obtiene la tarifa base.
     * 
     * @return tarifa base por kilómetro
     */
    public double getTarifaBase() {
        return tarifaBase;
    }
    
    /**
     * Establece la tarifa base con validación.
     * 
     * @param tarifaBase tarifa base por kilómetro
     * @throws ValidationException si la tarifa es inválida
     */
    public void setTarifaBase(double tarifaBase) throws ValidationException {
        if (tarifaBase <= 0) {
            throw new ValidationException("tarifa base", String.valueOf(tarifaBase), 
                "Debe ser mayor que cero");
        }
        
        if (tarifaBase > 1000000) {
            throw new ValidationException("tarifa base", String.valueOf(tarifaBase), 
                "Excede el límite máximo de $1,000,000");
        }
        
        this.tarifaBase = tarifaBase;
    }
    
    /**
     * Obtiene el factor de distancia.
     * 
     * @return factor multiplicador por distancia
     */
    public double getFactorDistancia() {
        return factorDistancia;
    }
    
    /**
     * Establece el factor de distancia con validación.
     * 
     * @param factorDistancia factor multiplicador
     * @throws ValidationException si el factor es inválido
     */
    public void setFactorDistancia(double factorDistancia) throws ValidationException {
        if (factorDistancia <= 0) {
            throw new ValidationException("factor distancia", String.valueOf(factorDistancia), 
                "Debe ser mayor que cero");
        }
        
        if (factorDistancia > 10.0) {
            throw new ValidationException("factor distancia", String.valueOf(factorDistancia), 
                "Excede el límite máximo de 10.0");
        }
        
        this.factorDistancia = factorDistancia;
    }
    
    /**
     * Obtiene el nombre de la tarifa.
     * 
     * @return nombre descriptivo de la tarifa
     */
    public String getNombreTarifa() {
        return nombreTarifa;
    }
    
    /**
     * Establece el nombre de la tarifa.
     * 
     * @param nombreTarifa nombre descriptivo
     */
    public void setNombreTarifa(String nombreTarifa) {
        this.nombreTarifa = (nombreTarifa == null || nombreTarifa.trim().isEmpty()) ? 
                           "Tarifa Base" : nombreTarifa.trim();
    }
    
    /**
     * Obtiene la descripción de la tarifa.
     * 
     * @return descripción de la tarifa
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Establece la descripción de la tarifa.
     * 
     * @param descripcion descripción de la tarifa
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = (descripcion == null) ? "" : descripcion.trim();
    }
    
    // ==================== MÉTODOS DE NEGOCIO ====================
    
    /**
     * Calcula el factor por distancia aplicando escalas.
     * Distancias largas pueden tener descuentos por volumen.
     * 
     * @param distancia distancia del viaje en kilómetros
     * @return factor ajustado por distancia
     */
    protected double calcularFactorPorDistancia(double distancia) {
        if (distancia <= 0) {
            return 1.0;
        }
        
        // Escala de descuentos por distancia
        if (distancia > 500) {
            return factorDistancia * 0.85; // 15% descuento para distancias muy largas
        } else if (distancia > 200) {
            return factorDistancia * 0.92; // 8% descuento para distancias largas
        } else if (distancia > 50) {
            return factorDistancia * 0.97; // 3% descuento para distancias medias
        } else {
            return factorDistancia; // Sin descuento para distancias cortas
        }
    }
    
    /**
     * Calcula recargos adicionales por horario o condiciones especiales.
     * 
     * @param esHorarioNocturno si el viaje es en horario nocturno
     * @param esDiaFestivo si el viaje es en día festivo
     * @param esUrgente si el viaje es urgente
     * @return factor de recargo adicional
     */
    protected double calcularRecargosEspeciales(boolean esHorarioNocturno, 
                                              boolean esDiaFestivo, boolean esUrgente) {
        double recargo = 1.0;
        
        if (esHorarioNocturno) {
            recargo += 0.20; // 20% recargo nocturno
        }
        
        if (esDiaFestivo) {
            recargo += 0.15; // 15% recargo festivo
        }
        
        if (esUrgente) {
            recargo += 0.25; // 25% recargo urgente
        }
        
        return recargo;
    }
    
    /**
     * Formatea el precio con separadores de miles.
     * 
     * @param precio precio a formatear
     * @return precio formateado como cadena
     */
    protected String formatearPrecio(double precio) {
        return String.format("$%,.0f", precio);
    }
    
    // ==================== MÉTODOS ABSTRACTOS ====================
    
    /**
     * Método abstracto para calcular la tarifa específica.
     * Debe ser implementado por cada tipo de tarifa.
     * 
     * @param distancia distancia del viaje en kilómetros
     * @param cliente cliente que solicita el servicio
     * @param vehiculo vehículo asignado al viaje
     * @return precio total del viaje
     */
    public abstract double calcular(double distancia, Cliente cliente, Vehiculo vehiculo);
    
    /**
     * Método abstracto para obtener detalles del cálculo.
     * Debe proporcionar información sobre cómo se calculó la tarifa.
     * 
     * @param distancia distancia del viaje
     * @param cliente cliente del viaje
     * @param vehiculo vehículo del viaje
     * @return detalles del cálculo de la tarifa
     */
    public abstract String obtenerDetallesCalculo(double distancia, Cliente cliente, Vehiculo vehiculo);
    
    // ==================== MÉTODOS SOBRESCRITOS ====================
    
    /**
     * Representación en cadena de la tarifa.
     * 
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return String.format("Tarifa{nombre='%s', base=%.0f, factor=%.2f}", 
                           nombreTarifa, tarifaBase, factorDistancia);
    }
}