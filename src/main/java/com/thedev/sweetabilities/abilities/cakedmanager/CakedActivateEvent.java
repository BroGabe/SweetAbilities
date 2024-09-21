package com.thedev.sweetabilities.abilities.cakedmanager;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CakedActivateEvent extends Event implements Cancellable {

    private final Player cakedPlayer;

    private boolean isCancelled = false;

    public CakedActivateEvent(Player cakedPlayer) {
        this.cakedPlayer = cakedPlayer;
    }

    public Player getCakedPlayer() {
        return cakedPlayer;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }
}
