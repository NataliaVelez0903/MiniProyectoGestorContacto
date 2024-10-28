/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package miniproject.modelo;

import java.util.ArrayList;
import java.util.List;
import miniproject.Contacto;

/**
 *
 * @author USUARIO
 */
public class GestorContacto {
    private List<Contacto> contactos;

    public GestorContacto() {
        contactos = new ArrayList<>();
    }

    public void agregarContacto(Contacto contacto) {
        contactos.add(contacto);
    }

    public void eliminarContacto(int index) {
        if (index >= 0 && index < contactos.size()) {
            contactos.remove(index);
        }
    }

    public void editarContacto(int index, Contacto contacto) {
        if (index >= 0 && index < contactos.size()) {
            contactos.set(index, contacto);
        }
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public boolean existeContacto(String nombre, String telefono) {
        for (Contacto contacto : contactos) {
            if (contacto.getNombre().equalsIgnoreCase(nombre) || contacto.getTelefono().equals(telefono)) {
                return true;
            }
        }
        return false;
    }
}
