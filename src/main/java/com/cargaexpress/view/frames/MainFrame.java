package com.cargaexpress.view.frames;

import com.cargaexpress.controller.SistemaTransporte;
import com.cargaexpress.model.entities.Empresa;
import com.cargaexpress.model.entities.Cliente;
import com.cargaexpress.exceptions.ValidationException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Ventana principal del sistema con interfaz gráfica Swing.
 * Implementa JInternalFrame para manejo de ventanas internas.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2025
 */
public class MainFrame extends JFrame {
    
    private SistemaTransporte sistema;
    private JDesktopPane desktopPane;
    private JLabel statusLabel;
    
    // Ventanas internas
    private EmpresaFrame empresaFrame;
    private ClienteFrame clienteFrame;
    private ConductorFrame conductorFrame;
    private VehiculoFrame vehiculoFrame;
    private ViajeFrame viajeFrame;
    
    /**
     * Constructor de la ventana principal.
     */
    public MainFrame() {
        this.sistema = new SistemaTransporte();
        initializeComponents();
        configurarVentana();
        cargarDatosIniciales();
    }
    
    /**
     * Inicializa todos los componentes de la interfaz.
     */
    private void initializeComponents() {
        // Configurar ventana principal
        setTitle("Sistema Carga Express S.A.S - Gestión de Transporte");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Crear desktop pane para ventanas internas
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(240, 248, 255));
        
        // Crear barra de menú
        setJMenuBar(crearBarraMenu());
        
        // Crear barra de herramientas
        add(crearBarraHerramientas(), BorderLayout.NORTH);
        
        // Agregar desktop pane al centro
        add(desktopPane, BorderLayout.CENTER);
        
        // Crear barra de estado
        statusLabel = new JLabel("Sistema iniciado - Listo para usar");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    /**
     * Crea la barra de menú principal.
     * 
     * @return barra de menú configurada
     */
    private JMenuBar crearBarraMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setMnemonic('A');
        
        JMenuItem itemNuevo = new JMenuItem("Nuevo");
        itemNuevo.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        itemNuevo.addActionListener(e -> limpiarDatos());
        
        JMenuItem itemCargar = new JMenuItem("Cargar Datos");
        itemCargar.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        itemCargar.addActionListener(e -> cargarDatos());
        
        JMenuItem itemGuardar = new JMenuItem("Guardar Datos");
        itemGuardar.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        itemGuardar.addActionListener(e -> guardarDatos());
        
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        itemSalir.addActionListener(e -> System.exit(0));
        
        menuArchivo.add(itemNuevo);
        menuArchivo.addSeparator();
        menuArchivo.add(itemCargar);
        menuArchivo.add(itemGuardar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        
        // Menú Empresa
        JMenu menuEmpresa = new JMenu("Empresa");
        menuEmpresa.setMnemonic('E');
        
        JMenuItem itemEmpresa = new JMenuItem("Información de Empresa");
        itemEmpresa.addActionListener(e -> mostrarVentanaEmpresa());
        menuEmpresa.add(itemEmpresa);
        
        // Menú Gestión
        JMenu menuGestion = new JMenu("Gestión");
        menuGestion.setMnemonic('G');
        
        JMenuItem itemClientes = new JMenuItem("Clientes");
        itemClientes.addActionListener(e -> mostrarVentanaClientes());
        
        JMenuItem itemConductores = new JMenuItem("Conductores");
        itemConductores.addActionListener(e -> mostrarVentanaConductores());
        
        JMenuItem itemVehiculos = new JMenuItem("Vehículos");
        itemVehiculos.addActionListener(e -> mostrarVentanaVehiculos());
        
        JMenuItem itemViajes = new JMenuItem("Viajes");
        itemViajes.addActionListener(e -> mostrarVentanaViajes());
        
        menuGestion.add(itemClientes);
        menuGestion.add(itemConductores);
        menuGestion.add(itemVehiculos);
        menuGestion.addSeparator();
        menuGestion.add(itemViajes);
        
        // Menú Reportes
        JMenu menuReportes = new JMenu("Reportes");
        menuReportes.setMnemonic('R');
        
        JMenuItem itemReporteGeneral = new JMenuItem("Reporte General");
        itemReporteGeneral.addActionListener(e -> mostrarReporteGeneral());
        
        JMenuItem itemEstadisticas = new JMenuItem("Estadísticas");
        itemEstadisticas.addActionListener(e -> mostrarEstadisticas());
        
        menuReportes.add(itemReporteGeneral);
        menuReportes.add(itemEstadisticas);
        
        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda");
        menuAyuda.setMnemonic('H');
        
        JMenuItem itemAcerca = new JMenuItem("Acerca de");
        itemAcerca.addActionListener(e -> mostrarAcercaDe());
        menuAyuda.add(itemAcerca);
        
        // Agregar menús a la barra
        menuBar.add(menuArchivo);
        menuBar.add(menuEmpresa);
        menuBar.add(menuGestion);
        menuBar.add(menuReportes);
        menuBar.add(menuAyuda);
        
        return menuBar;
    }
    
