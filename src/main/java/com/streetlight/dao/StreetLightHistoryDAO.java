package com.streetlight.dao;

import com.streetlight.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StreetLightHistoryDAO {
    public void recordEvent(Integer lightId, String lightCode, String eventType,
                            String oldStatus, String newStatus, String details) {
        String sql = """
                INSERT INTO street_light_history
                (light_id, light_code, event_type, old_status, new_status, details)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (lightId == null) statement.setNull(1, java.sql.Types.INTEGER);
            else statement.setInt(1, lightId);
            statement.setString(2, lightCode);
            statement.setString(3, eventType);
            statement.setString(4, oldStatus);
            statement.setString(5, newStatus);
            statement.setString(6, details);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Could not save light activity: " + e.getMessage());
        }
    }

    public List<Object[]> getRecentEvents() {
        String sql = """
                SELECT changed_at, light_code, event_type, old_status, new_status, details
                FROM street_light_history
                ORDER BY changed_at DESC, history_id DESC
                LIMIT 500
                """;
        List<Object[]> events = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rows = statement.executeQuery()) {
            while (rows.next()) {
                events.add(new Object[]{
                        rows.getTimestamp("changed_at"),
                        rows.getString("light_code"),
                        rows.getString("event_type"),
                        rows.getString("old_status"),
                        rows.getString("new_status"),
                        rows.getString("details")
                });
            }
        } catch (SQLException e) {
            System.err.println("Could not load light activity: " + e.getMessage());
        }
        return events;
    }
}
