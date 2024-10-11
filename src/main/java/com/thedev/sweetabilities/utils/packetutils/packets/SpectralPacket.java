package com.thedev.sweetabilities.utils.packetutils.packets;

import com.thedev.sweetabilities.utils.packetutils.packettypes.ExpiringPacket;


public class SpectralPacket extends ExpiringPacket {

    public SpectralPacket(int priority) {
        super(priority);
    }

    @Override
    protected void runPacket() {
        setExpired(false);
    }

    @Override
    protected void cancelPacket() {
        setExpired(true);
    }


}
