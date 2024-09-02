package com.thedev.sweetabilities.Utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.Utils.PacketWrappers.WrapperPlayServerEntityDestroy;
import com.thedev.sweetabilities.Utils.PacketWrappers.WrapperPlayServerNamedEntitySpawn;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PacketUtil {

    public static void changePlayerHelmetPacket(UUID player, UUID viewer, ItemStack itemStack) {
        Player targetPlayer = Bukkit.getPlayer(player);
        Player viewerPlayer = Bukkit.getPlayer(viewer);

        if(targetPlayer == null || viewerPlayer == null) return;

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packetContainer = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);

        packetContainer.getIntegers().write(0, targetPlayer.getEntityId());
        packetContainer.getIntegers().write(1, 4);

        packetContainer.getItemModifier().write(0, itemStack);

        protocolManager.sendServerPacket(viewerPlayer, packetContainer);
    }
}
