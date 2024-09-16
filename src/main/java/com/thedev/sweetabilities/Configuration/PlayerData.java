package com.thedev.sweetabilities.Configuration;

import com.thedev.sweetabilities.SweetAbilities;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerData {

    private final SweetAbilities plugin;

    public PlayerData(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration getPlayerConfig(UUID uuid) {
        return YamlConfiguration.loadConfiguration(getPlayerFile(uuid));
    }

    public File getPlayerFile(UUID uuid) {
        File folder = new File(plugin.getDataFolder(), "PlayerData");
        if(!folder.exists()) folder.mkdirs();

        return new File(folder, uuid.toString() +".yml");
    }

    public void saveConfig(UUID uuid, FileConfiguration config) {
        try {
            config.save(getPlayerFile(uuid));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
