package com.thedev.sweetabilities.abilities.hellmanager;

import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.configuration.DefaultConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HellListener implements Listener {

    private final SweetAbilities plugin;

    public HellListener(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        HellManager hellManager = plugin.getAbilityManager().getHellManager();

        DefaultConfig defaultConfig = plugin.getDefaultConfig();

        Player attacker = (Player) event.getDamager();

        if(!hellManager.hasAbility(attacker.getUniqueId())) return;

        if(!hellManager.ownsHellBlock(attacker.getUniqueId())) return;

        int increasePercentage = defaultConfig.HELL_DAMAGE_PERCENTAGE();

        double damageMultiplier = (increasePercentage / 100.0);

        event.setDamage(event.getDamage() + (event.getDamage() * damageMultiplier));
    }
}
