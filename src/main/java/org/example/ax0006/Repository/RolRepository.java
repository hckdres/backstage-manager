package org.example.ax0006.Repository;

import org.example.ax0006.Entity.Rol;
import org.example.ax0006.Entity.TipoObjeto;
import org.example.ax0006.db.H2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolRepository {
    private H2 h2;
    private List<Rol> roles = new ArrayList<>();

    //CONSTRUCTOR

    public RolRepository(H2 h2) {
        this.h2 = h2;
    }

    //INSERTA ROLES A LA BASE SE DATOS CON AYUDA DEL INSERT INTO A Rol:
    public void guardarRol(Rol R) {
        String sql = "INSERT INTO Rol (rol) VALUES (?)";
        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, R.getRol());
            stmt.executeUpdate();
            System.out.println("Rol guardado en BD: " + R.getRol());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //SE HACE LA CONSULTA AL ROL QUE SE RECIBE COMO PARAMETRO A LA BASE DE DATOS.
    public Rol buscarPorRol(String rol) {
        String sql = "SELECT rol, idRol FROM Rol WHERE rol = ?";
        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rol);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Rol(rs.getString("rol"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
