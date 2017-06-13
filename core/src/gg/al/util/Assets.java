package gg.al.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.cyphercove.gdx.covetools.assets.AssignmentAssetManager;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Created by Thomas Neumann on 02.06.2017.<br />
 * Container class containing all preset assets for the {@link AssignmentAssetManager}.
 */
public class Assets {
    public static class LevelAssets implements AssignmentAssetManager.AssetContainer {
//        @AssignmentAssetManager.Asset("assets/characters/kevin/char.atlas")
//        public TextureAtlas textureAtlas;

        @AssignmentAssetManager.Asset("assets/characters/kevin/attacksprites.png")
        public Texture attackspritesTexture;
        public TextureRegion attacksprites;

        @AssignmentAssetManager.Asset("assets/characters/kevin/ezreal.png")
        public Texture ezrealTexture;
        public TextureRegion ezreal;

        @AssignmentAssetManager.Asset("assets/characters/kevin/frontviewsprites.png")
        public Texture frontviewspritesTexture;
        public TextureRegion frontviewsprites;

        @AssignmentAssetManager.Asset("assets/characters/kevin/walkingsprites.png")
        public Texture walkingspritesTexture;
        public TextureRegion walkingsprites;

        @AssignmentAssetManager.Asset("assets/characters/kevin/backviewsprites.png")
        public Texture backviewspritesTexture;
        public TextureRegion backviewsprites;

        @AssignmentAssetManager.Asset("assets/characters/kevin/squatsprites.png")
        public Texture squatspritesTexture;
        public TextureRegion squatsprites;

        @AssignmentAssetManager.Asset("assets/characters/kevin/ultsprites.png")
        public Texture ultspritesTexture;
        public TextureRegion ultsprites;

        @AssignmentAssetManager.Asset("assets/characters/kevin/smashsprites.png")
        public Texture smashspritesTexture;
        public TextureRegion smashsprites;

        @AssignmentAssetManager.Asset("assets/characters/kevin/stabsprites.png")
        public Texture stabspritesTexture;
        public TextureRegion stabsprites;


        @AssignmentAssetManager.Asset("assets/map/tileMap.tmx")
        public TiledMap tileMap;

        @AssignmentAssetManager.Asset("assets/audio/sword_1.wav")
        public Sound sword_1;
        @AssignmentAssetManager.Asset("assets/audio/sword_2.wav")
        public Sound sword_2;
        @AssignmentAssetManager.Asset("assets/audio/sword_3.wav")
        public Sound sword_3;
        @AssignmentAssetManager.Asset("assets/audio/sword_4.wav")
        public Sound sword_4;

        @AssignmentAssetManager.Asset("assets/ui/ui.png")
        public Texture uioverlay;

        @AssignmentAssetManager.Asset("assets/ui/uifont.fnt")
        public BitmapFont uifont;

        @AssignmentAssetManager.Asset("assets/ui/uifont_small.fnt")
        public BitmapFont uifontsmall;

        @AssignmentAssetManager.Asset("assets/ui/uifont_smaller.fnt")
        public BitmapFont uifontsmaller;


        @AssignmentAssetManager.Asset("assets/ui/cooldown_gradient.png")
        public Texture cooldown_gradient;

        @AssignmentAssetManager.Asset("assets/ui/passive_cooldown_gradient.png")
        public Texture passive_cooldown_gradient;

        @AssignmentAssetManager.Asset("assets/ui/health_gradient.png")
        public Texture health_gradient;

        @AssignmentAssetManager.Asset("assets/ui/health_bar_gradient.png")
        public Texture health_bar_gradient;
        @AssignmentAssetManager.Asset("assets/ui/xp_gradient.png")
        public Texture xp_gradient;


        @AssignmentAssetManager.Asset("assets/ui/ability1.png")
        public Texture ability1;

        @AssignmentAssetManager.Asset("assets/ui/ability2.png")
        public Texture ability2;

        @AssignmentAssetManager.Asset("assets/ui/ability3.png")
        public Texture ability3;

        @AssignmentAssetManager.Asset("assets/ui/ability4.png")
        public Texture ability4;

