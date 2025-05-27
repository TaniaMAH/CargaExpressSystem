package com.cargaexpress.model.enums;

/**
 * Enumeración que define los posibles estados de un viaje.
 * Permite hacer seguimiento del ciclo de vida de cada viaje.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2024
 */
public enum EstadoViaje {
    
    /**
     * Viaje programado pero no iniciado
     */
    PROGRAMADO("Programado", "El viaje ha sido programado pero no ha iniciado"),
    
    /**
     * Viaje confirmado y listo para iniciar
     */
    CONFIRMADO("Confirmado", "El viaje ha sido confirmado por el conductor"),
    
    /**
     * Viaje en progreso
     */
    EN_CURSO("En Curso", "El viaje está actualmente en progreso"),
    
    /**
     * Viaje completado exitosamente
     */
    COMPLETADO("Completado", "El viaje ha sido completado exitosamente"),
    
    /**
     * Viaje cancelado antes de iniciar
     */
    CANCELADO("Cancelado", "El viaje ha sido cancelado"),
    
    /**
     * Viaje retrasado por circunstancias externas
     */
    RETRASADO("Retrasado", "El viaje presenta retrasos"),
    
    /**
     * Viaje suspendido temporalmente
     */
    SUSPENDIDO("Suspendido", "El viaje ha sido suspendido temporalmente");
    
    private final String nombre;
    private final String descripcion;
    
    /**
     * Constructor del enum EstadoViaje.
     * 
     * @param nombre nombre del estado
     * @param descripcion descripción detallada del estado
     */
    EstadoViaje(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    /**
     * Obtiene el nombre del estado.
     * 
     * @return nombre del estado
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Obtiene la descripción del estado.
     * 
     * @return descripción del estado
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Verifica si el viaje está activo (en curso o confirmado).
     * 
     * @return true si el viaje está activo
     */
    public boolean isActivo() {
        return this == EN_CURSO || this == CONFIRMADO;
    }
    
    /**
     * Verifica si el viaje está finalizado.
     * 
     * @return true si el viaje está finalizado
     */
    public boolean isFinalizado() {
        return this == COMPLETADO || this == CANCELADO;
    }
    
    /**
     * Verifica si el viaje puede ser modificado.
     * 
     * @return true si el viaje puede ser modificado
     */
    public boolean isModificable() {
        return this == PROGRAMADO || this == RETRASADO;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}