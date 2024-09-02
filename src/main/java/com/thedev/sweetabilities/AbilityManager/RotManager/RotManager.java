package com.thedev.sweetabilities.AbilityManager.RotManager;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.thedev.sweetabilities.SweetAbilities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class RotManager {

    private final SweetAbilities plugin;

    private final Map<Block, RotBlock> rotBlockMap = new HashMap<>();

    private final List<UUID> activeRotPlayers = new ArrayList<>();

    private BukkitTask rotDamageTask;

    public RotManager(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    public boolean hasActiveRot(UUID uuid) {
        return activeRotPlayers.contains(uuid);
    }

    public boolean isStandingInRot(UUID uuid) {
        Block block = Bukkit.getPlayer(uuid).getLocation().clone().subtract(0, 1, 0).getBlock();

        return rotBlockMap.containsKey(block);
    }

    public boolean playerOwnsBlock(UUID uuid) {
        Block block = Bukkit.getPlayer(uuid).getLocation().clone().subtract(0, 1, 0).getBlock();

        if(!isStandingInRot(uuid)) return false;

        RotBlock rotBlock = rotBlockMap.get(block);

        if(rotBlock == null) return false;

        return rotBlock.isOwner(uuid);
    }

    public void activateRot(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        if(player == null || !player.isOnline() || hasActiveRot(uuid)) return;

        List<Block> eligibleRotBlocks = getEligibleRotBlocks(uuid);

        if(eligibleRotBlocks.isEmpty()) return;

        activeRotPlayers.add(uuid);

        eligibleRotBlocks.forEach(block ->
                rotBlockMap.compute(block, (keyBlock, valueRotBlock) -> (valueRotBlock == null) ? new RotBlock(this, block).addOwner(uuid) : valueRotBlock.addOwner(uuid)));

        doBlockChanges(uuid, eligibleRotBlocks);
        startRotDamageTask();

        Bukkit.getScheduler().runTaskLater(plugin, () -> removeRotPlayer(uuid), (5 * 20L));
    }

    public void removeRotPlayer(UUID uuid) {
        activeRotPlayers.remove(uuid);

        Iterator<RotBlock> rotBlockIterator = rotBlockMap.values().iterator();

        while(rotBlockIterator.hasNext()) {
            RotBlock rotBlock = rotBlockIterator.next();
            if(!rotBlock.isOwner(uuid)) continue;
            rotBlock.removeOwner(uuid);

            if(rotBlock.getBlockOwners().isEmpty()) rotBlockIterator.remove();
        }
    }

    public void updateRotPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        if(player == null || !hasActiveRot(uuid)) {
            return;
        }

        List<Block> eligibleRotBlocks = getEligibleRotBlocks(uuid);

        Iterator<RotBlock> rotBlockIterator = rotBlockMap.values().iterator();

        while(rotBlockIterator.hasNext()) {
            RotBlock rotBlock = rotBlockIterator.next();

            if(!rotBlock.isOwner(uuid)) continue;

            rotBlock.updatePlayer(uuid, eligibleRotBlocks);

            if(rotBlock.getBlockOwners().isEmpty()) rotBlockIterator.remove();
        }

        doBlockChanges(uuid, eligibleRotBlocks);
    }

    private void doBlockChanges(UUID uuid, List<Block> eligibleRotBlocks) {
        Player player = Bukkit.getPlayer(uuid);

        if(player == null || !hasActiveRot(uuid)) return;

        eligibleRotBlocks.forEach(block ->
                rotBlockMap.compute(block, (keyBlock, valueRotBlock) -> (valueRotBlock == null) ? new RotBlock(this, block).addOwner(uuid) : valueRotBlock.addOwner(uuid)));

        eligibleRotBlocks.forEach(block -> player.sendBlockChange(block.getLocation(), Material.SEA_LANTERN, (byte) 0));

        for(Player worldPlayer : player.getWorld().getPlayers()) {
            if(worldPlayer == player) continue;

            eligibleRotBlocks.forEach(block -> worldPlayer.sendBlockChange(block.getLocation(), Material.REDSTONE_BLOCK, (byte) 0));
        }
    }

    public boolean isPlayerInSafeZone(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) return false;

        RegionContainer regionContainer = WorldGuardPlugin.inst().getRegionContainer();

        ApplicableRegionSet regionSet = Objects.requireNonNull(regionContainer.get(player.getWorld())).getApplicableRegions(player.getLocation());

        ProtectedRegion defaultRegion = regionContainer.get(player.getWorld()).getRegion("__global__");

        if(defaultRegion == null) return false;

        if(regionSet.size() == 0) {
            if(defaultRegion.getFlag(DefaultFlag.PVP) == null) return false;
            return (defaultRegion.getFlag(DefaultFlag.PVP) == StateFlag.State.DENY);
        }

        for(ProtectedRegion region : regionSet) {
            if(region.getFlag(DefaultFlag.PVP) != null && region.getFlag(DefaultFlag.PVP) == StateFlag.State.ALLOW) return false;
        }

        return true;
    }

    public void startRotDamageTask() {
        if(rotDamageTask != null && Bukkit.getScheduler().isCurrentlyRunning(rotDamageTask.getTaskId())) return;
        if(activeRotPlayers.isEmpty()) return;

        rotDamageTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if(activeRotPlayers.isEmpty()) rotDamageTask.cancel();

            Iterator<UUID> rotPlayerUUIDIterator = activeRotPlayers.iterator();

            while (rotPlayerUUIDIterator.hasNext()) {
                Player rotPlayer = Bukkit.getPlayer(rotPlayerUUIDIterator.next());

                if(rotPlayer == null) {
                    rotPlayerUUIDIterator.remove();
                    continue;
                }

                for(Entity nearbyEntity : rotPlayer.getNearbyEntities(15, 15, 15)) {
                    if(!(nearbyEntity instanceof Player)) continue;
                    Player nearbyPlayer = (Player) nearbyEntity;

                    if(!isStandingInRot(nearbyPlayer.getUniqueId())) continue;
                    if(playerOwnsBlock(nearbyPlayer.getUniqueId())) continue;
                    if(isPlayerInSafeZone(nearbyPlayer.getUniqueId())) continue;

                    double damageAmount = plugin.getDefaultConfig().ROT_DAMAGE();

                    RotDamagePlayerEvent event = new RotDamagePlayerEvent(nearbyPlayer, damageAmount);

                    Bukkit.getPluginManager().callEvent(event);

                    if(event.isCancelled()) continue;
                    nearbyPlayer.damage(0.01);
                    nearbyPlayer.setHealth(nearbyPlayer.getHealth() - event.getDamageAmount());
                }
            }
        }, 20, 20);
    }

    public List<Block> getEligibleRotBlocks(UUID playerUUID) {
        List<Block> eligibleBlocks = new ArrayList<>();

        List<Material> invalidMaterials = Arrays.asList(Material.BIRCH_WOOD_STAIRS, Material.WATER, Material.STATIONARY_LAVA, Material.STATIONARY_WATER,
                Material.STEP, Material.COBBLESTONE_STAIRS, Material.QUARTZ_STAIRS, Material.BRICK_STAIRS, Material.AIR);

        Player player = Bukkit.getPlayer(playerUUID);

        Location centerLocation = player.getLocation().clone().subtract(0, 1, 0);

        int x = centerLocation.getBlockX();
        int y = centerLocation.getBlockY();
        int z = centerLocation.getBlockZ();

        int[] ranges = {4, 4, 3, 2, 1};

        for (int i = 0; i < ranges.length; i++) {
            int range = ranges[i];
            int currentZ = z - i;

            for (int dx = -range; dx <= range; dx++) {
                Location loc = new Location(player.getWorld(), x + dx, y, currentZ);
                Block locBlock = loc.getBlock();
                if(invalidMaterials.contains(locBlock.getType())) continue;
                eligibleBlocks.add(locBlock);
            }
        }

        // Iterate through the pattern going forwards
        for (int i = 0; i < ranges.length; i++) {
            int range = ranges[i];
            int currentZ = z + i + 1; // +1 to skip the center row already processed

            for (int dx = -range; dx <= range; dx++) {
                Location loc = new Location(player.getWorld(), x + dx, y, currentZ);
                Block locBlock = loc.getBlock();
                if(invalidMaterials.contains(locBlock.getType())) continue;
                eligibleBlocks.add(locBlock);
            }
        }

        return eligibleBlocks;
    }
}
