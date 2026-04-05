package org.example.ax0006.Repository;

import org.example.ax0006.db.H2;

import java.sql.*;

public class horarioRepository {

    private H2 h2;

    public horarioRepository(H2 h2) {
        this.h2 = h2;
    }

    public int guardarHorario(String fecha, String horaInicio, String horaFin) {

        String sql = "INSERT INTO Horario (fecha, horaInc, horaFin) VALUES (?, ?, ?)";

        try (Connection conn = h2.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(fecha));
            stmt.setTime(2, Time.valueOf(horaInicio));
            stmt.setTime(3, Time.valueOf(horaFin));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Horario creado con ID: " + id);
                return id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
}