package com.cargaexpress.persistence.dao;

import com.cargaexpress.exceptions.CargaExpressException;
import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.model.entities.Cliente;
import com.cargaexpress.model.enums.TipoCliente;
import com.cargaexpress.persistence.file.FileManager;
import com.cargaexpress.utils.Constants;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para manejo de persistencia de clientes.
 * Guarda y lee datos de clientes en archivos de texto.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class ClienteDAO {
    
    private static final String ARCHIVO_CLIENTES = Constants.CLIENTES_FILE;
    private static final String SEPARADOR = "|";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Guarda todos los clientes en archivo.
     * 
     * @param clientes lista de clientes a guardar
     * @throws CargaExpressException si hay error al guardar
     */
    public static void guardarClientes(List<Cliente> clientes) throws CargaExpressException {
        List<String> lineas = new ArrayList<>();
        
        // Header
        lineas.add("cedula|nombre|apellido|telefono|email|fechaNacimiento|tipoCliente|fechaRegistro|viajesRealizados|direccion|empresa|activo");
        
        // Datos
        for (Cliente cliente : clientes) {
            StringBuilder linea = new StringBuilder();
            linea.append(cliente.getCedula()).append(SEPARADOR);
            linea.append(cliente.getNombre()).append(SEPARADOR);
            linea.append(cliente.getApellido()).append(SEPARADOR);
            linea.append(cliente.getTelefono()).append(SEPARADOR);
            linea.append(cliente.getEmail()).append(SEPARADOR);
            linea.append(cliente.getFechaNacimiento().format(DATE_FORMAT)).append(SEPARADOR);
            linea.append(cliente.getTipoCliente().name()).append(SEPARADOR);
            linea.append(cliente.getFechaRegistro().format(DATE_FORMAT)).append(SEPARADOR);
            linea.append(cliente.getViajesRealizados()).append(SEPARADOR);
            linea.append(cliente.getDireccion()).append(SEPARADOR);
            linea.append(cliente.getEmpresa()).append(SEPARADOR);
            linea.append(cliente.isActivo());
            
            lineas.add(linea.toString());
        }
        
        FileManager.guardarArchivo(ARCHIVO_CLIENTES, lineas);
    }
    
    /**
     * Lee todos los clientes desde archivo.
     * 
     * @return lista de clientes leídos
     * @throws CargaExpressException si hay error al leer
     */
    public static List<Cliente> leerClientes() throws CargaExpressException {
        List<String> lineas = FileManager.leerArchivo(ARCHIVO_CLIENTES);
        List<Cliente> clientes = new ArrayList<>();
        
        // Saltar header si existe
        int inicio = (lineas.size() > 0 && lineas.get(0).contains("cedula|nombre")) ? 1 : 0;
        
        for (int i = inicio; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            if (linea.trim().isEmpty()) continue;
            
            try {
                Cliente cliente = parsearCliente(linea);
                if (cliente != null) {
                    clientes.add(cliente);
                }
            } catch (Exception e) {
                System.err.println("Error parseando cliente en línea " + (i+1) + ": " + e.getMessage());
            }
        }
        
        return clientes;
    }
    
    /**
     * Parsea una línea de texto para crear un objeto Cliente.
     * 
     * @param linea línea de texto con datos del cliente
     * @return objeto Cliente creado
     * @throws ValidationException si hay error en los datos
     */
    private static Cliente parsearCliente(String linea) throws ValidationException {
        String[] campos = linea.split("\\" + SEPARADOR);
        
        if (campos.length < 12) {
            throw new ValidationException("línea cliente", linea, "Número insuficiente de campos");
        }
        
        Cliente cliente = new Cliente();
        
        cliente.setCedula(campos[0]);
        cliente.setNombre(campos[1]);
        cliente.setApellido(campos[2]);
        cliente.setTelefono(campos[3]);
        cliente.setEmail(campos[4]);
        cliente.setFechaNacimiento(LocalDate.parse(campos[5], DATE_FORMAT));
        cliente.setTipoCliente(TipoCliente.valueOf(campos[6]));
        cliente.setFechaRegistro(LocalDate.parse(campos[7], DATE_FORMAT));
        cliente.setViajesRealizados(Integer.parseInt(campos[8]));
        cliente.setDireccion(campos[9]);
        cliente.setEmpresa(campos[10]);
        cliente.setActivo(Boolean.parseBoolean(campos[11]));
        
        return cliente;
    }
}