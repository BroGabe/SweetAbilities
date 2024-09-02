package com.thedev.sweetabilities.AbilityManager;

import com.thedev.sweetabilities.AbilityManager.CakedManager.CakedManager;
import com.thedev.sweetabilities.AbilityManager.CursedMarkManager.CursedMarkManager;
import com.thedev.sweetabilities.AbilityManager.RotManager.RotManager;
import com.thedev.sweetabilities.AbilityManager.SpectralManager.SpectralManager;
import com.thedev.sweetabilities.AbilityManager.WrathManager.WrathManager;
import com.thedev.sweetabilities.SweetAbilities;

public class AbilityManager {

    private final RotManager rotManager;

    private final CakedManager cakedManager;

    private final CursedMarkManager cursedMarkManager;

    private final WrathManager wrathManager;

    private final SpectralManager spectralManager;

    public AbilityManager(SweetAbilities plugin) {

        rotManager = new RotManager(plugin);
        cakedManager = new CakedManager();
        cursedMarkManager = new CursedMarkManager(plugin);
        wrathManager = new WrathManager(plugin);
        spectralManager = new SpectralManager(plugin);
    }

    public WrathManager getWrathManager() {
        return wrathManager;
    }

    public SpectralManager getSpectralManager() {
        return spectralManager;
    }

    public RotManager getRotManager() {
        return rotManager;
    }

    public CakedManager getCakedManager() {
        return cakedManager;
    }

    public CursedMarkManager getCursedMarkManager() {
        return cursedMarkManager;
    }
}
