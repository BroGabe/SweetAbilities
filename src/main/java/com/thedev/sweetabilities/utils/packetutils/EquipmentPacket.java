package com.thedev.sweetabilities.utils.packetutils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.utils.enums.EquipmentType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EquipmentPacket {

    /**
     * Values: 4 - Helmet, 3 - Chestplate, 2 - Leggings, 1 - Boots, 0 - Hand
     * @param player
     * @param viewer
     * @param itemStack
     */

    public static void sendEquipmentPacket(UUID player, UUID viewer, ItemStack itemStack, EquipmentType equipmentType) {
        Player targetPlayer = Bukkit.getPlayer(player);
        Player viewerPlayer = Bukkit.getPlayer(viewer);

        if(targetPlayer == null || viewerPlayer == null) return;

        ProtocolManager protocolManager = SweetAbilities.getInst().getProtocolManager();
        PacketContainer packetContainer = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);

        int equipmentValue = 0;

        switch (equipmentType) {
            case HELMET:
                equipmentValue = 4;
                break;
            case CHESTPLATE:
                equipmentValue = 3;
                break;
            case LEGGINGS:
                equipmentValue = 2;
                break;
            case BOOTS: equipmentValue = 1;
        }

        // The player who gets their equipment changed
        packetContainer.getIntegers().write(0, targetPlayer.getEntityId());
        // Value is the armor equipment
        packetContainer.getIntegers().write(1, equipmentValue);

        packetContainer.getItemModifier().write(0, itemStack);

        protocolManager.sendServerPacket(viewerPlayer, packetContainer);
    }
}
