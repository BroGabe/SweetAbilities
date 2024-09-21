package com.thedev.sweetabilities.listeners;

import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.abilities.bleedmanager.BleedManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TestingListener implements Listener {

    private final SweetAbilities plugin;

    public TestingListener(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        System.out.println("Did event get called?");
        if(!event.getPlayer().isSneaking()) return;
        if(event.getAction() != Action.LEFT_CLICK_AIR) return;

        System.out.println("Did player bleed?");

        BleedManager bleedManager = plugin.getAbilityManager().getBleedManager();

        bleedManager.bleedPlayer(event.getPlayer().getUniqueId());
    }
}
