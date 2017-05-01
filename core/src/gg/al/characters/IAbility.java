package gg.al.characters;

import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.Stats;

/**
 * Created by Thomas Neumann on 01.05.2017.
 */
public interface IAbility {
    void cast(ArcadeWorld arcadeWorld);

    void passive(Stats stats);
}
