package com.streetlight.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** Creates the activity-history table needed by the monitoring screens. */
public final class DatabaseInitializer {
    private DatabaseInitializer() { }

    public static void initialize() {
        String sql = """
                CREATE TABLE IF NOT EXISTS street_light_history (
                    history_id INT AUTO_INCREMENT PRIMARY KEY,
                    light_id INT NULL,
                    light_code VARCHAR(80) NOT NULL,
                    event_type VARCHAR(40) NOT NULL,
                    old_status VARCHAR(10) NULL,
                    new_status VARCHAR(10) NULL,
                    details VARCHAR(255) NOT NULL,
                    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
                """;
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Could not initialize activity history: " + e.getMessage());
        }
    }
}
