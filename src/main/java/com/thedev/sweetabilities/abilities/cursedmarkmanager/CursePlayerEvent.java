package com.thedev.sweetabilities.abilities.cursedmarkmanager;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CursePlayerEvent extends Event implements Cancellable {

    private final Player cursedPlayer;

    private static final HandlerList handlerList = new HandlerList();

    private boolean isCancelled = false;

    public CursePlayerEvent(Player cursedPlayer) {
        this.cursedPlayer = cursedPlayer;
    }

    public Player getCursedPlayer() {
        return cursedPlayer;
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
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
