package com.spl2.givematch.dao;

import com.spl2.givematch.util.PasswordUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:givematch.db";
    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    private DatabaseManager() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initialize() {
        try (Connection connection = getConnection()) {
            runScript(connection, "/db/schema.sql");
            runScript(connection, "/db/seed.sql");
            seedDefaultAdmin(connection);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to initialize the database.", e);
        }
    }

    private static void runScript(Connection connection, String resourcePath) throws SQLException {
        String script = readResource(resourcePath);
        try (Statement statement = connection.createStatement()) {
            for (String rawStatement : script.split(";")) {
                String sql = rawStatement.trim();
                if (!sql.isEmpty()) {
                    statement.execute(sql);
                }
            }
        }
    }

    private static void seedDefaultAdmin(Connection connection) throws SQLException {
        try (Statement check = connection.createStatement();
             var resultSet = check.executeQuery("SELECT COUNT(*) AS total FROM users WHERE role = 'ADMIN'")) {
            resultSet.next();
            if (resultSet.getInt("total") > 0) {
                return;
            }
        }
        String hashed = PasswordUtil.hash(DEFAULT_ADMIN_PASSWORD);
        String insert = "INSERT INTO users (name, username, hashed_password, role, contact_info, priority_level) "
                + "VALUES ('Administrator', '" + DEFAULT_ADMIN_USERNAME + "', '" + hashed + "', 'ADMIN', 'admin@givematch.local', 0)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(insert);
        }
        System.out.println("Seeded default admin -> username: " + DEFAULT_ADMIN_USERNAME
                + " / password: " + DEFAULT_ADMIN_PASSWORD);
    }

    private static String readResource(String path) {
        try (InputStream inputStream = DatabaseManager.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IllegalStateException("Missing resource on classpath: " + path);
            }
            StringBuilder builder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().startsWith("--")) {
                        builder.append(line).append('\n');
                    }
                }
            }
            return builder.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read resource: " + path, e);
        }
    }
}
