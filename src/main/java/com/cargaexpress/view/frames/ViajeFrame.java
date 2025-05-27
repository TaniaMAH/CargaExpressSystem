package com.cargaexpress.view.frames;

import com.cargaexpress.controller.SistemaTransporte;
import com.cargaexpress.model.entities.*;
import com.cargaexpress.model.enums.EstadoViaje;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;

/**
 * Ventana para gestión de viajes.
 */
public class ViajeFrame extends JInternalFrame {
    
    private SistemaTransporte sistema;
    private DefaultTableModel tableModel;
    private JTable viajesTable;
    private JTextField txtOrigen, txtDestino;
    private JSpinner spnDistancia;
    private JComboBox<Cliente> cmbCliente;
    private JComboBox<Conductor> cmbConductor;
    private JComboBox<Vehiculo> cmbVehiculo;
    
    public ViajeFrame(SistemaTransporte sistema) {
        super("Gestión de Viajes", true, true, true, true);
        this.sistema = sistema;
        initComponents();
        cargarDatos();
        setSize(900, 600);
        setLocation(150, 50);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Lista", crearPanelLista());
        tabbedPane.addTab("Programar", crearPanelFormulario());
        add(tabbedPane, BorderLayout.CENTER);
        
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelLista() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnas = {"ID", "Origen", "Destino", "Distancia", "Cliente", "Estado", "Tarifa"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        viajesTable = new JTable(tableModel);
        panel.add(new JScrollPane(viajesTable), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Origen:"), gbc);
        gbc.gridx = 1;
        txtOrigen = new JTextField(20);
        panel.add(txtOrigen, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Destino:"), gbc);
        gbc.gridx = 1;
        txtDestino = new JTextField(20);
        panel.add(txtDestino, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Distancia (km):"), gbc);
        gbc.gridx = 1;
        spnDistancia = new JSpinner(new SpinnerNumberModel(10.0, 1.0, 2000.0, 1.0));
        panel.add(spnDistancia, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        cmbCliente = new JComboBox<>();
        panel.add(cmbCliente, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Conductor:"), gbc);
        gbc.gridx = 1;
        cmbConductor = new JComboBox<>();
        panel.add(cmbConductor, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Vehículo:"), gbc);
        gbc.gridx = 1;
        cmbVehiculo = new JComboBox<>();
        panel.add(cmbVehiculo, gbc);
        
        cargarComboBoxes();
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton btnNuevo = new JButton("Nuevo");
        btnNuevo.addActionListener(e -> limpiarFormulario());
        panel.add(btnNuevo);
        
        JButton btnProgramar = new JButton("Programar Viaje");
        btnProgramar.addActionListener(e -> programarViaje());
        panel.add(btnProgramar);
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos());
        panel.add(btnActualizar);
        
        return panel;
    }
    
    private void cargarComboBoxes() {
        // Cargar clientes
        cmbCliente.removeAllItems();
        for (Cliente cliente : sistema.getClientes()) {
            cmbCliente.addItem(cliente);
        }
        
        // Cargar conductores disponibles
        cmbConductor.removeAllItems();
        for (Conductor conductor : sistema.obtenerConductoresDisponibles()) {
            cmbConductor.addItem(conductor);
        }
        
        // Cargar vehículos disponibles
        cmbVehiculo.removeAllItems();
        for (Vehiculo vehiculo : sistema.obtenerVehiculosDisponibles()) {
            cmbVehiculo.addItem(vehiculo);
        }
    }
    
    private void cargarDatos() {
        tableModel.setRowCount(0);
        for (Viaje viaje : sistema.getViajes()) {
            Object[] fila = {
                viaje.getId(),
                viaje.getOrigen(),
                viaje.getDestino(),
                String.format("%.1f km", viaje.getDistancia()),
                viaje.getCliente() != null ? viaje.getCliente().getNombreCompleto() : "N/A",
                viaje.getEstado().getNombre(),
                String.format("$%.0f", viaje.getTarifaTotal())
            };
            tableModel.addRow(fila);
        }
    }
    
    private void programarViaje() {
        try {
            if (txtOrigen.getText().trim().isEmpty() || txtDestino.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Origen y destino son obligatorios");
                return;
            }
            
            Cliente cliente = (Cliente) cmbCliente.getSelectedItem();
            Conductor conductor = (Conductor) cmbConductor.getSelectedItem();
            Vehiculo vehiculo = (Vehiculo) cmbVehiculo.getSelectedItem();
            
            if (cliente == null || conductor == null || vehiculo == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar cliente, conductor y vehículo");
                return;
            }
            
            Viaje nuevoViaje = new Viaje(
                txtOrigen.getText().trim(),
                txtDestino.getText().trim(),
                LocalDateTime.now().plusHours(1), // Una hora después
                (Double) spnDistancia.getValue(),
                cliente,
                conductor,
                vehiculo
            );
            
            sistema.programarViaje(nuevoViaje);
            JOptionPane.showMessageDialog(this, 
                "Viaje programado exitosamente\nTarifa: $" + String.format("%.0f", nuevoViaje.getTarifaTotal()));
            
            cargarDatos();
            cargarComboBoxes(); // Actualizar disponibilidad
            limpiarFormulario();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarFormulario() {
        txtOrigen.setText("");
        txtDestino.setText("");
        spnDistancia.setValue(10.0);
        if (cmbCliente.getItemCount() > 0) cmbCliente.setSelectedIndex(0);
        if (cmbConductor.getItemCount() > 0) cmbConductor.setSelectedIndex(0);
        if (cmbVehiculo.getItemCount() > 0) cmbVehiculo.setSelectedIndex(0);
    }
    
    // Métodos toString para mostrar en ComboBox
    static {
        // Estos métodos ya están definidos en las clases de entidades
    }
}