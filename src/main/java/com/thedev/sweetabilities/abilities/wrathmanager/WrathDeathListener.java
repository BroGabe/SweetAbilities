package com.thedev.sweetabilities.abilities.wrathmanager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class WrathDeathListener implements Listener {

    private final WrathManager wrathManager;

    public WrathDeathListener(WrathManager wrathManager) {
        this.wrathManager = wrathManager;
    }


    @EventHandler
    public void onDeathEvent(PlayerDeathEvent event) {
        if(!wrathManager.isPlayerWrathed(event.getEntity().getUniqueId())) return;
        wrathManager.removeWrathPlayer(event.getEntity().getUniqueId());
    }
}
