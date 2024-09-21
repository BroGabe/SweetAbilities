package com.thedev.sweetabilities.abilities.miragemanager;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class MirageItemListener implements Listener {

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        NBTItem nbtItem = new NBTItem(event.getItem().getItemStack());

        if(!nbtItem.hasTag("SetDenyPickup")) return;
        event.setCancelled(true);
        event.getItem().remove();
    }
}
