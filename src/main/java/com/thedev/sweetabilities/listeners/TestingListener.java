package com.thedev.sweetabilities.listeners;

import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.abilities.diablomanager.DiabloAbilityManager;
import com.thedev.sweetabilities.abilities.hellmanager.HellManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TestingListener implements Listener {

    private final SweetAbilities plugin;

    public TestingListener(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!event.getPlayer().isSneaking()) return;
        if(event.getAction() != Action.LEFT_CLICK_AIR) return;

        UUID playerUUID = event.getPlayer().getUniqueId();

        HellManager hellManager = plugin.getAbilityManager().getHellManager();

        hellManager.activateHell(playerUUID, false);
    }
}
