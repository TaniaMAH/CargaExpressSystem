package com.cargaexpress.view.frames;

import com.cargaexpress.controller.SistemaTransporte;
import com.cargaexpress.exceptions.ClienteException;
import com.cargaexpress.exceptions.ValidationException;
import com.cargaexpress.model.entities.Cliente;
import com.cargaexpress.model.enums.TipoCliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Ventana interna para gestión de clientes.
 * Permite registrar, consultar, modificar y eliminar clientes del sistema.
 * 
 * @author Sistema Carga Express
 * @version 1.0
 * @since 2024
 */
public class ClienteFrame extends JInternalFrame {
    
    private SistemaTransporte sistema;
    private DefaultTableModel tableModel;
    private JTable clientesTable;
    
    // Campos del formulario
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtDireccion;
    private JTextField txtEmpresa;
    private JComboBox<TipoCliente> cmbTipoCliente;
    private JCheckBox chkActivo;
    private JSpinner spnViajes;
    
    /**
     * Constructor de la ventana de clientes.
     * 
     * @param sistema instancia del sistema de transporte
     */
    public ClienteFrame(SistemaTransporte sistema) {
        super("Gestión de Clientes", true, true, true, true);
        this.sistema = sistema;
        initializeComponents();
        cargarDatos();
        setSize(900, 650);
        setLocation(50, 50);
    }
    
