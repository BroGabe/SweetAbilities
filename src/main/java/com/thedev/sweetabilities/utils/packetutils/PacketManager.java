package com.thedev.sweetabilities.utils.packetutils;

import com.thedev.sweetabilities.SweetAbilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PacketManager {

    private final SweetAbilities plugin;

    private final Map<UUID, PacketQueue> playerPacketQueueMap = new HashMap<>();

    public PacketManager(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    public PacketQueue getPlayerPacketQueue(UUID uuid) {
        playerPacketQueueMap.putIfAbsent(uuid, new PacketQueue(plugin));

        return playerPacketQueueMap.get(uuid);
    }
}
