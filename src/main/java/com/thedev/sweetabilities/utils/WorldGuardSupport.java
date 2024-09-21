package com.thedev.sweetabilities.utils;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class WorldGuardSupport {

    public static boolean isPlayerInSafeZone(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) return false;

        RegionContainer regionContainer = WorldGuardPlugin.inst().getRegionContainer();

        ApplicableRegionSet regionSet = Objects.requireNonNull(regionContainer.get(player.getWorld())).getApplicableRegions(player.getLocation());

        ProtectedRegion defaultRegion = regionContainer.get(player.getWorld()).getRegion("__global__");

        if(defaultRegion != null && defaultRegion.getFlag(DefaultFlag.PVP) == StateFlag.State.DENY) return true;

        for(ProtectedRegion region : regionSet) {
            if(region.getFlag(DefaultFlag.PVP) != null && region.getFlag(DefaultFlag.PVP) == StateFlag.State.DENY) {
                return true;
            }
        }

        return false;
    }
}
