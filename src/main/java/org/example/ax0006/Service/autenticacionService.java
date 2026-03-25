/*
 * MARTIN SANMIGUEL
 */


package org.example.ax0006.Service;

import org.example.ax0006.Entity.usuario;
import org.example.ax0006.Repository.usuarioRepository;

public class autenticacionService {

    private usuarioRepository usuarioRepository;

    //NO SE UTILIZAN SINGLETONS, SE TIENEN QUE HACER DE ESTA MANERA Y SER CREADOS EN EL MAIN
    public autenticacionService(usuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // SIGN UP
    public boolean register(String nombre, String contrasena,  String gmail) {
        if (usuarioRepository.buscarPorNombre(nombre) != null) {
            System.out.println("Usuario ya existe");
            return false;
        }
        usuario nuevo = new usuario(nombre, contrasena, gmail);
        usuarioRepository.guardar(nuevo);

        return true;
    }

    // LOGIN
    public boolean login(String nombre, String contrasena, String gmail) {

        usuario u = usuarioRepository.buscarPorNombre(nombre);
        if (u == null) return false;
        return u.getContrasena().equals(contrasena);
    }
}

