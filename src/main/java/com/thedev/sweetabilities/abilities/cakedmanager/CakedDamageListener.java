package com.thedev.sweetabilities.abilities.cakedmanager;

import com.thedev.sweetabilities.SweetAbilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CakedDamageListener implements Listener {

    private final SweetAbilities plugin;

    private final CakedManager cakedManager;

    public CakedDamageListener(SweetAbilities plugin, CakedManager cakedManager) {
        this.plugin = plugin;
        this.cakedManager = cakedManager;
    }

    @EventHandler
    public void cakedPlayerDamaged(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if(!cakedManager.isCurrentlyCaked(player)) return;

        int damagePercentage = plugin.getDefaultConfig().CAKE_DAMAGE_PERCENTAGE();

        double damageMultiplier = damagePercentage / 100.0;

        event.setDamage(event.getDamage() + (event.getDamage() * damageMultiplier));
    }
}
