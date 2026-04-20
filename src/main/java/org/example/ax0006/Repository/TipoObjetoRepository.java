package org.example.ax0006.Repository;

import org.example.ax0006.Entity.TipoObjeto;
import org.example.ax0006.db.H2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoObjetoRepository {

    private H2 h2;

    public TipoObjetoRepository(H2 h2) {
        this.h2 = h2;
    }

    // Crear tipo
    public int guardar(String nombre) {

        String sql = "INSERT INTO TipoObjeto (nombre) VALUES (?)";

        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nombre);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Listar tipos
    public List<TipoObjeto> obtenerTodos() {

        List<TipoObjeto> lista = new ArrayList<>();

        String sql = "SELECT * FROM TipoObjeto";

        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new TipoObjeto(
                        rs.getInt("idTipoObjeto"),
                        rs.getString("nombre")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}