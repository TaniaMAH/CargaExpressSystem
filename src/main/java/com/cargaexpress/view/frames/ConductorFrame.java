package com.cargaexpress.view.frames;

import com.cargaexpress.controller.SistemaTransporte;
import com.cargaexpress.model.entities.Conductor;
import com.cargaexpress.model.enums.TipoLicencia;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

/**
 * Ventana para gestión de conductores.
 */
public class ConductorFrame extends JInternalFrame {
    
    private SistemaTransporte sistema;
    private DefaultTableModel tableModel;
    private JTable conductoresTable;
    private JTextField txtCedula, txtNombre, txtApellido, txtTelefono, txtEmail, txtLicencia;
    private JComboBox<TipoLicencia> cmbTipoLicencia;
    private JSpinner spnExperiencia;
    
    public ConductorFrame(SistemaTransporte sistema) {
        super("Gestión de Conductores", true, true, true, true);
        this.sistema = sistema;
        initComponents();
        cargarDatos();
        setSize(900, 600);
        setLocation(75, 75);
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
        
        String[] columnas = {"Cédula", "Nombre", "Licencia", "Tipo Lic.", "Experiencia", "Disponible"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        conductoresTable = new JTable(tableModel);
        conductoresTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conductoresTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarConductorSeleccionado();
        });
        
        panel.add(new JScrollPane(conductoresTable), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Cédula:"), gbc);
        gbc.gridx = 1;
        txtCedula = new JTextField(15);
        panel.add(txtCedula, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(15);
        panel.add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        txtApellido = new JTextField(15);
        panel.add(txtApellido, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(15);
        panel.add(txtTelefono, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        panel.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Licencia:"), gbc);
        gbc.gridx = 1;
        txtLicencia = new JTextField(15);
        panel.add(txtLicencia, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Tipo Licencia:"), gbc);
        gbc.gridx = 1;
        cmbTipoLicencia = new JComboBox<>(TipoLicencia.values());
        panel.add(cmbTipoLicencia, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Años Experiencia:"), gbc);
        gbc.gridx = 1;
        spnExperiencia = new JSpinner(new SpinnerNumberModel(1, 0, 30, 1));
        panel.add(spnExperiencia, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton btnNuevo = new JButton("Nuevo");
        btnNuevo.addActionListener(e -> limpiarFormulario());
        panel.add(btnNuevo);
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarConductor());
        panel.add(btnGuardar);
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos());
        panel.add(btnActualizar);
        
        return panel;
    }
    
    private void cargarDatos() {
        tableModel.setRowCount(0);
        for (Conductor conductor : sistema.getConductores()) {
            Object[] fila = {
                conductor.getCedula(),
                conductor.getNombreCompleto(),
                conductor.getLicencia(),
                conductor.getTipoLicencia().getCodigo(),
                conductor.getAnosExperiencia() + " años",
                conductor.isDisponible() ? "Sí" : "No"
            };
            tableModel.addRow(fila);
        }
    }
    
    private void cargarConductorSeleccionado() {
        int selectedRow = conductoresTable.getSelectedRow();
        if (selectedRow >= 0) {
            String cedula = (String) tableModel.getValueAt(selectedRow, 0);
            Conductor conductor = sistema.buscarConductorPorCedula(cedula);
            
            if (conductor != null) {
                txtCedula.setText(conductor.getCedula());
                txtNombre.setText(conductor.getNombre());
                txtApellido.setText(conductor.getApellido());
                txtTelefono.setText(conductor.getTelefono());
                txtEmail.setText(conductor.getEmail());
                txtLicencia.setText(conductor.getLicencia());
                cmbTipoLicencia.setSelectedItem(conductor.getTipoLicencia());
                spnExperiencia.setValue(conductor.getAnosExperiencia());
            }
        }
    }
    
    private void guardarConductor() {
        try {
            if (txtCedula.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La cédula es obligatoria");
                return;
            }
            
            Conductor nuevoConductor = new Conductor(
                txtCedula.getText().trim(),
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtTelefono.getText().trim(),
                txtEmail.getText().trim(),
                LocalDate.now().minusYears(30), // Fecha nacimiento por defecto
                txtLicencia.getText().trim(),
                (TipoLicencia) cmbTipoLicencia.getSelectedItem(),
                (Integer) spnExperiencia.getValue()
            );
            
            sistema.registrarConductor(nuevoConductor);
            JOptionPane.showMessageDialog(this, "Conductor registrado exitosamente");
            cargarDatos();
            limpiarFormulario();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarFormulario() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtLicencia.setText("");
        cmbTipoLicencia.setSelectedIndex(0);
        spnExperiencia.setValue(1);
        conductoresTable.clearSelection();
    }
}