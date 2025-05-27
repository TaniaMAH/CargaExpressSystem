package com.cargaexpress.persistence.file;

import com.cargaexpress.exceptions.CargaExpressException;
import com.cargaexpress.utils.Constants;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de archivos para persistencia de datos del sistema.
 * Maneja lectura y escritura de archivos de texto para todas las entidades.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class FileManager {
    
    /**
     * Guarda una lista de líneas en un archivo de texto.
     * 
     * @param nombreArchivo nombre del archivo
     * @param lineas líneas a guardar
     * @throws CargaExpressException si hay error al guardar
     */
    public static void guardarArchivo(String nombreArchivo, List<String> lineas) throws CargaExpressException {
        try {
            // Crear directorio si no existe
            Path dirPath = Paths.get(Constants.DATA_PATH);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            
            // Escribir archivo
            Path filePath = Paths.get(Constants.DATA_PATH + nombreArchivo);
            Files.write(filePath, lineas);
            
        } catch (IOException e) {
            throw new CargaExpressException("Error guardando archivo " + nombreArchivo + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Lee todas las líneas de un archivo de texto.
     * 
     * @param nombreArchivo nombre del archivo
     * @return lista de líneas leídas
     * @throws CargaExpressException si hay error al leer
     */
    public static List<String> leerArchivo(String nombreArchivo) throws CargaExpressException {
        try {
            Path filePath = Paths.get(Constants.DATA_PATH + nombreArchivo);
            
            if (!Files.exists(filePath)) {
                return new ArrayList<>(); // Archivo no existe, retornar lista vacía
            }
            
            return Files.readAllLines(filePath);
            
        } catch (IOException e) {
            throw new CargaExpressException("Error leyendo archivo " + nombreArchivo + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Verifica si un archivo existe.
     * 
     * @param nombreArchivo nombre del archivo
     * @return true si el archivo existe
     */
    public static boolean existeArchivo(String nombreArchivo) {
        Path filePath = Paths.get(Constants.DATA_PATH + nombreArchivo);
        return Files.exists(filePath);
    }
    
    /**
     * Elimina un archivo.
     * 
     * @param nombreArchivo nombre del archivo
     * @return true si se eliminó exitosamente
     */
    public static boolean eliminarArchivo(String nombreArchivo) {
        try {
            Path filePath = Paths.get(Constants.DATA_PATH + nombreArchivo);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Crea una copia de respaldo de un archivo.
     * 
     * @param nombreArchivo nombre del archivo original
     * @return nombre del archivo de respaldo creado
     * @throws CargaExpressException si hay error al crear respaldo
     */
    public static String crearRespaldo(String nombreArchivo) throws CargaExpressException {
        try {
            Path archivoOriginal = Paths.get(Constants.DATA_PATH + nombreArchivo);
            if (!Files.exists(archivoOriginal)) {
                return null;
            }
            
            String nombreRespaldo = nombreArchivo.replace(".txt", "_backup_" + 
                System.currentTimeMillis() + ".txt");
            Path archivoRespaldo = Paths.get(Constants.DATA_PATH + nombreRespaldo);
            
            Files.copy(archivoOriginal, archivoRespaldo);
            return nombreRespaldo;
            
        } catch (IOException e) {
            throw new CargaExpressException("Error creando respaldo: " + e.getMessage(), e);
        }
    }
}