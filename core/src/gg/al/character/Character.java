package gg.al.character;

import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.StatComponent;

/**
 * Created by Thomas Neumann on 23.05.2017.<br />
 */
public abstract class Character {

    protected final int PASSIVE = 0;
    protected final int ABILITY_1 = 1;
    protected final int ABILITY_2 = 2;
    protected final int ABILITY_3 = 3;
    protected final int ABILITY_4 = 4;

    protected final float[] cooldowns;

    private ArcadeWorld arcadeWorld;
    protected int entityID;

    public Character() {
        this.cooldowns = new float[5];
    }

    public void cast(int abilityInd) {
        StatComponent statComponent = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(entityID);
        if (cooldowns[abilityInd] == 0) {
            switch (abilityInd)
            {
                case PASSIVE:
                    cooldowns[PASSIVE] = statComponent.getCurrentStat(StatComponent.BaseStat.cooldownPassive);
                    break;
            }
            onCast(abilityInd);
        }
    }

    public void tick(float delta)
    {

    }

    protected abstract void onTick(float delta);

    protected abstract void onCast(int abilityInd);

    public abstract void affectStats(StatComponent statComponent);

}
