package gg.al.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import gg.al.config.*;

/**
 * Created by Thomas Neumann on 18.03.2017.
 * Environment class holding references to the {@link ArcadeLegendsGame} and
 * {@link Config} instances. The references are held in public static fields which allows static access to all sub systems.
 * <p>
 * This is normally a design faux pas but in this case is better than the alternatives.
 */
public class AL extends Gdx {
    public static ArcadeLegendsGame game;

    public static Config config;
    public static ConfigEditor cedit;
    public static IVideoConfig cvideo;
    public static IAudioConfig caudio;
    public static IMiscellaneousConfig cmisc;
    public static IGameplayConfig cgameplay;
    public static IInputConfig cinput;
    public static AssetManager asset;
}
