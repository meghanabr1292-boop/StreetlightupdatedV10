package com.streetlight.dao;

import com.streetlight.model.StreetLight;
import com.streetlight.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StreetLightDAO {

    // CREATE
    public boolean addStreetLight(StreetLight light) {

        String sql = """
                INSERT INTO street_lights
                (light_code, location, power_rating, status)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, light.getLightCode());
            statement.setString(2, light.getLocation());
            statement.setInt(3, light.getPowerRating());
            statement.setString(4, light.getStatus());

            if (statement.executeUpdate() == 0) {
                return false;
            }
            Integer lightId = null;
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    lightId = keys.getInt(1);
                }
            }
            new StreetLightHistoryDAO().recordEvent(lightId, light.getLightCode(), "ADDED",
                    null, light.getStatus(), "Street light added");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ALL
    public List<StreetLight> getAllStreetLights() {

        List<StreetLight> lights = new ArrayList<>();

        String sql = "SELECT * FROM street_lights ORDER BY light_id";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                StreetLight light = new StreetLight();

                light.setLightId(
                        resultSet.getInt("light_id")
                );

                light.setLightCode(
                        resultSet.getString("light_code")
                );

                light.setLocation(
                        resultSet.getString("location")
                );

                light.setPowerRating(
                        resultSet.getInt("power_rating")
                );

                light.setStatus(
                        resultSet.getString("status")
                );

                light.setCreatedAt(
                        resultSet.getString("created_at")
                );

                lights.add(light);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lights;
    }

    // UPDATE
    public boolean updateStreetLight(StreetLight light) {

        String sql = """
                UPDATE street_lights
                SET light_code = ?,
                    location = ?,
                    power_rating = ?,
                    status = ?
                WHERE light_id = ?
                """;

        String oldCode;
        String oldStatus;
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement lookup = connection.prepareStatement(
                    "SELECT light_code, status FROM street_lights WHERE light_id = ?")) {
                lookup.setInt(1, light.getLightId());
                try (ResultSet row = lookup.executeQuery()) {
                    if (!row.next()) return false;
                    oldCode = row.getString("light_code");
                    oldStatus = row.getString("status");
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, light.getLightCode());
                statement.setString(2, light.getLocation());
                statement.setInt(3, light.getPowerRating());
                statement.setString(4, light.getStatus());
                statement.setInt(5, light.getLightId());

                if (statement.executeUpdate() == 0) return false;
            }
            String changedCode = oldCode.equals(light.getLightCode()) ? light.getLightCode()
                    : oldCode + " → " + light.getLightCode();
            new StreetLightHistoryDAO().recordEvent(light.getLightId(), changedCode, "UPDATED",
                    oldStatus, light.getStatus(), "Street light details updated");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteStreetLight(int lightId) {

        String sql =
                "DELETE FROM street_lights WHERE light_id = ?";

        try (Connection connection = DBConnection.getConnection()) {
            String lightCode;
            String oldStatus;
            try (PreparedStatement lookup = connection.prepareStatement(
                    "SELECT light_code, status FROM street_lights WHERE light_id = ?")) {
                lookup.setInt(1, lightId);
                try (ResultSet row = lookup.executeQuery()) {
                    if (!row.next()) return false;
                    lightCode = row.getString("light_code");
                    oldStatus = row.getString("status");
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, lightId);

                if (statement.executeUpdate() == 0) return false;
            }
            new StreetLightHistoryDAO().recordEvent(lightId, lightCode, "DELETED",
                    oldStatus, null, "Street light deleted");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // AUTOMATIC ON/OFF
public boolean updateAllLightStatus(String status) {

    String historySql = """
            INSERT INTO street_light_history
            (light_id, light_code, event_type, old_status, new_status, details)
            SELECT light_id, light_code, 'AUTOMATIC', status, ?, 'Day/night schedule changed status'
            FROM street_lights
            WHERE status IS NULL OR status <> ?
            """;
    String sql = "UPDATE street_lights SET status = ?";

    try (Connection connection = DBConnection.getConnection()) {
        connection.setAutoCommit(false);
        try (PreparedStatement historyStatement = connection.prepareStatement(historySql);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            historyStatement.setString(1, status);
            historyStatement.setString(2, status);
            historyStatement.executeUpdate();

            statement.setString(1, status);

            statement.executeUpdate();
            connection.commit();
        return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
}
