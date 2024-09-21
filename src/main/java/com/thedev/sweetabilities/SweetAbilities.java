package com.thedev.sweetabilities;

import com.thedev.sweetabilities.abilities.AbilityManager;
import com.thedev.sweetabilities.abilities.bleedmanager.BleedListeners;
import com.thedev.sweetabilities.abilities.cakedmanager.CakedDamageListener;
import com.thedev.sweetabilities.abilities.cursedmarkmanager.CursedDamageListener;
import com.thedev.sweetabilities.abilities.loversmanager.LoversListener;
import com.thedev.sweetabilities.abilities.miragemanager.MirageItemListener;
import com.thedev.sweetabilities.abilities.rotmanager.RotDamageListener;
import com.thedev.sweetabilities.abilities.rotmanager.RotMoveListener;
import com.thedev.sweetabilities.abilities.spectralmanager.SpectralListener;
import com.thedev.sweetabilities.abilities.wrathmanager.WrathDeathListener;
import com.thedev.sweetabilities.configuration.DefaultConfig;
import com.thedev.sweetabilities.configuration.PlayerData;
import com.thedev.sweetabilities.listeners.TestingListener;
import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public final class SweetAbilities extends JavaPlugin{

    private static SweetAbilities inst;

    private AbilityManager abilityManager;

    private DefaultConfig defaultConfig;

    private PlayerData playerData;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        defaultConfig = new DefaultConfig(this);
        playerData = new PlayerData(this);
        abilityManager = new AbilityManager(this);


        Bukkit.getPluginManager().registerEvents(new RotMoveListener(abilityManager.getRotManager()), this);
        Bukkit.getPluginManager().registerEvents(new CakedDamageListener(this, abilityManager.getCakedManager()), this);
        Bukkit.getPluginManager().registerEvents(new CursedDamageListener(this, getAbilityManager().getCursedMarkManager()), this);
        Bukkit.getPluginManager().registerEvents(new WrathDeathListener(getAbilityManager().getWrathManager()), this);
        Bukkit.getPluginManager().registerEvents(new SpectralListener(this, getAbilityManager().getSpectralManager()), this);
        Bukkit.getPluginManager().registerEvents(new MirageItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new RotDamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new LoversListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BleedListeners(this), this);
        Bukkit.getPluginManager().registerEvents(new TestingListener(this), this);

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

    public PlayerData getPlayerData() {
        return playerData;
    }
}
