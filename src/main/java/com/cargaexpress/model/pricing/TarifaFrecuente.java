package com.cargaexpress.model.pricing;

import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.model.entities.Cliente;
import com.cargaexpress.model.entities.Vehiculo;
import com.cargaexpress.model.enums.TipoCliente;

/**
 * Implementación de tarifa con descuentos para clientes frecuentes.
 * Aplica descuentos progresivos según el tipo y historial del cliente.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class TarifaFrecuente extends Tarifa {
    
    // ==================== ATRIBUTOS ESPECÍFICOS ====================
    
    /**
     * Descuento base para clientes frecuentes
     */
    private double descuento;
    
    /**
     * Descuento adicional por volumen de viajes
     */
    private double descuentoVolumen;
    
    /**
     * Límite de viajes para descuento adicional
     */
    private int limiteViajesDescuento;
    
    /**
     * Tarifa mínima después de descuentos
     */
    private double tarifaMinima;
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor por defecto.
     */
    public TarifaFrecuente() {
        super();
        this.descuento = 0.15; // 15% descuento por defecto
        this.descuentoVolumen = 0.05; // 5% descuento adicional por volumen
        this.limiteViajesDescuento = 20; // A partir de 20 viajes
        this.tarifaMinima = 12000.0; // $12,000 mínimo (menor que estándar)
        this.nombreTarifa = "Tarifa Frecuente";
        this.descripcion = "Tarifa con descuentos para clientes frecuentes y corporativos";
    }
    
    /**
     * Constructor con parámetros básicos.
     * 
     * @param tarifaBase tarifa base por kilómetro
     * @param factorDistancia factor multiplicador por distancia
     * @param descuento descuento para clientes frecuentes
     * @throws ValidationException si los valores son inválidos
     */
    public TarifaFrecuente(double tarifaBase, double factorDistancia, double descuento) 
            throws ValidationException {
        super(tarifaBase, factorDistancia, "Tarifa Frecuente", 
              "Tarifa con descuentos para clientes frecuentes");
        setDescuento(descuento);
        this.descuentoVolumen = 0.05;
        this.limiteViajesDescuento = 20;
        this.tarifaMinima = 12000.0;
    }
    
    /**
     * Constructor completo.
     * 
     * @param tarifaBase tarifa base por kilómetro
     * @param factorDistancia factor multiplicador por distancia
     * @param descuento descuento base
     * @param descuentoVolumen descuento adicional por volumen
     * @param limiteViajesDescuento límite de viajes para descuento adicional
     * @throws ValidationException si los valores son inválidos
     */
    public TarifaFrecuente(double tarifaBase, double factorDistancia, double descuento, 
                          double descuentoVolumen, int limiteViajesDescuento) 
            throws ValidationException {
        this(tarifaBase, factorDistancia, descuento);
        setDescuentoVolumen(descuentoVolumen);
        setLimiteViajesDescuento(limiteViajesDescuento);
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    /**
     * Obtiene el descuento base.
     * 
     * @return descuento base
     */
    public double getDescuento() {
        return descuento;
    }
    
    /**
     * Establece el descuento base.
     * 
     * @param descuento descuento base (0.0 - 0.5)
     * @throws ValidationException si el descuento es inválido
     */
    public void setDescuento(double descuento) throws ValidationException {
        if (descuento < 0.0 || descuento > 0.5) {
            throw new ValidationException("descuento", String.valueOf(descuento), 
                "Debe estar entre 0.0 y 0.5 (0% - 50%)");
        }
        
        this.descuento = descuento;
    }
    
    /**
     * Obtiene el descuento por volumen.
     * 
     * @return descuento adicional por volumen
     */
    public double getDescuentoVolumen() {
        return descuentoVolumen;
    }
    
    /**
     * Establece el descuento por volumen.
     * 
     * @param descuentoVolumen descuento adicional por volumen
     * @throws ValidationException si el descuento es inválido
     */
    public void setDescuentoVolumen(double descuentoVolumen) throws ValidationException {
        if (descuentoVolumen < 0.0 || descuentoVolumen > 0.2) {
            throw new ValidationException("descuento volumen", String.valueOf(descuentoVolumen), 
                "Debe estar entre 0.0 y 0.2 (0% - 20%)");
        }
        
        this.descuentoVolumen = descuentoVolumen;
    }
    
    /**
     * Obtiene el límite de viajes para descuento.
     * 
     * @return límite de viajes
     */
    public int getLimiteViajesDescuento() {
        return limiteViajesDescuento;
    }
    
    /**
     * Establece el límite de viajes para descuento.
     * 
     * @param limiteViajesDescuento límite de viajes
     * @throws ValidationException si el límite es inválido
     */
    public void setLimiteViajesDescuento(int limiteViajesDescuento) throws ValidationException {
        if (limiteViajesDescuento < 1) {
            throw new ValidationException("límite viajes descuento", 
                String.valueOf(limiteViajesDescuento), "Debe ser mayor que cero");
        }
        
        this.limiteViajesDescuento = limiteViajesDescuento;
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
    
    // ==================== MÉTODOS DE NEGOCIO ESPECÍFICOS ====================
    
    /**
     * Calcula el descuento total aplicable al cliente.
     * 
     * @param cliente cliente para calcular descuento
     * @return descuento total aplicable
     */
    private double calcularDescuentoTotal(Cliente cliente) {
        if (cliente == null) {
            return 0.0;
        }
        
        double descuentoTotal = 0.0;
        
        // Descuento base según tipo de cliente
        TipoCliente tipoCliente = cliente.getTipoCliente();
        if (tipoCliente != null) {
            descuentoTotal += tipoCliente.getDescuento();
        }
        
        // Descuento adicional por volumen de viajes
        if (cliente.getViajesRealizados() >= limiteViajesDescuento) {
            descuentoTotal += descuentoVolumen;
        }
        
        // Descuento adicional para clientes VIP con muchos viajes
        if (tipoCliente == TipoCliente.VIP && cliente.getViajesRealizados() > 50) {
            descuentoTotal += 0.05; // 5% adicional para VIP con más de 50 viajes
        }
        
        // Limitar descuento máximo al 60%
        return Math.min(descuentoTotal, 0.60);
    }
    
    // ==================== IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS ====================
    
    /**
     * Calcula la tarifa con descuentos para clientes frecuentes.
     * Aplica descuentos progresivos según el tipo y historial del cliente.
     * 
     * @param distancia distancia del viaje en kilómetros
     * @param cliente cliente que solicita el servicio
     * @param vehiculo vehículo asignado al viaje
     * @return precio total del viaje con descuentos aplicados
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
        
        // Aplicar descuentos del cliente
        double descuentoTotal = calcularDescuentoTotal(cliente);
        if (descuentoTotal > 0) {
            tarifaCalculada = tarifaCalculada * (1.0 - descuentoTotal);
        }
        
        // Aplicar tarifa mínima
        tarifaCalculada = Math.max(tarifaCalculada, tarifaMinima);
        
        // Redondear a múltiplos de 100
        return Math.round(tarifaCalculada / 100.0) * 100.0;
    }
    
    /**
     * Obtiene detalles del cálculo de la tarifa frecuente.
     * 
     * @param distancia distancia del viaje
     * @param cliente cliente del viaje
     * @param vehiculo vehículo del viaje
     * @return detalles del cálculo con descuentos
     */
    @Override
    public String obtenerDetallesCalculo(double distancia, Cliente cliente, Vehiculo vehiculo) {
        StringBuilder detalles = new StringBuilder();
        detalles.append("=== CÁLCULO TARIFA FRECUENTE ===\n");
        
        if (vehiculo != null) {
            double tarifaVehiculo = vehiculo.calcularTarifaBase();
            detalles.append("Tarifa base vehículo: ").append(formatearPrecio(tarifaVehiculo)).append("/km\n");
            detalles.append("Distancia: ").append(String.format("%.1f", distancia)).append(" km\n");
            
            double factorAjustado = calcularFactorPorDistancia(distancia);
            detalles.append("Factor distancia: ").append(String.format("%.3f", factorAjustado)).append("\n");
            
            double subtotal = tarifaVehiculo * distancia * factorAjustado;
            detalles.append("Subtotal: ").append(formatearPrecio(subtotal)).append("\n");
            
            // Detalles de descuentos
            if (cliente != null) {
                double descuentoTotal = calcularDescuentoTotal(cliente);
                if (descuentoTotal > 0) {
                    detalles.append("\n--- DESCUENTOS APLICADOS ---\n");
                    
                    TipoCliente tipoCliente = cliente.getTipoCliente();
                    if (tipoCliente != null && tipoCliente.getDescuento() > 0) {
                        detalles.append("Descuento ").append(tipoCliente.getDescripcion())
                               .append(": ").append(String.format("%.1f%%", tipoCliente.getDescuento() * 100)).append("\n");
                    }
                    
                    if (cliente.getViajesRealizados() >= limiteViajesDescuento) {
                        detalles.append("Descuento por volumen (").append(cliente.getViajesRealizados())
                               .append(" viajes): ").append(String.format("%.1f%%", descuentoVolumen * 100)).append("\n");
                    }
                    
                    double montoDescuento = subtotal * descuentoTotal;
                    detalles.append("Total descuentos: ").append(String.format("%.1f%%", descuentoTotal * 100))
                           .append(" = ").append(formatearPrecio(montoDescuento)).append("\n");
                    
                    subtotal -= montoDescuento;
                }
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
     * Representación en cadena de la tarifa frecuente.
     * 
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return String.format("TarifaFrecuente{base=%.0f, descuento=%.1f%%, volumen=%.1f%%, minima=%.0f}", 
                           tarifaBase, descuento * 100, descuentoVolumen * 100, tarifaMinima);
    }
}