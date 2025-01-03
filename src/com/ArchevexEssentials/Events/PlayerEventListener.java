package com.ArchevexEssentials.Events.PlayerEventListener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PlayerEventListener implements Listener {

    private final MySQLManager mysqlManager;

    public PlayerEventListener(MySQLManager mysqlManager) {
        this.mysqlManager = mysqlManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String username = event.getPlayer().getName();
        String uuid = event.getPlayer().getUniqueId().toString();
        String ip = event.getPlayer().getAddress().getAddress().getHostAddress();
        LocalDateTime now = LocalDateTime.now();

        try (Connection connection = mysqlManager.getConnection()) {
            String selectQuery = "SELECT dbid FROM player_data WHERE uuid = ?;";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            selectStmt.setString(1, uuid);
            ResultSet resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                String updateQuery = "UPDATE player_data SET lastjoined = ?, ip_address = ? WHERE uuid = ?;";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setObject(1, now);
                updateStmt.setString(2, ip);
                updateStmt.setString(3, uuid);
                updateStmt.execute();
            } else {
                String insertQuery = "INSERT INTO player_data (username, uuid, firstjoined, lastjoined, ip_address) VALUES (?, ?, ?, ?, ?);";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setString(1, username);
                insertStmt.setString(2, uuid);
                insertStmt.setObject(3, now);
                insertStmt.setObject(4, now);
                insertStmt.setString(5, ip);
                insertStmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Optionally handle player quit logic
    }
}
