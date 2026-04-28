package org.example.ax0006.Repository;

import org.example.ax0006.Entity.Concierto;
import org.example.ax0006.Entity.Horario;
import org.example.ax0006.Entity.Usuario;
import org.example.ax0006.db.H2;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

public class ConciertoRepository {

    private H2 h2;

    public ConciertoRepository(H2 h2) {
        this.h2 = h2;
    }

    public int guardar(Concierto c, int idHorario) {
        String sql = """
            INSERT INTO Concierto (nombreConcierto, idHorario, aforo, idContrato, programado)
            VALUES (?, ?, ?, ?, ?)
        """;
        int idConciertoGenerado = 0;
        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, c.getNombreConcierto());
            stmt.setInt(2, idHorario);
            stmt.setInt(3, c.getAforo());
            stmt.setInt(4, c.getIdContrato());
            stmt.setBoolean(5, c.isProgramado());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idConciertoGenerado = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idConciertoGenerado;
    }

    public List<Concierto> obtenerConciertosSolos() {
        List<Concierto> lista = new ArrayList<>();
        String sql = """
            SELECT c.idConcierto, c.nombreConcierto, c.aforo, c.programado, c.idContrato,
                   h.idHorario, h.fechaInc, h.fechaFin, h.horaInc, h.horaFin
            FROM Concierto c
            JOIN Horario h ON c.idHorario = h.idHorario
        """;
        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Horario h = new Horario();
                h.setIdHorario(rs.getInt("idHorario"));
                h.setFechaInicio(rs.getDate("fechaInc").toLocalDate());
                h.setFechaFin(rs.getDate("fechaFin").toLocalDate());
                h.setHoraInicio(rs.getTime("horaInc").toLocalTime());
                h.setHoraFin(rs.getTime("horaFin").toLocalTime());
                Concierto c = new Concierto(
                        rs.getInt("idConcierto"),
                        rs.getString("nombreConcierto"),
                        h,
                        rs.getInt("aforo"),
                        null,
                        rs.getBoolean("programado")
                );
                c.setIdContrato(rs.getInt("idContrato"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Concierto> obtenerConciertos() {
        List<Concierto> lista = new ArrayList<>();
        String sql = """
            SELECT c.idConcierto, c.nombreConcierto, c.aforo, c.programado,
                   h.idHorario, h.fechaInc, h.fechaFin, h.horaInc, h.horaFin,
                   u.idUsuario, u.nombre
            FROM Concierto c
            JOIN Horario h ON c.idHorario = h.idHorario
            LEFT JOIN RolConciertoUsuario rcu ON c.idConcierto = rcu.idConcierto AND rcu.idRol = 3
            LEFT JOIN Usuario u ON rcu.idUsuario = u.idUsuario
        """;
        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Horario h = new Horario();
                h.setIdHorario(rs.getInt("idHorario"));
                h.setFechaInicio(rs.getDate("fechaInc").toLocalDate());
                h.setFechaFin(rs.getDate("fechaFin").toLocalDate());
                h.setHoraInicio(rs.getTime("horaInc").toLocalTime());
                h.setHoraFin(rs.getTime("horaFin").toLocalTime());
                Usuario artista = null;
                if (rs.getObject("idUsuario") != null) {
                    artista = new Usuario();
                    artista.setIdUsuario(rs.getInt("idUsuario"));
                    artista.setNombre(rs.getString("nombre"));
                }
                Concierto c = new Concierto(
                        rs.getInt("idConcierto"),
                        rs.getString("nombreConcierto"),
                        h,
                        rs.getInt("aforo"),
                        artista,
                        rs.getBoolean("programado")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Concierto> buscarPorArtista(String nombreArtista) {
        List<Concierto> lista = new ArrayList<>();
        String sql = """
            SELECT c.idConcierto, c.nombreConcierto, c.aforo, c.programado,
                   h.idHorario, h.fechaInc, h.fechaFin, h.horaInc, h.horaFin,
                   u.idUsuario, u.nombre
            FROM Concierto c
            JOIN Horario h ON c.idHorario = h.idHorario
            JOIN RolConciertoUsuario rcu ON c.idConcierto = rcu.idConcierto
            JOIN Usuario u ON rcu.idUsuario = u.idUsuario
            WHERE u.nombre = ?
        """;
        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreArtista);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Horario h = new Horario();
                h.setIdHorario(rs.getInt("idHorario"));
                h.setFechaInicio(rs.getDate("fechaInc").toLocalDate());
                h.setFechaFin(rs.getDate("fechaFin").toLocalDate());
                h.setHoraInicio(rs.getTime("horaInc").toLocalTime());
                h.setHoraFin(rs.getTime("horaFin").toLocalTime());
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("idUsuario"));
                u.setNombre(rs.getString("nombre"));
                Concierto c = new Concierto(
                        rs.getInt("idConcierto"),
                        rs.getString("nombreConcierto"),
                        h,
                        rs.getInt("aforo"),
                        u,
                        rs.getBoolean("programado")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void guardarRelacionArtista(int idUsuario, int idConcierto, int idRol) {
        String sql = "INSERT INTO RolConciertoUsuario (idRol, idUsuario, idConcierto) VALUES (?, ?, ?)";
        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRol);
            stmt.setInt(2, idUsuario);
            stmt.setInt(3, idConcierto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void aprobarConcierto(int id) {
        String sql = "UPDATE Concierto SET programado = TRUE WHERE idConcierto = ?";
        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarConcierto(int idConcierto) {
        try (Connection conn = h2.getConnection()) {
            conn.setAutoCommit(false);

            String sqlInv = "DELETE FROM ConciertoDocumentoInventario WHERE idConcierto = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlInv)) {
                stmt.setInt(1, idConcierto);
                stmt.executeUpdate();
            }

            String sqlRel = "DELETE FROM RolConciertoUsuario WHERE idConcierto = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlRel)) {
                stmt.setInt(1, idConcierto);
                stmt.executeUpdate();
            }

            String sqlConc = "DELETE FROM Concierto WHERE idConcierto = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlConc)) {
                stmt.setInt(1, idConcierto);
                stmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}