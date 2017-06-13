package gg.al.logic.component.data;

import gg.al.logic.component.StatComponent;

/**
 * Created by Thomas Neumann on 31.05.2017.<br />
 * Interface denoting an object capable of affecting a {@link StatComponent}.
 */
public interface IStatAffect {
    /**
     * Applies flat stat calculation.
     *
     * @param stats the {@link StatComponent} to be changed
     */
    void applyValue(StatComponent stats);

    /**
     * Applies percentage stat calculation.
     *
     * @param stats the {@link StatComponent} to be changed
     */
    void applyPercentage(StatComponent stats);

    /**
     * Applies percentage base-stat calculation.
     *
     * @param stats the {@link StatComponent} to be changed
     */
    void applyBasePercentage(StatComponent stats);
}
