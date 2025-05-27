package com.cargaexpress.view.frames;

import com.cargaexpress.controller.SistemaTransporte;
import com.cargaexpress.model.entities.Empresa;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana para configuración de información de empresa.
 */
public class EmpresaFrame extends JInternalFrame {
    
    private SistemaTransporte sistema;
    private JTextField txtNombre, txtNit, txtDireccion, txtTelefono, txtEmail;
    
    public EmpresaFrame(SistemaTransporte sistema) {
        super("Información de Empresa", true, true, true, true);
        this.sistema = sistema;
        initComponents();
        cargarDatos();
        setSize(500, 400);
        setLocation(100, 100);
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Campos de empresa
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("NIT:"), gbc);
        gbc.gridx = 1;
        txtNit = new JTextField(20);
        add(txtNit, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        txtDireccion = new JTextField(20);
        add(txtDireccion, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(20);
        add(txtTelefono, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        add(txtEmail, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarEmpresa());
        panelBotones.add(btnGuardar);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(panelBotones, gbc);
    }
    
    private void cargarDatos() {
        Empresa empresa = sistema.getEmpresa();
        if (empresa != null) {
            txtNombre.setText(empresa.getNombre());
            txtNit.setText(empresa.getNit());
            txtDireccion.setText(empresa.getDireccion());
            txtTelefono.setText(empresa.getTelefono());
            txtEmail.setText(empresa.getEmail());
        }
    }
    
    private void guardarEmpresa() {
        try {
            Empresa empresa = new Empresa(
                txtNombre.getText().trim(),
                txtNit.getText().trim(),
                txtDireccion.getText().trim(),
                txtTelefono.getText().trim(),
                txtEmail.getText().trim()
            );
            
            sistema.setEmpresa(empresa);
            JOptionPane.showMessageDialog(this, "Empresa actualizada exitosamente");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}