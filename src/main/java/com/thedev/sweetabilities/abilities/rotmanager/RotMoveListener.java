package com.thedev.sweetabilities.abilities.rotmanager;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class RotMoveListener implements Listener {

    private final RotManager rotManager;

    public RotMoveListener(RotManager rotManager) {
        this.rotManager = rotManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Block initialBlock = event.getFrom().clone().subtract(0, 1, 0).getBlock();
        Block toBlock = event.getTo().clone().subtract(0, 1, 0).getBlock();

        if(initialBlock.equals(toBlock)) return;

        Player player = event.getPlayer();

        if(!rotManager.hasActiveRot(player.getUniqueId())) return;

        rotManager.updateRotPlayer(player.getUniqueId());
    }

    @EventHandler
    public void onRotDeath(PlayerDeathEvent event) {
        if(!rotManager.hasActiveRot(event.getEntity().getUniqueId())) return;

        rotManager.removeRotPlayer(event.getEntity().getUniqueId());
    }
}
