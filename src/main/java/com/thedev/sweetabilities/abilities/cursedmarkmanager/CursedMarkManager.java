package com.thedev.sweetabilities.abilities.cursedmarkmanager;

import com.thedev.sweetabilities.SweetAbilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CursedMarkManager {

    private final SweetAbilities plugin;

    public CursedMarkManager(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    private final Set<UUID> cursedPlayers = new HashSet<>();

    public boolean isPlayerCursed(UUID uuid) {
        return cursedPlayers.contains(uuid);
    }

    private void removeCursedPlayer(UUID uuid) {
        cursedPlayers.remove(uuid);
    }

    private void addCursedPlayer(UUID uuid) {
        cursedPlayers.add(uuid);
    }

    public void cursePlayer(UUID uuid) {
        Player cursedPlayer = Bukkit.getPlayer(uuid);

        if(cursedPlayer == null || !cursedPlayer.isOnline() || isPlayerCursed(uuid)) return;

        CursePlayerEvent event = new CursePlayerEvent(cursedPlayer);

        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled()) return;

        addCursedPlayer(uuid);

        Bukkit.getScheduler().runTaskLater(plugin, () -> removeCursedPlayer(uuid), 100L);
    }
}
