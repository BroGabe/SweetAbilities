package com.thedev.sweetabilities.abilities.bleedmanager;

import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BleedManager {

    private final SweetAbilities plugin;

    private Set<UUID> bleedingPlayers = new HashSet<>();

    private BukkitTask bleedTask;

    public BleedManager(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    public void bleedPlayer(UUID uuid) {
        if(isPlayerBleeding(uuid)) return;
        if(Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) return;

        Player player = Bukkit.getPlayer(uuid);
        player.sendMessage(ColorUtil.color("&4&l*&c&l*&4&l* &c&lBLEEDING &4&l*&c&l*&4&l*"));

        addToBleedingSet(uuid);
        startBleedTask();

        Bukkit.getScheduler().runTaskLater(plugin, () -> removeBleedingPlayer(uuid), (20L * plugin.getDefaultConfig().BLEED_TIME()));
    }

    protected boolean isPlayerBleeding(UUID uuid) {
        return bleedingPlayers.contains(uuid);
    }

    private void addToBleedingSet(UUID uuid) {
        bleedingPlayers.add(uuid);
    }

    protected void removeBleedingPlayer(UUID uuid) {
        bleedingPlayers.remove(uuid);
    }

    private void startBleedTask() {
        if(bleedTask != null && Bukkit.getScheduler().isCurrentlyRunning(bleedTask.getTaskId())) return;
        if(bleedingPlayers.isEmpty()) return;

        bleedTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if(bleedingPlayers.isEmpty() && bleedTask != null) {
                bleedTask.cancel();
            }

            for(UUID uuid : bleedingPlayers) {
                if(Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) continue;

                Player player = Bukkit.getPlayer(uuid);
                double damageAmount = plugin.getDefaultConfig().BLEED_DAMAGE();

                BleedDamageEvent event = new BleedDamageEvent(player, damageAmount);
                Bukkit.getPluginManager().callEvent(event);

                if(event.isCancelled()) continue;

                player.damage(0.01);
                player.setHealth(Math.max(0.0, player.getHealth() - event.getDamage()));
            }
        }, 20L, 20L);
    }

}
