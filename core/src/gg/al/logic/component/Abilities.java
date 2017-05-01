package gg.al.logic.component;

import gg.al.characters.Genericus;
import gg.al.characters.IAbility;
import gg.al.characters.ICharacter;
import gg.al.characters.IPassive;
import gg.al.logic.data.IComponentDef;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Abilities extends PooledDefComponent {

    public ICharacter character;

    public IAbility ability1;
    public IAbility ability2;
    public IAbility ability3;
    public IAbility ability4;

    public IPassive passive;


    @Override
    protected void reset() {
        ability1 = null;
        ability2 = null;
        ability3 = null;
        ability4 = null;
        character = null;
        passive = null;
    }


    @Override
    public IComponentDef getDefaultDef() {
        return new AbilitiesDef();
    }

    @Override
    public void fromDef(IComponentDef def) {
        AbilitiesDef abilitiesDef = (AbilitiesDef) def;
        switch (((AbilitiesDef) def).characterName) {
            case "Genericus":
                character = new Genericus();
                character.setAbilities(this);
                break;
        }
    }

    public static class AbilitiesDef extends IComponentDef {
        String characterName;
    }
}
