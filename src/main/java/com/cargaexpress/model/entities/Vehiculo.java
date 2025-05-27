package com.cargaexpress.model.entities;

import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.exceptions.VehiculoException;
import com.cargaexpress.model.enums.TipoVehiculo;
import com.cargaexpress.utils.Constants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Clase abstracta que representa un vehículo en el sistema de transporte.
 * Define la estructura común para todos los tipos de vehículos.
 * Implementa polimorfismo para cálculos específicos de cada tipo.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public abstract class Vehiculo {
    
    // ==================== ATRIBUTOS PROTEGIDOS ====================
    
    /**
     * Placa del vehículo (identificador único)
     */
    protected String placa;
    
    /**
     * Marca del vehículo
     */
    protected String marca;
    
    /**
     * Modelo del vehículo
     */
    protected String modelo;
    
    /**
     * Año de fabricación del vehículo
     */
    protected int ano;
    
    /**
     * Capacidad del vehículo (toneladas para carga, pasajeros para transporte)
     */
    protected double capacidad;
    
    /**
     * Estado de disponibilidad del vehículo
     */
    protected boolean disponible;
    
    /**
     * Kilometraje actual del vehículo
     */
    protected double kilometraje;
    
    /**
     * Tipo de vehículo (enum)
     */
    protected TipoVehiculo tipoVehiculo;
    
    /**
     * Color del vehículo
     */
    protected String color;
    
    /**
     * Fecha de la última revisión técnica
     */
    protected LocalDate ultimaRevision;
    
    /**
     * Fecha de vencimiento del SOAT
     */
    protected LocalDate vencimientoSoat;
    
    /**
     * Estado del vehículo (Excelente, Bueno, Regular, Malo)
     */
    protected String estado;
    
    /**
     * Observaciones sobre el vehículo
     */
    protected String observaciones;
    
    /**
     * Consumo de combustible por kilómetro
     */
    protected double consumoCombustible;
    
    // ==================== PATRONES DE VALIDACIÓN ====================
    
    private static final Pattern PLACA_PATTERN = Pattern.compile(Constants.PLACA_PATTERN);
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor por defecto.
     * Inicializa un vehículo con valores predeterminados.
     */
    public Vehiculo() {
        this.placa = "";
        this.marca = "";
        this.modelo = "";
        this.ano = LocalDate.now().getYear();
        this.capacidad = 0.0;
        this.disponible = true;
        this.kilometraje = 0.0;
        this.tipoVehiculo = TipoVehiculo.AUTOMOVIL;
        this.color = "";
        this.ultimaRevision = LocalDate.now().minusYears(1);
        this.vencimientoSoat = LocalDate.now().plusYears(1);
        this.estado = "Bueno";
        this.observaciones = "";
        this.consumoCombustible = 0.0;
    }
    
    /**
     * Constructor básico con datos principales.
     * 
     * @param placa placa del vehículo
     * @param marca marca del vehículo
     * @param modelo modelo del vehículo
     * @param ano año de fabricación
     * @param capacidad capacidad del vehículo
     * @param tipoVehiculo tipo de vehículo
     * @throws ValidationException si los datos son inválidos
     */
    public Vehiculo(String placa, String marca, String modelo, int ano, 
                    double capacidad, TipoVehiculo tipoVehiculo) throws ValidationException {
        setPlaca(placa);
        setMarca(marca);
        setModelo(modelo);
        setAno(ano);
        setCapacidad(capacidad);
        setTipoVehiculo(tipoVehiculo);
        
        this.disponible = true;
        this.kilometraje = 0.0;
        this.color = "";
        this.ultimaRevision = LocalDate.now().minusYears(1);
        this.vencimientoSoat = LocalDate.now().plusYears(1);
        this.estado = "Bueno";
        this.observaciones = "";
        this.consumoCombustible = 0.0;
    }
    
    /**
     * Constructor completo con todos los datos.
     * 
     * @param placa placa del vehículo
     * @param marca marca del vehículo
     * @param modelo modelo del vehículo
     * @param ano año de fabricación
     * @param capacidad capacidad del vehículo
     * @param tipoVehiculo tipo de vehículo
     * @param color color del vehículo
     * @param ultimaRevision fecha de última revisión
     * @param vencimientoSoat fecha de vencimiento del SOAT
     * @throws ValidationException si los datos son inválidos
     */
    public Vehiculo(String placa, String marca, String modelo, int ano, 
                    double capacidad, TipoVehiculo tipoVehiculo, String color,
                    LocalDate ultimaRevision, LocalDate vencimientoSoat) throws ValidationException {
        this(placa, marca, modelo, ano, capacidad, tipoVehiculo);
        setColor(color);
        setUltimaRevision(ultimaRevision);
        setVencimientoSoat(vencimientoSoat);
    }
    
    // ==================== GETTERS Y SETTERS CON VALIDACIÓN ====================
    
    /**
     * Obtiene la placa del vehículo.
     * 
     * @return placa del vehículo
     */
    public String getPlaca() {
        return placa;
    }
    
    /**
     * Establece la placa del vehículo con validación.
     * 
     * @param placa placa del vehículo
     * @throws ValidationException si la placa es inválida
     */
    public void setPlaca(String placa) throws ValidationException {
        if (placa == null || placa.trim().isEmpty()) {
            throw ValidationException.campoRequerido("placa");
        }
        
        String placaLimpia = placa.trim().toUpperCase();
        if (!PLACA_PATTERN.matcher(placaLimpia).matches()) {
            throw ValidationException.formatoInvalido("placa", placaLimpia, "ABC123 (3 letras + 3 números)");
        }
        
        this.placa = placaLimpia;
    }
    
    /**
     * Obtiene la marca del vehículo.
     * 
     * @return marca del vehículo
     */
    public String getMarca() {
        return marca;
    }
    
    /**
     * Establece la marca del vehículo.
     * 
     * @param marca marca del vehículo
     * @throws ValidationException si la marca es inválida
     */
    public void setMarca(String marca) throws ValidationException {
        if (marca == null || marca.trim().isEmpty()) {
            throw ValidationException.campoRequerido("marca");
        }
        
        String marcaLimpia = marca.trim();
        if (marcaLimpia.length() < 2 || marcaLimpia.length() > 30) {
            throw ValidationException.longitudInvalida("marca", marcaLimpia, 2, 30);
        }
        
        this.marca = marcaLimpia;
    }
    
    /**
     * Obtiene el modelo del vehículo.
     * 
     * @return modelo del vehículo
     */
    public String getModelo() {
        return modelo;
    }
    
    /**
     * Establece el modelo del vehículo.
     * 
     * @param modelo modelo del vehículo
     * @throws ValidationException si el modelo es inválido
     */
    public void setModelo(String modelo) throws ValidationException {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw ValidationException.campoRequerido("modelo");
        }
        
        String modeloLimpio = modelo.trim();
        if (modeloLimpio.length() < 1 || modeloLimpio.length() > 50) {
            throw ValidationException.longitudInvalida("modelo", modeloLimpio, 1, 50);
        }
        
        this.modelo = modeloLimpio;
    }
    
    /**
     * Obtiene el año de fabricación.
     * 
     * @return año de fabricación
     */
    public int getAno() {
        return ano;
    }
    
    /**
     * Establece el año de fabricación con validación.
     * 
     * @param ano año de fabricación
     * @throws ValidationException si el año es inválido
     */
    public void setAno(int ano) throws ValidationException {
        int anoActual = LocalDate.now().getYear();
        
        if (ano < 1950 || ano > anoActual + 1) {
            throw new ValidationException("año", String.valueOf(ano), 
                "Debe estar entre 1950 y " + (anoActual + 1));
        }
        
        this.ano = ano;
    }
    
    /**
     * Obtiene la capacidad del vehículo.
     * 
     * @return capacidad del vehículo
     */
    public double getCapacidad() {
        return capacidad;
    }
    
    /**
     * Establece la capacidad del vehículo.
     * 
     * @param capacidad capacidad del vehículo
     * @throws ValidationException si la capacidad es inválida
     */
    public void setCapacidad(double capacidad) throws ValidationException {
        if (capacidad < 0) {
            throw new ValidationException("capacidad", String.valueOf(capacidad), 
                "No puede ser negativa");
        }
        
        this.capacidad = capacidad;
    }
    
    /**
     * Verifica si el vehículo está disponible.
     * 
     * @return true si está disponible
     */
    public boolean isDisponible() {
        return disponible;
    }
    
    /**
     * Establece la disponibilidad del vehículo.
     * 
     * @param disponible estado de disponibilidad
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    /**
     * Obtiene el kilometraje actual.
     * 
     * @return kilometraje actual
     */
    public double getKilometraje() {
        return kilometraje;
    }
    
    /**
     * Establece el kilometraje del vehículo.
     * 
     * @param kilometraje kilometraje actual
     * @throws ValidationException si el kilometraje es inválido
     */
    public void setKilometraje(double kilometraje) throws ValidationException {
        if (kilometraje < 0) {
            throw new ValidationException("kilometraje", String.valueOf(kilometraje), 
                "No puede ser negativo");
        }
        
        if (kilometraje < this.kilometraje) {
            throw new ValidationException("kilometraje", String.valueOf(kilometraje), 
                "No puede ser menor al kilometraje actual (" + this.kilometraje + ")");
        }
        
        this.kilometraje = kilometraje;
    }
    
    /**
     * Obtiene el tipo de vehículo.
     * 
     * @return tipo de vehículo
     */
    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }
    
    /**
     * Establece el tipo de vehículo.
     * 
     * @param tipoVehiculo tipo de vehículo
     */
    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = (tipoVehiculo == null) ? TipoVehiculo.AUTOMOVIL : tipoVehiculo;
    }
    
    /**
     * Obtiene el color del vehículo.
     * 
     * @return color del vehículo
     */
    public String getColor() {
        return color;
    }
    
    /**
     * Establece el color del vehículo.
     * 
     * @param color color del vehículo
     */
    public void setColor(String color) {
        this.color = (color == null) ? "" : color.trim();
    }
    
    /**
     * Obtiene la fecha de última revisión.
     * 
     * @return fecha de última revisión
     */
    public LocalDate getUltimaRevision() {
        return ultimaRevision;
    }
    
    /**
     * Establece la fecha de última revisión.
     * 
     * @param ultimaRevision fecha de última revisión
     * @throws ValidationException si la fecha es inválida
     */
    public void setUltimaRevision(LocalDate ultimaRevision) throws ValidationException {
        if (ultimaRevision != null && ultimaRevision.isAfter(LocalDate.now())) {
            throw new ValidationException("última revisión", ultimaRevision.toString(), 
                "No puede ser fecha futura");
        }
        
        this.ultimaRevision = ultimaRevision;
    }
    
    /**
     * Obtiene la fecha de vencimiento del SOAT.
     * 
     * @return fecha de vencimiento del SOAT
     */
    public LocalDate getVencimientoSoat() {
        return vencimientoSoat;
    }
    
    /**
     * Establece la fecha de vencimiento del SOAT.
     * 
     * @param vencimientoSoat fecha de vencimiento del SOAT
     * @throws ValidationException si la fecha es inválida
     */
    public void setVencimientoSoat(LocalDate vencimientoSoat) throws ValidationException {
        if (vencimientoSoat != null && vencimientoSoat.isBefore(LocalDate.now())) {
            throw new ValidationException("vencimiento SOAT", vencimientoSoat.toString(), 
                "SOAT vencido");
        }
        
        this.vencimientoSoat = vencimientoSoat;
    }
    
    /**
     * Obtiene el estado del vehículo.
     * 
     * @return estado del vehículo
     */
    public String getEstado() {
        return estado;
    }
    
    /**
     * Establece el estado del vehículo.
     * 
     * @param estado estado del vehículo
     */
    public void setEstado(String estado) {
        this.estado = (estado == null) ? "Bueno" : estado.trim();
    }
    
    /**
     * Obtiene las observaciones.
     * 
     * @return observaciones del vehículo
     */
    public String getObservaciones() {
        return observaciones;
    }
    
    /**
     * Establece las observaciones.
     * 
     * @param observaciones observaciones del vehículo
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = (observaciones == null) ? "" : observaciones.trim();
    }
    
    /**
     * Obtiene el consumo de combustible.
     * 
     * @return consumo de combustible por km
     */
    public double getConsumoCombustible() {
        return consumoCombustible;
    }
    
    /**
     * Establece el consumo de combustible.
     * 
     * @param consumoCombustible consumo de combustible por km
     * @throws ValidationException si el consumo es inválido
     */
    public void setConsumoCombustible(double consumoCombustible) throws ValidationException {
        if (consumoCombustible < 0) {
            throw new ValidationException("consumo combustible", 
                String.valueOf(consumoCombustible), "No puede ser negativo");
        }
        
        this.consumoCombustible = consumoCombustible;
    }
    
    // ==================== MÉTODOS DE NEGOCIO ====================
    
    /**
     * Valida si la placa tiene formato correcto.
     * 
     * @return true si la placa es válida
     */
    public boolean validarPlaca() {
        return placa != null && PLACA_PATTERN.matcher(placa).matches();
    }
    
    /**
     * Verifica si el vehículo está disponible y en condiciones de operar.
     * 
     * @return true si está disponible para usar
     * @throws VehiculoException si no está disponible
     */
    public boolean estaDisponible() throws VehiculoException {
        if (!disponible) {
            throw VehiculoException.vehiculoNoDisponible(placa);
        }
        
        if (!validarDocumentacion()) {
            throw new VehiculoException("Vehículo con documentación vencida o inválida");
        }
        
        return true;
    }
    
    /**
     * Valida que toda la documentación esté vigente.
     * 
     * @return true si la documentación está vigente
     */
    public boolean validarDocumentacion() {
        LocalDate hoy = LocalDate.now();
        
        // SOAT vigente
        if (vencimientoSoat == null || vencimientoSoat.isBefore(hoy)) {
            return false;
        }
        
        // Revisión técnica no debe ser muy antigua (1 año)
        if (ultimaRevision == null || ultimaRevision.isBefore(hoy.minusYears(1))) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Actualiza el kilometraje del vehículo.
     * 
     * @param kmAdicionales kilómetros adicionales recorridos
     * @throws ValidationException si los km son inválidos
     */
    public void actualizarKilometraje(double kmAdicionales) throws ValidationException {
        if (kmAdicionales < 0) {
            throw new ValidationException("kilómetros adicionales", 
                String.valueOf(kmAdicionales), "No puede ser negativo");
        }
        
        this.kilometraje += kmAdicionales;
    }
    
    /**
     * Calcula la antigüedad del vehículo en años.
     * 
     * @return antigüedad en años
     */
    public int calcularAntiguedad() {
        return LocalDate.now().getYear() - ano;
    }
    
    /**
     * Verifica si el vehículo necesita mantenimiento (cada 10,000 km).
     * 
     * @return true si necesita mantenimiento
     */
    public boolean necesitaMantenimiento() {
        return kilometraje > 0 && (kilometraje % 10000) < 500;
    }
    
    /**
     * Verifica si el SOAT está próximo a vencer (menos de 30 días).
     * 
     * @return true si está próximo a vencer
     */
    public boolean soatProximoVencer() {
        if (vencimientoSoat == null) return true;
        
        long diasParaVencer = java.time.temporal.ChronoUnit.DAYS.between(
            LocalDate.now(), vencimientoSoat);
        return diasParaVencer <= 30;
    }
    
    /**
     * Calcula el costo estimado de combustible para una distancia.
     * 
     * @param distanciaKm distancia en kilómetros
     * @param precioCombustible precio por litro de combustible
     * @return costo estimado de combustible
     */
    public double calcularCostoCombustible(double distanciaKm, double precioCombustible) {
        if (consumoCombustible <= 0 || distanciaKm <= 0 || precioCombustible <= 0) {
            return 0.0;
        }
        
        return distanciaKm * consumoCombustible * precioCombustible;
    }
    
    /**
     * Genera alertas sobre el estado del vehículo.
     * 
     * @return cadena con alertas importantes
     */
    public String generarAlertas() {
        StringBuilder alertas = new StringBuilder();
        
        if (!validarDocumentacion()) {
            alertas.append("⚠️ DOCUMENTACIÓN VENCIDA O INVÁLIDA\n");
        } else if (soatProximoVencer()) {
            alertas.append("⚠️ SOAT próximo a vencer (").append(
                java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), vencimientoSoat)
            ).append(" días)\n");
        }
        
        if (necesitaMantenimiento()) {
            alertas.append("🔧 Vehículo necesita mantenimiento (").append(String.format("%.0f", kilometraje)).append(" km)\n");
        }
        
        int antiguedad = calcularAntiguedad();
        if (antiguedad > 15) {
            alertas.append("⚠️ Vehículo antiguo (").append(antiguedad).append(" años)\n");
        }
        
        if (estado.equalsIgnoreCase("Malo") || estado.equalsIgnoreCase("Regular")) {
            alertas.append("⚠️ Estado del vehículo: ").append(estado).append("\n");
        }
        
        if (!disponible) {
            alertas.append("🚫 Vehículo no disponible\n");
        }
        
        if (alertas.length() == 0) {
            alertas.append("✅ Sin alertas - Vehículo en buen estado");
        }
        
        return alertas.toString();
    }
    
    // ==================== MÉTODOS ABSTRACTOS ====================
    
    /**
     * Método abstracto para calcular la tarifa base específica de cada tipo de vehículo.
     * Debe ser implementado por las clases concretas.
     * 
     * @return tarifa base por kilómetro
     */
    public abstract double calcularTarifaBase();
    
    /**
     * Método abstracto para mostrar información específica de cada tipo de vehículo.
     * Debe ser implementado por las clases concretas.
     * 
     * @return información detallada del vehículo
     */
    public abstract String mostrarInformacion();
    
    /**
     * Método abstracto para obtener información específica del tipo de vehículo.
     * 
     * @return información específica del tipo
     */
    public abstract String getInformacionTipo();
    
    // ==================== MÉTODOS SOBRESCRITOS DE OBJECT ====================
    
    /**
     * Compara dos vehículos por su placa (identificador único).
     * 
     * @param obj objeto a comparar
     * @return true si tienen la misma placa
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Vehiculo vehiculo = (Vehiculo) obj;
        return Objects.equals(placa, vehiculo.placa);
    }
    
    /**
     * Genera código hash basado en la placa.
     * 
     * @return código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(placa);
    }
    
    /**
     * Representación en cadena básica del vehículo.
     * 
     * @return representación en cadena
     */
    @Override
    public String toString() {
        return String.format("Vehiculo{placa='%s', marca='%s', modelo='%s', año=%d, tipo='%s', disponible=%s}", 
                           placa, marca, modelo, ano, tipoVehiculo.getNombre(), disponible);
    }
}