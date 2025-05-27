// =====================================================================
// ARCHIVO: src/main/java/com/cargaexpress/model/entities/VehiculoCarga.java
// =====================================================================
package com.cargaexpress.model.entities;

import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.exceptions.VehiculoException;
import com.cargaexpress.model.enums.TipoVehiculo;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un vehículo de carga en el sistema de transporte.
 * Implementa funcionalidades específicas para transporte de mercancías.
 * Extiende la clase abstracta Vehiculo e implementa polimorfismo.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class VehiculoCarga extends Vehiculo {
    
    // ==================== ATRIBUTOS ESPECÍFICOS ====================
    
    /**
     * Tipo de carga que puede transportar (General, Refrigerada, Peligrosa, etc.)
     */
    private String tipoCarga;
    
    /**
     * Peso máximo que puede cargar en toneladas
     */
    private double pesoMaximo;
    
    /**
     * Indica si el vehículo tiene grúa incorporada
     */
    private boolean tieneGrua;
    
    /**
     * Volumen máximo de carga en metros cúbicos
     */
    private double volumenMaximo;
    
    /**
     * Indica si tiene sistema de refrigeración
     */
    private boolean tieneRefrigeracion;
    
    /**
     * Número de ejes del vehículo
     */
    private int numeroEjes;
    
    /**
     * Altura máxima permitida en metros
     */
    private double alturaMaxima;
    
    /**
     * Longitud de la carrocería en metros
     */
    private double longitudCarroceria;
    
    /**
     * Material de la carrocería (Acero, Aluminio, Fibra, etc.)
     */
    private String materialCarroceria;
    
    /**
     * Indica si tiene sistema de seguridad para la carga
     */
    private boolean tieneSeguridadCarga;
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor por defecto.
     * Inicializa un vehículo de carga con valores predeterminados.
     */
    public VehiculoCarga() {
        super();
        this.tipoCarga = "General";
        this.pesoMaximo = 3.5; // 3.5 toneladas por defecto
        this.tieneGrua = false;
        this.volumenMaximo = 15.0; // 15 m³ por defecto
        this.tieneRefrigeracion = false;
        this.numeroEjes = 2;
        this.alturaMaxima = 2.5;
        this.longitudCarroceria = 4.0;
        this.materialCarroceria = "Acero";
        this.tieneSeguridadCarga = true;
        
        // Configurar tipo de vehículo por defecto
        setTipoVehiculo(TipoVehiculo.CAMIONETA);
    }
    
    /**
     * Constructor básico para vehículo de carga.
     * 
     * @param placa placa del vehículo
     * @param marca marca del vehículo
     * @param modelo modelo del vehículo
     * @param ano año de fabricación
     * @param capacidad capacidad en toneladas
     * @param tipoVehiculo tipo específico de vehículo de carga
     * @param tipoCarga tipo de carga que transporta
     * @param pesoMaximo peso máximo en toneladas
     * @param tieneGrua si tiene grúa incorporada
     * @throws ValidationException si los datos son inválidos
     */
    public VehiculoCarga(String placa, String marca, String modelo, int ano, 
                         double capacidad, TipoVehiculo tipoVehiculo, String tipoCarga, 
                         double pesoMaximo, boolean tieneGrua) throws ValidationException {
        super(placa, marca, modelo, ano, capacidad, tipoVehiculo);
        
        // Validar que sea un tipo de vehículo de carga
        if (tipoVehiculo.isTransportaPasajeros() && tipoVehiculo != TipoVehiculo.CAMIONETA) {
            throw new ValidationException("tipo de vehículo", tipoVehiculo.getNombre(), 
                "Debe ser un vehículo de carga");
        }
        
        setTipoCarga(tipoCarga);
        setPesoMaximo(pesoMaximo);
        setTieneGrua(tieneGrua);
        
        // Valores por defecto para otros atributos
        this.volumenMaximo = capacidad * 5; // Estimación: 5 m³ por tonelada
        this.tieneRefrigeracion = tipoCarga.equalsIgnoreCase("Refrigerada");
        this.numeroEjes = (pesoMaximo > 10) ? 3 : 2;
        this.alturaMaxima = 2.5;
        this.longitudCarroceria = Math.max(4.0, capacidad * 1.2);
        this.materialCarroceria = "Acero";
        this.tieneSeguridadCarga = true;
    }
    
    /**
     * Constructor completo con todas las especificaciones.
     * 
     * @param placa placa del vehículo
     * @param marca marca del vehículo
     * @param modelo modelo del vehículo
     * @param ano año de fabricación
     * @param capacidad capacidad en toneladas
     * @param tipoVehiculo tipo específico de vehículo
     * @param tipoCarga tipo de carga
     * @param pesoMaximo peso máximo en toneladas
     * @param tieneGrua si tiene grúa
     * @param volumenMaximo volumen máximo en m³
     * @param tieneRefrigeracion si tiene refrigeración
     * @param numeroEjes número de ejes
     * @throws ValidationException si los datos son inválidos
     */
    public VehiculoCarga(String placa, String marca, String modelo, int ano, 
                         double capacidad, TipoVehiculo tipoVehiculo, String tipoCarga, 
                         double pesoMaximo, boolean tieneGrua, double volumenMaximo, 
                         boolean tieneRefrigeracion, int numeroEjes) throws ValidationException {
        this(placa, marca, modelo, ano, capacidad, tipoVehiculo, tipoCarga, pesoMaximo, tieneGrua);
        setVolumenMaximo(volumenMaximo);
        setTieneRefrigeracion(tieneRefrigeracion);
        setNumeroEjes(numeroEjes);
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    /**
     * Obtiene el tipo de carga.
     * 
     * @return tipo de carga que puede transportar
     */
    public String getTipoCarga() {
        return tipoCarga;
    }
    
    /**
     * Establece el tipo de carga con validación.
     * 
     * @param tipoCarga tipo de carga
     * @throws ValidationException si el tipo es inválido
     */
    public void setTipoCarga(String tipoCarga) throws ValidationException {
        if (tipoCarga == null || tipoCarga.trim().isEmpty()) {
            throw ValidationException.campoRequerido("tipo de carga");
        }
        
        String tipoLimpio = tipoCarga.trim();
        // Validar tipos de carga permitidos
        String[] tiposPermitidos = {"General", "Refrigerada", "Peligrosa", "Líquidos", 
                                   "Contenedores", "Construcción", "Maquinaria", "Especial"};
        
        boolean tipoValido = false;
        for (String tipo : tiposPermitidos) {
            if (tipo.equalsIgnoreCase(tipoLimpio)) {
                this.tipoCarga = tipo;
                tipoValido = true;
                break;
            }
        }
        
        if (!tipoValido) {
            this.tipoCarga = tipoLimpio; // Permitir otros tipos pero registrar como ingresado
        }
    }
    
    /**
     * Obtiene el peso máximo.
     * 
     * @return peso máximo en toneladas
     */
    public double getPesoMaximo() {
        return pesoMaximo;
    }
    
    /**
     * Establece el peso máximo con validación.
     * 
     * @param pesoMaximo peso máximo en toneladas
     * @throws ValidationException si el peso es inválido
     */
    public void setPesoMaximo(double pesoMaximo) throws ValidationException {
        if (pesoMaximo <= 0) {
            throw new ValidationException("peso máximo", String.valueOf(pesoMaximo), 
                "Debe ser mayor que cero");
        }
        
        if (pesoMaximo > 50) {
            throw new ValidationException("peso máximo", String.valueOf(pesoMaximo), 
                "Excede el límite legal de 50 toneladas");
        }
        
        this.pesoMaximo = pesoMaximo;
    }
    
    /**
     * Verifica si tiene grúa incorporada.
     * 
     * @return true si tiene grúa
     */
    public boolean isTieneGrua() {
        return tieneGrua;
    }
    
    /**
     * Establece si tiene grúa incorporada.
     * 
     * @param tieneGrua si tiene grúa
     */
    public void setTieneGrua(boolean tieneGrua) {
        this.tieneGrua = tieneGrua;
    }
    
    /**
     * Obtiene el volumen máximo.
     * 
     * @return volumen máximo en metros cúbicos
     */
    public double getVolumenMaximo() {
        return volumenMaximo;
    }
    
    /**
     * Establece el volumen máximo.
     * 
     * @param volumenMaximo volumen máximo en m³
     * @throws ValidationException si el volumen es inválido
     */
    public void setVolumenMaximo(double volumenMaximo) throws ValidationException {
        if (volumenMaximo <= 0) {
            throw new ValidationException("volumen máximo", String.valueOf(volumenMaximo), 
                "Debe ser mayor que cero");
        }
        
        this.volumenMaximo = volumenMaximo;
    }
    
    /**
     * Verifica si tiene refrigeración.
     * 
     * @return true si tiene sistema de refrigeración
     */
    public boolean isTieneRefrigeracion() {
        return tieneRefrigeracion;
    }
    
    /**
     * Establece si tiene refrigeración.
     * 
     * @param tieneRefrigeracion si tiene refrigeración
     */
    public void setTieneRefrigeracion(boolean tieneRefrigeracion) {
        this.tieneRefrigeracion = tieneRefrigeracion;
    }
    
    /**
     * Obtiene el número de ejes.
     * 
     * @return número de ejes del vehículo
     */
    public int getNumeroEjes() {
        return numeroEjes;
    }
    
    /**
     * Establece el número de ejes.
     * 
     * @param numeroEjes número de ejes
     * @throws ValidationException si el número es inválido
     */
    public void setNumeroEjes(int numeroEjes) throws ValidationException {
        if (numeroEjes < 2 || numeroEjes > 6) {
            throw new ValidationException("número de ejes", String.valueOf(numeroEjes), 
                "Debe estar entre 2 y 6");
        }
        
        this.numeroEjes = numeroEjes;
    }
    
    /**
     * Obtiene la altura máxima.
     * 
     * @return altura máxima en metros
     */
    public double getAlturaMaxima() {
        return alturaMaxima;
    }
    
    /**
     * Establece la altura máxima.
     * 
     * @param alturaMaxima altura máxima en metros
     * @throws ValidationException si la altura es inválida
     */
    public void setAlturaMaxima(double alturaMaxima) throws ValidationException {
        if (alturaMaxima <= 0 || alturaMaxima > 4.5) {
            throw new ValidationException("altura máxima", String.valueOf(alturaMaxima), 
                "Debe estar entre 0.1 y 4.5 metros");
        }
        
        this.alturaMaxima = alturaMaxima;
    }
    
    /**
     * Obtiene la longitud de la carrocería.
     * 
     * @return longitud de la carrocería en metros
     */
    public double getLongitudCarroceria() {
        return longitudCarroceria;
    }
    
    /**
     * Establece la longitud de la carrocería.
     * 
     * @param longitudCarroceria longitud en metros
     * @throws ValidationException si la longitud es inválida
     */
    public void setLongitudCarroceria(double longitudCarroceria) throws ValidationException {
        if (longitudCarroceria <= 0 || longitudCarroceria > 20) {
            throw new ValidationException("longitud carrocería", String.valueOf(longitudCarroceria), 
                "Debe estar entre 0.1 y 20 metros");
        }
        
        this.longitudCarroceria = longitudCarroceria;
    }
    
    /**
     * Obtiene el material de la carrocería.
     * 
     * @return material de la carrocería
     */
    public String getMaterialCarroceria() {
        return materialCarroceria;
    }
    
    /**
     * Establece el material de la carrocería.
     * 
     * @param materialCarroceria material de la carrocería
     */
    public void setMaterialCarroceria(String materialCarroceria) {
        this.materialCarroceria = (materialCarroceria == null) ? "Acero" : materialCarroceria.trim();
    }
    
    /**
     * Verifica si tiene seguridad para la carga.
     * 
     * @return true si tiene sistema de seguridad
     */
    public boolean isTieneSeguridadCarga() {
        return tieneSeguridadCarga;
    }
    
    /**
     * Establece si tiene seguridad para la carga.
     * 
     * @param tieneSeguridadCarga si tiene sistema de seguridad
     */
    public void setTieneSeguridadCarga(boolean tieneSeguridadCarga) {
        this.tieneSeguridadCarga = tieneSeguridadCarga;
    }
    
    // ==================== MÉTODOS DE NEGOCIO ESPECÍFICOS ====================
    
    /**
     * Valida si el vehículo puede transportar un peso específico.
     * 
     * @param peso peso a transportar en toneladas
     * @return true si puede transportar el peso
     * @throws VehiculoException si excede la capacidad
     */
    public boolean validarPeso(double peso) throws VehiculoException {
        if (peso <= 0) {
            throw new VehiculoException("El peso debe ser mayor que cero");
        }
        
        if (peso > pesoMaximo) {
            throw VehiculoException.capacidadExcedida(peso, pesoMaximo);
        }
        
        return true;
    }
    
    /**
     * Valida si el vehículo puede transportar un volumen específico.
     * 
     * @param volumen volumen a transportar en m³
     * @return true si puede transportar el volumen
     * @throws VehiculoException si excede la capacidad
     */
    public boolean validarVolumen(double volumen) throws VehiculoException {
        if (volumen <= 0) {
            throw new VehiculoException("El volumen debe ser mayor que cero");
        }
        
        if (volumen > volumenMaximo) {
            throw VehiculoException.capacidadExcedida(volumen, volumenMaximo);
        }
        
        return true;
    }
    
    /**
     * Verifica si el vehículo puede transportar un tipo específico de carga.
     * 
     * @param tipoCargaRequerida tipo de carga requerida
     * @return true si puede transportar ese tipo de carga
     */
    public boolean puedeTransportarTipoCarga(String tipoCargaRequerida) {
        if (tipoCargaRequerida == null || tipoCargaRequerida.trim().isEmpty()) {
            return true; // Carga general
        }
        
        String tipoRequerido = tipoCargaRequerida.trim();
        
        // Si es carga general, puede transportar cualquier cosa
        if (tipoCarga.equalsIgnoreCase("General")) {
            return !tipoRequerido.equalsIgnoreCase("Peligrosa") && 
                   !tipoRequerido.equalsIgnoreCase("Refrigerada");
        }
        
        // Si es refrigerada, puede transportar refrigerada y general
        if (tipoCarga.equalsIgnoreCase("Refrigerada")) {
            return tipoRequerido.equalsIgnoreCase("Refrigerada") || 
                   tipoRequerido.equalsIgnoreCase("General");
        }
        
        // Para otros tipos, debe coincidir exactamente
        return tipoCarga.equalsIgnoreCase(tipoRequerido);
    }
    
    /**
     * Calcula el factor de eficiencia del vehículo basado en sus características.
     * 
     * @return factor de eficiencia (0.8 - 1.2)
     */
    public double calcularFactorEficiencia() {
        double factor = 1.0;
        
        // Bonificación por grúa (más versátil)
        if (tieneGrua) {
            factor += 0.1;
        }
        
        // Bonificación por refrigeración (carga especializada)
        if (tieneRefrigeracion) {
            factor += 0.15;
        }
        
        // Bonificación por seguridad de carga
        if (tieneSeguridadCarga) {
            factor += 0.05;
        }
        
        // Penalización por antigüedad
        int antiguedad = calcularAntiguedad();
        if (antiguedad > 10) {
            factor -= 0.1;
        } else if (antiguedad > 5) {
            factor -= 0.05;
        }
        
        // Factor por número de ejes (más ejes = más capacidad pero más costo)
        if (numeroEjes > 2) {
            factor += (numeroEjes - 2) * 0.05;
        }
        
        return Math.max(0.8, Math.min(1.2, factor));
    }
    
    /**
     * Calcula el costo adicional por características especiales.
     * 
     * @param tarifaBase tarifa base del viaje
     * @return costo adicional
     */
    public double calcularCostoAdicional(double tarifaBase) {
        double costoAdicional = 0.0;
        
        if (tieneGrua) {
            costoAdicional += tarifaBase * 0.20; // 20% adicional por grúa
        }
        
        if (tieneRefrigeracion) {
            costoAdicional += tarifaBase * 0.25; // 25% adicional por refrigeración
        }
        
        if (tipoCarga.equalsIgnoreCase("Peligrosa")) {
            costoAdicional += tarifaBase * 0.30; // 30% adicional por carga peligrosa
        }
        
        return costoAdicional;
    }
    
    // ==================== IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS ====================
    
    /**
     * Calcula la tarifa base específica para vehículos de carga.
     * Considera peso máximo, tipo de carga y características especiales.
     * 
     * @return tarifa base por kilómetro
     */
    @Override
    public double calcularTarifaBase() {
        // Tarifa base del tipo de vehículo
        double tarifaBase = getTipoVehiculo().getTarifaBase();
        
        // Factor por peso máximo
        double factorPeso = 1.0 + (pesoMaximo / 10.0) * 0.1; // 10% adicional por cada 10 toneladas
        
        // Factor de eficiencia del vehículo
        double factorEficiencia = calcularFactorEficiencia();
        
        // Calcular tarifa final
        double tarifaFinal = tarifaBase * factorPeso * factorEficiencia;
        
        return Math.round(tarifaFinal * 100.0) / 100.0; // Redondear a 2 decimales
    }
    
    /**
     * Obtiene información específica del tipo de vehículo de carga.
     * 
     * @return información específica del vehículo de carga
     */
    @Override
    public String getInformacionTipo() {
        StringBuilder info = new StringBuilder();
        info.append("VEHÍCULO DE CARGA\n");
        info.append("Tipo de carga: ").append(tipoCarga).append("\n");
        info.append("Peso máximo: ").append(String.format("%.1f", pesoMaximo)).append(" toneladas\n");
        info.append("Volumen máximo: ").append(String.format("%.1f", volumenMaximo)).append(" m³\n");
        info.append("Número de ejes: ").append(numeroEjes).append("\n");
        info.append("Dimensiones: ").append(String.format("%.1f", longitudCarroceria))
             .append("m x ").append(String.format("%.1f", alturaMaxima)).append("m\n");
        
        if (tieneGrua) info.append("✓ Con grúa incorporada\n");
        if (tieneRefrigeracion) info.append("✓ Con refrigeración\n");
        if (tieneSeguridadCarga) info.append("✓ Con sistema de seguridad\n");
        
        return info.toString();
    }
    
    /**
     * Muestra información completa y detallada del vehículo de carga.
     * 
     * @return información detallada del vehículo
     */
    @Override
    public String mostrarInformacion() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder info = new StringBuilder();
        
        info.append("=== INFORMACIÓN DEL VEHÍCULO DE CARGA ===\n");
        info.append("Placa: ").append(getPlaca()).append("\n");
        info.append("Marca: ").append(getMarca()).append(" ").append(getModelo()).append("\n");
        info.append("Año: ").append(getAno()).append(" (").append(calcularAntiguedad()).append(" años)\n");
        info.append("Color: ").append(getColor()).append("\n");
        info.append("Kilometraje: ").append(String.format("%.0f", getKilometraje())).append(" km\n");
        
        info.append("\n--- ESPECIFICACIONES DE CARGA ---\n");
        info.append("Tipo de carga: ").append(tipoCarga).append("\n");
        info.append("Capacidad de peso: ").append(String.format("%.1f", pesoMaximo)).append(" toneladas\n");
        info.append("Volumen máximo: ").append(String.format("%.1f", volumenMaximo)).append(" m³\n");
        info.append("Número de ejes: ").append(numeroEjes).append("\n");
        info.append("Dimensiones carrocería: ").append(String.format("%.1f", longitudCarroceria))
             .append("m x ").append(String.format("%.1f", alturaMaxima)).append("m\n");
        info.append("Material carrocería: ").append(materialCarroceria).append("\n");
        
        info.append("\n--- EQUIPAMIENTO ESPECIAL ---\n");
        info.append("Grúa incorporada: ").append(tieneGrua ? "Sí" : "No").append("\n");
        info.append("Sistema refrigeración: ").append(tieneRefrigeracion ? "Sí" : "No").append("\n");
        info.append("Seguridad de carga: ").append(tieneSeguridadCarga ? "Sí" : "No").append("\n");
        
        info.append("\n--- INFORMACIÓN TÉCNICA ---\n");
        info.append("Tarifa base: $").append(String.format("%.0f", calcularTarifaBase())).append("/km\n");
        info.append("Factor eficiencia: ").append(String.format("%.2f", calcularFactorEficiencia())).append("\n");
        info.append("Consumo combustible: ").append(String.format("%.2f", getConsumoCombustible())).append(" L/km\n");
        
        info.append("\n--- DOCUMENTACIÓN ---\n");
        if (getUltimaRevision() != null) {
            info.append("Última revisión: ").append(getUltimaRevision().format(formatter)).append("\n");
        }
        if (getVencimientoSoat() != null) {
            info.append("Vencimiento SOAT: ").append(getVencimientoSoat().format(formatter)).append("\n");
        }
        
        info.append("\n--- ESTADO ACTUAL ---\n");
        info.append("Estado: ").append(getEstado()).append("\n");
        info.append("Disponible: ").append(isDisponible() ? "Sí" : "No").append("\n");
        
        if (necesitaMantenimiento()) {
            info.append("🔧 Necesita mantenimiento\n");
        }
        
        if (!getObservaciones().isEmpty()) {
            info.append("Observaciones: ").append(getObservaciones()).append("\n");
        }
        
        info.append("\n--- ALERTAS ---\n");
        info.append(generarAlertas());
        
        return info.toString();
    }
    
    // ==================== MÉTODOS SOBRESCRITOS ====================
    
    /**
     * Representación en cadena del vehículo de carga.
     * 
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return String.format("VehiculoCarga{placa='%s', marca='%s %s', tipoCarga='%s', pesoMax=%.1ft, disponible=%s}", 
                           getPlaca(), getMarca(), getModelo(), tipoCarga, pesoMaximo, isDisponible());
    }
}