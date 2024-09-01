package com.thedev.sweetabilities.AbilityManager;

import com.thedev.sweetabilities.AbilityManager.RotManager.RotManager;
import com.thedev.sweetabilities.SweetAbilities;

public class AbilityManager {

    private final SweetAbilities plugin;

    private final RotManager rotManager;

    public AbilityManager(SweetAbilities plugin) {
        this.plugin = plugin;

        rotManager = new RotManager(plugin);
    }

    public RotManager getRotManager() {
        return rotManager;
    }
}
