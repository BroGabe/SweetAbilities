package com.thedev.sweetabilities.listeners;

import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.abilities.bleedmanager.BleedManager;
import com.thedev.sweetabilities.abilities.diabloability.DiabloAbilityManager;
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
        if(!event.getPlayer().isSneaking()) return;
        if(event.getAction() != Action.LEFT_CLICK_AIR) return;

        DiabloAbilityManager diabloAbilityManager = plugin.getAbilityManager().getDiabloAbilityManager();;

        diabloAbilityManager.diabloPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getUniqueId());

        System.out.println("Did it diablo player?");
    }
}
