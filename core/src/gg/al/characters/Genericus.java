package gg.al.characters;

import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.Abilities;
import gg.al.logic.component.Stats;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 01.05.2017.
 */
@Slf4j
public class Genericus implements ICharacter {

    @Override
    public void setAbilities(Abilities abilities) {
        abilities.passive = new PassiveSearingBlade();
        abilities.ability1 = new AbilitySwordStrike();
        abilities.ability2 = new AbilityHealCircle();
        abilities.ability3 = new AbilityBladeDash();
        abilities.ability4 = new AbilitySevenStrikes();
    }

    public class AbilitySwordStrike implements IAbility {

        @Override
        public void cast(ArcadeWorld arcadeWorld) {
            log.debug("AbilitySwordStrike");
        }

        @Override
        public void passive(Stats stats) {

        }
    }

    public class AbilityHealCircle implements IAbility {
        @Override
        public void cast(ArcadeWorld arcadeWorld) {
            log.debug("AbilityHealCircle");
        }

        @Override
        public void passive(Stats stats) {

        }
    }

    public class AbilityBladeDash implements IAbility {
        @Override
        public void cast(ArcadeWorld arcadeWorld) {
            log.debug("AbilityBladeDash");
        }

        @Override
        public void passive(Stats stats) {

        }
    }

    public class AbilitySevenStrikes implements IAbility {
        @Override
        public void cast(ArcadeWorld arcadeWorld) {
            log.debug("AbilitySevenStrikes");
        }

        @Override
        public void passive(Stats stats) {

        }
    }

    public class PassiveSearingBlade implements IPassive {

        @Override
        public void tick(float delta) {

        }

        @Override
        public void passive(Stats stats) {

        }
    }
}
