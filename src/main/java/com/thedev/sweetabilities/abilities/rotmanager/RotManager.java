package com.thedev.sweetabilities.abilities.rotmanager;

import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.utils.FactionsSupport;
import com.thedev.sweetabilities.utils.WorldGuardSupport;
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

            Material rotMaterial = (FactionsSupport.isRelated(worldPlayer, player)) ? Material.SEA_LANTERN : Material.REDSTONE;

            eligibleRotBlocks.forEach(block -> worldPlayer.sendBlockChange(block.getLocation(), rotMaterial, (byte) 0));
        }
    }

    public void startRotDamageTask() {
        if(rotDamageTask != null && Bukkit.getScheduler().isCurrentlyRunning(rotDamageTask.getTaskId())) return;
        if(activeRotPlayers.isEmpty()) return;

        rotDamageTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if(activeRotPlayers.isEmpty()) rotDamageTask.cancel();

            Iterator<UUID> rotPlayerUUIDIterator = activeRotPlayers.iterator();

            while (rotPlayerUUIDIterator.hasNext()) {
                Player rotPlayer = Bukkit.getPlayer(rotPlayerUUIDIterator.next());

                if(rotPlayer == null || !rotPlayer.isOnline()) {
                    rotPlayerUUIDIterator.remove();
                    continue;
                }

                for(Entity nearbyEntity : rotPlayer.getNearbyEntities(15, 15, 15)) {
                    if(!(nearbyEntity instanceof Player)) continue;
                    Player nearbyPlayer = (Player) nearbyEntity;

                    if(nearbyPlayer == null || !nearbyPlayer.isOnline() || nearbyPlayer.isDead()) continue;

                    if(!isStandingInRot(nearbyPlayer.getUniqueId())) continue;
                    if(playerOwnsBlock(nearbyPlayer.getUniqueId())) continue;
                    if(WorldGuardSupport.isPlayerInSafeZone(nearbyPlayer.getUniqueId())) continue;

                    Block block = nearbyPlayer.getLocation().clone().subtract(0, 1, 0).getBlock();

                    if(!rotBlockMap.containsKey(block)) continue;

                    double damageAmount = plugin.getDefaultConfig().ROT_DAMAGE();

                    RotDamagePlayerEvent event = new RotDamagePlayerEvent(nearbyPlayer, damageAmount, rotBlockMap.get(block).getBlockOwners());

                    Bukkit.getPluginManager().callEvent(event);

                    if(event.isCancelled()) continue;
                    nearbyPlayer.damage(0.01);
                    nearbyPlayer.setHealth(Math.max(0, nearbyPlayer.getHealth() - event.getDamageAmount()));
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
