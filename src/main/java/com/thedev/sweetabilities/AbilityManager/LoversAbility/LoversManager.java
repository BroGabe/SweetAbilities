package com.thedev.sweetabilities.AbilityManager.LoversAbility;

import com.thedev.sweetabilities.SweetAbilities;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoversManager {

    private final Map<UUID, Double> activeLovers = new HashMap<>();

    private final Map<UUID, Double> cachedRemove = new HashMap<>();

    private final SweetAbilities plugin;

    public LoversManager(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    public void activateLovers(UUID uuid) {
        if(activeLovers.containsKey(uuid) || Bukkit.getPlayer(uuid) == null) return;

        activeLovers.put(uuid, 0.0);

        addLoversHeart(uuid);

        Bukkit.getScheduler().runTaskLater(plugin, () -> removeLovers(uuid), (20L * plugin.getDefaultConfig().LOVERS_TIME()));
    }

    public void addLoversHeart(UUID uuid) {
        if(!activeLovers.containsKey(uuid)) return;
        if(Bukkit.getPlayer(uuid) == null || !Bukkit.getPlayer(uuid).isOnline()) return;

        Player player = Bukkit.getPlayer(uuid);

        double heartsPer = plugin.getDefaultConfig().LOVERS_HEARTS_PER();

        double maxHearts = plugin.getDefaultConfig().LOVERS_MAX_HEARTS();

        double newHearts = Math.min(maxHearts, activeLovers.get(uuid) + heartsPer);

        if(newHearts == maxHearts) {
            double finalHealth = maxHearts - activeLovers.get(uuid);

            player.setMaxHealth((player.getMaxHealth() + Math.max(0, finalHealth)));
        } else {
            player.setMaxHealth(player.getMaxHealth() + heartsPer);
        }

        activeLovers.put(uuid, newHearts);
    }

    private void removeLovers(UUID uuid) {
        if(!activeLovers.containsKey(uuid)) return;
        if(Bukkit.getOfflinePlayer(uuid) == null) return;

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        double extraHearts = activeLovers.get(uuid);

        activeLovers.remove(uuid);

        if(!offlinePlayer.isOnline()) {
            cachedRemove.put(offlinePlayer.getUniqueId(), extraHearts);
            activeLovers.remove(offlinePlayer.getUniqueId());
            return;
        }

        Player player = Bukkit.getPlayer(uuid);
        player.setMaxHealth(player.getMaxHealth() - extraHearts);
        activeLovers.remove(offlinePlayer.getUniqueId());
    }

    public boolean isCached(UUID uuid) {
        return cachedRemove.containsKey(uuid);
    }

    public void removeCachedPlayer(UUID uuid) {
        if(!isCached(uuid)) return;
        if(Bukkit.getPlayer(uuid) == null) return;

        Player player = Bukkit.getPlayer(uuid);

        player.setMaxHealth(player.getMaxHealth() - cachedRemove.get(uuid));
    }

    public boolean hasActiveLovers(UUID uuid) {
        return activeLovers.containsKey(uuid);
    }
}
