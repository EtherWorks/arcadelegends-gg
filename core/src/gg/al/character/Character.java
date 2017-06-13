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
 * Created by Thomas Neumann on 23.05.2017.<br>
 * Main sandbox superclass for all characters.
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

    /**
     * Returns the current cooldown for the given ability from the given {@link StatComponent}
     *
     * @param ability
     * @param stats
     * @return the current cooldown
     */
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

    /**
     * @return whether the character is casting
     */
    public boolean isCasting() {
        return casting[TRAIT] || casting[ABILITY_1] || casting[ABILITY_2] || casting[ABILITY_3] || casting[ABILITY_4];
    }

    /**
     * @param ability
     * @return whether the character is casting the given ability
     */
    public boolean isCasting(int ability) {
        return casting[ability];
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    public void setArcadeWorld(ArcadeWorld arcadeWorld) {
        this.arcadeWorld = arcadeWorld;
    }

    /**
     * Method for starting the cast of an ability.
     * Checks prerequisites like cooldown and cost before starting the cast.
     *
     * @param abilityInd the ability to be evoked
     */
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

    /**
     * Called every time the entity enginge updates.
     * Updates the current cooldowns and cast times, and casts the abilities if ready.
     *
     * @param delta the time since last frame
     */
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

    /**
     * Starts one auto-attack.
     */
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

    /**
     * Stops the current running auto-attack completely.
     */
    public void stopAttack() {
        preparingAttack = false;
        attackTimer = 0;
        castAttackSpeed = 0;
        finishedAttack = true;
        attackAccomplished = false;
        prepTime = 0;
    }

    /**
     * Stops the auto-attack, and clears the target of the character.
     */
    public void resetAttack() {
        stopAttack();
        getComponent(entityID, CharacterComponent.class).targetId = -1;
    }

    /**
     * Stops the running auto-attack only if it is not in the accomplished phase.
     */
    public void cancelAttack() {
        if (preparingAttack) {
            resetAttack();
        }
    }

    /**
     * Logic portion of the auto-attack. May be overridden by subclasses.
     *
     * @param enemyId the enemy to apply damage to
     */
    protected void attack(int enemyId) {
        StatComponent stats = getComponent(entityID, StatComponent.class);
        float amount = stats.getCurrentStat(StatComponent.BaseStat.attackDamage);
        if (random.nextFloat() <= stats.getCurrentStat(StatComponent.BaseStat.criticalStrikeChance))
            amount *= (2 + stats.getCurrentStat(StatComponent.BaseStat.criticalStrikeDamage));
        Damage dmg = new Damage(Damage.DamageType.Physical,
                amount,
                stats.getCurrentStat(StatComponent.BaseStat.armorPenetration));
        getComponent(enemyId, StatComponent.class).damages.add(dmg);
    }

    /**
     * Called at the start of the cast of an ability.
     * Useful for setting current animation.
     *
     * @param ability
     */
    protected abstract void castBegin(int ability);

    public String[] getIconNames() {
        return ICON_NAMES;
    }

    /**
     * Called every time the engine steps.
     *
     * @param delta the time since last frame
     */
    protected abstract void onTick(float delta);

    /**
     * Method for checking whether this ability can begin cast.
     *
     * @param abilityInd the ability to check
     * @return whether this ability can be cast or not
     */
    protected abstract boolean checkOnCast(int abilityInd);

    /**
     * Method for checking whether this ability can finish casting.
     *
     * @param abilityInd the ability to check
     * @return whether this ability can finish casting or not
     */
    protected abstract boolean checkOnTrigger(int abilityInd);

    /**
     * Called after successful cast of ability.
     * Apply logic of abilities here.
     *
     * @param abilityInd the ability to be cast
     */
    protected abstract void onCast(int abilityInd);

    /**
     * Called everytime the {@link StatComponent} updates, useful for buffs.
     *
     * @param statComponent the {@link StatComponent to be manipulated}
     */
    public abstract void affectStats(StatComponent statComponent);

    /**
     * @return percent of auto-attack completion
     */
    public float getAttackCompletionPercent() {
        return ((1 / castAttackSpeed) - attackTimer) / (1 / castAttackSpeed);
    }

    public boolean hasFinishedAttack() {
        return finishedAttack;
    }

    /**
     * Called right before calling {@link Character#checkOnCast(int)}.
     * Should be overridden by subclasses if the cost should be modified.
     *
     * @param ability the ability for the cost
     * @param cost    current cost of the ability
     * @return new cost of the ability
     */
    protected float modifyCost(int ability, float cost) {
        return cost;
    }

    /**
     * Sandbox method for getting a {@link Component}
     *
     * @param entityID id of entity
     * @param type     type of component
     * @param <T>      type of component
     * @return the component, or null
     */
    protected <T extends Component> T getComponent(int entityID, Class<T> type) {
        return arcadeWorld.getEntityWorld().getMapper(type).get(entityID);
    }

    /**
     * Sandbox method for checking the range between two entities.
     *
     * @param casterID entity A
     * @param targetID entity B
     * @param maxRange the maximum range to which to check
     * @return if A is in range of B
     */
    protected boolean checkRange(int casterID, int targetID, float maxRange) {
        Vector2 vector = vectorPool.obtain();
        PositionComponent caster = getComponent(casterID, PositionComponent.class);
        PositionComponent target = getComponent(targetID, PositionComponent.class);
        boolean result = Math.abs(vector.set(caster.position).dst(target.position)) <= maxRange;
        vectorPool.free(vector);
        return result;
    }

    /**
     * Sandbox method for applying {@link Damage} to an entity.
     *
     * @param entity target of the {@link Damage}
     * @param damage {@link Damage} describing the amount
     */
    protected void applyDamage(int entity, Damage damage) {
        StatComponent statComponent = getComponent(entity, StatComponent.class);
        if (statComponent != null)
            statComponent.damages.add(damage);
    }

    /**
     * Sandbox method for applying {@link StatusEffect} to an entity.
     *
     * @param entityID   target of the {@link StatusEffect}
     * @param effect     {@link Damage} describing the amount
     * @param effectName name of the {@link StatusEffect}
     */
    protected void applyStatusEffect(int entityID, StatusEffect effect, String effectName) {
        StatComponent statComponent = getComponent(entityID, StatComponent.class);
        if (statComponent != null)
            statComponent.statusEffects.put(effectName, effect);
    }

    /**
     * Sandbox method to check if character is moving.
     *
     * @return whether the character is moving
     */
    protected boolean isMoving() {
        return !getComponent(entityID, PhysicComponent.class).body.getLinearVelocity().equals(Vector2.Zero);
    }

    /**
     * Sandbox method for returning the entities in an area
     *
     * @param mid   center of the area to search
     * @param start left upper corner relative to the center
     * @param end   right lower corner relative to the center
     * @return entities in area
     */
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

    /**
     * Same as {@link Character#getEntitiesInArea(Vector2, Vector2, Vector2)}, only centered around the character.
     *
     * @param start left upper corner relative to the center
     * @param end   right lower corner relative to the center
     * @return entities in area
     */
    protected IntArray getEntitiesInArea(Vector2 start, Vector2 end) {
        return getEntitiesInArea(getComponent(entityID, PositionComponent.class).position, start, end);
    }

    /**
     * Sandbox method for spawning an entity.
     *
     * @param entity
     * @param arguments
     * @return
     */
    protected int spawn(Entities entity, EntityArguments arguments) {
        return arcadeWorld.spawn(entity, arguments);
    }

    /**
     * Sandbox method for loading {@link EntityArguments}.
     *
     * @param fileName name of the {@link EntityArguments} file
     * @return the {@link EntityArguments} of this file
     */
    public EntityArguments getArguments(String fileName) {
        return arcadeWorld.getArguments(fileName);
    }

    /**
     * Sandbox method for deleting an entity.
     *
     * @param id the entity to be deleted
     */
    public void delete(int id) {
        arcadeWorld.delete(id);
    }

    /**
     * Sandbox method for getting the mouse-position.
     *
     * @return the mouse-position
     */
    public Vector2 getMousePos() {
        Ray ray = arcadeWorld.getCam().getPickRay(AL.input.getX(), AL.input.getY());
        Vector3 vec = new Vector3();
        Intersector.intersectRayPlane(ray, arcadeWorld.getMapHitbox(), vec);
        return new Vector2(vec.x, vec.y);
    }

    /**
     * Same as {@link Character#getMousePos()}, only returning rounded coordinates.
     *
     * @return the rounded mouse-position
     */
    public Vector2 getRoundMousePos() {
        Ray ray = arcadeWorld.getCam().getPickRay(AL.input.getX(), AL.input.getY());
        Vector3 vec = new Vector3();
        Intersector.intersectRayPlane(ray, arcadeWorld.getMapHitbox(), vec);
        return new Vector2(Math.round(vec.x), Math.round(vec.y));
    }

    /**
     * Sandbox method for getting the current character position.
     *
     * @return current character position
     */
    public Vector2 getCharacterPosition() {

        PhysicComponent phys = getComponent(entityID, PhysicComponent.class);

        if (phys != null)
            return phys.body.getPosition();
        PositionComponent pos = getComponent(entityID, PositionComponent.class);
        return pos.position;
    }

    /**
     * Sandbox method for getting the entity under the mouse-pointer.
     *
     * @return entity under mousep-ointer
     */
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

    /**
     * Sandbox method for getting a {@link Tile} from the map.
     *
     * @param vec coordinates of the {@link Tile}
     * @return the found {@link Tile}
     */
    public Tile getTile(Vector2 vec) {
        return arcadeWorld.getTile(vec);
    }

    /**
     * Same as {@link Character#getTile(Vector2)}, only with int coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the found tile
     */
    public Tile getTile(int x, int y) {
        return arcadeWorld.getLogicMap().getTile(x, y);
    }

    public Color getAbilityOverlay(int ability) {
        return null;
    }

    public AIExtension getAIExtension() {
        return null;
    }

    /**
     * Interface for AI control
     */
    public interface AIExtension {
        float getRange(int ability);

        boolean disabled(int ability);
    }
}
