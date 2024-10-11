package com.thedev.sweetabilities.utils.packetutils.packets;

import com.thedev.sweetabilities.utils.packetutils.packettypes.ExpiringPacket;


public class SpectralPacket extends ExpiringPacket {

    public SpectralPacket(int priority) {
        super(priority);
    }

    @Override
    public void runPacket() {
        setExpired(false);
    }

    @Override
    public void cancelPacket() {
        setExpired(true);
    }


}
