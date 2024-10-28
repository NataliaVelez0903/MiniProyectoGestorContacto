/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package miniproject.GUI;

import miniproject.Contacto;
import miniproject.modelo.GestorContacto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import miniproject.modelo.Formulario;

public class VentanaPrincipal extends JFrame {
    private GestorContacto controlador;
    private JTable tablaContactos;
    private DefaultTableModel modeloTabla;
    private Formulario formularioContacto;
    private JTextField CampoBuscar; 
    private JButton botonBuscar; 

    public VentanaPrincipal() {
        controlador = new GestorContacto();
        modeloTabla = new DefaultTableModel(new String[]{"Nombre", "Apellido", "Teléfono", "Correo", "Dirección", "Estado Civil"}, 0);
        tablaContactos = new JTable(modeloTabla) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; 
            }
        };
        formularioContacto = new Formulario(this, true);

        setTitle("Gestor de Contactos");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Hacer menu
        JMenuBar menuBar = new JMenuBar();
        JMenu archivoMenu = new JMenu("Archivo");
        JMenuItem nuevoMenuItem = new JMenuItem("Nuevo");
        JMenuItem guardarMenuItem = new JMenuItem("Guardar");
        JMenuItem salirMenuItem = new JMenuItem("Salir");
        archivoMenu.add(nuevoMenuItem);
        archivoMenu.add(guardarMenuItem);
        archivoMenu.add(salirMenuItem);
        menuBar.add(archivoMenu);
        JMenu ayudaMenu = new JMenu("Ayuda");
        JMenuItem acercaDeMenuItem = new JMenuItem("Acerca de");
        ayudaMenu.add(acercaDeMenuItem);
        menuBar.add(ayudaMenu);
        setJMenuBar(menuBar);

        // Hacer panel para buscar 
        JPanel panelBusqueda = new JPanel();
        CampoBuscar = new JTextField(20); 
        botonBuscar = new JButton("Buscar"); 
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(CampoBuscar);
        panelBusqueda.add(botonBuscar);
        add(panelBusqueda, BorderLayout.NORTH); 

       //tabla
        add(new JScrollPane(tablaContactos), BorderLayout.CENTER);

        //  hacer oanel de botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        // hacer los eventos 
        btnAgregar.addActionListener(e -> {
            formularioContacto.setVisible(true);
            Contacto nuevoContacto = formularioContacto.getContacto();
            if (nuevoContacto != null) {
                if (!controlador.existeContacto(nuevoContacto.getNombre(), nuevoContacto.getTelefono())) {
                    controlador.agregarContacto(nuevoContacto);
                    modeloTabla.addRow(new Object[]{
                        nuevoContacto.getNombre(),
                        nuevoContacto.getApellido(),
                        nuevoContacto.getTelefono(),
                        nuevoContacto.getCorreo(),
                        nuevoContacto.getDireccion(),
                        nuevoContacto.getEstadoCivil()
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Ya existe un contacto con ese nombre o número.");
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaContactos.getSelectedRow();
            if (filaSeleccionada >= 0) {
                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este contacto?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    controlador.eliminarContacto(filaSeleccionada);
                    modeloTabla.removeRow(filaSeleccionada);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un contacto para eliminar.");
            }
        });

        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tablaContactos.getSelectedRow();
            if (filaSeleccionada >= 0) {
                Contacto contacto = controlador.getContactos().get(filaSeleccionada);
                formularioContacto.setContacto(contacto);
                formularioContacto.setVisible(true);
                
                // Después de cerrar el formulario,se verificar si se han realizado cambios
                Contacto contactoEditado = formularioContacto.getContacto();
                if (contactoEditado != null) {
                    // Validar el número y el correo antes de guardar
                    if (validarContacto(contactoEditado)) {
                        controlador.editarContacto(filaSeleccionada, contactoEditado);
                        // Actualizar la tabla con los nuevos datos
                        modeloTabla.setValueAt(contactoEditado.getNombre(), filaSeleccionada, 0);
                        modeloTabla.setValueAt(contactoEditado.getApellido(), filaSeleccionada, 1);
                        modeloTabla.setValueAt(contactoEditado.getTelefono(), filaSeleccionada, 2);
                        modeloTabla.setValueAt(contactoEditado.getCorreo(), filaSeleccionada, 3);
                        modeloTabla.setValueAt(contactoEditado.getDireccion(), filaSeleccionada, 4);
                        modeloTabla.setValueAt(contactoEditado.getEstadoCivil(), filaSeleccionada, 5);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un contacto para editar.");
            }
        });

        tablaContactos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tablaContactos.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    Contacto contacto = controlador.getContactos().get(filaSeleccionada);
                    formularioContacto.setContacto(contacto);
                }
            }
        });

        // Evento para buscar
        botonBuscar.addActionListener(e -> buscarContacto());

        // Atajos de teclado
        nuevoMenuItem.setAccelerator(KeyStroke.getKeyStroke('N'));
        nuevoMenuItem.addActionListener(e -> btnAgregar.doClick());

        guardarMenuItem.setAccelerator(KeyStroke.getKeyStroke('S'));
        guardarMenuItem.addActionListener(e -> btnAgregar.doClick());

        salirMenuItem.addActionListener(e -> System.exit(0));

        // Atajos de teclado para tabla
        tablaContactos.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int filaSeleccionada = tablaContactos.getSelectedRow();
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    // Eliminar el contacto seleccionado
                    if (filaSeleccionada >= 0) {
                        int confirmacion = JOptionPane.showConfirmDialog(VentanaPrincipal.this, "¿Está seguro de eliminar este contacto?", "Confirmar", JOptionPane.YES_NO_OPTION);
                        if (confirmacion == JOptionPane.YES_OPTION) {
                            controlador.eliminarContacto(filaSeleccionada);
                            modeloTabla.removeRow(filaSeleccionada);
                        }
                    } else {
                        JOptionPane.showMessageDialog(VentanaPrincipal.this, "Seleccione un contacto para eliminar.");
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_E) {
                    // Editar contacto
                    if (filaSeleccionada >= 0) {
                        Contacto contacto = controlador.getContactos().get(filaSeleccionada);
                        formularioContacto.setContacto(contacto); 
                        formularioContacto.setVisible(true);

                        // Después de cerrar el formulario, verificar si se han realizado cambios
                        Contacto contactoEditado = formularioContacto.getContacto();
                        if (contactoEditado != null) {
                            // Validar el número y el correo antes de guardar
                            if (validarContacto(contactoEditado)) {
                                controlador.editarContacto(filaSeleccionada, contactoEditado);
                                // Actualizar la tabla con los nuevos datos
                                modeloTabla.setValueAt(contactoEditado.getNombre(), filaSeleccionada, 0);
                                modeloTabla.setValueAt(contactoEditado.getApellido(), filaSeleccionada, 1);
                                modeloTabla.setValueAt(contactoEditado.getTelefono(), filaSeleccionada, 2);
                                modeloTabla.setValueAt(contactoEditado.getCorreo(), filaSeleccionada, 3);
                                modeloTabla.setValueAt(contactoEditado.getDireccion(), filaSeleccionada, 4);
                                modeloTabla.setValueAt(contactoEditado.getEstadoCivil(), filaSeleccionada, 5);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(VentanaPrincipal.this, "Seleccione un contacto para editar.");
                    }
                }
            }
        });

        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buscarContacto() {
        String textoBusqueda = CampoBuscar.getText().toLowerCase();
        modeloTabla.setRowCount(0); 
        for (Contacto contacto : controlador.getContactos()) {
            if (contacto.getNombre().toLowerCase().contains(textoBusqueda) ||
                contacto.getApellido().toLowerCase().contains(textoBusqueda)) {
                modeloTabla.addRow(new Object[]{
                    contacto.getNombre(),
                    contacto.getApellido(),
                    contacto.getTelefono(),
                    contacto.getCorreo(),
                    contacto.getDireccion(),
                    contacto.getEstadoCivil()
                });
            }
        }
    }

    private boolean validarContacto(Contacto contacto) {
        if (contacto.getTelefono().isEmpty() || contacto.getCorreo().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El teléfono y el correo son obligatorios.");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}





