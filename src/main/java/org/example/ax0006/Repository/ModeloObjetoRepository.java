package org.example.ax0006.Repository;

import org.example.ax0006.Entity.ModeloObjeto;
import org.example.ax0006.Entity.TipoObjeto;
import org.example.ax0006.db.H2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModeloObjetoRepository {

    private H2 h2;

    public ModeloObjetoRepository(H2 h2) {
        this.h2 = h2;
    }

    // Crear modelo
    public int guardar(String nombre, int idTipoObjeto) {

        String sql = """
            INSERT INTO ModeloObjeto (nombre, idTipoObjeto)
            VALUES (?, ?)
        """;

        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nombre);
            stmt.setInt(2, idTipoObjeto);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // Listar modelos con su tipo
    public List<ModeloObjeto> obtenerPorTipo(int idTipo) {

        List<ModeloObjeto> modelos = new ArrayList<>();

        String sql = "SELECT * FROM ModeloObjeto WHERE idTipoObjeto = ?";

        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTipo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ModeloObjeto m = new ModeloObjeto();
                m.setIdModelo(rs.getInt("idModelo"));
                m.setNombre(rs.getString("nombre"));
                modelos.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return modelos;
    }
}