    /**
     * Crea la barra de herramientas.
     * 
     * @return barra de herramientas configurada
     */
    private JToolBar crearBarraHerramientas() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        // Botón Nuevo
        JButton btnNuevo = new JButton("Nuevo");
        btnNuevo.setToolTipText("Limpiar todos los datos");
        btnNuevo.addActionListener(e -> limpiarDatos());
        toolBar.add(btnNuevo);
        
        // Botón Guardar
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setToolTipText("Guardar datos en archivos");
        btnGuardar.addActionListener(e -> guardarDatos());
        toolBar.add(btnGuardar);
        
        toolBar.addSeparator();
        
        // Botón Clientes
        JButton btnClientes = new JButton("Clientes");
        btnClientes.setToolTipText("Gestionar clientes");
        btnClientes.addActionListener(e -> mostrarVentanaClientes());
        toolBar.add(btnClientes);
        
        // Botón Conductores
        JButton btnConductores = new JButton("Conductores");
        btnConductores.setToolTipText("Gestionar conductores");
        btnConductores.addActionListener(e -> mostrarVentanaConductores());
        toolBar.add(btnConductores);
        
        // Botón Vehículos
        JButton btnVehiculos = new JButton("Vehículos");
        btnVehiculos.setToolTipText("Gestionar vehículos");
        btnVehiculos.addActionListener(e -> mostrarVentanaVehiculos());
        toolBar.add(btnVehiculos);
        
        // Botón Viajes
        JButton btnViajes = new JButton("Viajes");
        btnViajes.setToolTipText("Programar viajes");
        btnViajes.addActionListener(e -> mostrarVentanaViajes());
        toolBar.add(btnViajes);
        
