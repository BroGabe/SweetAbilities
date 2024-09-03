package com.thedev.sweetabilities;

import com.thedev.sweetabilities.AbilityManager.AbilityManager;
import com.thedev.sweetabilities.AbilityManager.CakedManager.CakedDamageListener;
import com.thedev.sweetabilities.AbilityManager.CursedMarkManager.CursedDamageListener;
import com.thedev.sweetabilities.AbilityManager.MirageManager.MirageItemListener;
import com.thedev.sweetabilities.AbilityManager.RotManager.RotMoveListener;
import com.thedev.sweetabilities.AbilityManager.SpectralManager.SpectralListener;
import com.thedev.sweetabilities.AbilityManager.WrathManager.WrathDeathListener;
import com.thedev.sweetabilities.Configuration.DefaultConfig;
import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public final class SweetAbilities extends JavaPlugin{

    private static SweetAbilities inst;

    private AbilityManager abilityManager;

    private DefaultConfig defaultConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        defaultConfig = new DefaultConfig(this);
        abilityManager = new AbilityManager(this);

        Bukkit.getPluginManager().registerEvents(new RotMoveListener(abilityManager.getRotManager()), this);
        Bukkit.getPluginManager().registerEvents(new CakedDamageListener(this, abilityManager.getCakedManager()), this);
        Bukkit.getPluginManager().registerEvents(new CursedDamageListener(this, getAbilityManager().getCursedMarkManager()), this);
        Bukkit.getPluginManager().registerEvents(new WrathDeathListener(getAbilityManager().getWrathManager()), this);
        Bukkit.getPluginManager().registerEvents(new SpectralListener(this, getAbilityManager().getSpectralManager()), this);
        Bukkit.getPluginManager().registerEvents(new MirageItemListener(), this);

        inst = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    public static SweetAbilities getInst() {
        return inst;
    }
}