    /**
     * Inicializa todos los componentes de la interfaz.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Pestaña de lista de clientes
        tabbedPane.addTab("📋 Lista de Clientes", crearPanelLista());
        
        // Pestaña de registro/edición
        tabbedPane.addTab("✏️ Registrar Cliente", crearPanelFormulario());
        
        // Pestaña de estadísticas
        tabbedPane.addTab("📊 Estadísticas", crearPanelEstadisticas());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Panel de botones
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }
    
    /**
     * Crea el panel con la lista de clientes.
     * 
     * @return panel configurado
     */
    private JPanel crearPanelLista() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Crear tabla
        String[] columnas = {"Cédula", "Nombre", "Apellido", "Email", "Teléfono", "Tipo", "Viajes", "Estado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) return Integer.class; // Viajes como número
                return String.class;
            }
        };
        
        clientesTable = new JTable(tableModel);
        clientesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clientesTable.setRowHeight(25);
        clientesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarClienteSeleccionado();
            }
        });
        
        // Configurar anchos de columnas
        clientesTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Cédula
        clientesTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Nombre
        clientesTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Apellido
        clientesTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Email
        clientesTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Teléfono
        clientesTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Tipo
        clientesTable.getColumnModel().getColumn(6).setPreferredWidth(60);  // Viajes
        clientesTable.getColumnModel().getColumn(7).setPreferredWidth(80);  // Estado
        
        JScrollPane scrollPane = new JScrollPane(clientesTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("🔍 Búsqueda"));
        
        panelBusqueda.add(new JLabel("Buscar por:"));
        JComboBox<String> cmbCriterioBusqueda = new JComboBox<>(new String[]{"Cédula", "Nombre", "Email"});
        panelBusqueda.add(cmbCriterioBusqueda);
        
        JTextField txtBuscar = new JTextField(15);
        txtBuscar.addActionListener(e -> buscarCliente(txtBuscar.getText(), (String) cmbCriterioBusqueda.getSelectedItem()));
        panelBusqueda.add(txtBuscar);
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarCliente(txtBuscar.getText(), (String) cmbCriterioBusqueda.getSelectedItem()));
        panelBusqueda.add(btnBuscar);
        
        JButton btnMostrarTodos = new JButton("📋 Mostrar Todos");
        btnMostrarTodos.addActionListener(e -> cargarDatos());
        panelBusqueda.add(btnMostrarTodos);
        
        panel.add(panelBusqueda, BorderLayout.NORTH);
        
        // Panel de información rápida
        JPanel panelInfo = new JPanel(new FlowLayout());
        panelInfo.setBorder(BorderFactory.createTitledBorder("ℹ️ Información"));
        JLabel lblInfo = new JLabel("Total clientes: 0");
        panelInfo.add(lblInfo);
        
        // Actualizar información cuando cambie la tabla
        tableModel.addTableModelListener(e -> {
            lblInfo.setText("Total clientes: " + tableModel.getRowCount());
        });
        
        panel.add(panelInfo, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Crea el panel del formulario de registro.
     * 
     * @return panel configurado
     */
    private JPanel crearPanelFormulario() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("📝 Datos del Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Fila 1
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Cédula: *"), gbc);
        gbc.gridx = 1;
        txtCedula = new JTextField(15);
        txtCedula.setToolTipText("Ingrese la cédula de ciudadanía (8-10 dígitos)");
        panel.add(txtCedula, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Nombre: *"), gbc);
        gbc.gridx = 3;
        txtNombre = new JTextField(15);
        txtNombre.setToolTipText("Ingrese el primer nombre");
        panel.add(txtNombre, gbc);
        
        // Fila 2
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Apellido: *"), gbc);
        gbc.gridx = 1;
        txtApellido = new JTextField(15);
        txtApellido.setToolTipText("Ingrese el apellido");
        panel.add(txtApellido, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Teléfono: *"), gbc);
        gbc.gridx = 3;
        txtTelefono = new JTextField(15);
        txtTelefono.setToolTipText("Ingrese el número de teléfono (ej: 3001234567)");
        panel.add(txtTelefono, gbc);
        
        // Fila 3
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email: *"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtEmail = new JTextField(35);
        txtEmail.setToolTipText("Ingrese el correo electrónico (ej: usuario@email.com)");
        panel.add(txtEmail, gbc);
        gbc.gridwidth = 1;
        
        // Fila 4
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtDireccion = new JTextField(35);
        txtDireccion.setToolTipText("Ingrese la dirección completa (opcional)");
        panel.add(txtDireccion, gbc);
        gbc.gridwidth = 1;
        
        // Fila 5
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Empresa:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtEmpresa = new JTextField(35);
        txtEmpresa.setToolTipText("Ingrese el nombre de la empresa (para clientes corporativos)");
        panel.add(txtEmpresa, gbc);
        gbc.gridwidth = 1;
        
        // Fila 6
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Tipo Cliente:"), gbc);
        gbc.gridx = 1;
        cmbTipoCliente = new JComboBox<>(TipoCliente.values());
        cmbTipoCliente.setToolTipText("Seleccione el tipo de cliente");
        panel.add(cmbTipoCliente, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Viajes Realizados:"), gbc);
        gbc.gridx = 3;
        spnViajes = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        spnViajes.setToolTipText("Número de viajes realizados por el cliente");
        panel.add(spnViajes, gbc);
        
        // Fila 7
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        chkActivo = new JCheckBox("Cliente Activo");
        chkActivo.setSelected(true);
        chkActivo.setToolTipText("Marque si el cliente está activo en el sistema");
        panel.add(chkActivo, gbc);
        
        mainPanel.add(panel, BorderLayout.CENTER);
        
        // Panel de nota
        JPanel panelNota = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNota.add(new JLabel("* Campos obligatorios"));
        mainPanel.add(panelNota, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Crea el panel de estadísticas de clientes.
     * 
     * @return panel configurado
     */
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de tipos de cliente
        JPanel panelTipos = new JPanel(new BorderLayout());
        panelTipos.setBorder(BorderFactory.createTitledBorder("📊 Clientes por Tipo"));
        JTextArea txtTipos = new JTextArea(8, 20);
        txtTipos.setEditable(false);
        txtTipos.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        panelTipos.add(new JScrollPane(txtTipos), BorderLayout.CENTER);
        
        // Panel de viajes
        JPanel panelViajes = new JPanel(new BorderLayout());
        panelViajes.setBorder(BorderFactory.createTitledBorder("🚗 Estadísticas de Viajes"));
        JTextArea txtViajes = new JTextArea(8, 20);
        txtViajes.setEditable(false);
        txtViajes.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        panelViajes.add(new JScrollPane(txtViajes), BorderLayout.CENTER);
        
        // Panel de actividad
        JPanel panelActividad = new JPanel(new BorderLayout());
        panelActividad.setBorder(BorderFactory.createTitledBorder("📈 Estado de Actividad"));
        JTextArea txtActividad = new JTextArea(8, 20);
        txtActividad.setEditable(false);
        txtActividad.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        panelActividad.add(new JScrollPane(txtActividad), BorderLayout.CENTER);
        
        // Panel de resumen
        JPanel panelResumen = new JPanel(new BorderLayout());
        panelResumen.setBorder(BorderFactory.createTitledBorder("📋 Resumen General"));
        JTextArea txtResumen = new JTextArea(8, 20);
        txtResumen.setEditable(false);
        txtResumen.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        panelResumen.add(new JScrollPane(txtResumen), BorderLayout.CENTER);
        
        // Botón para actualizar estadísticas
        JButton btnActualizarStats = new JButton("🔄 Actualizar Estadísticas");
        btnActualizarStats.addActionListener(e -> {
            actualizarEstadisticas(txtTipos, txtViajes, txtActividad, txtResumen);
        });
        
        panel.add(panelTipos);
        panel.add(panelViajes);
        panel.add(panelActividad);
        panel.add(panelResumen);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        
        JPanel panelBoton = new JPanel(new FlowLayout());
        panelBoton.add(btnActualizarStats);
        mainPanel.add(panelBoton, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Crea el panel de botones.
     * 
     * @return panel configurado
     */
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        
        JButton btnNuevo = new JButton("🆕 Nuevo");
        btnNuevo.setToolTipText("Limpiar formulario para nuevo cliente");
        btnNuevo.addActionListener(e -> limpiarFormulario());
        panel.add(btnNuevo);
        
        JButton btnGuardar = new JButton("💾 Guardar");
        btnGuardar.setToolTipText("Guardar o actualizar cliente");
        btnGuardar.addActionListener(e -> guardarCliente());
        panel.add(btnGuardar);
        
        JButton btnEliminar = new JButton("🗑️ Eliminar");
        btnEliminar.setToolTipText("Eliminar cliente seleccionado");
        btnEliminar.addActionListener(e -> eliminarCliente());
        panel.add(btnEliminar);
        
        JButton btnActualizar = new JButton("🔄 Actualizar Lista");
        btnActualizar.setToolTipText("Recargar lista de clientes");
        btnActualizar.addActionListener(e -> cargarDatos());
        panel.add(btnActualizar);
        
        JButton btnExportar = new JButton("📊 Exportar");
        btnExportar.setToolTipText("Exportar datos de clientes");
        btnExportar.addActionListener(e -> exportarDatos());
        panel.add(btnExportar);
        
        return panel;
    }
    
    /**
     * Carga los datos de clientes en la tabla.
     */
    private void cargarDatos() {
        tableModel.setRowCount(0);
        ArrayList<Cliente> clientes = sistema.getClientes();
        
        for (Cliente cliente : clientes) {
            Object[] fila = {
                cliente.getCedula(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getTipoCliente().getDescripcion(),
                cliente.getViajesRealizados(),
                cliente.isActivo() ? "✅ Activo" : "❌ Inactivo"
            };
            tableModel.addRow(fila);
        }
    }
    
    /**
     * Carga los datos del cliente seleccionado en el formulario.
     */
    private void cargarClienteSeleccionado() {
        int selectedRow = clientesTable.getSelectedRow();
        if (selectedRow >= 0) {
            String cedula = (String) tableModel.getValueAt(selectedRow, 0);
            Cliente cliente = sistema.buscarClientePorCedula(cedula);
            
            if (cliente != null) {
                txtCedula.setText(cliente.getCedula());
                txtNombre.setText(cliente.getNombre());
                txtApellido.setText(cliente.getApellido());
                txtTelefono.setText(cliente.getTelefono());
                txtEmail.setText(cliente.getEmail());
                txtDireccion.setText(cliente.getDireccion());
                txtEmpresa.setText(cliente.getEmpresa());
                cmbTipoCliente.setSelectedItem(cliente.getTipoCliente());
                spnViajes.setValue(cliente.getViajesRealizados());
                chkActivo.setSelected(cliente.isActivo());
            }
        }
    }
    
    /**
     * Guarda o actualiza un cliente.
     */
    private void guardarCliente() {
        try {
            // Validar campos obligatorios
            if (txtCedula.getText().trim().isEmpty() || 
                txtNombre.getText().trim().isEmpty() || 
                txtApellido.getText().trim().isEmpty() ||
                txtTelefono.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this, 
                    "Los campos marcados con (*) son obligatorios", 
                    "❌ Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Buscar si existe el cliente
            Cliente clienteExistente = sistema.buscarClientePorCedula(txtCedula.getText().trim());
            
            if (clienteExistente == null) {
                // Crear nuevo cliente
                Cliente nuevoCliente = new Cliente(
                    txtCedula.getText().trim(),
                    txtNombre.getText().trim(),
                    txtApellido.getText().trim(),
                    txtTelefono.getText().trim(),
                    txtEmail.getText().trim(),
                    LocalDate.now().minusYears(25), // Fecha de nacimiento por defecto
                    txtDireccion.getText().trim(),
                    txtEmpresa.getText().trim()
                );
                
                nuevoCliente.setTipoCliente((TipoCliente) cmbTipoCliente.getSelectedItem());
                nuevoCliente.setViajesRealizados((Integer) spnViajes.getValue());
                nuevoCliente.setActivo(chkActivo.isSelected());
                
                sistema.registrarCliente(nuevoCliente);
                JOptionPane.showMessageDialog(this, 
                    "✅ Cliente registrado exitosamente\n" +
                    "Cédula: " + nuevoCliente.getCedula() + "\n" +
                    "Nombre: " + nuevoCliente.getNombreCompleto(), 
                    "✅ Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Actualizar cliente existente
                clienteExistente.setNombre(txtNombre.getText().trim());
                clienteExistente.setApellido(txtApellido.getText().trim());
                clienteExistente.setTelefono(txtTelefono.getText().trim());
                clienteExistente.setEmail(txtEmail.getText().trim());
                clienteExistente.setDireccion(txtDireccion.getText().trim());
                clienteExistente.setEmpresa(txtEmpresa.getText().trim());
                clienteExistente.setTipoCliente((TipoCliente) cmbTipoCliente.getSelectedItem());
                clienteExistente.setViajesRealizados((Integer) spnViajes.getValue());
                clienteExistente.setActivo(chkActivo.isSelected());
                
                JOptionPane.showMessageDialog(this, 
                    "✅ Cliente actualizado exitosamente", 
                    "✅ Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            
            cargarDatos();
            limpiarFormulario();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "❌ Error guardando cliente:\n" + e.getMessage(), 
                "❌ Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Elimina el cliente seleccionado.
     */
    private void eliminarCliente() {
        int selectedRow = clientesTable.getSelectedRow();
        if (selectedRow >= 0) {
            String cedula = (String) tableModel.getValueAt(selectedRow, 0);
            String nombre = (String) tableModel.getValueAt(selectedRow, 1) + " " + 
                           (String) tableModel.getValueAt(selectedRow, 2);
            
            int opcion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de que desea eliminar el cliente?\n\n" +
                "Cédula: " + cedula + "\n" +
                "Nombre: " + nombre + "\n\n" +
                "⚠️ Esta acción no se puede deshacer.", 
                "🗑️ Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (opcion == JOptionPane.YES_OPTION) {
                // Remover de la lista
                sistema.getClientes().removeIf(c -> c.getCedula().equals(cedula));
                cargarDatos();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this, 
                    "✅ Cliente eliminado exitosamente", 
                    "✅ Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Seleccione un cliente de la lista para eliminar", 
                "⚠️ Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Busca un cliente por criterio específico.
     * 
     * @param criterio criterio de búsqueda
     * @param tipoBusqueda tipo de búsqueda (Cédula, Nombre, Email)
     */
    private void buscarCliente(String criterio, String tipoBusqueda) {
        if (criterio.trim().isEmpty()) {
            cargarDatos();
            return;
        }
        
        tableModel.setRowCount(0);
        ArrayList<Cliente> clientes = sistema.getClientes();
        String criterioBusqueda = criterio.toLowerCase().trim();
        
        for (Cliente cliente : clientes) {
            boolean coincide = false;
            
            switch (tipoBusqueda) {
                case "Cédula":
                    coincide = cliente.getCedula().contains(criterioBusqueda);
                    break;
                case "Nombre":
                    coincide = cliente.getNombreCompleto().toLowerCase().contains(criterioBusqueda);
                    break;
                case "Email":
                    coincide = cliente.getEmail().toLowerCase().contains(criterioBusqueda);
                    break;
            }
            
            if (coincide) {
                Object[] fila = {
                    cliente.getCedula(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getEmail(),
                    cliente.getTelefono(),
                    cliente.getTipoCliente().getDescripcion(),
                    cliente.getViajesRealizados(),
                    cliente.isActivo() ? "✅ Activo" : "❌ Inactivo"
                };
                tableModel.addRow(fila);
            }
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "🔍 No se encontraron clientes que coincidan con el criterio de búsqueda:\n" +
                tipoBusqueda + ": " + criterio, 
                "🔍 Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Actualiza las estadísticas en el panel correspondiente.
     */
    private void actualizarEstadisticas(JTextArea txtTipos, JTextArea txtViajes, 
                                      JTextArea txtActividad, JTextArea txtResumen) {
        ArrayList<Cliente> clientes = sistema.getClientes();
        
        // Estadísticas por tipo
        StringBuilder tiposStats = new StringBuilder();
        tiposStats.append("CLIENTES POR TIPO:\n");
        tiposStats.append("═".repeat(30)).append("\n");
        
        for (TipoCliente tipo : TipoCliente.values()) {
            long count = clientes.stream()
                    .filter(c -> c.getTipoCliente() == tipo)
                    .count();
            tiposStats.append(String.format("%-12s: %3d clientes\n", tipo.getDescripcion(), count));
        }
        txtTipos.setText(tiposStats.toString());
        
        // Estadísticas de viajes
        StringBuilder viajesStats = new StringBuilder();
        viajesStats.append("ESTADÍSTICAS DE VIAJES:\n");
        viajesStats.append("═".repeat(30)).append("\n");
        
        int totalViajes = clientes.stream().mapToInt(Cliente::getViajesRealizados).sum();
        double promedioViajes = clientes.isEmpty() ? 0 : (double) totalViajes / clientes.size();
        long clientesConViajes = clientes.stream().filter(c -> c.getViajesRealizados() > 0).count();
        
        viajesStats.append(String.format("Total viajes realizados: %d\n", totalViajes));
        viajesStats.append(String.format("Promedio por cliente: %.1f\n", promedioViajes));
        viajesStats.append(String.format("Clientes con viajes: %d\n", clientesConViajes));
        viajesStats.append(String.format("Clientes sin viajes: %d\n", clientes.size() - clientesConViajes));
        txtViajes.setText(viajesStats.toString());
        
        // Estado de actividad
        StringBuilder actividadStats = new StringBuilder();
        actividadStats.append("ESTADO DE ACTIVIDAD:\n");
        actividadStats.append("═".repeat(30)).append("\n");
        
        long activos = clientes.stream().filter(Cliente::isActivo).count();
        long inactivos = clientes.size() - activos;
        
        actividadStats.append(String.format("Clientes activos: %d (%.1f%%)\n", 
                activos, clientes.isEmpty() ? 0 : (activos * 100.0 / clientes.size())));
        actividadStats.append(String.format("Clientes inactivos: %d (%.1f%%)\n", 
                inactivos, clientes.isEmpty() ? 0 : (inactivos * 100.0 / clientes.size())));
        txtActividad.setText(actividadStats.toString());
        
        // Resumen general
        StringBuilder resumenStats = new StringBuilder();
        resumenStats.append("RESUMEN GENERAL:\n");
        resumenStats.append("═".repeat(30)).append("\n");
    }
}