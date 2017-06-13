package gg.al.character;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.Pool;
import gg.al.game.AL;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.CharacterComponent;
import gg.al.logic.component.PhysicComponent;
import gg.al.logic.component.PositionComponent;
import gg.al.logic.component.StatComponent;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.StatusEffect;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;
import gg.al.logic.map.Tile;

import java.util.Random;

/**
 * Created by Thomas Neumann on 23.05.2017.<br />
 */
public abstract class Character {

    public static final int TRAIT = 0;
    public static final int ABILITY_1 = 1;
    public static final int ABILITY_2 = 2;
    public static final int ABILITY_3 = 3;
    public static final int ABILITY_4 = 4;
    private static String[] ICON_NAMES = new String[5];
    protected final float[] cooldownTimer;
    protected final float[] castTimer;
    protected final boolean[] casting;
    protected final Object[] extraData;
    protected final float[] cooldowns;
    protected final float[] costs;
    protected final Pool<Vector2> vectorPool;
    protected boolean preparingAttack;
    protected boolean finishedAttack;
    protected boolean attackAccomplished;
    protected float castAttackSpeed;
    protected float attackTimer;
    protected float prepTime;
    protected int entityID;
    protected Random random;
    private ArcadeWorld arcadeWorld;

    public Character() {
        this.cooldownTimer = new float[5];
        this.castTimer = new float[5];
        this.casting = new boolean[5];
        this.extraData = new Object[5];
        this.cooldowns = new float[5];
        this.costs = new float[5];
        this.random = new Random();

        finishedAttack = true;

        vectorPool = new Pool<Vector2>() {
            @Override
            protected Vector2 newObject() {
                return new Vector2();
            }

            @Override
            protected void reset(Vector2 object) {
                object.setZero();
            }
        };
    }

    public static float getCooldown(int ability, StatComponent stats) {
        switch (ability) {
            case TRAIT:
                return stats.getCurrentStat(StatComponent.BaseStat.cooldownTrait)
                        - stats.getCurrentStat(StatComponent.BaseStat.cooldownTrait) * stats.getCurrentStat(StatComponent.BaseStat.cooldownReduction);
            case ABILITY_1:
                return stats.getCurrentStat(StatComponent.BaseStat.cooldownAbility1)
                        - stats.getCurrentStat(StatComponent.BaseStat.cooldownAbility1) * stats.getCurrentStat(StatComponent.BaseStat.cooldownReduction);
            case ABILITY_2:
                return stats.getCurrentStat(StatComponent.BaseStat.cooldownAbility2)
                        - stats.getCurrentStat(StatComponent.BaseStat.cooldownAbility2) * stats.getCurrentStat(StatComponent.BaseStat.cooldownReduction);
            case ABILITY_3:
                return stats.getCurrentStat(StatComponent.BaseStat.cooldownAbility3)
                        - stats.getCurrentStat(StatComponent.BaseStat.cooldownAbility3) * stats.getCurrentStat(StatComponent.BaseStat.cooldownReduction);
            case ABILITY_4:
                return stats.getCurrentStat(StatComponent.BaseStat.cooldownAbility4)
                        - stats.getCurrentStat(StatComponent.BaseStat.cooldownAbility4) * stats.getCurrentStat(StatComponent.BaseStat.cooldownReduction);
        }
        throw new IllegalArgumentException("No ability with index" + ability);
    }

    public boolean isCasting() {
        return casting[TRAIT] || casting[ABILITY_1] || casting[ABILITY_2] || casting[ABILITY_3] || casting[ABILITY_4];
    }

