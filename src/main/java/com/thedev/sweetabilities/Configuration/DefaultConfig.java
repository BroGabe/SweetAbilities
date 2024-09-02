package com.thedev.sweetabilities.Configuration;

import com.thedev.sweetabilities.SweetAbilities;

public class DefaultConfig {

    private final SweetAbilities plugin;

    public DefaultConfig(SweetAbilities plugin) {
        this.plugin = plugin;
    }

    public int ROT_DAMAGE() {
        return plugin.getConfig().getInt("settings.rot-ability.damage");
    }

    public int CAKE_DAMAGE_PERCENTAGE() {
        return plugin.getConfig().getInt("settings.cake-ability.damage-percent");
    }

    public int CURSED_DAMAGE_PERCENTAGE() {
        return plugin.getConfig().getInt("settings.cursed-mark-ability.damage-percent");
    }

    public double WRATH_DAMAGE() {
        return plugin.getConfig().getDouble("settings.wrath-ability.damage");
    }

    public double ACTIVATION_PER_LEVEL(String string) {
        switch (string.toUpperCase()) {
            case "ROT_ABILITY":
                return plugin.getConfig().getDouble("settings.rot-ability.activation-per-level");
            case "CAKE_ABILITY":
                return plugin.getConfig().getDouble("settings.cake-ability.activation-per-level");
            case "SPECTRAL_ABILITY":
                return plugin.getConfig().getDouble("settings.spectral-ability.activation-per-level");
            case "CURSED_MARK_ABILITY":
                return plugin.getConfig().getDouble("settings.cursed-mark-ability.activation-per-level");
            case "WRATH_ABILITY":
                return plugin.getConfig().getDouble("settings.wrath-ability.activation-per-level");
            default:
                return 0.5;

        }
    }
}
