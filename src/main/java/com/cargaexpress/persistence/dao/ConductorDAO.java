package com.cargaexpress.persistence.dao;

import com.cargaexpress.exceptions.CargaExpressException;
import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.model.entities.Conductor;
import com.cargaexpress.model.enums.TipoLicencia;
import com.cargaexpress.persistence.file.FileManager;
import com.cargaexpress.utils.Constants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejo de persistencia de conductores.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class ConductorDAO {
    
    private static final String ARCHIVO_CONDUCTORES = Constants.CONDUCTORES_FILE;
    private static final String SEPARADOR = "|";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Guarda todos los conductores en archivo.
     * 
     * @param conductores lista de conductores a guardar
     * @throws CargaExpressException si hay error al guardar
     */
    public static void guardarConductores(List<Conductor> conductores) throws CargaExpressException {
        List<String> lineas = new ArrayList<>();
        
        // Header
        lineas.add("cedula|nombre|apellido|telefono|email|fechaNacimiento|licencia|tipoLicencia|anosExperiencia|salarioBase|disponible|fechaIngreso|vencimientoLicencia|viajesRealizados|calificacion");
        
        // Datos
        for (Conductor conductor : conductores) {
            StringBuilder linea = new StringBuilder();
            linea.append(conductor.getCedula()).append(SEPARADOR);
            linea.append(conductor.getNombre()).append(SEPARADOR);
            linea.append(conductor.getApellido()).append(SEPARADOR);
            linea.append(conductor.getTelefono()).append(SEPARADOR);
            linea.append(conductor.getEmail()).append(SEPARADOR);
            linea.append(conductor.getFechaNacimiento().format(DATE_FORMAT)).append(SEPARADOR);
            linea.append(conductor.getLicencia()).append(SEPARADOR);
            linea.append(conductor.getTipoLicencia().name()).append(SEPARADOR);
            linea.append(conductor.getAnosExperiencia()).append(SEPARADOR);
            linea.append(conductor.getSalarioBase()).append(SEPARADOR);
            linea.append(conductor.isDisponible()).append(SEPARADOR);
            linea.append(conductor.getFechaIngreso().format(DATE_FORMAT)).append(SEPARADOR);
            linea.append(conductor.getVencimientoLicencia().format(DATE_FORMAT)).append(SEPARADOR);
            linea.append(conductor.getViajesRealizados()).append(SEPARADOR);
            linea.append(conductor.getCalificacionPromedio());
            
            lineas.add(linea.toString());
        }
        
        FileManager.guardarArchivo(ARCHIVO_CONDUCTORES, lineas);
    }
    
    /**
     * Lee todos los conductores desde archivo.
     * 
     * @return lista de conductores leídos
     * @throws CargaExpressException si hay error al leer
     */
    public static List<Conductor> leerConductores() throws CargaExpressException {
        List<String> lineas = FileManager.leerArchivo(ARCHIVO_CONDUCTORES);
        List<Conductor> conductores = new ArrayList<>();
        
        // Saltar header si existe
        int inicio = (lineas.size() > 0 && lineas.get(0).contains("cedula|nombre")) ? 1 : 0;
        
        for (int i = inicio; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            if (linea.trim().isEmpty()) continue;
            
            try {
                Conductor conductor = parsearConductor(linea);
                if (conductor != null) {
                    conductores.add(conductor);
                }
            } catch (Exception e) {
                System.err.println("Error parseando conductor en línea " + (i+1) + ": " + e.getMessage());
            }
        }
        
        return conductores;
    }
    
    /**
     * Parsea una línea de texto para crear un objeto Conductor.
     * 
     * @param linea línea de texto con datos del conductor
     * @return objeto Conductor creado
     * @throws ValidationException si hay error en los datos
     */
    private static Conductor parsearConductor(String linea) throws ValidationException {
        String[] campos = linea.split("\\" + SEPARADOR);
        
        if (campos.length < 15) {
            throw new ValidationException("línea conductor", linea, "Número insuficiente de campos");
        }
        
        Conductor conductor = new Conductor();
        
        conductor.setCedula(campos[0]);
        conductor.setNombre(campos[1]);
        conductor.setApellido(campos[2]);
        conductor.setTelefono(campos[3]);
        conductor.setEmail(campos[4]);
        conductor.setFechaNacimiento(LocalDate.parse(campos[5], DATE_FORMAT));
        conductor.setLicencia(campos[6]);
        conductor.setTipoLicencia(TipoLicencia.valueOf(campos[7]));
        conductor.setAnosExperiencia(Integer.parseInt(campos[8]));
        conductor.setSalarioBase(Double.parseDouble(campos[9]));
        conductor.setDisponible(Boolean.parseBoolean(campos[10]));
        conductor.setFechaIngreso(LocalDate.parse(campos[11], DATE_FORMAT));
        conductor.setVencimientoLicencia(LocalDate.parse(campos[12], DATE_FORMAT));
        conductor.setViajesRealizados(Integer.parseInt(campos[13]));
        conductor.setCalificacionPromedio(Double.parseDouble(campos[14]));
        
        return conductor;
    }
}