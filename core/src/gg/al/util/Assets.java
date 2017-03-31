package gg.al.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import javax.xml.soap.Text;

/**
 * Created by Thomas Neumann on 18.03.2017.<br />
 * Utility interface holding {@link AssetDescriptor} to all assets.
 */
public interface Assets {
    AssetDescriptor<Music> PT_BITRUSH = new AssetDescriptor<>("assets/prototype/audio/bitrush.mp3", Music.class);
    AssetDescriptor<TiledMap> PT_TEST = new AssetDescriptor<>("assets/prototype/map/test.tmx", TiledMap.class);
    AssetDescriptor<Texture> PT_AWDSTILESET = new AssetDescriptor<>("assets/prototype/sprites/awdstileset.png", Texture.class);
    AssetDescriptor<Texture> PT_EZREAL = new AssetDescriptor<>("assets/prototype/sprites/ezreal.png", Texture.class);
    AssetDescriptor<Texture> PT_EZ_IDLE = new AssetDescriptor<>("assets/prototype/sprites/ez_idle.png", Texture.class);
    AssetDescriptor<Texture> PT_FACTIONS = new AssetDescriptor<>("assets/prototype/sprites/Factions.png", Texture.class);
    AssetDescriptor<Texture> PT_KNIGHTTEMPLATE = new AssetDescriptor<>("assets/prototype/sprites/knighttemplate.png", Texture.class);
    AssetDescriptor<Texture> PT_LAWBRINGER = new AssetDescriptor<>("assets/prototype/sprites/lawbringer.png", Texture.class);
    AssetDescriptor<Texture> PT_LAWBRINGER32 = new AssetDescriptor<>("assets/prototype/sprites/lawbringer32.png", Texture.class);
    AssetDescriptor<Texture> PT_LOGO16 = new AssetDescriptor<>("assets/prototype/sprites/logo16.png", Texture.class);
    AssetDescriptor<Texture> PT_LOGO32 = new AssetDescriptor<>("assets/prototype/sprites/logo32.png", Texture.class);
    AssetDescriptor<Texture> PT_RETICLE = new AssetDescriptor<>("assets/prototype/sprites/reticle.png", Texture.class);
    AssetDescriptor<Texture> PT_RIPPEDTILESET = new AssetDescriptor<>("assets/prototype/sprites/rippedtileset.png", Texture.class);
    AssetDescriptor<Texture> PT_BACKGROUND = new AssetDescriptor<>("assets/prototype/sprites/splash/background.jpg", Texture.class);
    AssetDescriptor<Texture> PT_SPLASH = new AssetDescriptor<>("assets/prototype/sprites/splash/splash.png", Texture.class);
    AssetDescriptor<Texture> PT_TILESET = new AssetDescriptor<>("assets/prototype/sprites/tileset.png", Texture.class);
    AssetDescriptor<Texture> PT_TOWER = new AssetDescriptor<>("assets/prototype/sprites/tower.png", Texture.class);
    AssetDescriptor<Texture> PT_TOWERFULL = new AssetDescriptor<>("assets/prototype/sprites/towerfull.png", Texture.class);
    AssetDescriptor<Texture> PT_TOWERTOP = new AssetDescriptor<>("assets/prototype/sprites/towertop.png", Texture.class);
    AssetDescriptor<BitmapFont> PT_TESTFONT = new AssetDescriptor<>("assets/prototype/styles/buttonfont/testfont.fnt", BitmapFont.class);
    AssetDescriptor<Texture> PT_TESTFONT1 = new AssetDescriptor<>("assets/prototype/styles/buttonfont/testfont1.png", Texture.class);
    AssetDescriptor<Texture> PT_TESTFONT2 = new AssetDescriptor<>("assets/prototype/styles/buttonfont/testfont2.png", Texture.class);
    AssetDescriptor<TextureAtlas> PT_TEXTBUTTONSTYLES_ATLAS = new AssetDescriptor<>("assets/prototype/styles/buttonfont/textbuttonstyles.atlas", TextureAtlas.class);
    AssetDescriptor<Skin> PT_TEXTBUTTONSTYLES_JSON = new AssetDescriptor<>("assets/prototype/styles/buttonfont/textbuttonstyles.json", Skin.class);
    AssetDescriptor<Texture> PT_TEXTBUTTONSTYLES_PNG = new AssetDescriptor<>("assets/prototype/styles/buttonfont/textbuttonstyles.png", Texture.class);
    AssetDescriptor<Texture> PT_TEXTBUTTONSTYLES2 = new AssetDescriptor<>("assets/prototype/styles/buttonfont/textbuttonstyles2.png", Texture.class);
    AssetDescriptor<Texture> PT_UP = new AssetDescriptor<>("assets/prototype/styles/buttonfont/up.png", Texture.class);
    AssetDescriptor<BitmapFont> PT_BOCKLIN = new AssetDescriptor<>("assets/prototype/styles/textbutton_style/bocklin.fnt", BitmapFont.class);
    AssetDescriptor<TextureAtlas> PT_TEXTBUTTON_ATLAS = new AssetDescriptor<>("assets/prototype/styles/textbutton_style/textbutton.atlas", TextureAtlas.class);
    AssetDescriptor<Skin> PT_TEXTBUTTON_JSON = new AssetDescriptor<>("assets/prototype/styles/textbutton_style/textbutton.json", Skin.class);
    AssetDescriptor<Texture> PT_TEXTBUTTON_PNG = new AssetDescriptor<>("assets/prototype/styles/textbutton_style/textbutton.png", Texture.class);
    AssetDescriptor<Texture> PT_TESTMAINSCREEN = new AssetDescriptor<>("assets/prototype/sprites/testmainscreen.jpg", Texture.class);
    AssetDescriptor<Skin> PT_NEON_JSON = new AssetDescriptor<>("assets/prototype/styles/neon_ui_style/neon.json", Skin.class);
    AssetDescriptor<Skin> PT_ARCADE_UI_JSON = new AssetDescriptor<>("assets/prototype/styles/arcade/arcade-ui.json", Skin.class);
    AssetDescriptor<Texture> PT_SELECTION = new AssetDescriptor<>("assets/prototype/styles/teststyle/selection.png", Texture.class);
}
