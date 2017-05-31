package gg.al.logic.component.data;

import gg.al.logic.component.StatComponent;

/**
 * Created by Thomas Neumann on 31.05.2017.<br />
 */
public interface IStatAffect {
    void applyValue(StatComponent stats);
    void applyPercentage(StatComponent stats);
}
