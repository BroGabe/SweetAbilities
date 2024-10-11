package com.thedev.sweetabilities.utils.packetutils;

import com.thedev.sweetabilities.SweetAbilities;
import com.thedev.sweetabilities.utils.enums.EquipmentType;
import com.thedev.sweetabilities.utils.packetutils.packettypes.ExpiringPacket;
import com.thedev.sweetabilities.utils.packetutils.packettypes.Packet;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PacketQueue {

    private final SweetAbilities plugin;

    public PacketQueue(SweetAbilities plugin) {
        this.plugin = plugin;

        startSchedulerTask();
    }

    private final Map<EquipmentType, Map<Class<? extends Packet>, Packet>> packetQueue = new HashMap<>();

    public void addPacket(EquipmentType equipmentType, Packet packet) {
        Map<Class<? extends Packet>, Packet> packetMap = packetQueue.computeIfAbsent(equipmentType, k -> new HashMap<>());

        packetMap.putIfAbsent(packet.getClass(), packet);
    }

    public boolean isPriority(EquipmentType equipmentType, Class<? extends Packet> clazz) {
        if (!packetQueue.containsKey(equipmentType) || !packetQueue.get(equipmentType).containsKey(clazz)) {
            return false;
        }

        Packet targetPacket = packetQueue.get(equipmentType).get(clazz);

        for (Packet otherPacket : packetQueue.get(equipmentType).values()) {
            if (targetPacket.compareTo(otherPacket) > 0) {
                return false;
            }
        }

        return true;
    }

    public boolean isPriority(EquipmentType equipmentType, Packet packet) {
        return isPriority(equipmentType, packet.getClass());
    }

    private void startSchedulerTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::removeExpiredTask, 5L, 5L);
    }

    private void removePacket(EquipmentType equipmentType, Class<? extends Packet> clazz) {
        if (!packetQueue.containsKey(equipmentType) || !packetQueue.get(equipmentType).containsKey(clazz)) {
            return;
        }

        Packet packet = packetQueue.get(equipmentType).get(clazz);
        packet.cancelPacket();

        packetQueue.get(equipmentType).remove(clazz);

        Packet topPacket = getTopPriorityPacket(equipmentType);

        if(topPacket == null) return;

        topPacket.runPacket();
    }

    private void removeExpiredTask() {
        for(EquipmentType equipmentType : packetQueue.keySet()) {
            Iterator<Packet> packetIterator = packetQueue.get(equipmentType).values().iterator();

            while(packetIterator.hasNext()) {
                Packet packet = packetIterator.next();

                if(!(packet instanceof ExpiringPacket)) continue;
                ExpiringPacket expiringPacket = (ExpiringPacket) packet;

                if(!expiringPacket.isExpired()) continue;
                expiringPacket.cancelPacket();
                packetIterator.remove();

                Packet topPacket = getTopPriorityPacket(equipmentType);
                if(topPacket == null) continue;
                topPacket.runPacket();
            }
        }
    }

    private Packet getTopPriorityPacket(EquipmentType equipmentType) {
        if (!packetQueue.containsKey(equipmentType) || packetQueue.get(equipmentType).isEmpty()) {
            return null;
        }

        Packet topPriorityPacket = null;

        for (Packet packet : packetQueue.get(equipmentType).values()) {
            if (topPriorityPacket == null || packet.compareTo(topPriorityPacket) < 0) {
                topPriorityPacket = packet;
            }
        }

        return topPriorityPacket;
    }
}
