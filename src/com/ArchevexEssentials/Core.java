package com.ArchevexEssentials.Core;

import com.ArchevexEssentials.Utils.MySQLManager;
// import com.ArchevexEssentials.Events.PlayerEvent
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

    private static PlayerDataStorage instance;
    private MySQLManager mysqlManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig(); // Load config for database credentials
        mysqlManager = new MySQLManager();
        mysqlManager.setupDatabase();

        Bukkit.getPluginManager().registerEvents(new PlayerEventListener(mysqlManager), this);

        getLogger().info("PlayerDataStorage plugin enabled!");
    }

    @Override
    public void onDisable() {
        if (mysqlManager != null) {
            mysqlManager.closePool();
        }
        getLogger().info("PlayerDataStorage plugin disabled!");
    }

    public static PlayerDataStorage getInstance() {
        return instance;
    }

    public MySQLManager getMySQLManager() {
        return mysqlManager;
    }
}
