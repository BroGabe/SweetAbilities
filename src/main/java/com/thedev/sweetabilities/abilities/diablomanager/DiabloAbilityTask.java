package com.thedev.sweetabilities.abilities.diablomanager;

import com.thedev.sweetabilities.utils.WorldGuardSupport;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DiabloAbilityTask extends BukkitRunnable {
    private final List<ArmorStand> swords = new ArrayList<>();
    private final Player user;
    private final Player target;
    private int currentTick = 0;
    private int swordRotationTick = 0;
    private int teleportTicks = 0;
    private Iterator<ArmorStand> iter;
    private ArmorStand current;

    public DiabloAbilityTask(Player user, Player target) {
        this.user = user;
        this.target = target;
        this.currentTick = 0;
        this.swordRotationTick = 0;
        this.teleportTicks = 0;
        this.iter = null;
        this.current = null;
    }

    public void run() {
        if (this.currentTick <= 100)
            for (int i = 0; i < 6; i++) {
                double rotationAngle;
                double radius = 3.25D;
                double angle = 6.283185307179586D * i / 6.0D;
                double x = this.target.getLocation().getX() + radius * Math.cos(angle);
                double y = this.target.getLocation().getY() + 2.5D;
                double z = this.target.getLocation().getZ() + radius * Math.sin(angle);
                Location location = new Location(this.target.getWorld(), x, y, z);
                if (this.swords.size() != 6) {
                    ArmorStand armorStand1 = location.getWorld().spawn(location, ArmorStand.class);
                    armorStand1.setVisible(false);
                    armorStand1.setGravity(false);
                    armorStand1.setArms(true);
                    armorStand1.setBasePlate(true);
                    armorStand1.setItemInHand(new ItemStack(Material.IRON_SWORD));
                    armorStand1.setRightArmPose(new EulerAngle(-1.5707963267948966D, 0.0D, 0.0D));
                    this.swords.add(armorStand1);
                }
                ArmorStand armorStand = this.swords.get(i);
                if (this.currentTick > 75) {
                    rotationAngle = (this.swordRotationTick += 4);
                } else {
                    rotationAngle = this.swordRotationTick++;
                }
                location.setYaw((float)rotationAngle);

                armorStand.teleport(location);
            }
        if (this.currentTick > 100 && this.currentTick < 120)
            for (ArmorStand armorStand : this.swords) {
                if (this.target == null || !this.target.isOnline() || this.target.isDead() || this.target.getWorld() != armorStand.getWorld()) {
                    this.swords.forEach(Entity::remove);
                    this.swords.clear();
                    cancel();
                    return;
                }
                Vector directionToCenter = this.target.getEyeLocation().clone().subtract(armorStand.getLocation()).toVector();
                armorStand.setRightArmPose(armorStand.getRightArmPose().add(0.11D, 0.0D, 0.0D));
                Location targetLocation = armorStand.getLocation().setDirection(directionToCenter);
                armorStand.teleport(targetLocation);
            }
        if (this.currentTick == 120)
            this.iter = this.swords.iterator();
        if (this.currentTick > 120 && this.iter != null) {
            if (this.current == null)
                if (this.iter.hasNext()) {
                    this.current = this.iter.next();
                } else {
                    this.swords.forEach(Entity::remove);
                    this.swords.clear();
                    cancel();
                    return;
                }
            if (this.target != null && this.target.isOnline() && this.target.getWorld() == this.current.getWorld() && !this.target.isDead()) {
                double distance = this.target.getEyeLocation().distance(this.current.getLocation());
                if (distance <= 1.5D) {
                    this.current.remove();
                    this.iter.remove();
                    this.current = null;
                    if (!WorldGuardSupport.isPlayerInSafeZone(target.getUniqueId()))
                        target.damage(0.01);
                } else if (this.teleportTicks > 9) {
                    Location nextLocation = this.target.getEyeLocation();
                    this.current.teleport(nextLocation);
                    this.teleportTicks = 0;
                } else {
                    this.teleportTicks++;
                }
            } else {
                if (this.current != null)
                    this.current.remove();
                this.iter.remove();
                this.swords.forEach(Entity::remove);
                this.swords.clear();
                cancel();
                return;
            }
        }
        this.currentTick++;
        if (this.swordRotationTick >= 360)
            this.swordRotationTick = 0;
    }
}
