package com.thedev.sweetabilities.utils.packetutils.packettypes;

public abstract class ExpiringPacket extends Packet{

    private boolean expired = true;

    public ExpiringPacket(int priority) {
        super(priority);
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isExpired() {
        return expired;
    }
}
