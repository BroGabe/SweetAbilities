package com.thedev.sweetabilities.abilities.rotmanager;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;
import java.util.UUID;

public class RotDamagePlayerEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    private final Set<UUID> rotOwners;

    private final Player damagedPlayer;

    private double damageAmount;

    private boolean isCancelled = false;

    public RotDamagePlayerEvent(Player damagedPlayer, double damageAmount, Set<UUID> rotOwners) {
        this.damagedPlayer = damagedPlayer;
        this.damageAmount = damageAmount;
        this.rotOwners = rotOwners;
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

    public Set<UUID> getRotOwners() {
        return rotOwners;
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
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
