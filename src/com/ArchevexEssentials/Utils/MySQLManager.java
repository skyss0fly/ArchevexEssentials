package com.ArchevexEssentials.Utils.MySQLManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLManager {

    private HikariDataSource dataSource;

    public void setupDatabase() {
        String host = PlayerDataStorage.getInstance().getConfig().getString("mysql.host");
        String database = PlayerDataStorage.getInstance().getConfig().getString("mysql.database");
        String user = PlayerDataStorage.getInstance().getConfig().getString("mysql.user");
        String password = PlayerDataStorage.getInstance().getConfig().getString("mysql.password");
        int port = PlayerDataStorage.getInstance().getConfig().getInt("mysql.port");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);

        dataSource = new HikariDataSource(config);

        try (Connection connection = getConnection()) {
            String tableCreate = "CREATE TABLE IF NOT EXISTS player_data (" +
                    "dbid INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(16), " +
                    "uuid VARCHAR(36), " +
                    "firstjoined DATETIME, " +
                    "lastjoined DATETIME, " +
                    "inventory_data TEXT, " +
                    "ip_address VARCHAR(45)" +
                    ");";
            connection.prepareStatement(tableCreate).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closePool() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
