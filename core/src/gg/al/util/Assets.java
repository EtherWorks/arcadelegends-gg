package gg.al.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.cyphercove.gdx.covetools.assets.AssignmentAssetManager;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Created by Thomas Neumann on 02.06.2017.<br />
 */
public class Assets {
    public static class LevelAssets implements AssignmentAssetManager.AssetContainer {
        @AssignmentAssetManager.Asset("assets/characters/kevin/char.atlas")
        public TextureAtlas textureAtlas;
        public TextureRegion autoattack;
        public TextureRegion ezreal;
        public TextureRegion frontwalk;
        public TextureRegion rightsidewalk;

        //        @AssignmentAssetManager.Asset("assets/characters/kevin/autoattack.png")
//        public Texture autoattack;
//
//        @AssignmentAssetManager.Asset("assets/characters/kevin/ezreal.png")
//        public Texture ezreal;
//
//        @AssignmentAssetManager.Asset("assets/characters/kevin/frontwalk.png")
//        public Texture frontwalk;
//
//        @AssignmentAssetManager.Asset("assets/characters/kevin/rightsidewalk.png")
//        public Texture rightsidewalk;
//
//        @AssignmentAssetManager.Asset("assets/characters/kevin/sideviewsheet.png")
//        public Texture sideviewsheet;
//
//        @AssignmentAssetManager.Asset("assets/characters/kevin/squatsheet.png")
//        public Texture squatsheet;
        public TextureRegion sideviewsheet;
        public TextureRegion squatsheet;
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


        @AssignmentAssetManager.Asset("assets/ui/cooldown_gradient.png")
        public Texture cooldown_gradient;

        @AssignmentAssetManager.Asset("assets/ui/passive_cooldown_gradient.png")
        public Texture passive_cooldown_gradient;

        @AssignmentAssetManager.Asset("assets/ui/health_gradient.png")
        public Texture health_gradient;

        @AssignmentAssetManager.Asset("assets/ui/health_bar_gradient.png")
        public Texture health_bar_gradient;

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
                assetMap.put("textureAtlas", textureAtlas);
            }
            return assetMap;
        }

        @Override
        public String getAssetPathPrefix() {
            return "";
        }

        @Override
        public void onAssetsLoaded() {
            autoattack = textureAtlas.findRegion("kevin/autoattack");
            ezreal = textureAtlas.findRegion("kevin/ezreal");
            frontwalk = textureAtlas.findRegion("kevin/frontwalk");
            rightsidewalk = textureAtlas.findRegion("kevin/rightsidewalk");
            sideviewsheet = textureAtlas.findRegion("kevin/sideviewsheet");
            squatsheet = textureAtlas.findRegion("kevin/squatsheet");

            assetMap = getAssetMap();

            assetMap.put("autoattack", autoattack);
            assetMap.put("ezreal", ezreal);
            assetMap.put("frontwalk", frontwalk);
            assetMap.put("rightsidewalk", rightsidewalk);
            assetMap.put("sideviewsheet", sideviewsheet);
            assetMap.put("squatsheet", squatsheet);
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
