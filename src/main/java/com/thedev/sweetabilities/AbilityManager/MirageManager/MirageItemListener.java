package com.thedev.sweetabilities.AbilityManager.MirageManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class MirageItemListener implements Listener {

    // In the future, Shade NBT Api into the plugin
    // and add a tag to prevent item pickup even after server crash or restart.
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if(event.getItem().hasMetadata("DenyItemPickup")) {
            event.setCancelled(true);
            event.getItem().remove();
        }
    }
}
