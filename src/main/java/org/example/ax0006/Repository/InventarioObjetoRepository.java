package org.example.ax0006.Repository;

import org.example.ax0006.Entity.Horario;
import org.example.ax0006.db.H2;

import java.sql.*;

public class InventarioObjetoRepository {
    private H2 h2;

    public InventarioObjetoRepository(H2 h2) {
        this.h2 = h2;
    }

    public int guardarObjetoEnInventario(int inventarioId, int objetoId) {
        try (Connection conn = h2.getConnection()) {
            String sqlConflicto = """
                SELECT 1
                FROM ObjetoDocumentoInventario odi
                JOIN DocumentoInventarioHorario dih1 ON odi.idInventario = dih1.idDocumentoInventario
                JOIN Horario h1 ON dih1.idHorario = h1.idHorario
                JOIN DocumentoInventarioHorario dih2 ON dih2.idDocumentoInventario = ?
                JOIN Horario h2 ON dih2.idHorario = h2.idHorario
                WHERE odi.idObjeto = ?
                AND (
                    h1.fechaInc < h2.fechaFin AND h1.fechaFin > h2.fechaInc
                    AND h1.horaInc < h2.horaFin AND h1.horaFin > h2.horaInc
                )
            """;

            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlConflicto)) {
                stmtCheck.setInt(1, inventarioId);
                stmtCheck.setInt(2, objetoId);
                try (ResultSet rs = stmtCheck.executeQuery()) {
                    if (rs.next()) {
                        return -1;
                    }
                }
            }

            String sqlInsert = "INSERT INTO ObjetoDocumentoInventario (idInventario, idObjeto) VALUES (?, ?)";
            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                stmtInsert.setInt(1, inventarioId);
                stmtInsert.setInt(2, objetoId);
                stmtInsert.executeUpdate();
                return 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean objetoEnUsoEnRango(int objetoId, Horario hNuevo) {
        String sql = """
            SELECT 1
            FROM ObjetoDocumentoInventario odi
            JOIN DocumentoInventarioHorario dih ON odi.idInventario = dih.idDocumentoInventario
            JOIN Horario h ON dih.idHorario = h.idHorario
            WHERE odi.idObjeto = ?
            AND (
                CAST(h.fechaInc || ' ' || h.horaInc AS TIMESTAMP) < CAST(? || ' ' || ? AS TIMESTAMP)
                AND
                CAST(h.fechaFin || ' ' || h.horaFin AS TIMESTAMP) > CAST(? || ' ' || ? AS TIMESTAMP)
            )
        """;
        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, objetoId);
            stmt.setDate(2, Date.valueOf(hNuevo.getFechaFin()));
            stmt.setTime(3, Time.valueOf(hNuevo.getHoraFin()));
            stmt.setDate(4, Date.valueOf(hNuevo.getFechaInicio()));
            stmt.setTime(5, Time.valueOf(hNuevo.getHoraInicio()));

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}