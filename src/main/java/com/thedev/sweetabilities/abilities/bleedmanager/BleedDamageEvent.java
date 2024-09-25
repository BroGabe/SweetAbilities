package com.thedev.sweetabilities.abilities.bleedmanager;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BleedDamageEvent extends Event implements Cancellable {

    private static HandlerList handlerList = new HandlerList();

    private final Player bleedingPlayer;

    private double damageAmount;

    public BleedDamageEvent(Player bleedingPlayer, double damageAmount) {
        this.bleedingPlayer = bleedingPlayer;
        this.damageAmount = damageAmount;
    }

    public Player getBleedingPlayer() {
        return bleedingPlayer;
    }

    public double getDamage() {
        return damageAmount;
    }

    public void setDamage(double damageAmount) {
        this.damageAmount = damageAmount;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {

    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
