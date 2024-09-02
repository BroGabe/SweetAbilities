package com.thedev.sweetabilities.AbilityManager.SpectralManager;

import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.Utils.ItemBuilder;
import com.thedev.sweetabilities.Utils.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class SpectralManager {

    private final SweetAbilities plugin;

    private final Set<UUID> spectralPlayers = new HashSet<>();

    private final List<ItemStack> glassItemList = new ArrayList<>();

    private BukkitTask spectralTask;

    public SpectralManager(SweetAbilities plugin) {
        this.plugin = plugin;

        ItemBuilder glassOne = new ItemBuilder(Material.STAINED_GLASS, "Spectral Ability", 1, 6, null, "");
        ItemBuilder glassTwo = new ItemBuilder(Material.STAINED_GLASS, "Spectral Ability", 1, 3, null, "");
        ItemBuilder glassThree = new ItemBuilder(Material.STAINED_GLASS, "Spectral Ability", 1, 4, null, "");
        ItemBuilder glassFour = new ItemBuilder(Material.STAINED_GLASS, "Spectral Ability", 1, 5, null, "");

        glassItemList.addAll(Arrays.asList(glassOne.getItem(), glassTwo.getItem(), glassThree.getItem(), glassFour.getItem()));
    }

    public boolean isPlayerSpectral(UUID uuid) {
        return spectralPlayers.contains(uuid);
    }

    private void addSpectralPlayer(UUID uuid) {
        spectralPlayers.add(uuid);
    }

    private void removeSpectralPlayer(UUID uuid) {
        spectralPlayers.remove(uuid);
    }

    public void spectralPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        if(player == null || !player.isOnline() || isPlayerSpectral(player.getUniqueId())) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 80, 3));

        addSpectralPlayer(uuid);

        startSpectralTask();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(player == null || !player.isOnline()) {
                removeSpectralPlayer(uuid);
                return;
            }

            removeSpectralPlayer(uuid);

            ItemStack itemStack = player.getInventory().getHelmet();

            PacketUtil.changePlayerHelmetPacket(player.getUniqueId(), player.getUniqueId(), itemStack);

            player.getNearbyEntities(15, 15, 15).forEach(nearbyEntity -> {
                if(nearbyEntity instanceof Player) {
                    Player nearbyPlayer = (Player) nearbyEntity;
                    PacketUtil.changePlayerHelmetPacket(player.getUniqueId(), nearbyPlayer.getUniqueId(), itemStack);
                }
            });
        }, 80L);

    }

    private void startSpectralTask() {
        if(spectralTask != null && Bukkit.getScheduler().isCurrentlyRunning(spectralTask.getTaskId())) return;

        spectralTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if(spectralPlayers.isEmpty()) {
                spectralTask.cancel();
                return;
            }

            Iterator<UUID> uuidIterator = spectralPlayers.iterator();

            while (uuidIterator.hasNext()) {
                UUID playerUUID = uuidIterator.next();

                Player spectralPlayer = Bukkit.getPlayer(playerUUID);
                if(spectralPlayer == null || !spectralPlayer.isOnline()) {
                    uuidIterator.remove();
                    continue;
                }

                Random random = new Random();

                ItemStack glass = glassItemList.get(random.nextInt(glassItemList.size()));

                PacketUtil.changePlayerHelmetPacket(spectralPlayer.getUniqueId(), spectralPlayer.getUniqueId(), glass);

                spectralPlayer.getNearbyEntities(15, 15, 15).forEach(nearbyEntity -> {
                    if(nearbyEntity instanceof Player) {
                        Player nearbyPlayer = (Player) nearbyEntity;

                        PacketUtil.changePlayerHelmetPacket(spectralPlayer.getUniqueId(), nearbyPlayer.getUniqueId(), glass);
                    }
                });
            }
        }, 0L, 5L);
    }


}
