package com.cargaexpress.main;

import com.cargaexpress.controller.SistemaTransporte;
import com.cargaexpress.model.entities.*;
import com.cargaexpress.utils.Constants;

/**
 * Clase principal del sistema Carga Express.
 * Punto de entrada de la aplicación.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2024
 */
public class Main {
    
    /**
     * Método principal que inicia la aplicación.
     * 
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(50));
        System.out.println("🚛 SISTEMA CARGA EXPRESS S.A.S");
        System.out.println("=".repeat(50));
        
        try {
            // Inicializar sistema
            SistemaTransporte sistema = new SistemaTransporte();
            
            // TODO: Implementar lógica de inicialización
            System.out.println("✅ Sistema iniciado correctamente");
            
            // TODO: Lanzar interfaz gráfica o menú de consola
            mostrarMenuPrincipal(sistema);
            
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar el sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra el menú principal del sistema (versión consola).
     * 
     * @param sistema instancia del sistema de transporte
     */
    private static void mostrarMenuPrincipal(SistemaTransporte sistema) {
        System.out.println("\n📋 MENÚ PRINCIPAL");
        System.out.println("1. Registrar Cliente");
        System.out.println("2. Registrar Conductor");
        System.out.println("3. Registrar Vehículo");
        System.out.println("4. Programar Viaje");
        System.out.println("5. Ver Reportes");
        System.out.println("0. Salir");
        System.out.println("\n⚠️  Interfaz gráfica en desarrollo...");
    }
}
