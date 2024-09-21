package com.thedev.sweetabilities.abilities.loversmanager;

import com.thedev.sweetabilities.configuration.DefaultConfig;
import com.thedev.sweetabilities.configuration.PlayerData;
import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoversManager {

    private final Map<UUID, BukkitTask> loversPlayersTask = new HashMap<>();

    private final SweetAbilities plugin;

    public LoversManager(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    public void activateLovers(UUID uuid) {
        if(Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) return;
        if(hasLovers(uuid)) return;

        Player player = Bukkit.getPlayer(uuid);

        player.sendMessage(ColorUtil.color(plugin.getConfig().getString("settings.lovers-ability.lovers-msg")));
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 7);

        DefaultConfig defaultConfig = plugin.getDefaultConfig();

        PlayerData playerData = plugin.getPlayerData();
        FileConfiguration playerConfig = playerData.getPlayerConfig(uuid);

        playerConfig.set("lovers-ability", 0.0);
        playerData.saveConfig(uuid, playerConfig);

        addLoversHearts(uuid);

        Bukkit.getScheduler().runTaskLater(plugin, () -> removeLovers(uuid, false), (20L * defaultConfig.LOVERS_TIME()));
    }

    public void removeLovers(UUID uuid, boolean playerDied) {
        if(!hasLovers(uuid)) return;

        OfflinePlayer player = Bukkit.getPlayer(uuid);

        PlayerData playerData = plugin.getPlayerData();
        FileConfiguration playerConfig = playerData.getPlayerConfig(uuid);

        if(hasLoversTask(uuid)) {
            loversPlayersTask.get(uuid).cancel();
            loversPlayersTask.remove(uuid);
        }

        if(playerDied) {
            playerConfig.set("lovers-ability", null);
            playerData.saveConfig(uuid, playerConfig);
            return;
        }

        if(!player.isOnline()) return;

        Player onlinePlayer = Bukkit.getPlayer(uuid);

        onlinePlayer.setMaxHealth(onlinePlayer.getMaxHealth() - getLoversHearts(uuid));

        playerConfig.set("lovers-ability", null);
        playerData.saveConfig(uuid, playerConfig);
    }

    protected void addLoversHearts(UUID uuid) {
        if(Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) return;
        if(!hasLovers(uuid)) return;

        Player player = Bukkit.getPlayer(uuid);

        DefaultConfig defaultConfig = plugin.getDefaultConfig();

        PlayerData playerData = plugin.getPlayerData();
        FileConfiguration playerConfig = playerData.getPlayerConfig(uuid);

        double heartsPer = defaultConfig.LOVERS_HEARTS_PER();
        double maxHearts = defaultConfig.LOVERS_MAX_HEARTS();

        double newHearts = Math.min(maxHearts, getLoversHearts(uuid) + heartsPer);

        if(getLoversHearts(uuid) != 0.0) {
            player.sendMessage(ColorUtil.color(plugin.getConfig().getString("settings.lovers-ability.lovers-proc-msg")));
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 7);
        }

        if(newHearts == maxHearts) {
            double finalHealth = maxHearts - getLoversHearts(uuid);

            player.setMaxHealth((player.getMaxHealth() + Math.max(0, finalHealth)));
        } else {
            player.setMaxHealth(player.getMaxHealth() + heartsPer);
        }

        playerConfig.set("lovers-ability", newHearts);
        playerData.saveConfig(uuid, playerConfig);
    }

    public boolean hasLovers(UUID uuid) {
        if(Bukkit.getOfflinePlayer(uuid) == null) return false;

        PlayerData playerData = plugin.getPlayerData();
        FileConfiguration playerConfig = playerData.getPlayerConfig(uuid);

        return playerConfig.contains("lovers-ability");
    }

    private double getLoversHearts(UUID uuid) {
        if(Bukkit.getOfflinePlayer(uuid) == null) return 0.0;
        if(!hasLovers(uuid)) return 0.0;

        PlayerData playerData = plugin.getPlayerData();
        FileConfiguration playerConfig = playerData.getPlayerConfig(uuid);

        return playerConfig.getDouble("lovers-ability", 0.0);
    }

    protected boolean hasLoversTask(UUID uuid) {
        return loversPlayersTask.containsKey(uuid);
    }

}
