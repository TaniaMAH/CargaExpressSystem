package com.cargaexpress.model.entities;

import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.exceptions.VehiculoException;
import com.cargaexpress.model.enums.TipoVehiculo;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un vehículo de pasajeros en el sistema de transporte.
 * Implementa funcionalidades específicas para transporte de personas.
 * Extiende la clase abstracta Vehiculo e implementa polimorfismo.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class VehiculoPasajeros extends Vehiculo {
    
    // ==================== ATRIBUTOS ESPECÍFICOS ====================
    
    /**
     * Número máximo de pasajeros que puede transportar
     */
    private int numeroPasajeros;
    
    /**
     * Indica si tiene aire acondicionado
     */
    private boolean tieneAireAcondicionado;
    
    /**
     * Nivel de comodidad (Básico, Estándar, Premium, Lujo)
     */
    private String nivelComodidad;
    
    /**
     * Indica si tiene cinturones de seguridad para todos los pasajeros
     */
    private boolean tieneCinturones;
    
    /**
     * Indica si tiene sistema de entretenimiento
     */
    private boolean tieneEntretenimiento;
    
    /**
     * Número de puertas del vehículo
     */
    private int numeroPuertas;
    
    /**
     * Indica si tiene acceso para personas con discapacidad
     */
    private boolean tieneAccesoDiscapacitados;
    
    /**
     * Tipo de combustible (Gasolina, Diesel, Eléctrico, Híbrido)
     */
    private String tipoCombustible;
    
    /**
     * Indica si tiene WiFi disponible
     */
    private boolean tieneWifi;
    
    /**
     * Capacidad del maletero en litros
     */
    private double capacidadMaletero;
    
    /**
     * Indica si tiene GPS incorporado
     */
    private boolean tieneGps;
    
    /**
     * Sistema de audio (Básico, Premium, Professional)
     */
    private String sistemaAudio;
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor por defecto.
     * Inicializa un vehículo de pasajeros con valores predeterminados.
     */
    public VehiculoPasajeros() {
        super();
        this.numeroPasajeros = 4; // 4 pasajeros por defecto
        this.tieneAireAcondicionado = true;
        this.nivelComodidad = "Estándar";
        this.tieneCinturones = true;
        this.tieneEntretenimiento = false;
        this.numeroPuertas = 4;
        this.tieneAccesoDiscapacitados = false;
        this.tipoCombustible = "Gasolina";
        this.tieneWifi = false;
        this.capacidadMaletero = 400.0; // 400 litros por defecto
        this.tieneGps = true;
        this.sistemaAudio = "Básico";
        
        // Configurar tipo de vehículo por defecto
        setTipoVehiculo(TipoVehiculo.AUTOMOVIL);
    }
    
    /**
     * Constructor básico para vehículo de pasajeros.
     * 
     * @param placa placa del vehículo
     * @param marca marca del vehículo
     * @param modelo modelo del vehículo
     * @param ano año de fabricación
     * @param capacidad capacidad (número de pasajeros)
     * @param tipoVehiculo tipo específico de vehículo de pasajeros
     * @param numeroPasajeros número de pasajeros
     * @param tieneAireAcondicionado si tiene aire acondicionado
     * @param nivelComodidad nivel de comodidad
     * @throws ValidationException si los datos son inválidos
     */
    public VehiculoPasajeros(String placa, String marca, String modelo, int ano, 
                            double capacidad, TipoVehiculo tipoVehiculo, int numeroPasajeros, 
                            boolean tieneAireAcondicionado, String nivelComodidad) throws ValidationException {
        super(placa, marca, modelo, ano, capacidad, tipoVehiculo);
        
        // Validar que sea un tipo de vehículo de pasajeros
        if (!tipoVehiculo.isTransportaPasajeros()) {
            throw new ValidationException("tipo de vehículo", tipoVehiculo.getNombre(), 
                "Debe ser un vehículo de transporte de pasajeros");
        }
        
        setNumeroPasajeros(numeroPasajeros);
        setTieneAireAcondicionado(tieneAireAcondicionado);
        setNivelComodidad(nivelComodidad);
        
        // Valores por defecto para otros atributos
        this.tieneCinturones = true;
        this.tieneEntretenimiento = nivelComodidad.equalsIgnoreCase("Premium") || 
                                   nivelComodidad.equalsIgnoreCase("Lujo");
        this.numeroPuertas = (numeroPasajeros <= 4) ? 4 : 5;
        this.tieneAccesoDiscapacitados = (tipoVehiculo == TipoVehiculo.BUS);
        this.tipoCombustible = "Gasolina";
        this.tieneWifi = nivelComodidad.equalsIgnoreCase("Lujo");
        this.capacidadMaletero = Math.max(200, numeroPasajeros * 50);
        this.tieneGps = true;
        this.sistemaAudio = nivelComodidad.equalsIgnoreCase("Básico") ? "Básico" : "Premium";
    }
    
    /**
     * Constructor completo con todas las especificaciones.
     * 
     * @param placa placa del vehículo
     * @param marca marca del vehículo
     * @param modelo modelo del vehículo
     * @param ano año de fabricación
     * @param capacidad capacidad del vehículo
     * @param tipoVehiculo tipo específico de vehículo
     * @param numeroPasajeros número de pasajeros
     * @param tieneAireAcondicionado si tiene aire acondicionado
     * @param nivelComodidad nivel de comodidad
     * @param tipoCombustible tipo de combustible
     * @param capacidadMaletero capacidad del maletero
     * @throws ValidationException si los datos son inválidos
     */
    public VehiculoPasajeros(String placa, String marca, String modelo, int ano, 
                            double capacidad, TipoVehiculo tipoVehiculo, int numeroPasajeros, 
                            boolean tieneAireAcondicionado, String nivelComodidad, 
                            String tipoCombustible, double capacidadMaletero) throws ValidationException {
        this(placa, marca, modelo, ano, capacidad, tipoVehiculo, numeroPasajeros, 
             tieneAireAcondicionado, nivelComodidad);
        setTipoCombustible(tipoCombustible);
        setCapacidadMaletero(capacidadMaletero);
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    /**
     * Obtiene el número de pasajeros.
     * 
     * @return número máximo de pasajeros
     */
    public int getNumeroPasajeros() {
        return numeroPasajeros;
    }
    
    /**
     * Establece el número de pasajeros con validación.
     * 
     * @param numeroPasajeros número de pasajeros
     * @throws ValidationException si el número es inválido
     */
    public void setNumeroPasajeros(int numeroPasajeros) throws ValidationException {
        if (numeroPasajeros <= 0) {
            throw new ValidationException("número de pasajeros", String.valueOf(numeroPasajeros), 
                "Debe ser mayor que cero");
        }
        
        if (numeroPasajeros > 60) {
            throw new ValidationException("número de pasajeros", String.valueOf(numeroPasajeros), 
                "Excede el límite máximo de 60 pasajeros");
        }
        
        this.numeroPasajeros = numeroPasajeros;
        
        // Actualizar capacidad del vehículo base
        try {
            setCapacidad(numeroPasajeros);
        } catch (ValidationException e) {
            // Ignorar error de capacidad ya que se estableció en constructor padre
        }
    }
    
    /**
     * Verifica si tiene aire acondicionado.
     * 
     * @return true si tiene aire acondicionado
     */
    public boolean isTieneAireAcondicionado() {
        return tieneAireAcondicionado;
    }
    
    /**
     * Establece si tiene aire acondicionado.
     * 
     * @param tieneAireAcondicionado si tiene aire acondicionado
     */
    public void setTieneAireAcondicionado(boolean tieneAireAcondicionado) {
        this.tieneAireAcondicionado = tieneAireAcondicionado;
    }
    
    /**
     * Obtiene el nivel de comodidad.
     * 
     * @return nivel de comodidad del vehículo
     */
    public String getNivelComodidad() {
        return nivelComodidad;
    }
    
    /**
     * Establece el nivel de comodidad con validación.
     * 
     * @param nivelComodidad nivel de comodidad
     * @throws ValidationException si el nivel es inválido
     */
    public void setNivelComodidad(String nivelComodidad) throws ValidationException {
        if (nivelComodidad == null || nivelComodidad.trim().isEmpty()) {
            this.nivelComodidad = "Estándar";
            return;
        }
        
        String nivelLimpio = nivelComodidad.trim();
        String[] nivelesPermitidos = {"Básico", "Estándar", "Premium", "Lujo"};
        
        boolean nivelValido = false;
        for (String nivel : nivelesPermitidos) {
            if (nivel.equalsIgnoreCase(nivelLimpio)) {
                this.nivelComodidad = nivel;
                nivelValido = true;
                break;
            }
        }
        
        if (!nivelValido) {
            throw ValidationException.formatoInvalido("nivel de comodidad", nivelLimpio, 
                "Básico, Estándar, Premium o Lujo");
        }
    }
    
    /**
     * Verifica si tiene cinturones de seguridad.
     * 
     * @return true si tiene cinturones para todos
     */
    public boolean isTieneCinturones() {
        return tieneCinturones;
    }
    
    /**
     * Establece si tiene cinturones de seguridad.
     * 
     * @param tieneCinturones si tiene cinturones
     */
    public void setTieneCinturones(boolean tieneCinturones) {
        this.tieneCinturones = tieneCinturones;
    }
    
    /**
     * Verifica si tiene sistema de entretenimiento.
     * 
     * @return true si tiene entretenimiento
     */
    public boolean isTieneEntretenimiento() {
        return tieneEntretenimiento;
    }
    
    /**
     * Establece si tiene sistema de entretenimiento.
     * 
     * @param tieneEntretenimiento si tiene entretenimiento
     */
    public void setTieneEntretenimiento(boolean tieneEntretenimiento) {
        this.tieneEntretenimiento = tieneEntretenimiento;
    }
    
    /**
     * Obtiene el número de puertas.
     * 
     * @return número de puertas
     */
    public int getNumeroPuertas() {
        return numeroPuertas;
    }
    
    /**
     * Establece el número de puertas.
     * 
     * @param numeroPuertas número de puertas
     * @throws ValidationException si el número es inválido
     */
    public void setNumeroPuertas(int numeroPuertas) throws ValidationException {
        if (numeroPuertas < 2 || numeroPuertas > 6) {
            throw new ValidationException("número de puertas", String.valueOf(numeroPuertas), 
                "Debe estar entre 2 y 6");
        }
        
        this.numeroPuertas = numeroPuertas;
    }
    
    /**
     * Verifica si tiene acceso para discapacitados.
     * 
     * @return true si tiene acceso para discapacitados
     */
    public boolean isTieneAccesoDiscapacitados() {
        return tieneAccesoDiscapacitados;
    }
    
    /**
     * Establece si tiene acceso para discapacitados.
     * 
     * @param tieneAccesoDiscapacitados si tiene acceso para discapacitados
     */
    public void setTieneAccesoDiscapacitados(boolean tieneAccesoDiscapacitados) {
        this.tieneAccesoDiscapacitados = tieneAccesoDiscapacitados;
    }
    
    /**
     * Obtiene el tipo de combustible.
     * 
     * @return tipo de combustible
     */
    public String getTipoCombustible() {
        return tipoCombustible;
    }
    
    /**
     * Establece el tipo de combustible.
     * 
     * @param tipoCombustible tipo de combustible
     * @throws ValidationException si el tipo es inválido
     */
    public void setTipoCombustible(String tipoCombustible) throws ValidationException {
        if (tipoCombustible == null || tipoCombustible.trim().isEmpty()) {
            this.tipoCombustible = "Gasolina";
            return;
        }
        
        String tipoLimpio = tipoCombustible.trim();
        String[] tiposPermitidos = {"Gasolina", "Diesel", "Eléctrico", "Híbrido", "GLP", "GNV"};
        
        boolean tipoValido = false;
        for (String tipo : tiposPermitidos) {
            if (tipo.equalsIgnoreCase(tipoLimpio)) {
                this.tipoCombustible = tipo;
                tipoValido = true;
                break;
            }
        }
        
        if (!tipoValido) {
            throw ValidationException.formatoInvalido("tipo de combustible", tipoLimpio, 
                "Gasolina, Diesel, Eléctrico, Híbrido, GLP o GNV");
        }
    }
    
    /**
     * Verifica si tiene WiFi.
     * 
     * @return true si tiene WiFi
     */
    public boolean isTieneWifi() {
        return tieneWifi;
    }
    
    /**
     * Establece si tiene WiFi.
     * 
     * @param tieneWifi si tiene WiFi
     */
    public void setTieneWifi(boolean tieneWifi) {
        this.tieneWifi = tieneWifi;
    }
    
    /**
     * Obtiene la capacidad del maletero.
     * 
     * @return capacidad del maletero en litros
     */
    public double getCapacidadMaletero() {
        return capacidadMaletero;
    }
    
    /**
     * Establece la capacidad del maletero.
     * 
     * @param capacidadMaletero capacidad en litros
     * @throws ValidationException si la capacidad es inválida
     */
    public void setCapacidadMaletero(double capacidadMaletero) throws ValidationException {
        if (capacidadMaletero < 0) {
            throw new ValidationException("capacidad maletero", String.valueOf(capacidadMaletero), 
                "No puede ser negativa");
        }
        
        this.capacidadMaletero = capacidadMaletero;
    }
    
    /**
     * Verifica si tiene GPS.
     * 
     * @return true si tiene GPS
     */
    public boolean isTieneGps() {
        return tieneGps;
    }
    
    /**
     * Establece si tiene GPS.
     * 
     * @param tieneGps si tiene GPS
     */
    public void setTieneGps(boolean tieneGps) {
        this.tieneGps = tieneGps;
    }
    
    /**
     * Obtiene el sistema de audio.
     * 
     * @return tipo de sistema de audio
     */
    public String getSistemaAudio() {
        return sistemaAudio;
    }
    
    /**
     * Establece el sistema de audio.
     * 
     * @param sistemaAudio tipo de sistema de audio
     */
    public void setSistemaAudio(String sistemaAudio) {
        this.sistemaAudio = (sistemaAudio == null) ? "Básico" : sistemaAudio.trim();
    }
    
    // ==================== MÉTODOS DE NEGOCIO ESPECÍFICOS ====================
    
    /**
     * Valida si el vehículo puede transportar un número específico de pasajeros.
     * 
     * @param numPasajeros número de pasajeros a transportar
     * @return true si puede transportar los pasajeros
     * @throws VehiculoException si excede la capacidad
     */
    public boolean validarCapacidadPasajeros(int numPasajeros) throws VehiculoException {
        if (numPasajeros <= 0) {
            throw new VehiculoException("El número de pasajeros debe ser mayor que cero");
        }
        
        if (numPasajeros > numeroPasajeros) {
            throw VehiculoException.capacidadExcedida(numPasajeros, numeroPasajeros);
        }
        
        return true;
    }
    
    /**
     * Calcula el nivel de confort basado en las características del vehículo.
     * 
     * @return puntuación de confort (0-100)
     */
    public int calcularNivelConfort() {
        int confort = 50; // Base
        
        // Por nivel de comodidad
        switch (nivelComodidad.toLowerCase()) {
            case "lujo":
                confort += 30;
                break;
            case "premium":
                confort += 20;
                break;
            case "estándar":
                confort += 10;
                break;
            default: // Básico
                break;
        }
        
        // Características adicionales
        if (tieneAireAcondicionado) confort += 10;
        if (tieneEntretenimiento) confort += 8;
        if (tieneWifi) confort += 5;
        if (tieneGps) confort += 3;
        if (tieneCinturones) confort += 5;
        if (tieneAccesoDiscapacitados) confort += 5;
        
        // Penalización por antigüedad
        int antiguedad = calcularAntiguedad();
        if (antiguedad > 10) {
            confort -= 15;
        } else if (antiguedad > 5) {
            confort -= 8;
        }
        
        return Math.max(0, Math.min(100, confort));
    }
    
    /**
     * Verifica si el vehículo es ecológico.
     * 
     * @return true si es ecológico
     */
    public boolean esEcologico() {
        return tipoCombustible.equalsIgnoreCase("Eléctrico") || 
               tipoCombustible.equalsIgnoreCase("Híbrido");
    }
    
    /**
     * Calcula el factor de eficiencia del vehículo basado en sus características.
     * 
     * @return factor de eficiencia (0.8 - 1.3)
     */
    public double calcularFactorEficiencia() {
        double factor = 1.0;
        
        // Bonificación por nivel de comodidad
        switch (nivelComodidad.toLowerCase()) {
            case "lujo":
                factor += 0.25;
                break;
            case "premium":
                factor += 0.15;
                break;
            case "estándar":
                factor += 0.05;
                break;
        }
        
        // Bonificación por características especiales
        if (tieneAireAcondicionado) factor += 0.05;
        if (tieneEntretenimiento) factor += 0.05;
        if (tieneWifi) factor += 0.03;
        if (tieneAccesoDiscapacitados) factor += 0.08;
        
        // Bonificación por combustible ecológico
        if (esEcologico()) {
            factor += 0.10;
        }
        
        // Penalización por antigüedad
        int antiguedad = calcularAntiguedad();
        if (antiguedad > 8) {
            factor -= 0.15;
        } else if (antiguedad > 4) {
            factor -= 0.08;
        }
        
        return Math.max(0.8, Math.min(1.3, factor));
    }
    
    /**
     * Calcula el costo adicional por comodidades especiales.
     * 
     * @param tarifaBase tarifa base del viaje
     * @return costo adicional
     */
    public double calcularCostoAdicional(double tarifaBase) {
        double costoAdicional = 0.0;
        
        // Costo por nivel de comodidad
        switch (nivelComodidad.toLowerCase()) {
            case "lujo":
                costoAdicional += tarifaBase * 0.40; // 40% adicional
                break;
            case "premium":
                costoAdicional += tarifaBase * 0.20; // 20% adicional
                break;
            case "estándar":
                costoAdicional += tarifaBase * 0.05; // 5% adicional
                break;
        }
        
        // Costos por características especiales
        if (tieneWifi) {
            costoAdicional += tarifaBase * 0.05;
        }
        
        if (tieneEntretenimiento) {
            costoAdicional += tarifaBase * 0.08;
        }
        
        if (tieneAccesoDiscapacitados) {
            costoAdicional += tarifaBase * 0.03; // Pequeño costo por accesibilidad
        }
        
        return costoAdicional;
    }
    
    /**
     * Genera recomendaciones de mejora para el vehículo.
     * 
     * @return lista de recomendaciones
     */
    public String generarRecomendaciones() {
        StringBuilder recomendaciones = new StringBuilder();
        
        if (!tieneAireAcondicionado && numeroPasajeros > 2) {
            recomendaciones.append("• Considerar instalar aire acondicionado\n");
        }
        
        if (!tieneGps) {
            recomendaciones.append("• Instalar sistema GPS para mejor navegación\n");
        }
        
        if (!tieneCinturones) {
            recomendaciones.append("• URGENTE: Instalar cinturones de seguridad\n");
        }
        
        if (calcularAntiguedad() > 8) {
            recomendaciones.append("• Considerar renovación del vehículo por antigüedad\n");
        }
        
        if (nivelComodidad.equalsIgnoreCase("Básico") && numeroPasajeros > 10) {
            recomendaciones.append("• Mejorar nivel de comodidad para servicio de grupos\n");
        }
        
        if (!esEcologico() && calcularAntiguedad() > 5) {
            recomendaciones.append("• Considerar migrar a combustible más ecológico\n");
        }
        
        if (recomendaciones.length() == 0) {
            recomendaciones.append("✅ Vehículo en excelentes condiciones");
        }
        
        return recomendaciones.toString();
    }
    
    // ==================== IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS ====================
    
    /**
     * Calcula la tarifa base específica para vehículos de pasajeros.
     * Considera número de pasajeros, nivel de comodidad y características especiales.
     * 
     * @return tarifa base por kilómetro
     */
    @Override
    public double calcularTarifaBase() {
        // Tarifa base del tipo de vehículo
        double tarifaBase = getTipoVehiculo().getTarifaBase();
        
        // Factor por número de pasajeros
        double factorPasajeros = 1.0 + (numeroPasajeros / 10.0) * 0.1; // 10% adicional por cada 10 pasajeros
        
        // Factor de eficiencia del vehículo
        double factorEficiencia = calcularFactorEficiencia();
        
        // Calcular tarifa final
        double tarifaFinal = tarifaBase * factorPasajeros * factorEficiencia;
        
        return Math.round(tarifaFinal * 100.0) / 100.0; // Redondear a 2 decimales
    }
    
    /**
     * Obtiene información específica del tipo de vehículo de pasajeros.
     * 
     * @return información específica del vehículo de pasajeros
     */
    @Override
    public String getInformacionTipo() {
        StringBuilder info = new StringBuilder();
        info.append("VEHÍCULO DE PASAJEROS\n");
        info.append("Capacidad: ").append(numeroPasajeros).append(" pasajeros\n");
        info.append("Nivel de comodidad: ").append(nivelComodidad).append("\n");
        info.append("Tipo combustible: ").append(tipoCombustible).append("\n");
        info.append("Número de puertas: ").append(numeroPuertas).append("\n");
        info.append("Capacidad maletero: ").append(String.format("%.0f", capacidadMaletero)).append(" L\n");
        
        info.append("\nCaracterísticas:\n");
        if (tieneAireAcondicionado) info.append("✓ Aire acondicionado\n");
        if (tieneCinturones) info.append("✓ Cinturones de seguridad\n");
        if (tieneEntretenimiento) info.append("✓ Sistema de entretenimiento\n");
        if (tieneWifi) info.append("✓ WiFi\n");
        if (tieneGps) info.append("✓ GPS\n");
        if (tieneAccesoDiscapacitados) info.append("✓ Acceso para discapacitados\n");
        
        info.append("\nNivel de confort: ").append(calcularNivelConfort()).append("/100");
        
        return info.toString();
    }
    
    /**
     * Muestra información completa y detallada del vehículo de pasajeros.
     * 
     * @return información detallada del vehículo
     */
    @Override
    public String mostrarInformacion() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder info = new StringBuilder();
        
        info.append("=== INFORMACIÓN DEL VEHÍCULO DE PASAJEROS ===\n");
        info.append("Placa: ").append(getPlaca()).append("\n");
        info.append("Marca: ").append(getMarca()).append(" ").append(getModelo()).append("\n");
        info.append("Año: ").append(getAno()).append(" (").append(calcularAntiguedad()).append(" años)\n");
        info.append("Color: ").append(getColor()).append("\n");
        info.append("Kilometraje: ").append(String.format("%.0f", getKilometraje())).append(" km\n");
        
        info.append("\n--- ESPECIFICACIONES DE PASAJEROS ---\n");
        info.append("Capacidad máxima: ").append(numeroPasajeros).append(" pasajeros\n");
        info.append("Nivel de comodidad: ").append(nivelComodidad).append("\n");
        info.append("Número de puertas: ").append(numeroPuertas).append("\n");
        info.append("Capacidad maletero: ").append(String.format("%.0f", capacidadMaletero)).append(" litros\n");
        info.append("Tipo de combustible: ").append(tipoCombustible);
        if (esEcologico()) {
            info.append(" (Ecológico)");
        }
        info.append("\n");
        
        info.append("\n--- COMODIDADES Y EQUIPAMIENTO ---\n");
        info.append("Aire acondicionado: ").append(tieneAireAcondicionado ? "Sí" : "No").append("\n");
        info.append("Cinturones de seguridad: ").append(tieneCinturones ? "Sí" : "No").append("\n");
        info.append("Sistema entretenimiento: ").append(tieneEntretenimiento ? "Sí" : "No").append("\n");
        info.append("WiFi disponible: ").append(tieneWifi ? "Sí" : "No").append("\n");
        info.append("GPS incorporado: ").append(tieneGps ? "Sí" : "No").append("\n");
        info.append("Acceso discapacitados: ").append(tieneAccesoDiscapacitados ? "Sí" : "No").append("\n");
        info.append("Sistema de audio: ").append(sistemaAudio).append("\n");
        
        info.append("\n--- INFORMACIÓN TÉCNICA ---\n");
        info.append("Tarifa base: $").append(String.format("%.0f", calcularTarifaBase())).append("/km\n");
        info.append("Factor eficiencia: ").append(String.format("%.2f", calcularFactorEficiencia())).append("\n");
        info.append("Nivel de confort: ").append(calcularNivelConfort()).append("/100\n");
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
        
        info.append("\n--- RECOMENDACIONES ---\n");
        info.append(generarRecomendaciones());
        
        return info.toString();
    }
    
    // ==================== MÉTODOS SOBRESCRITOS ====================
    
    /**
     * Representación en cadena del vehículo de pasajeros.
     * 
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return String.format("VehiculoPasajeros{placa='%s', marca='%s %s', pasajeros=%d, comodidad='%s', disponible=%s}", 
                           getPlaca(), getMarca(), getModelo(), numeroPasajeros, nivelComodidad, isDisponible());
    }
}