package com.thedev.sweetabilities.abilities.hellmanager;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HellBlock {

    private final Set<UUID> blockOwners = new HashSet<>();

    private final Block block;

    private final HellManager hellManager;

    public HellBlock(HellManager hellManager, Block block) {
        this.block = block;
        this.hellManager = hellManager;
    }

    public HellBlock addOwner(UUID uuid) {
        blockOwners.add(uuid);
        return this;
    }

    public void removeOwner(UUID uuid, List<UUID> players) {
        blockOwners.remove(uuid);

       if(!blockOwners.isEmpty()) return;

       for(UUID playerUUID : players) {
           if(Bukkit.getPlayer(playerUUID) == null || !Bukkit.getPlayer(playerUUID).isOnline()) continue;
           Player player = Bukkit.getPlayer(playerUUID);

           player.sendBlockChange(block.getLocation(), block.getType(), block.getData());

           Block fireBlock = block.getLocation().clone().add(0.0, 1.0, 0.0).getBlock();

           player.sendBlockChange(fireBlock.getLocation(), fireBlock.getType(), fireBlock.getData());
       }


    }

    public boolean isOwner(UUID uuid) {
        return blockOwners.contains(uuid);
    }

    public Set<UUID> getBlockOwners() {
        return blockOwners;
    }
}
