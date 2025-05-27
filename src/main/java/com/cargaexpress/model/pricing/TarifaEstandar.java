/ =====================================================================
// ARCHIVO: src/main/java/com/cargaexpress/model/pricing/TarifaEstandar.java
// =====================================================================
package com.cargaexpress.model.pricing;

import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.model.entities.Cliente;
import com.cargaexpress.model.entities.Vehiculo;
import com.cargaexpress.utils.Constants;

/**
 * Implementación de tarifa estándar para clientes regulares.
 * Aplica tarifas base sin descuentos especiales.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class TarifaEstandar extends Tarifa {
    
    // ==================== ATRIBUTOS ESPECÍFICOS ====================
    
    /**
     * Recargo adicional aplicable a la tarifa estándar
     */
    private double recargo;
    
    /**
     * Tarifa mínima para viajes cortos
     */
    private double tarifaMinima;
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor por defecto.
     */
    public TarifaEstandar() {
        super();
        this.recargo = 0.0; // Sin recargo por defecto
        this.tarifaMinima = 15000.0; // $15,000 mínimo
        this.nombreTarifa = "Tarifa Estándar";
        this.descripcion = "Tarifa regular para clientes estándar";
    }
    
    /**
     * Constructor con parámetros básicos.
     * 
     * @param tarifaBase tarifa base por kilómetro
     * @param factorDistancia factor multiplicador por distancia
     * @param recargo recargo adicional
     * @throws ValidationException si los valores son inválidos
     */
    public TarifaEstandar(double tarifaBase, double factorDistancia, double recargo) 
            throws ValidationException {
        super(tarifaBase, factorDistancia, "Tarifa Estándar", 
              "Tarifa regular para clientes estándar");
        setRecargo(recargo);
        this.tarifaMinima = 15000.0;
    }
    
    /**
     * Constructor completo.
     * 
     * @param tarifaBase tarifa base por kilómetro
     * @param factorDistancia factor multiplicador por distancia
     * @param recargo recargo adicional
     * @param tarifaMinima tarifa mínima
     * @throws ValidationException si los valores son inválidos
     */
    public TarifaEstandar(double tarifaBase, double factorDistancia, double recargo, 
                         double tarifaMinima) throws ValidationException {
        this(tarifaBase, factorDistancia, recargo);
        setTarifaMinima(tarifaMinima);
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    /**
     * Obtiene el recargo.
     * 
     * @return recargo adicional
     */
    public double getRecargo() {
        return recargo;
    }
    
    /**
     * Establece el recargo con validación.
     * 
     * @param recargo recargo adicional (0.0 - 1.0)
     * @throws ValidationException si el recargo es inválido
     */
    public void setRecargo(double recargo) throws ValidationException {
        if (recargo < 0.0 || recargo > 1.0) {
            throw new ValidationException("recargo", String.valueOf(recargo), 
                "Debe estar entre 0.0 y 1.0 (0% - 100%)");
        }
        
        this.recargo = recargo;
    }
    
    /**
     * Obtiene la tarifa mínima.
     * 
     * @return tarifa mínima
     */
    public double getTarifaMinima() {
        return tarifaMinima;
    }
    
    /**
     * Establece la tarifa mínima.
     * 
     * @param tarifaMinima tarifa mínima
     * @throws ValidationException si la tarifa es inválida
     */
    public void setTarifaMinima(double tarifaMinima) throws ValidationException {
        if (tarifaMinima < 0) {
            throw new ValidationException("tarifa mínima", String.valueOf(tarifaMinima), 
                "No puede ser negativa");
        }
        
        this.tarifaMinima = tarifaMinima;
    }
    
    // ==================== IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS ====================
    
    /**
     * Calcula la tarifa estándar para el viaje.
     * Aplica tarifa base del vehículo, factor de distancia y recargos.
     * 
     * @param distancia distancia del viaje en kilómetros
     * @param cliente cliente que solicita el servicio
     * @param vehiculo vehículo asignado al viaje
     * @return precio total del viaje
     */
    @Override
    public double calcular(double distancia, Cliente cliente, Vehiculo vehiculo) {
        if (distancia <= 0 || vehiculo == null) {
            return tarifaMinima;
        }
        
        // Tarifa base del vehículo
        double tarifaVehiculo = vehiculo.calcularTarifaBase();
        
        // Factor ajustado por distancia
        double factorAjustado = calcularFactorPorDistancia(distancia);
        
        // Cálculo base
        double tarifaCalculada = tarifaVehiculo * distancia * factorAjustado;
        
        // Aplicar recargo de tarifa estándar
        if (recargo > 0) {
            tarifaCalculada += tarifaCalculada * recargo;
        }
        
        // Aplicar tarifa mínima
        tarifaCalculada = Math.max(tarifaCalculada, tarifaMinima);
        
        // Redondear a múltiplos de 100
        return Math.round(tarifaCalculada / 100.0) * 100.0;
    }
    
    /**
     * Obtiene detalles del cálculo de la tarifa estándar.
     * 
     * @param distancia distancia del viaje
     * @param cliente cliente del viaje
     * @param vehiculo vehículo del viaje
     * @return detalles del cálculo
     */
    @Override
    public String obtenerDetallesCalculo(double distancia, Cliente cliente, Vehiculo vehiculo) {
        StringBuilder detalles = new StringBuilder();
        detalles.append("=== CÁLCULO TARIFA ESTÁNDAR ===\n");
        
        if (vehiculo != null) {
            double tarifaVehiculo = vehiculo.calcularTarifaBase();
            detalles.append("Tarifa base vehículo: ").append(formatearPrecio(tarifaVehiculo)).append("/km\n");
            detalles.append("Distancia: ").append(String.format("%.1f", distancia)).append(" km\n");
            
            double factorAjustado = calcularFactorPorDistancia(distancia);
            detalles.append("Factor distancia: ").append(String.format("%.3f", factorAjustado)).append("\n");
            
            double subtotal = tarifaVehiculo * distancia * factorAjustado;
            detalles.append("Subtotal: ").append(formatearPrecio(subtotal)).append("\n");
            
            if (recargo > 0) {
                double montoRecargo = subtotal * recargo;
                detalles.append("Recargo (").append(String.format("%.1f", recargo * 100))
                       .append("%): ").append(formatearPrecio(montoRecargo)).append("\n");
                subtotal += montoRecargo;
            }
            
            double tarifaFinal = Math.max(subtotal, tarifaMinima);
            if (tarifaFinal == tarifaMinima && subtotal < tarifaMinima) {
                detalles.append("Tarifa mínima aplicada: ").append(formatearPrecio(tarifaMinima)).append("\n");
            }
            
            detalles.append("TOTAL: ").append(formatearPrecio(tarifaFinal));
        } else {
            detalles.append("Vehículo no especificado\n");
            detalles.append("TOTAL: ").append(formatearPrecio(tarifaMinima));
        }
        
        return detalles.toString();
    }
    
    // ==================== MÉTODOS SOBRESCRITOS ====================
    
    /**
     * Representación en cadena de la tarifa estándar.
     * 
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return String.format("TarifaEstandar{base=%.0f, recargo=%.1f%%, minima=%.0f}", 
                           tarifaBase, recargo * 100, tarifaMinima);
    }
}