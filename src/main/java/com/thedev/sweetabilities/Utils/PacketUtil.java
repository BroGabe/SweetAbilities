package com.thedev.sweetabilities.Utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
