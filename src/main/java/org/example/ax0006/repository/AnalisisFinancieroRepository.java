package org.example.ax0006.repository;

import org.example.ax0006.entity.AnalisisFinanciero;
import org.example.ax0006.db.H2;

import java.sql.*;

public class AnalisisFinancieroRepository {

    private H2 h2;

    public AnalisisFinancieroRepository(H2 h2) {
        this.h2 = h2;
    }

    // =========================
    // CREAR PRESUPUESTO
    // =========================
    public int guardar(AnalisisFinanciero af) {

        String sql = """
            INSERT INTO AnalisisFinanciero (presupuesto, gastos, aprobado, precioBoleta)
            VALUES (?, ?, ?, ?)
        """;

        int idGenerado = 0;

        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, af.getPresupuesto());
            stmt.setInt(2, af.getGastos());
            stmt.setBoolean(3, af.isAprobado());
            stmt.setInt(4, af.getPrecioBoleta());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idGenerado;
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public AnalisisFinanciero buscarPorId(int id) {

        String sql = "SELECT * FROM AnalisisFinanciero WHERE idAnalisisF = ?";

        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                AnalisisFinanciero af = new AnalisisFinanciero();
                af.setIdAnalisisF(rs.getInt("idAnalisisF"));
                af.setPresupuesto(rs.getInt("presupuesto"));
                af.setGastos(rs.getInt("gastos"));
                af.setAprobado(rs.getBoolean("aprobado"));
                af.setPrecioBoleta(rs.getInt("precioBoleta"));
                return af;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // =========================
    // ELIMINAR PRESUPUESTO
    // =========================
    public void eliminar(int id) {

        String sql = "DELETE FROM AnalisisFinanciero WHERE idAnalisisF = ?";

        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // ACTUALIZAR GASTOS
    // =========================
    public void actualizarGastos(int id, int gastos) {

        String sql = "UPDATE AnalisisFinanciero SET gastos = ? WHERE idAnalisisF = ?";

        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gastos);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // APROBAR PRESUPUESTO
    // =========================
    public void aprobar(int id) {

        String sql = "UPDATE AnalisisFinanciero SET aprobado = TRUE WHERE idAnalisisF = ?";

        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // PRECIO BOLETA
    // =========================
    public void actualizarPrecioBoleta(int id, int precio) {

        String sql = "UPDATE AnalisisFinanciero SET precioBoleta = ? WHERE idAnalisisF = ?";

        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, precio);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}