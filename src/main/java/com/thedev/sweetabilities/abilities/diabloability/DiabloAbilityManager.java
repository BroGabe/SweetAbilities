package com.thedev.sweetabilities.abilities.diabloability;

import com.thedev.sweetabilities.SweetAbilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


import java.util.*;

public class DiabloAbilityManager {

    private final SweetAbilities plugin;

    private final Map<UUID, DiabloAbilityTask> diabloPlayers = new HashMap<>();

    public DiabloAbilityManager(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    public void diabloPlayer(UUID playerUUID, UUID targetUUID) {
        if(Bukkit.getPlayer(targetUUID) == null || !Bukkit.getPlayer(targetUUID).isOnline() || Bukkit.getPlayer(playerUUID) == null || !Bukkit.getPlayer(playerUUID).isOnline()) return;
        if(diabloPlayers.containsKey(targetUUID)) return;

        Player player = Bukkit.getPlayer(playerUUID);
        Player target = Bukkit.getPlayer(targetUUID);

        diabloPlayers.put(targetUUID, new DiabloAbilityTask(player, target));

        diabloPlayers.get(targetUUID).runTaskTimer(plugin, 1L, 1L);

        Bukkit.getScheduler().runTaskLater(plugin, () -> diabloPlayers.remove(targetUUID), 380L);
    }
}
