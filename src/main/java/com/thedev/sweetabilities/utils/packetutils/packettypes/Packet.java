package com.thedev.sweetabilities.utils.packetutils.packettypes;

import org.jetbrains.annotations.NotNull;

public abstract class Packet implements Comparable<Packet>{

    private final int priority;

    public Packet(int priority) {
        this.priority = priority;
    }

    public abstract void runPacket();

    public abstract void cancelPacket();

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(@NotNull Packet other) {
        // Check if one of the packets is an ExpiringPacket
        boolean thisIsExpiring = this instanceof ExpiringPacket;
        boolean otherIsExpiring = other instanceof ExpiringPacket;

        // If this packet is expiring and the other isn't, give this one higher priority
        if (thisIsExpiring && !otherIsExpiring) {
            return -1; // This packet has higher priority
        }

        // If the other packet is expiring and this one isn't, give the other one higher priority
        if (!thisIsExpiring && otherIsExpiring) {
            return 1; // The other packet has higher priority
        }

        return Integer.compare(other.getPriority(), this.priority); // Higher priority first
    }
}
