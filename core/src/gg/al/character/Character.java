package gg.al.character;

import gg.al.logic.component.StatComponent;

/**
 * Created by Thomas Neumann on 23.05.2017.<br />
 */
public abstract class Character {
    public abstract void castAbility1();

    public abstract void castAbility2();

    public abstract void castAbility3();

    public abstract void castAbility4();

    public abstract void tick(float delta);

    public abstract void affectStats(StatComponent statComponent);

}
