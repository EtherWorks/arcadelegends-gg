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

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public void setArcadeWorld(ArcadeWorld arcadeWorld) {
        this.arcadeWorld = arcadeWorld;
    }

    public void cast(int abilityInd) {
        StatComponent statComponent = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(entityID);
        if (cooldowns[abilityInd] == 0) {
            switch (abilityInd) {
                case PASSIVE:
                    cooldowns[PASSIVE] = statComponent.getCurrentStat(StatComponent.BaseStat.cooldownPassive);
                    break;
                case ABILITY_1:
                    cooldowns[ABILITY_1] = statComponent.getCurrentStat(StatComponent.BaseStat.cooldownAbility1);
                    break;
                case ABILITY_2:
                    cooldowns[ABILITY_2] = statComponent.getCurrentStat(StatComponent.BaseStat.cooldownAbility2);
                    break;
                case ABILITY_3:
                    cooldowns[ABILITY_3] = statComponent.getCurrentStat(StatComponent.BaseStat.cooldownAbility3);
                    break;
                case ABILITY_4:
                    cooldowns[ABILITY_4] = statComponent.getCurrentStat(StatComponent.BaseStat.cooldownAbility4);
                    break;
            }
            onCast(abilityInd);
        }
    }

    public void tick(float delta) {
        for (int i = 0; i < cooldowns.length; i++) {
            if (cooldowns[i] != 0)
                cooldowns[i] = cooldowns[i] - delta <= 0 ? 0 : cooldowns[i] - delta;
        }
        onTick(delta);
    }

    protected abstract void onTick(float delta);

    protected abstract void onCast(int abilityInd);

    public abstract void affectStats(StatComponent statComponent);

}
