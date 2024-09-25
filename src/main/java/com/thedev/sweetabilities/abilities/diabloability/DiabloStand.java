package com.thedev.sweetabilities.abilities.diabloability;

import com.thedev.sweetabilities.SweetAbilities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;


public class DiabloStand {

    private final Player targetPlayer;

    private final SweetAbilities plugin;

    private BukkitTask standTask;

    private float yawFacingPlayer;

    private final Vector sideVector;

    private ArmorStand armorStand;

    private int ticksLived = 0;

    public DiabloStand(SweetAbilities plugin, Player player, Vector sideVector) {
        this.sideVector = sideVector;
        this.targetPlayer = player;
        this.plugin = plugin;
    }

    public void startTask() {
        if(armorStand != null && !armorStand.isDead()) {
            return;
        }

        Location standLocation = getFacingPlayerLoc();
        armorStand = standLocation.getWorld().spawn(standLocation, ArmorStand.class);
        armorStand.setItemInHand(new ItemStack(Material.IRON_SWORD));

        System.out.println("Original location vector: " + standLocation.getDirection());
        System.out.println("Original location yaw: " + standLocation.getYaw());


        standTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            ticksLived++;

            float previousYaw = armorStand.getLocation().getYaw();
            Location location = getStandLocation();
            location.setYaw(previousYaw + 5);
            armorStand.teleport(location);

            System.out.println("New location vector: " + armorStand.getLocation().getDirection());
            System.out.println("New location yaw: " + armorStand.getLocation().getYaw());

            if(ticksLived >= 100) {
                ticksLived = 0;
                armorStand.remove();
                standTask.cancel();
            }

        }, 1L, 2L);
    }

    private Location getFacingPlayerLoc() {
        Location standLocation = getStandLocation();

        Vector playerVector = targetPlayer.getLocation().toVector();
        Vector standVector = standLocation.toVector();

        Vector facingVector = playerVector.subtract(standVector).normalize();

        standLocation.setDirection(facingVector);

        return standLocation;
    }

    private Location getStandLocation() {
        return targetPlayer.getLocation().clone().add(sideVector).add(0, 3, 0);
    }
}
