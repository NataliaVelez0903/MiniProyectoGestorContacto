/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package miniproject.modelo;

import miniproject.Contacto;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Formulario extends JDialog {
    private JTextField CampoNombre, CampoApellido, CampoTelefono, CampoCorreo, CampoDireccion;
    private JRadioButton botonSoltero, botonCasado, botonUnionLibre, botonDivorciado;
    private JButton botonGuardar, botonCancelar;
    private Contacto contacto;
    private boolean editar;

    public Formulario(Frame owner, boolean modal) {
        super(owner, modal);
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(400, 500));
        
        CampoNombre = new JTextField();
        CampoApellido = new JTextField();
        CampoTelefono = new JTextField();
        CampoCorreo = new JTextField();
        CampoDireccion = new JTextField();
        botonSoltero = new JRadioButton("Soltero");
        botonCasado = new JRadioButton("Casado");
        botonUnionLibre = new JRadioButton("Unión Libre");
        botonDivorciado = new JRadioButton("Divorciado");
        ButtonGroup bgEstadoCivil = new ButtonGroup();
        bgEstadoCivil.add(botonSoltero);
        bgEstadoCivil.add(botonCasado);
        bgEstadoCivil.add(botonUnionLibre);
        bgEstadoCivil.add(botonDivorciado);
        botonGuardar = new JButton("Guardar");
        botonCancelar = new JButton("Cancelar");

        // Añadiendo componentes al panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; add(CampoNombre, gbc);
        gbc.gridx = 0; gbc.gridy++; add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1; add(CampoApellido, gbc);
        gbc.gridx = 0; gbc.gridy++; add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; add(CampoTelefono, gbc);
        gbc.gridx = 0; gbc.gridy++; add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1; add(CampoCorreo, gbc);
        gbc.gridx = 0; gbc.gridy++; add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1; add(CampoDireccion, gbc);
        gbc.gridx = 0; gbc.gridy++; add(new JLabel("Estado Civil:"), gbc);
        gbc.gridx = 1; gbc.gridy++; add(botonSoltero, gbc);
        gbc.gridy++; add(botonCasado, gbc);
        gbc.gridy++; add(botonUnionLibre, gbc);
        gbc.gridy++; add(botonDivorciado, gbc);
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; add(panelBotones, gbc);

        // Acciones de botones
        botonCancelar.addActionListener(e -> dispose());
        botonGuardar.addActionListener(e -> {
            if (validarCampos()) {
                guardarContacto();
            }
        });

        // Atajos de teclado
        botonGuardar.setMnemonic(KeyEvent.VK_G);
        botonCancelar.setMnemonic(KeyEvent.VK_C);

        pack(); // Ajusta el tamaño del JDialog al contenido
        setLocationRelativeTo(owner);
    }

    private boolean validarCampos() {
        if (CampoNombre.getText().isEmpty() || CampoTelefono.getText().isEmpty() || CampoCorreo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.");
            return false;
        }
        if (!CampoTelefono.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El número de teléfono debe contener solo dígitos.");
            return false;
        }
        if (!CampoCorreo.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "El correo electrónico no es válido.");
            return false;
        }
        return true;
    }

    private void guardarContacto() {
        String estadoCivil = botonSoltero.isSelected() ? "Soltero" :botonCasado.isSelected() ? "Casado" :botonUnionLibre.isSelected() ? "Unión Libre" : "Divorciado";
        contacto = new Contacto(
            CampoNombre.getText(),
            CampoApellido.getText(),
            CampoTelefono.getText(),
            CampoCorreo.getText(),
            CampoDireccion.getText(),
            estadoCivil,
            null  
        );
        dispose();
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
        CampoNombre.setText(contacto.getNombre());
        CampoApellido.setText(contacto.getApellido());
        CampoTelefono.setText(contacto.getTelefono());
        CampoCorreo.setText(contacto.getCorreo());
        CampoDireccion.setText(contacto.getDireccion());
        editar = true;
    }

    public Contacto getContacto() {
        return contacto;
    }
}
