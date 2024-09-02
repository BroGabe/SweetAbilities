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
}
