package com.cargaexpress.persistence.dao;

import com.cargaexpress.exceptions.CargaExpressException;
import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.model.entities.*;
import com.cargaexpress.model.enums.EstadoViaje;
import com.cargaexpress.persistence.file.FileManager;
import com.cargaexpress.utils.Constants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejo de persistencia de viajes.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class ViajeDAO {
    
    private static final String ARCHIVO_VIAJES = Constants.VIAJES_FILE;
    private static final String SEPARADOR = "|";
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * Guarda todos los viajes en archivo.
     * 
     * @param viajes lista de viajes a guardar
     * @throws CargaExpressException si hay error al guardar
     */
    public static void guardarViajes(List<Viaje> viajes) throws CargaExpressException {
        List<String> lineas = new ArrayList<>();
        
        // Header
        lineas.add("id|origen|destino|fechaViaje|distancia|duracionEstimada|estado|tarifaTotal|esUrgente|esNocturno|costoAdicional|calificacion|observaciones");
        
        // Datos
        for (Viaje viaje : viajes) {
            StringBuilder linea = new StringBuilder();
            linea.append(viaje.getId()).append(SEPARADOR);
            linea.append(viaje.getOrigen()).append(SEPARADOR);
            linea.append(viaje.getDestino()).append(SEPARADOR);
            linea.append(viaje.getFechaViaje().format(DATETIME_FORMAT)).append(SEPARADOR);
            linea.append(viaje.getDistancia()).append(SEPARADOR);
            linea.append(viaje.getDuracionEstimada()).append(SEPARADOR);
            linea.append(viaje.getEstado().name()).append(SEPARADOR);
            linea.append(viaje.getTarifaTotal()).append(SEPARADOR);
            linea.append(viaje.isEsUrgente()).append(SEPARADOR);
            linea.append(viaje.isEsNocturno()).append(SEPARADOR);
            linea.append(viaje.getCostoAdicional()).append(SEPARADOR);
            linea.append(viaje.getCalificacion()).append(SEPARADOR);
            linea.append(viaje.getObservaciones().replace("|", "¦")); // Escapar separador
            
            lineas.add(linea.toString());
        }
        
        FileManager.guardarArchivo(ARCHIVO_VIAJES, lineas);
    }
    
    /**
     * Lee todos los viajes desde archivo.
     * 
     * @return lista de viajes leídos
     * @throws CargaExpressException si hay error al leer
     */
    public static List<Viaje> leerViajes() throws CargaExpressException {
        List<String> lineas = FileManager.leerArchivo(ARCHIVO_VIAJES);
        List<Viaje> viajes = new ArrayList<>();
        
        // Saltar header si existe
        int inicio = (lineas.size() > 0 && lineas.get(0).contains("id|origen")) ? 1 : 0;
        
        for (int i = inicio; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            if (linea.trim().isEmpty()) continue;
            
            try {
                Viaje viaje = parsearViaje(linea);
                if (viaje != null) {
                    viajes.add(viaje);
                }
            } catch (Exception e) {
                System.err.println("Error parseando viaje en línea " + (i+1) + ": " + e.getMessage());
            }
        }
        
        return viajes;
    }
    
    /**
     * Parsea una línea de texto para crear un objeto Viaje.
     * 
     * @param linea línea de texto con datos del viaje
     * @return objeto Viaje creado
     * @throws ValidationException si hay error en los datos
     */
    private static Viaje parsearViaje(String linea) throws ValidationException {
        String[] campos = linea.split("\\" + SEPARADOR);
        
        if (campos.length < 13) {
            throw new ValidationException("línea viaje", linea, "Número insuficiente de campos");
        }
        
        Viaje viaje = new Viaje();
        
        viaje.setId(campos[0]);
        viaje.setOrigen(campos[1]);
        viaje.setDestino(campos[2]);
        viaje.setFechaViaje(LocalDateTime.parse(campos[3], DATETIME_FORMAT));
        viaje.setDistancia(Double.parseDouble(campos[4]));
        viaje.setDuracionEstimada(Integer.parseInt(campos[5]));
        viaje.setEstado(EstadoViaje.valueOf(campos[6]));
        viaje.setTarifaTotal(Double.parseDouble(campos[7]));
        viaje.setEsUrgente(Boolean.parseBoolean(campos[8]));
        // esNocturno se calcula automáticamente
        viaje.setCostoAdicional(Double.parseDouble(campos[10]));
        viaje.setCalificacion(Double.parseDouble(campos[11]));
        viaje.setObservaciones(campos[12].replace("¦", "|")); // Desescapar separador
        
        return viaje;
    }
}