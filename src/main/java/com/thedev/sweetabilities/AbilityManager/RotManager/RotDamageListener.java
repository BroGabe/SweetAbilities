package com.thedev.sweetabilities.AbilityManager.RotManager;

import com.thedev.sweetabilities.Utils.FactionsSupport;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class RotDamageListener implements Listener {

    @EventHandler
    public void onRotDamage(RotDamagePlayerEvent event) {
        for(UUID blockOwner : event.getRotOwners()) {
            Player rotPlayer = Bukkit.getPlayer(blockOwner);

            if(!FactionsSupport.isRelated(rotPlayer, event.getDamagedPlayer())) continue;

            event.setCancelled(true);
            return;
        }
    }
}
