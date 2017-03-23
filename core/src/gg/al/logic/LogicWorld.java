package gg.al.logic;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import lombok.Getter;

/**
 * Created by Thomas Neumann on 23.03.2017.<br />
 */
public class LogicWorld implements Disposable{
    @Getter
    private World physicsWorld;
    @Getter
    private EntityWorld entityWorld;

    @Getter
    private TiledMap map;
    @Getter
    private float worldViewRotation;

    public LogicWorld(TiledMap map, float worldViewRotation) {
        this.map = map;
        this.worldViewRotation = worldViewRotation;
        physicsWorld = new World(Vector2.Zero, true);

        WorldConfiguration worldConfiguration = new WorldConfigurationBuilder().build();
        entityWorld = new EntityWorld(worldConfiguration);
    }

    public void step(float delta)
    {
        physicsWorld.step(1 / 45f, 6, 2);
        entityWorld.setDelta(delta);
        entityWorld.process();
    }

    public void render()
    {

    }


    @Override
    public void dispose() {

    }
}
