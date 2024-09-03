package com.thedev.sweetabilities.AbilityManager.CakedManager;

import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.Utils.ItemBuilder;
import com.thedev.sweetabilities.Utils.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CakedManager {

    public Set<UUID> isCakedSet = new HashSet<>();

    public Map<UUID, UUID> seesCakedPlayerMap = new HashMap<>();

    public boolean isCurrentlyCaked(Player player) {
        return isCakedSet.contains(player.getUniqueId());
    }

    private void removeFromCakeSet(UUID playerUUID) {
        isCakedSet.remove(playerUUID);
    }

    private void addToCakeSet(Player player) {
        isCakedSet.add(player.getUniqueId());
    }

    public void visualizeCake(UUID cakedPlayerUUID) {
        Player player = Bukkit.getPlayer(cakedPlayerUUID);

        if(player == null) return;
        if(isCurrentlyCaked(player)) return;

        CakedActivateEvent event = new CakedActivateEvent(player);

        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled()) return;

        addToCakeSet(event.getCakedPlayer());

        ItemStack cakedSkull = new ItemBuilder(Material.SKULL_ITEM, "You Have Been Caked", 1, 3, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDU2MWRlZDhkODM4NWI5MTNhMDkxYWVmNDc4M2ZjY2JmZDNkMzhlZGQ5MGIyZTg5YjcyM2I1YTU3NDM0YmY0In19fQ==", "&7Cake Item").getItem();

        PacketUtil.changePlayerHelmetPacket(event.getCakedPlayer().getUniqueId(), event.getCakedPlayer().getUniqueId(), cakedSkull);
        seesCakedPlayerMap.put(event.getCakedPlayer().getUniqueId(), event.getCakedPlayer().getUniqueId());

        player.getNearbyEntities(20, 20, 20).forEach(nearbyEntity -> {
            if(nearbyEntity instanceof Player) {
                Player nearbyPlayer = (Player) nearbyEntity;

                PacketUtil.changePlayerHelmetPacket(event.getCakedPlayer().getUniqueId(), nearbyPlayer.getUniqueId(), cakedSkull);
                seesCakedPlayerMap.put(nearbyPlayer.getUniqueId(), event.getCakedPlayer().getUniqueId());
            }
        });


        Bukkit.getScheduler().runTaskLater(SweetAbilities.getInst(), () -> {
            Material material = (event.getCakedPlayer().getInventory().getHelmet() == null) ? Material.AIR : player.getInventory().getHelmet().getType();

            removeFromCakeSet(cakedPlayerUUID);

            Iterator<Map.Entry<UUID, UUID>> entryIterator = seesCakedPlayerMap.entrySet().iterator();

            while (entryIterator.hasNext()) {
                Map.Entry<UUID, UUID> entry = entryIterator.next();
                Player viewerPlayer = Bukkit.getPlayer(entry.getKey());
                Player cakedPlayer = Bukkit.getPlayer(entry.getValue());

                if(viewerPlayer == null || cakedPlayer == null || !viewerPlayer.isOnline() || !cakedPlayer.isOnline()) {
                    entryIterator.remove();
                    continue;
                }

                PacketUtil.changePlayerHelmetPacket(cakedPlayer.getUniqueId(), viewerPlayer.getUniqueId(), new ItemStack(material));
                entryIterator.remove();
            }
        }, 100L);
    }
}
