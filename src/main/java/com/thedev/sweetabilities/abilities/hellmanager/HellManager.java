package com.thedev.sweetabilities.abilities.hellmanager;

import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.utils.ChanceUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class HellManager {

    private final SweetAbilities plugin;

    private final Set<UUID> abilityPlayers = new HashSet<>();

    private final Map<Block, HellBlock> hellBlockMap = new HashMap<>();

    public HellManager(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    public boolean hasAbility(UUID uuid) {
        return abilityPlayers.contains(uuid);
    }

    // Checks if they are standing on a hell block
    // then checks if they own it, if so, returns true.
    protected boolean ownsHellBlock(UUID uuid) {
        if(Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline() || !abilityPlayers.contains(uuid)) return false;

        Location location = Bukkit.getPlayer(uuid).getLocation().clone().subtract(0.0, 1.0, 0.0);

        if(!hellBlockMap.containsKey(location.getBlock())) return false;

        return hellBlockMap.get(location.getBlock()).isOwner(uuid);
    }

    protected void removeAbility(UUID uuid, List<UUID> players) {
        if(!abilityPlayers.contains(uuid)) return;
        abilityPlayers.remove(uuid);

        Iterator<HellBlock> hellBlockIterator = hellBlockMap.values().iterator();

        while(hellBlockIterator.hasNext()) {
            HellBlock hellBlock = hellBlockIterator.next();

            if(!hellBlock.isOwner(uuid)) continue;
            hellBlock.removeOwner(uuid, players);

            if(!hellBlock.getBlockOwners().isEmpty()) continue;
            hellBlockIterator.remove();
        }
    }

    private void addAbility(UUID uuid) {
        if(abilityPlayers.contains(uuid)) return;
        abilityPlayers.add(uuid);
    }

    public void activateHell(UUID uuid, boolean empowered) {
        if(Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) return;
        if(abilityPlayers.contains(uuid)) return;

        addAbility(uuid);

        List<UUID> players = getNearbyPlayers(uuid);
        players.add(uuid);


        List<Block> eligibleBlocks = getEligibleBlocks(uuid);
        List<Block> fireBlocks = eligibleFireBlocks(eligibleBlocks);

        sendBlockChanges(players, eligibleBlocks, fireBlocks);

        eligibleBlocks.forEach(block ->
                hellBlockMap.compute(block, (keyBlock, valueBlock) -> (valueBlock == null) ? new HellBlock(this, block).addOwner(uuid) : valueBlock.addOwner(uuid)));

        BukkitTask empoweredTask;

        if(empowered) {
            empoweredTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                if(Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline() || !abilityPlayers.contains(uuid)) return;
                if(!ownsHellBlock(uuid)) return;

                Player player = Bukkit.getPlayer(uuid);
                player.setHealth(player.getHealth() + 3);
            }, 10L, 10L);
        } else {
            empoweredTask = null;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            removeAbility(uuid, players);

            if(empoweredTask == null) return;
            empoweredTask.cancel();
        }, 80L);
    }


    private void sendBlockChanges(List<UUID> players, List<Block> eligibleBlocks, List<Block> fireBlocks) {
        for(Block block : eligibleBlocks) {
            Material material = (ChanceUtil.doChance(65)) ? Material.NETHERRACK : Material.QUARTZ_ORE;

            for(UUID playerUUID : players) {
                if(Bukkit.getPlayer(playerUUID) == null || !Bukkit.getPlayer(playerUUID).isOnline()) continue;
                Player player = Bukkit.getPlayer(playerUUID);

                player.sendBlockChange(block.getLocation(), material, (byte) 0);
            }
        }

        for(Block fireBlock : fireBlocks) {
            if(!ChanceUtil.doChance(25)) continue;
            for(UUID playerUUID : players) {
                if(Bukkit.getPlayer(playerUUID) == null || !Bukkit.getPlayer(playerUUID).isOnline()) continue;
                Player player = Bukkit.getPlayer(playerUUID);
                player.sendBlockChange(fireBlock.getLocation(), Material.FIRE, (byte) 0);
            }
        }
    }

    private List<UUID> getNearbyPlayers(UUID uuid) {
        if(Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) return new ArrayList<>();
        Player player = Bukkit.getPlayer(uuid);
        List<UUID> nearbyPlayers = new ArrayList<>();

        for(Entity nearbyEntity : player.getNearbyEntities(15, 15, 15)) {
            if(!(nearbyEntity instanceof Player)) continue;

            nearbyPlayers.add(nearbyEntity.getUniqueId());
        }

        return nearbyPlayers;
    }

    private List<Block> eligibleFireBlocks(List<Block> eligibleBlocks) {
        List<Block> eligibleFireBlocks = new ArrayList<>();

        for(Block block : eligibleBlocks) {
            if(block.getType() == Material.AIR) continue;

            Location fireLocation = block.getLocation().clone().add(0.0, 1.0, 0.0);
            if(fireLocation.getBlock().getType() != Material.AIR) continue;

            eligibleFireBlocks.add(fireLocation.getBlock());
        }

        return eligibleFireBlocks;
    }

    private List<Block> getEligibleBlocks(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        List<Block> eligibleBlocks = new ArrayList<>();

        List<Material> invalidMaterials = Arrays.asList(Material.BIRCH_WOOD_STAIRS, Material.WATER, Material.STATIONARY_LAVA, Material.STATIONARY_WATER,
                Material.STEP, Material.COBBLESTONE_STAIRS, Material.QUARTZ_STAIRS, Material.BRICK_STAIRS, Material.AIR);

        Location centerLocation = player.getLocation().clone().subtract(0, 1, 0);

        for(int x = -5; x<=5; x++) {
            for(int z = -5; z<=5; z++) {
                Location blockLocation = centerLocation.clone().add(x, 0, z).getBlock().getLocation();
                Block block = blockLocation.getBlock();

                if(invalidMaterials.contains(block.getType())) {
                    Location beneath = blockLocation.clone().subtract(0, 1, 0).getBlock().getLocation();

                    if(invalidMaterials.contains(beneath.getBlock().getType())) continue;
                    eligibleBlocks.add(beneath.getBlock());
                    continue;
                }
                eligibleBlocks.add(block);
            }
        }
        return eligibleBlocks;
    }
}
