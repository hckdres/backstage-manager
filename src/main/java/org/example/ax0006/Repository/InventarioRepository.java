package org.example.ax0006.Repository;

import org.example.ax0006.db.H2;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioRepository {

    private H2 h2;

    public InventarioRepository(H2 h2) {
        this.h2 = h2;
    }

    public int crearDocumentoInventario(int idConcierto, int idHorario, List<Integer> idsObjetos) {
        String sqlDoc = "INSERT INTO DocumentoInventario DEFAULT VALUES";
        String sqlRelConcierto = "INSERT INTO ConciertoDocumentoInventario (idDocumentoInventario, idConcierto) VALUES (?, ?)";
        String sqlRelHorario = "INSERT INTO DocumentoInventarioHorario (idDocumentoInventario, idHorario) VALUES (?, ?)";
        String sqlRelObjetos = "INSERT INTO ObjetoDocumentoInventario (idInventario, idObjeto) VALUES (?, ?)";

        int idGenerado = -1;

        try (Connection conn = h2.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtDoc = conn.prepareStatement(sqlDoc, Statement.RETURN_GENERATED_KEYS)) {
                stmtDoc.executeUpdate();
                ResultSet rs = stmtDoc.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }

            try (PreparedStatement stmtRelC = conn.prepareStatement(sqlRelConcierto)) {
                stmtRelC.setInt(1, idGenerado);
                stmtRelC.setInt(2, idConcierto);
                stmtRelC.executeUpdate();
            }

            try (PreparedStatement stmtRelH = conn.prepareStatement(sqlRelHorario)) {
                stmtRelH.setInt(1, idGenerado);
                stmtRelH.setInt(2, idHorario);
                stmtRelH.executeUpdate();
            }

            try (PreparedStatement stmtRelO = conn.prepareStatement(sqlRelObjetos)) {
                for (Integer idObjeto : idsObjetos) {
                    stmtRelO.setInt(1, idGenerado);
                    stmtRelO.setInt(2, idObjeto);
                    stmtRelO.addBatch();
                }
                stmtRelO.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idGenerado;
    }

    public List<String> obtenerObjetosPorConcierto(int idConcierto) {
        List<String> lista = new ArrayList<>();
        String sql = """
        SELECT t.tipo, r.referencia 
        FROM ObjetoDocumentoInventario odi
        JOIN ConciertoDocumentoInventario cdi ON odi.idInventario = cdi.idDocumentoInventario
        JOIN Objeto o ON odi.idObjeto = o.idObjeto
        JOIN TipoObjeto t ON o.idTipoObjeto = t.idTipoObjeto
        JOIN ReferenciaDeObjeto r ON o.idReferenciaObjeto = r.idReferenciaObjeto
        WHERE cdi.idConcierto = ?
    """;
        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idConcierto);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("tipo") + " - " + rs.getString("referencia"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}