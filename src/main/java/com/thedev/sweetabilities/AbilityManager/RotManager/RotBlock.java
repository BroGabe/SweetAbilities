package com.thedev.sweetabilities.AbilityManager.RotManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class RotBlock {

    private final RotManager rotManager;

    private final Block block;

    private final byte blockData;

    private final Set<UUID> blockOwners = new HashSet<>();

    public RotBlock(RotManager rotManager, Block block) {
        this.rotManager = rotManager;
        this.block = block;
        blockData = block.getData();
    }

    public void updatePlayer(UUID uuid, List<Block> eligibleRotBlocks) {
        if(isValidLocation(eligibleRotBlocks)) {
            return;
        }

        removeOwner(uuid);
    }

    public void removeOwner(UUID uuid) {
        if(!isOwner(uuid) && !blockOwners.isEmpty()) {
            return;
        }

        blockOwners.remove(uuid);

        Player player = Bukkit.getPlayer(uuid);

        Material blockChange = (blockOwners.isEmpty()) ? block.getType() : Material.REDSTONE_BLOCK;
        byte newBlockData = (blockOwners.isEmpty()) ? blockData : (byte) 0;

        player.sendBlockChange(block.getLocation(), blockChange, newBlockData);

        block.getLocation().getWorld().getPlayers().forEach(loopedPlayer ->  {
            if(loopedPlayer != player) {
                loopedPlayer.sendBlockChange(block.getLocation(), blockChange, newBlockData);
            }
        });
    }

    public RotBlock addOwner(UUID uuid) {
        blockOwners.add(uuid);
        return this;
    }

    public boolean isOwner(UUID uuid) {
        return blockOwners.contains(uuid);
    }

    public Set<UUID> getBlockOwners() {
        return blockOwners;
    }

    public Block getBlock() {
        return block;
    }

    private boolean isValidLocation(List<Block> eligibleRotBlocks) {
        return eligibleRotBlocks.contains(block);
    }
}
