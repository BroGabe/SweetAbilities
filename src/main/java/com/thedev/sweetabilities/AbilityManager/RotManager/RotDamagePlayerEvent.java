package com.thedev.sweetabilities.AbilityManager.RotManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RotDamagePlayerEvent extends Event implements Cancellable {

    private final Player damagedPlayer;

    private double damageAmount;

    private boolean isCancelled = false;

    public RotDamagePlayerEvent(Player damagedPlayer, double damageAmount) {
        this.damagedPlayer = damagedPlayer;
        this.damageAmount = damageAmount;
    }

    public Player getDamagedPlayer() {
        return damagedPlayer;
    }

    public double getDamageAmount() {
        return damageAmount;
    }

    public void setDamageAmount(double damageAmount) {
        this.damageAmount = damageAmount;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }
}
