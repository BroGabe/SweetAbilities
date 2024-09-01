package com.thedev.sweetabilities;

import com.thedev.sweetabilities.AbilityManager.AbilityManager;
import com.thedev.sweetabilities.AbilityManager.RotManager.RotMoveListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SweetAbilities extends JavaPlugin implements Listener {

    /**
     * Note:
     * For enchants that last multiple seconds and require a runnable
     * check if it should happen first, and only create the custom event
     * if it can happen.
     *
     * AbilityManager can hold all the maps and stuff such as
     * rot map, caked map, wrath map, etc.
     *
     * Cursed Mark: Puts player on a list for x seconds, players in the list take x2 damage
     * will be cancelled if player is already in the list.
     *
     * Equalizer:
     * Put player in a list for x seconds, take reduced damage.
     * Note: Enchant plugin will manage this, it will check if the player taking damage has Equalizer
     * then put the attacked player in a map with list of attackers. After x seconds the attacker will be removed
     * from the map and if the list is empty the defender will be removed. But if the list contains 3 attackers
     * then enchant will activate.
     *
     * Spectral:
     * puts defender in a list for x seconds, players in the list take no damage.
     * Will have a custom event SpectralDeflectEvent, which will deflect damage
     * but allow to be cancelled. So if x player has the ability to hit through
     * spectral, then they would.
     *
     * Rot & Decay:
     * Rot & Decay manager class will have a list of active rot players. Players with active rot
     * will set redstone blocks around the environment. Player move event will detect if a player
     * is standing on a rot block. Then a runnable will damage this player every second and detect if
     * the player is still on a rot block, if not, player will be removed from list. Rot blocks
     * will have custom MetaData.
     *
     * Caked:
     * Will put player in a list for a few seconds, player will take 15% more damage
     * Player will also have a fake cake for the helmet done through packets.
     *
     * Wrath:
     *
     */

    private static SweetAbilities inst;

    private AbilityManager abilityManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        abilityManager = new AbilityManager(this);

        Bukkit.getPluginManager().registerEvents(new RotMoveListener(abilityManager.getRotManager()), this);
        Bukkit.getPluginManager().registerEvents(this,this);

        inst = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public AbilityManager getAbilityManager() {
        return abilityManager;
    }

    public static SweetAbilities getInst() {
        return inst;
    }
}
