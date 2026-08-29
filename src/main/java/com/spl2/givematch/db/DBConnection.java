package com.spl2.givematch.db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton — opens one SQLite connection and reuses it everywhere.
 * Also responsible for running schema.sql / seed.sql on first startup.
 */
public final class DBConnection {

    private static final String DB_FILE = "givematch.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;

    private static DBConnection instance;
    private final Connection connection;

    private DBConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            boolean isNewDatabase = !Files.exists(Path.of(DB_FILE));
            this.connection = DriverManager.getConnection(URL);

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }

            if (isNewDatabase) {
                runScript("/db/schema.sql");
                runScript("/db/seed.sql");
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void runScript(String resourcePath) {
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new RuntimeException("Could not find " + resourcePath + " on classpath");
            }
            String sql = new String(in.readAllBytes());
            try (Statement stmt = connection.createStatement()) {
                for (String statement : sql.split(";")) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to run script " + resourcePath, e);
        }
    }
}
