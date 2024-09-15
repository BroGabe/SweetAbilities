package com.thedev.sweetabilities.AbilityManager.LoversAbility;

import com.thedev.sweetabilities.SweetAbilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;

public class LoversListener implements Listener {

    private final SweetAbilities plugin;

    public LoversListener(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDefense(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if(!plugin.getAbilityManager().getLoversManager().hasActiveLovers(player.getUniqueId())) return;

        Random random = new Random();

        int randomInt = random.nextInt(101);

        if(randomInt > plugin.getDefaultConfig().LOVERS_PROC_CHANCE()) return;

        plugin.getAbilityManager().getLoversManager().addLoversHeart(player.getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!plugin.getAbilityManager().getLoversManager().isCached(event.getPlayer().getUniqueId())) return;

        LoversManager loversManager = plugin.getAbilityManager().getLoversManager();

        loversManager.removeCachedPlayer(event.getPlayer().getUniqueId());
    }
}
