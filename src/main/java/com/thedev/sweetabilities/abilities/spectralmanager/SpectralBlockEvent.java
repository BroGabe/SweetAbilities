package com.thedev.sweetabilities.abilities.spectralmanager;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpectralBlockEvent extends Event implements Cancellable {

    private final Player player;

    private final Player damager;

    private boolean isCanacelled = false;

    public SpectralBlockEvent(Player player, Player damager) {
        this.player = player;
        this.damager = damager;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getDamager() {
        return damager;
    }

    @Override
    public boolean isCancelled() {
        return isCanacelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCanacelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }
}
