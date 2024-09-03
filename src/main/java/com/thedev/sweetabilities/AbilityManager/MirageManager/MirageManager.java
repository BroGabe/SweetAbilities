package com.thedev.sweetabilities.AbilityManager.MirageManager;

import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.Utils.ItemBuilder;
import de.tr7zw.nbtapi.NBTItem;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class MirageManager {

    private final SweetAbilities plugin;

    private final Set<UUID> miragedPlayerSet = new HashSet<>();

    public MirageManager(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    public void activateMirage(UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);

        if(player == null || !player.isOnline() || isCurrentlyMirage(playerUUID)) return;

        addMiragePlayer(playerUUID);

        System.out.println("Mirage activated!");

        for(Entity nearbyEntity : player.getNearbyEntities(15, 15, 15)) {
            if(!(nearbyEntity instanceof Player)) continue;

            Player nearbyPlayer = (Player) nearbyEntity;

            MiragePlayerEvent event = new MiragePlayerEvent(nearbyPlayer);
            Bukkit.getPluginManager().callEvent(event);

            if(event.isCancelled()) continue;

            disappearPlayer(plugin, player.getUniqueId(), nearbyPlayer.getUniqueId(), plugin.getDefaultConfig().MIRAGE_TIME());
        }

        fakePlayerDeathNPC(player.getUniqueId());
    }

    private boolean isCurrentlyMirage(UUID uuid) {
        return miragedPlayerSet.contains(uuid);
    }

    private void addMiragePlayer(UUID uuid) {
        miragedPlayerSet.add(uuid);
    }

    private void removePlayerMirage(UUID uuid) {
        miragedPlayerSet.remove(uuid);
    }

    private void fakePlayerDeathNPC(UUID playerUUID) {
        Player playerToFake = Bukkit.getPlayer(playerUUID);
        if(playerToFake == null || !playerToFake.isOnline()) return;

        ItemStack playerHelmet = (playerToFake.getInventory().getHelmet() == null) ? new ItemBuilder(Material.DIAMOND_HELMET, "", 1, 0, null, "").setGlow() : new ItemBuilder(playerToFake.getInventory().getHelmet().getType(), "", 1, 0, null, "").setGlow();
        ItemStack playerChestplate = (playerToFake.getInventory().getHelmet() == null) ? new ItemBuilder(Material.DIAMOND_CHESTPLATE, "", 1, 0, null, "").setGlow() : new ItemBuilder(playerToFake.getInventory().getChestplate().getType(), "", 1, 0, null, "").setGlow();
        ItemStack playerLeggings = (playerToFake.getInventory().getHelmet() == null) ? new ItemBuilder(Material.DIAMOND_LEGGINGS, "", 1, 0, null, "").setGlow() : new ItemBuilder(playerToFake.getInventory().getLeggings().getType(), "", 1, 0, null, "").setGlow();
        ItemStack playerBoots = (playerToFake.getInventory().getHelmet() == null) ? new ItemBuilder(Material.DIAMOND_BOOTS, "", 1, 0, null, "").setGlow() : new ItemBuilder(playerToFake.getInventory().getBoots().getType(), "", 1, 0, null, "").setGlow();
        ItemStack playerHand = (playerToFake.getInventory().getHelmet() == null) ? new ItemBuilder(Material.DIAMOND_SWORD, "", 1, 0, null, "").setGlow() : new ItemBuilder(playerToFake.getItemInHand().getType(), "", 1, 0, null, "").setGlow();

        ItemStack pots  = new ItemBuilder(Material.POTION, "", 12, 373, null, "").setGlow();
        PotionMeta potionMeta = (PotionMeta) pots.getItemMeta();
        potionMeta.setMainEffect(PotionEffectType.HEAL);
        pots.setItemMeta(potionMeta);

        List<ItemStack> droppedItems = new ArrayList<>();

        for(ItemStack itemStack : Arrays.asList(playerHelmet, playerChestplate, playerLeggings, playerBoots, pots, playerHand)) {
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setBoolean("SetDenyPickup", true);

            droppedItems.add(nbtItem.getItem());
        }

        droppedItems.forEach(droppedItem -> playerToFake.getWorld().dropItem(playerToFake.getLocation(), droppedItem));

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, playerToFake.getName());
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HELMET, playerHelmet);
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.CHESTPLATE, playerChestplate);
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.LEGGINGS, playerLeggings);
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.BOOTS, playerBoots);
        npc.getOrAddTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, playerHand);

        npc.setProtected(false);
        npc.spawn(playerToFake.getLocation());


        Entity npcEntity = npc.getEntity();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(npcEntity instanceof Player) {
                Player npcPlayer = (Player) npcEntity;
                npcPlayer.damage(1000);
                npc.destroy();
            }
        }, 10L);
    }

    private void disappearPlayer(SweetAbilities plugin, UUID playerUUID, UUID viewerUUID, int seconds) {
        Player targetPlayer = Bukkit.getPlayer(playerUUID);
        Player viewerPlayer = Bukkit.getPlayer(viewerUUID);

        if(targetPlayer == null || viewerPlayer == null) return;

        viewerPlayer.hidePlayer(targetPlayer);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(isCurrentlyMirage(playerUUID)) {
                removePlayerMirage(playerUUID);
            }

            if(viewerPlayer == null || !viewerPlayer.isOnline() || targetPlayer == null || !targetPlayer.isOnline()) return;

            viewerPlayer.showPlayer(targetPlayer);
        }, (20L * seconds));
    }
}
