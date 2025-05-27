package com.cargaexpress.view.frames;

import com.cargaexpress.controller.SistemaTransporte;
import com.cargaexpress.model.entities.*;
import com.cargaexpress.model.enums.TipoVehiculo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Ventana para gestión de vehículos.
 */
public class VehiculoFrame extends JInternalFrame {
    
    private SistemaTransporte sistema;
    private DefaultTableModel tableModel;
    private JTable vehiculosTable;
    private JTextField txtPlaca, txtMarca, txtModelo, txtColor;
    private JSpinner spnAno;
    private JComboBox<TipoVehiculo> cmbTipoVehiculo;
    private JComboBox<String> cmbClaseVehiculo;
    
    public VehiculoFrame(SistemaTransporte sistema) {
        super("Gestión de Vehículos", true, true, true, true);
        this.sistema = sistema;
        initComponents();
        cargarDatos();
        setSize(800, 500);
        setLocation(100, 150);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Lista", crearPanelLista());
        tabbedPane.addTab("Registro", crearPanelFormulario());
        add(tabbedPane, BorderLayout.CENTER);
        
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelLista() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnas = {"Placa", "Marca", "Modelo", "Año", "Tipo", "Disponible"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        vehiculosTable = new JTable(tableModel);
        panel.add(new JScrollPane(vehiculosTable), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Placa:"), gbc);
        gbc.gridx = 1;
        txtPlaca = new JTextField(15);
        panel.add(txtPlaca, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1;
        txtMarca = new JTextField(15);
        panel.add(txtMarca, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 1;
        txtModelo = new JTextField(15);
        panel.add(txtModelo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1;
        spnAno = new JSpinner(new SpinnerNumberModel(2020, 1990, 2025, 1));
        panel.add(spnAno, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Color:"), gbc);
        gbc.gridx = 1;
        txtColor = new JTextField(15);
        panel.add(txtColor, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        cmbTipoVehiculo = new JComboBox<>(TipoVehiculo.values());
        panel.add(cmbTipoVehiculo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Clase:"), gbc);
        gbc.gridx = 1;
        cmbClaseVehiculo = new JComboBox<>(new String[]{"Carga", "Pasajeros"});
        panel.add(cmbClaseVehiculo, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton btnNuevo = new JButton("Nuevo");
        btnNuevo.addActionListener(e -> limpiarFormulario());
        panel.add(btnNuevo);
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarVehiculo());
        panel.add(btnGuardar);
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos());
        panel.add(btnActualizar);
        
        return panel;
    }
    
    private void cargarDatos() {
        tableModel.setRowCount(0);
        for (Vehiculo vehiculo : sistema.getVehiculos()) {
            Object[] fila = {
                vehiculo.getPlaca(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getAno(),
                vehiculo.getTipoVehiculo().getNombre(),
                vehiculo.isDisponible() ? "Sí" : "No"
            };
            tableModel.addRow(fila);
        }
    }
    
    private void guardarVehiculo() {
        try {
            if (txtPlaca.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La placa es obligatoria");
                return;
            }
            
            Vehiculo nuevoVehiculo;
            TipoVehiculo tipo = (TipoVehiculo) cmbTipoVehiculo.getSelectedItem();
            String clase = (String) cmbClaseVehiculo.getSelectedItem();
            
            if ("Carga".equals(clase)) {
                nuevoVehiculo = new VehiculoCarga(
                    txtPlaca.getText().trim(),
                    txtMarca.getText().trim(),
                    txtModelo.getText().trim(),
                    (Integer) spnAno.getValue(),
                    5.0, // Capacidad por defecto
                    tipo,
                    "General", // Tipo carga
                    5.0, // Peso máximo
                    false // Grúa
                );
            } else {
                nuevoVehiculo = new VehiculoPasajeros(
                    txtPlaca.getText().trim(),
                    txtMarca.getText().trim(),
                    txtModelo.getText().trim(),
                    (Integer) spnAno.getValue(),
                    4.0, // Capacidad por defecto
                    tipo,
                    4, // Número pasajeros
                    true, // Aire acondicionado
                    "Estándar" // Nivel comodidad
                );
            }
            
            nuevoVehiculo.setColor(txtColor.getText().trim());
            
            sistema.registrarVehiculo(nuevoVehiculo);
            JOptionPane.showMessageDialog(this, "Vehículo registrado exitosamente");
            cargarDatos();
            limpiarFormulario();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarFormulario() {
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtColor.setText("");
        spnAno.setValue(2020);
        cmbTipoVehiculo.setSelectedIndex(0);
        cmbClaseVehiculo.setSelectedIndex(0);
    }
}