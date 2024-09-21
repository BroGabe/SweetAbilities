package com.thedev.sweetabilities.abilities.wrathmanager;

import com.thedev.sweetabilities.SweetAbilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class WrathManager {

    private final SweetAbilities plugin;

    private final Set<UUID> wrathedPlayers = new HashSet<>();

    private BukkitTask wrathDamageTask;

    public WrathManager(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    public void wrathPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        if(player == null || !player.isOnline() || wrathedPlayers.contains(uuid)) return;

        addWrathedPlayer(uuid);

        startWrathTask();

        Bukkit.getScheduler().runTaskLater(plugin, () -> removeWrathPlayer(uuid), 80L);
    }

    public boolean isPlayerWrathed(UUID uuid) {
        return wrathedPlayers.contains(uuid);
    }

    public void removeWrathPlayer(UUID uuid) {
        wrathedPlayers.remove(uuid);
    }

    private void addWrathedPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        if(player == null || !player.isOnline()) return;

        wrathedPlayers.add(uuid);
    }

    private void startWrathTask() {
        if(wrathDamageTask != null && Bukkit.getScheduler().isCurrentlyRunning(wrathDamageTask.getTaskId())) return;

        wrathDamageTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            if(wrathedPlayers.isEmpty()) {
                wrathDamageTask.cancel();
                return;
            }

            Iterator<UUID> uuidIterator = wrathedPlayers.iterator();
            while(uuidIterator.hasNext()) {
                UUID wrathedPlayerUUID = uuidIterator.next();
                Player player = Bukkit.getPlayer(wrathedPlayerUUID);

                if(player == null || !player.isOnline()) {
                    uuidIterator.remove();
                    continue;
                }

                player.getWorld().strikeLightningEffect(player.getLocation());
                player.damage(0.01);

                double newHealth = player.getHealth() - plugin.getDefaultConfig().WRATH_DAMAGE();

                if(newHealth < 0) {
                    newHealth = 0;
                }

                player.setHealth(newHealth);
            }
        }, 20L, 20L);
    }
}
