package com.thedev.sweetabilities.abilities.miragemanager;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MiragePlayerEvent extends Event implements Cancellable {

    private boolean isCancelled = false;

    private final Player targetPlayer;

    public MiragePlayerEvent(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
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
