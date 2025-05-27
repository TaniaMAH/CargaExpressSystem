package com.cargaexpress.persistence.dao;

import com.cargaexpress.exceptions.CargaExpressException;
import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.model.entities.*;
import com.cargaexpress.model.enums.TipoVehiculo;
import com.cargaexpress.persistence.file.FileManager;
import com.cargaexpress.utils.Constants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejo de persistencia de vehículos.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class VehiculoDAO {
    
    private static final String ARCHIVO_VEHICULOS = Constants.VEHICULOS_FILE;
    private static final String SEPARADOR = "|";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Guarda todos los vehículos en archivo.
     * 
     * @param vehiculos lista de vehículos a guardar
     * @throws CargaExpressException si hay error al guardar
     */
    public static void guardarVehiculos(List<Vehiculo> vehiculos) throws CargaExpressException {
        List<String> lineas = new ArrayList<>();
        
        // Header
        lineas.add("tipoClase|placa|marca|modelo|año|capacidad|tipoVehiculo|disponible|kilometraje|color|ultimaRevision|vencimientoSoat|estado|datosEspecificos");
        
        // Datos
        for (Vehiculo vehiculo : vehiculos) {
            StringBuilder linea = new StringBuilder();
            
            // Tipo de clase para saber cómo instanciar
            linea.append(vehiculo.getClass().getSimpleName()).append(SEPARADOR);
            linea.append(vehiculo.getPlaca()).append(SEPARADOR);
            linea.append(vehiculo.getMarca()).append(SEPARADOR);
            linea.append(vehiculo.getModelo()).append(SEPARADOR);
            linea.append(vehiculo.getAno()).append(SEPARADOR);
            linea.append(vehiculo.getCapacidad()).append(SEPARADOR);
            linea.append(vehiculo.getTipoVehiculo().name()).append(SEPARADOR);
            linea.append(vehiculo.isDisponible()).append(SEPARADOR);
            linea.append(vehiculo.getKilometraje()).append(SEPARADOR);
            linea.append(vehiculo.getColor()).append(SEPARADOR);
            linea.append(vehiculo.getUltimaRevision() != null ? vehiculo.getUltimaRevision().format(DATE_FORMAT) : "").append(SEPARADOR);
            linea.append(vehiculo.getVencimientoSoat() != null ? vehiculo.getVencimientoSoat().format(DATE_FORMAT) : "").append(SEPARADOR);
            linea.append(vehiculo.getEstado()).append(SEPARADOR);
            
            // Datos específicos según el tipo
            if (vehiculo instanceof VehiculoCarga) {
                VehiculoCarga vc = (VehiculoCarga) vehiculo;
                linea.append(vc.getTipoCarga()).append("~");
                linea.append(vc.getPesoMaximo()).append("~");
                linea.append(vc.isTieneGrua());
            } else if (vehiculo instanceof VehiculoPasajeros) {
                VehiculoPasajeros vp = (VehiculoPasajeros) vehiculo;
                linea.append(vp.getNumeroPasajeros()).append("~");
                linea.append(vp.isTieneAireAcondicionado()).append("~");
                linea.append(vp.getNivelComodidad());
            }
            
            lineas.add(linea.toString());
        }
        
        FileManager.guardarArchivo(ARCHIVO_VEHICULOS, lineas);
    }
    
    /**
     * Lee todos los vehículos desde archivo.
     * 
     * @return lista de vehículos leídos
     * @throws CargaExpressException si hay error al leer
     */
    public static List<Vehiculo> leerVehiculos() throws CargaExpressException {
        List<String> lineas = FileManager.leerArchivo(ARCHIVO_VEHICULOS);
        List<Vehiculo> vehiculos = new ArrayList<>();
        
        // Saltar header si existe
        int inicio = (lineas.size() > 0 && lineas.get(0).contains("tipoClase|placa")) ? 1 : 0;
        
        for (int i = inicio; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            if (linea.trim().isEmpty()) continue;
            
            try {
                Vehiculo vehiculo = parsearVehiculo(linea);
                if (vehiculo != null) {
                    vehiculos.add(vehiculo);
                }
            } catch (Exception e) {
                System.err.println("Error parseando vehículo en línea " + (i+1) + ": " + e.getMessage());
            }
        }
        
        return vehiculos;
    }
    
    /**
     * Parsea una línea de texto para crear un objeto Vehiculo.
     * 
     * @param linea línea de texto con datos del vehículo
     * @return objeto Vehiculo creado
     * @throws ValidationException si hay error en los datos
     */
    private static Vehiculo parsearVehiculo(String linea) throws ValidationException {
        String[] campos = linea.split("\\" + SEPARADOR);
        
        if (campos.length < 14) {
            throw new ValidationException("línea vehículo", linea, "Número insuficiente de campos");
        }
        
        String tipoClase = campos[0];
        Vehiculo vehiculo;
        
        // Crear instancia según el tipo
        if ("VehiculoCarga".equals(tipoClase)) {
            vehiculo = new VehiculoCarga();
            
            // Datos específicos de carga
            if (campos.length > 13 && !campos[13].isEmpty()) {
                String[] datosEspecificos = campos[13].split("~");
                if (datosEspecificos.length >= 3) {
                    VehiculoCarga vc = (VehiculoCarga) vehiculo;
                    vc.setTipoCarga(datosEspecificos[0]);
                    vc.setPesoMaximo(Double.parseDouble(datosEspecificos[1]));
                    vc.setTieneGrua(Boolean.parseBoolean(datosEspecificos[2]));
                }
            }
        } else if ("VehiculoPasajeros".equals(tipoClase)) {
            vehiculo = new VehiculoPasajeros();
            
            // Datos específicos de pasajeros
            if (campos.length > 13 && !campos[13].isEmpty()) {
                String[] datosEspecificos = campos[13].split("~");
                if (datosEspecificos.length >= 3) {
                    VehiculoPasajeros vp = (VehiculoPasajeros) vehiculo;
                    vp.setNumeroPasajeros(Integer.parseInt(datosEspecificos[0]));
                    vp.setTieneAireAcondicionado(Boolean.parseBoolean(datosEspecificos[1]));
                    vp.setNivelComodidad(datosEspecificos[2]);
                }
            }
        } else {
            // Tipo desconocido, crear VehiculoCarga por defecto
            vehiculo = new VehiculoCarga();
        }
        
        // Datos comunes
        vehiculo.setPlaca(campos[1]);
        vehiculo.setMarca(campos[2]);
        vehiculo.setModelo(campos[3]);
        vehiculo.setAno(Integer.parseInt(campos[4]));
        vehiculo.setCapacidad(Double.parseDouble(campos[5]));
        vehiculo.setTipoVehiculo(TipoVehiculo.valueOf(campos[6]));
        vehiculo.setDisponible(Boolean.parseBoolean(campos[7]));
        vehiculo.setKilometraje(Double.parseDouble(campos[8]));
        vehiculo.setColor(campos[9]);
        
        if (!campos[10].isEmpty()) {
            vehiculo.setUltimaRevision(LocalDate.parse(campos[10], DATE_FORMAT));
        }
        if (!campos[11].isEmpty()) {
            vehiculo.setVencimientoSoat(LocalDate.parse(campos[11], DATE_FORMAT));
        }
        
        vehiculo.setEstado(campos[12]);
        
        return vehiculo;
    }
}