        return toolBar;
    }
    
    /**
     * Configura propiedades adicionales de la ventana.
     */
    private void configurarVentana() {
        // Look and Feel moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("No se pudo establecer Look and Feel: " + e.getMessage());
        }
    }
    
    /**
     * Carga datos iniciales del sistema.
     */
    private void cargarDatosIniciales() {
        try {
            sistema.cargarDatos();
            actualizarStatus("Datos cargados exitosamente");
        } catch (Exception e) {
            sistema.cargarDatosPrueba();
            actualizarStatus("Datos de prueba cargados");
        }
    }
    
    // ==================== MÉTODOS DE VENTANAS INTERNAS ====================
    
    /**
     * Muestra la ventana de información de empresa.
     */
    private void mostrarVentanaEmpresa() {
        if (empresaFrame == null || empresaFrame.isClosed()) {
            empresaFrame = new EmpresaFrame(sistema);
            desktopPane.add(empresaFrame);
        }
        empresaFrame.setVisible(true);
        empresaFrame.toFront();
        try {
            empresaFrame.setSelected(true);
        } catch (Exception e) {
            // Ignorar excepción de selección
        }
    }
    
    /**
     * Muestra la ventana de gestión de clientes.
     */
    private void mostrarVentanaClientes() {
        if (clienteFrame == null || clienteFrame.isClosed()) {
            clienteFrame = new ClienteFrame(sistema);
            desktopPane.add(clienteFrame);
        }
        clienteFrame.setVisible(true);
        clienteFrame.toFront();
        try {
            clienteFrame.setSelected(true);
        } catch (Exception e) {
            // Ignorar excepción de selección
        }
    }
    
    /**
     * Muestra la ventana de gestión de conductores.
     */
    private void mostrarVentanaConductores() {
        if (conductorFrame == null || conductorFrame.isClosed()) {
            conductorFrame = new ConductorFrame(sistema);
            desktopPane.add(conductorFrame);
        }
        conductorFrame.setVisible(true);
        conductorFrame.toFront();
        try {
            conductorFrame.setSelected(true);
        } catch (Exception e) {
            // Ignorar excepción de selección
        }
    }
    
    /**
     * Muestra la ventana de gestión de vehículos.
     */
    private void mostrarVentanaVehiculos() {
        if (vehiculoFrame == null || vehiculoFrame.isClosed()) {
            vehiculoFrame = new VehiculoFrame(sistema);
            desktopPane.add(vehiculoFrame);
        }
        vehiculoFrame.setVisible(true);
        vehiculoFrame.toFront();
        try {
            vehiculoFrame.setSelected(true);
        } catch (Exception e) {
            // Ignorar excepción de selección
        }
    }
    
    /**
     * Muestra la ventana de gestión de viajes.
     */
    private void mostrarVentanaViajes() {
        if (viajeFrame == null || viajeFrame.isClosed()) {
            viajeFrame = new ViajeFrame(sistema);
            desktopPane.add(viajeFrame);
        }
        viajeFrame.setVisible(true);
        viajeFrame.toFront();
        try {
            viajeFrame.setSelected(true);
        } catch (Exception e) {
            // Ignorar excepción de selección
        }
    }
    
    // ==================== MÉTODOS DE FUNCIONALIDAD ====================
    
    /**
     * Guarda todos los datos del sistema.
     */
    private void guardarDatos() {
        try {
            sistema.guardarDatos();
            actualizarStatus("Datos guardados exitosamente");
            JOptionPane.showMessageDialog(this, "Datos guardados correctamente", 
                                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            actualizarStatus("Error guardando datos: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error guardando datos: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Carga datos del sistema.
     */
    private void cargarDatos() {
        try {
            sistema.cargarDatos();
            actualizarStatus("Datos cargados exitosamente");
            JOptionPane.showMessageDialog(this, "Datos cargados correctamente", 
                                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            actualizarStatus("Error cargando datos: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpia todos los datos del sistema.
     */
    private void limpiarDatos() {
        int opcion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de que desea limpiar todos los datos?\nEsta acción no se puede deshacer.", 
            "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            try {
                sistema = new SistemaTransporte();
                actualizarStatus("Datos limpiados - Sistema reiniciado");
                JOptionPane.showMessageDialog(this, "Datos limpiados correctamente", 
                                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                actualizarStatus("Error limpiando datos: " + e.getMessage());
            }
        }
    }
    
    /**
     * Muestra el reporte general del sistema.
     */
    private void mostrarReporteGeneral() {
        try {
            String reporte = sistema.generarReporte();
            
            JTextArea textArea = new JTextArea(reporte);
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Reporte General", 
                                        JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generando reporte: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Muestra las estadísticas del sistema.
     */
    private void mostrarEstadisticas() {
        try {
            StringBuilder stats = new StringBuilder();
            stats.append("=== ESTADÍSTICAS DEL SISTEMA ===\n\n");
            
            stats.append("📊 TOTALES:\n");
            stats.append("• Clientes registrados: ").append(sistema.getClientes().size()).append("\n");
            stats.append("• Conductores registrados: ").append(sistema.getConductores().size()).append("\n");
            stats.append("• Vehículos registrados: ").append(sistema.getVehiculos().size()).append("\n");
            stats.append("• Viajes programados: ").append(sistema.getViajes().size()).append("\n\n");
            
            stats.append("🚗 DISPONIBILIDAD:\n");
            stats.append("• Conductores disponibles: ").append(sistema.obtenerConductoresDisponibles().size()).append("\n");
            stats.append("• Vehículos disponibles: ").append(sistema.obtenerVehiculosDisponibles().size()).append("\n\n");
            
            stats.append("💰 FINANCIERO:\n");
            stats.append("• Total facturado: $").append(String.format("%.0f", sistema.calcularTotalIngresos())).append("\n");
            
            JTextArea textArea = new JTextArea(stats.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Estadísticas", 
                                        JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generando estadísticas: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Muestra información sobre la aplicación.
     */
    private void mostrarAcercaDe() {
        String mensaje = "Sistema de Transporte Carga Express S.A.S\n\n" +
                        "Versión: 1.0\n" +
                        "Desarrollado con Java Swing\n\n" +
                        "Funcionalidades:\n" +
                        "• Gestión de clientes y conductores\n" +
                        "• Administración de vehículos\n" +
                        "• Programación de viajes\n" +
                        "• Cálculo automático de tarifas\n" +
                        "• Persistencia en archivos\n" +
                        "• Reportes y estadísticas\n\n" +
                        "© 2024 - Proyecto Académico";
        
        JOptionPane.showMessageDialog(this, mensaje, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Actualiza el mensaje de estado en la barra inferior.
     * 
     * @param mensaje mensaje a mostrar
     */
    private void actualizarStatus(String mensaje) {
        statusLabel.setText(mensaje);
    }
    
    /**
     * Método principal para ejecutar la aplicación.
     * 
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Configurar Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
                
                // Crear y mostrar la ventana principal
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error iniciando la aplicación: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}