        @AssignmentAssetManager.Asset("assets/ui/trait.png")
        public Texture trait;

        @AssignmentAssetManager.Asset("assets/ui/stats.png")
        public Texture uistats;

        @AssignmentAssetManager.Asset("assets/audio/rocketlauncher.wav")
        public Sound rocketLauncher;

        @AssignmentAssetManager.Asset("assets/audio/boom.wav")
        public Sound boom;

        private BiMap<String, Object> assetMap;

        public <T> T get(String name) {
            return (T) getAssetMap().get(name);
        }

        public String getName(Object obj) {
            return getAssetMap().inverse().get(obj);
        }

        private BiMap<String, Object> getAssetMap() {
            if (assetMap == null) {
                assetMap = HashBiMap.create();
                assetMap.put("tileMap", tileMap);
//                assetMap.put("textureAtlas", textureAtlas);
                assetMap.put("ability1", ability1);
                assetMap.put("ability2", ability2);
                assetMap.put("ability3", ability3);
                assetMap.put("ability4", ability4);
                assetMap.put("trait", trait);
            }
            return assetMap;
        }

        @Override
        public String getAssetPathPrefix() {
            return "";
        }

        @Override
        public void onAssetsLoaded() {
//            attacksprites = textureAtlas.findRegion("kevin/attacksprites");
//            ezreal = textureAtlas.findRegion("kevin/ezreal");
//            frontviewsprites = textureAtlas.findRegion("kevin/frontviewsprites");
//            walkingsprites = textureAtlas.findRegion("kevin/walkingsprites");
//            backviewsprites = textureAtlas.findRegion("kevin/backviewsprites");
//            squatsprites = textureAtlas.findRegion("kevin/squatsprites");
//            ultsprites = textureAtlas.findRegion("kevin/ultsprites");
//            smashsprites = textureAtlas.findRegion("kevin/smashsprites");
//            stabsprites = textureAtlas.findRegion("kevin/stabsprites");

            attacksprites = new TextureRegion(attackspritesTexture);
            ezreal = new TextureRegion(ezrealTexture);
            frontviewsprites = new TextureRegion(frontviewspritesTexture);
            walkingsprites = new TextureRegion(walkingspritesTexture);
            backviewsprites = new TextureRegion(backviewspritesTexture);
            squatsprites = new TextureRegion(squatspritesTexture);
            ultsprites = new TextureRegion(ultspritesTexture);
            smashsprites = new TextureRegion(smashspritesTexture);
            stabsprites = new TextureRegion(stabspritesTexture);

            assetMap = getAssetMap();

            assetMap.put("attacksprites", attacksprites);
            assetMap.put("ezreal", ezreal);
            assetMap.put("frontviewsprites", frontviewsprites);
            assetMap.put("walkingsprites", walkingsprites);
            assetMap.put("backviewsprites", backviewsprites);
            assetMap.put("squatsprites", squatsprites);
            assetMap.put("stabsprites", stabsprites);
            assetMap.put("smashsprites", smashsprites);
            assetMap.put("ultsprites", ultsprites);
        }
    }

    public static class MenuAssets {

        @AssignmentAssetManager.Asset("assets/sprites/testmainscreen.jpg")
        public Texture testmainscreen;

        @AssignmentAssetManager.Asset("assets/styles/styles/styles.json")
        public Skin styles_json;

        @AssignmentAssetManager.Asset("assets/audio/bitrush.mp3")
        public Music bitrush;
    }

    public static class SettingsAssets {
        @AssignmentAssetManager.Asset("assets/styles/styles/styles.json")
        public Skin styles_json;

        @AssignmentAssetManager.Asset("assets/sprites/testmainscreen.jpg")
        public Texture testmainscreen;

        @AssignmentAssetManager.Asset("assets/styles/styles/bocklin.fnt")
        public BitmapFont bocklin_fnt;

        @AssignmentAssetManager.Asset("assets/styles/styles/background_textbutton.png")
        public Texture background_textbutton;

        @AssignmentAssetManager.Asset("assets/styles/dlgstyle/dlgbackground.png")
        public Texture dlgbackground;
    }
}
