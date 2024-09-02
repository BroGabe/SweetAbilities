package com.thedev.sweetabilities.AbilityManager.CursedMarkManager;

import com.thedev.sweetabilities.SweetAbilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CursedDamageListener implements Listener {

    private final SweetAbilities plugin;

    private final CursedMarkManager cursedMarkManager;

    public CursedDamageListener(SweetAbilities plugin, CursedMarkManager cursedMarkManager) {
        this.plugin = plugin;
        this.cursedMarkManager = cursedMarkManager;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        Player damagedPlayer = (Player) event.getEntity();

        if(!cursedMarkManager.isPlayerCursed(damagedPlayer.getUniqueId())) return;

        int increasePercentage = plugin.getDefaultConfig().CURSED_DAMAGE_PERCENTAGE();

        double damageMultiplier = (increasePercentage / 100.0);

        event.setDamage(event.getDamage() + (event.getDamage() * damageMultiplier));
    }
}
