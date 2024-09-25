package com.thedev.sweetabilities.abilities;

import com.thedev.sweetabilities.abilities.bleedmanager.BleedManager;
import com.thedev.sweetabilities.abilities.cakedmanager.CakedManager;
import com.thedev.sweetabilities.abilities.cursedmarkmanager.CursedMarkManager;
import com.thedev.sweetabilities.abilities.diabloability.DiabloAbilityManager;
import com.thedev.sweetabilities.abilities.loversmanager.LoversManager;
import com.thedev.sweetabilities.abilities.miragemanager.MirageManager;
import com.thedev.sweetabilities.abilities.rotmanager.RotManager;
import com.thedev.sweetabilities.abilities.spectralmanager.SpectralManager;
import com.thedev.sweetabilities.abilities.wrathmanager.WrathManager;
import com.thedev.sweetabilities.SweetAbilities;

public class AbilityManager {

    private final RotManager rotManager;

    private final CakedManager cakedManager;

    private final CursedMarkManager cursedMarkManager;

    private final WrathManager wrathManager;

    private final SpectralManager spectralManager;

    private final MirageManager mirageManager;

    private final BleedManager bleedManager;

    private final LoversManager loversManager;

    private final DiabloAbilityManager diabloAbilityManager;

    public AbilityManager(SweetAbilities plugin) {

        rotManager = new RotManager(plugin);
        cakedManager = new CakedManager();
        cursedMarkManager = new CursedMarkManager(plugin);
        wrathManager = new WrathManager(plugin);
        spectralManager = new SpectralManager(plugin);
        mirageManager = new MirageManager(plugin);
        loversManager = new LoversManager(plugin);
        bleedManager = new BleedManager(plugin);
        diabloAbilityManager = new DiabloAbilityManager(plugin);
    }

    public WrathManager getWrathManager() {
        return wrathManager;
    }

    public DiabloAbilityManager getDiabloAbilityManager() {
        return diabloAbilityManager;
    }

    public LoversManager getLoversManager() {
        return loversManager;
    }

    public MirageManager getMirageManager() {
        return mirageManager;
    }

    public BleedManager getBleedManager() {
        return bleedManager;
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