    public boolean isCasting(int ability) {
        return casting[ability];
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public void setArcadeWorld(ArcadeWorld arcadeWorld) {
        this.arcadeWorld = arcadeWorld;
    }

    public void cast(int abilityInd) {
        StatComponent statComponent = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(entityID);
        if (cooldownTimer[abilityInd] == 0) {
            float cooldown = 0;
            float cost = 0;
            float castTime = 0;
            switch (abilityInd) {
                case TRAIT:
                    cooldown = statComponent.getCurrentStat(StatComponent.BaseStat.cooldownTrait);
                    cost = statComponent.getCurrentStat(StatComponent.BaseStat.costTrait);
                    castTime = statComponent.getCurrentStat(StatComponent.BaseStat.castTimeTrait);
                    break;
                case ABILITY_1:
                    cooldown = statComponent.getCurrentStat(StatComponent.BaseStat.cooldownAbility1);
                    cost = statComponent.getCurrentStat(StatComponent.BaseStat.costAbility1);
                    castTime = statComponent.getCurrentStat(StatComponent.BaseStat.castTimeAbility1);
                    break;
                case ABILITY_2:
                    cooldown = statComponent.getCurrentStat(StatComponent.BaseStat.cooldownAbility2);
                    cost = statComponent.getCurrentStat(StatComponent.BaseStat.costAbility2);
                    castTime = statComponent.getCurrentStat(StatComponent.BaseStat.castTimeAbility2);
                    break;
                case ABILITY_3:
                    cooldown = statComponent.getCurrentStat(StatComponent.BaseStat.cooldownAbility3);
                    cost = statComponent.getCurrentStat(StatComponent.BaseStat.costAbility3);
                    castTime = statComponent.getCurrentStat(StatComponent.BaseStat.castTimeAbility3);
                    break;
                case ABILITY_4:
                    cooldown = statComponent.getCurrentStat(StatComponent.BaseStat.cooldownAbility4);
                    cost = statComponent.getCurrentStat(StatComponent.BaseStat.costAbility4);
                    castTime = statComponent.getCurrentStat(StatComponent.BaseStat.castTimeAbility4);
                    break;
            }
            cooldown -= cooldown * statComponent.getCurrentStat(StatComponent.BaseStat.cooldownReduction);
            cost = modifyCost(abilityInd, cost);
            if (!isCasting() && statComponent.getRuntimeStat(StatComponent.RuntimeStat.resource) - cost >= 0) {
                if (checkOnCast(abilityInd)) {
                    cooldowns[abilityInd] = cooldown;
                    costs[abilityInd] = cost;
                    casting[abilityInd] = true;
                    castTimer[abilityInd] = castTime;
                    castBegin(abilityInd);
                }
            }
        }
    }

    public float getCooldownTimer(int ability) {
        return cooldownTimer[ability];
    }

    public void tick(float delta) {
        for (int i = 0; i < cooldownTimer.length; i++) {
            if (cooldownTimer[i] != 0)
                cooldownTimer[i] = cooldownTimer[i] - delta <= 0 ? 0 : cooldownTimer[i] - delta;
            if (casting[i]) {
                castTimer[i] -= delta;
                if (castTimer[i] <= 0) {
                    castTimer[i] = 0;
                    casting[i] = false;
                    StatComponent statComponent = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(entityID);
                    if (statComponent.getRuntimeStat(StatComponent.RuntimeStat.resource) - costs[i] >= 0 && checkOnTrigger(i)) {
                        statComponent.addRuntimeStat(StatComponent.RuntimeStat.resource, -costs[i]);
                        cooldownTimer[i] = cooldowns[i];
                        onCast(i);
                    }
                }
            }
        }
        if (!finishedAttack && (preparingAttack || attackAccomplished)) {
            attackTimer -= delta;
            if (attackTimer <= prepTime * (1 / castAttackSpeed) && !attackAccomplished) {
                CharacterComponent character = getComponent(entityID, CharacterComponent.class);
                if (character.targetId != -1) {
                    StatComponent stats = getComponent(entityID, StatComponent.class);
                    PositionComponent otherPos = getComponent(character.targetId, PositionComponent.class);
                    if (otherPos != null) {
                        if (checkRange(entityID, character.targetId, stats.getCurrentStat(StatComponent.BaseStat.attackRange))) {
                            attack(character.targetId);
                            preparingAttack = false;
                            attackAccomplished = true;
                        }
                    }
                } else {
                    stopAttack();
                }
            }
            if (attackTimer <= 0) {
                stopAttack();
            }
        }
        onTick(delta);
    }

    public void startAttack() {
        if (!preparingAttack && !attackAccomplished) {
            StatComponent stats = getComponent(entityID, StatComponent.class);
            preparingAttack = true;
            finishedAttack = false;
            attackAccomplished = false;
            attackTimer = 1 / stats.getCurrentStat(StatComponent.BaseStat.attackSpeed);
            castAttackSpeed = stats.getCurrentStat(StatComponent.BaseStat.attackSpeed);
            prepTime = stats.getCurrentStat(StatComponent.BaseStat.attackPrepTime);
        }
    }

    public void stopAttack() {
        preparingAttack = false;
        attackTimer = 0;
        castAttackSpeed = 0;
        finishedAttack = true;
        attackAccomplished = false;
        prepTime = 0;
    }

    public void resetAttack() {
        stopAttack();
        getComponent(entityID, CharacterComponent.class).targetId = -1;
    }

    public void cancelAttack() {
        if (preparingAttack) {
            resetAttack();
        }
    }

    public void attack(int enemyId) {
        StatComponent stats = getComponent(entityID, StatComponent.class);
        float amount = stats.getCurrentStat(StatComponent.BaseStat.attackDamage);
        if (random.nextFloat() <= stats.getCurrentStat(StatComponent.BaseStat.criticalStrikeChance))
            amount *= (2 + stats.getCurrentStat(StatComponent.BaseStat.criticalStrikeDamage));
        Damage dmg = new Damage(Damage.DamageType.Physical,
                amount,
                stats.getCurrentStat(StatComponent.BaseStat.armorPenetration));
        getComponent(enemyId, StatComponent.class).damages.add(dmg);
    }

    protected abstract void castBegin(int ability);

    public String[] getIconNames() {
        return ICON_NAMES;
    }

    protected abstract void onTick(float delta);

    protected abstract boolean checkOnCast(int abilityInd);

    protected abstract boolean checkOnTrigger(int abilityInd);

    protected abstract void onCast(int abilityInd);

    public abstract void affectStats(StatComponent statComponent);

    public float getAttackCompletionPercent() {
        return ((1 / castAttackSpeed) - attackTimer) / (1 / castAttackSpeed);
    }

    public boolean hasFinishedAttack() {
        return finishedAttack;
    }

    protected float modifyCost(int ability, float cost) {
        return cost;
    }

    protected <T extends Component> T getComponent(int entityID, Class<T> type) {
        return arcadeWorld.getEntityWorld().getMapper(type).get(entityID);
    }

    protected boolean checkRange(int casterID, int targetID, float maxRange) {
        Vector2 vector = vectorPool.obtain();
        PositionComponent caster = getComponent(casterID, PositionComponent.class);
        PositionComponent target = getComponent(targetID, PositionComponent.class);
        boolean result = Math.abs(vector.set(caster.position).dst(target.position)) <= maxRange;
        vectorPool.free(vector);
        return result;
    }

    protected void applyDamage(int entity, Damage damage) {
        StatComponent statComponent = getComponent(entity, StatComponent.class);
        if (statComponent != null)
            statComponent.damages.add(damage);
    }

    protected void applyStatusEffect(int entityID, StatusEffect effect, String effectName) {
        StatComponent statComponent = getComponent(entityID, StatComponent.class);
        if (statComponent != null)
            statComponent.statusEffects.put(effectName, effect);
    }

    protected boolean isMoving() {
        return !getComponent(entityID, PhysicComponent.class).body.getLinearVelocity().equals(Vector2.Zero);
    }

    protected IntArray getEntitiesInArea(Vector2 mid, Vector2 start, Vector2 end) {
        IntArray array = new IntArray();
        for (float x = mid.x + Math.min(start.x, end.x); x <= mid.x + Math.max(start.x, end.x); x++) {
            for (float y = mid.y + Math.min(start.y, end.y); y <= mid.y + Math.max(start.y, end.y); y++) {
                try {
                    Tile t = arcadeWorld.getTile((int) x, (int) y);
                    if (t.getEntities().size != 0)
                        array.add(t.getEntities().first());
                } catch (IndexOutOfBoundsException ex) {
                    continue;
                }
            }
        }
        return array;
    }

    protected IntArray getEntitiesInArea(Vector2 start, Vector2 end) {
        return getEntitiesInArea(getComponent(entityID, PositionComponent.class).position, start, end);
    }

    protected int spawn(Entities entity, EntityArguments arguments) {
        return arcadeWorld.spawn(entity, arguments);
    }

    public EntityArguments getArguments(String fileName) {
        return arcadeWorld.getArguments(fileName);
    }

    public void delete(int id) {
        arcadeWorld.delete(id);
    }

    public Vector2 getMousePos() {
        Ray ray = arcadeWorld.getCam().getPickRay(AL.input.getX(), AL.input.getY());
        Vector3 vec = new Vector3();
        Intersector.intersectRayPlane(ray, arcadeWorld.getMapHitbox(), vec);
        return new Vector2(vec.x, vec.y);
    }

    public Vector2 getRoundMousePos() {
        Ray ray = arcadeWorld.getCam().getPickRay(AL.input.getX(), AL.input.getY());
        Vector3 vec = new Vector3();
        Intersector.intersectRayPlane(ray, arcadeWorld.getMapHitbox(), vec);
        return new Vector2(Math.round(vec.x), Math.round(vec.y));
    }

    public Vector2 getCharacterPosition() {

        PhysicComponent phys = getComponent(entityID, PhysicComponent.class);

        if (phys != null)
            return phys.body.getPosition();
        PositionComponent pos = getComponent(entityID, PositionComponent.class);
        return pos.position;
    }

    public int getEntityAtMouse() {
        Vector2 mousePos = getMousePos();
        Tile t = getTile(Math.round(mousePos.x), Math.round(mousePos.y));
        if (t.getEntities().size == 0)
            return -1;
        IntSet.IntSetIterator iterator = t.getEntities().iterator();
        ComponentMapper<StatComponent> statMapper = arcadeWorld.getEntityWorld().getMapper(StatComponent.class);
        while (iterator.hasNext) {
            int entity = iterator.next();
            if (statMapper.has(entity))
                return entity;
        }
        return -1;
    }

    public Tile getTile(Vector2 vec) {
        return arcadeWorld.getTile(vec);
    }

    public Tile getTile(int x, int y) {
        return arcadeWorld.getLogicMap().getTile(x, y);
    }

    public Color getAbilityOverlay(int ability) {
        return null;
    }

    public AIExtension getAIExtension() {
        return null;
    }

    public interface AIExtension {
        float getRange(int ability);
        boolean disabled(int ability);
    }
}
