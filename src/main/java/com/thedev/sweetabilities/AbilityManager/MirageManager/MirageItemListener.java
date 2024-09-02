package com.thedev.sweetabilities.AbilityManager.MirageManager;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class MirageItemListener implements Listener {

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        NBTItem nbtItem = new NBTItem(event.getItem().getItemStack());

        if(!nbtItem.hasTag("SetDenyPickup")) return;
        event.setCancelled(true);
        Bukkit.broadcastMessage("denied pickup!");
//        event.getItem().remove();
    }
}
