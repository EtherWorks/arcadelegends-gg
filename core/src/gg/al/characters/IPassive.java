package gg.al.characters;

import gg.al.logic.component.Stats;

/**
 * Created by Thomas Neumann on 01.05.2017.
 */
public interface IPassive {
    void tick(float delta);

    void passive(Stats stats);
}
