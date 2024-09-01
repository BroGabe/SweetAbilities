package com.thedev.sweetabilities.Abilities;

import java.util.UUID;

public interface Ability {

    void doAbility(UUID player);

    void doAbility(UUID player, UUID target);
}
