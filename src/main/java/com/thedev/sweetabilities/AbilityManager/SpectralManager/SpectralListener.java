package com.thedev.sweetabilities.AbilityManager.SpectralManager;

import com.thedev.sweetabilities.SweetAbilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SpectralListener implements Listener {

    private final SweetAbilities plugin;

    private final SpectralManager spectralManager;

    public SpectralListener(SweetAbilities plugin, SpectralManager spectralManager) {
        this.plugin = plugin;
        this.spectralManager = spectralManager;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        if(!spectralManager.isPlayerSpectral(event.getEntity().getUniqueId())) return;

        Player spectralPlayer = (Player) event.getEntity();

        Player attackerPlayer = (Player) event.getDamager();

        SpectralBlockEvent spectralBlockEvent = new SpectralBlockEvent(spectralPlayer, attackerPlayer);

        Bukkit.getPluginManager().callEvent(spectralBlockEvent);

        if(spectralBlockEvent.isCancelled()) return;

        event.setCancelled(true);
    }
}
