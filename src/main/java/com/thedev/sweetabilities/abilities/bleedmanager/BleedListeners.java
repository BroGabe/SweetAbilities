package com.thedev.sweetabilities.abilities.bleedmanager;

import com.thedev.sweetabilities.SweetAbilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BleedListeners implements Listener {

    private final SweetAbilities plugin;

    public BleedListeners(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(!plugin.getAbilityManager().getBleedManager().isPlayerBleeding(event.getEntity().getUniqueId())) return;
        plugin.getAbilityManager().getBleedManager().removeBleedingPlayer(event.getEntity().getUniqueId());
    }
}
