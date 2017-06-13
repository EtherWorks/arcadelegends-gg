public class Assets {
    @AssignmentAssetManager.Asset("assets/map/rippedtileset.png")
    public Texture rippedtileset;
    @AssignmentAssetManager.Asset("assets/map/tileMap.tmx")
    public TiledMap tileMap;
    @AssignmentAssetManager.Asset("assets/map/tileset.png")
    public Texture tileset;
    @AssignmentAssetManager.Asset("assets/raw/bg.png")
    public Texture bg;
    @AssignmentAssetManager.Asset("assets/raw/booth.png")
    public Texture booth;
    @AssignmentAssetManager.Asset("assets/raw/button-bg.png")
    public Texture button_bg;
    @AssignmentAssetManager.Asset("assets/raw/button-pressed.png")
    public Texture button_pressed;
    @AssignmentAssetManager.Asset("assets/raw/button.png")
    public Texture button;
    @AssignmentAssetManager.Asset("assets/raw/font-export.fnt")
    public BitmapFont font_export_fnt;
    @AssignmentAssetManager.Asset("assets/raw/font-export.png")
    public Texture font_export_png;
    @AssignmentAssetManager.Asset("assets/raw/font.png")
    public Texture font;
    @AssignmentAssetManager.Asset("assets/raw/joystick-bg.png")
    public Texture joystick_bg;
    @AssignmentAssetManager.Asset("assets/raw/joystick-d.png")
    public Texture joystick_d;
    @AssignmentAssetManager.Asset("assets/raw/joystick-dl.png")
    public Texture joystick_dl;
    @AssignmentAssetManager.Asset("assets/raw/joystick-dr.png")
    public Texture joystick_dr;
    @AssignmentAssetManager.Asset("assets/raw/joystick-l.png")
    public Texture joystick_l;
    @AssignmentAssetManager.Asset("assets/raw/joystick-r.png")
    public Texture joystick_r;
    @AssignmentAssetManager.Asset("assets/raw/joystick-u.png")
    public Texture joystick_u;
    @AssignmentAssetManager.Asset("assets/raw/joystick-ul.png")
    public Texture joystick_ul;
    @AssignmentAssetManager.Asset("assets/raw/joystick-ur.png")
    public Texture joystick_ur;
    @AssignmentAssetManager.Asset("assets/raw/joystick.png")
    public Texture joystick;
    @AssignmentAssetManager.Asset("assets/raw/screen-export.fnt")
    public BitmapFont screen_export_fnt;
    @AssignmentAssetManager.Asset("assets/raw/screen-export.png")
    public Texture screen_export_png;
    @AssignmentAssetManager.Asset("assets/raw/screen.png")
    public Texture screen;
    @AssignmentAssetManager.Asset("assets/raw/title-export.fnt")
    public BitmapFont title_export_fnt;
    @AssignmentAssetManager.Asset("assets/raw/title-export.png")
    public Texture title_export_png;
    @AssignmentAssetManager.Asset("assets/raw/title.png")
    public Texture title;
    @AssignmentAssetManager.Asset("assets/raw/white.png")
    public Texture white;
    @AssignmentAssetManager.Asset("assets/sprites/awdstileset.png")
    public Texture awdstileset;
    @AssignmentAssetManager.Asset("assets/sprites/ez_back.png")
    public Texture ez_back;
    @AssignmentAssetManager.Asset("assets/sprites/ez_idle.png")
    public Texture ez_idle;
    @AssignmentAssetManager.Asset("assets/sprites/Factions.png")
    public Texture Factions;
    @AssignmentAssetManager.Asset("assets/sprites/knighttemplate.png")
    public Texture knighttemplate;
    @AssignmentAssetManager.Asset("assets/sprites/lawbringer.png")
    public Texture lawbringer;
    @AssignmentAssetManager.Asset("assets/sprites/lawbringer32.png")
    public Texture lawbringer32;
    @AssignmentAssetManager.Asset("assets/sprites/logo16.png")
    public Texture logo16;
    @AssignmentAssetManager.Asset("assets/sprites/logo32.png")
    public Texture logo32;
    @AssignmentAssetManager.Asset("assets/sprites/reticle.png")
    public Texture reticle;
    @AssignmentAssetManager.Asset("assets/sprites/splash/background.jpg")
    public Texture background;
    @AssignmentAssetManager.Asset("assets/sprites/splash/splash.png")
    public Texture splash;
    @AssignmentAssetManager.Asset("assets/sprites/tower.png")
    public Texture tower;
    @AssignmentAssetManager.Asset("assets/sprites/towerfull.png")
    public Texture towerfull;
    @AssignmentAssetManager.Asset("assets/sprites/towertop.png")
    public Texture towertop;
    @AssignmentAssetManager.Asset("assets/styles/dlgstyle/dlgbackground.png")
    public Texture dlgbackground;
    @AssignmentAssetManager.Asset("assets/styles/styles/bocklin.png")
    public Texture bocklin_png;
    @AssignmentAssetManager.Asset("assets/styles/styles/styles.atlas")
    public TextureAtlas styles_atlas;
    @AssignmentAssetManager.Asset("assets/styles/styles/styles.png")
    public Texture styles_png;
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
            assetMap.put("bitrush", bitrush);
            assetMap.put("autoattack", autoattack);
            assetMap.put("ezreal", ezreal);
            assetMap.put("frontwalk", frontwalk);
            assetMap.put("leftsidewalk", leftsidewalk);
            assetMap.put("rightsidewalk", rightsidewalk);
            assetMap.put("sideviewsheet", sideviewsheet);
            assetMap.put("squatsheet", squatsheet);
            assetMap.put("rippedtileset", rippedtileset);
            assetMap.put("tileMap", tileMap);
            assetMap.put("tileset", tileset);
            assetMap.put("bg", bg);
            assetMap.put("booth", booth);
            assetMap.put("button_bg", button_bg);
            assetMap.put("button_pressed", button_pressed);
            assetMap.put("button", button);
            assetMap.put("font_export_fnt", font_export_fnt);
            assetMap.put("font_export_png", font_export_png);
            assetMap.put("font", font);
            assetMap.put("joystick_bg", joystick_bg);
            assetMap.put("joystick_d", joystick_d);
            assetMap.put("joystick_dl", joystick_dl);
            assetMap.put("joystick_dr", joystick_dr);
            assetMap.put("joystick_l", joystick_l);
            assetMap.put("joystick_r", joystick_r);
            assetMap.put("joystick_u", joystick_u);
            assetMap.put("joystick_ul", joystick_ul);
            assetMap.put("joystick_ur", joystick_ur);
            assetMap.put("joystick", joystick);
            assetMap.put("screen_export_fnt", screen_export_fnt);
            assetMap.put("screen_export_png", screen_export_png);
            assetMap.put("screen", screen);
            assetMap.put("title_export_fnt", title_export_fnt);
            assetMap.put("title_export_png", title_export_png);
            assetMap.put("title", title);
            assetMap.put("white", white);
            assetMap.put("awdstileset", awdstileset);
            assetMap.put("ez_back", ez_back);
            assetMap.put("ez_idle", ez_idle);
            assetMap.put("Factions", Factions);
            assetMap.put("knighttemplate", knighttemplate);
            assetMap.put("lawbringer", lawbringer);
            assetMap.put("lawbringer32", lawbringer32);
            assetMap.put("logo16", logo16);
            assetMap.put("logo32", logo32);
            assetMap.put("reticle", reticle);
            assetMap.put("background", background);
            assetMap.put("splash", splash);
            assetMap.put("testmainscreen", testmainscreen);
            assetMap.put("tower", tower);
            assetMap.put("towerfull", towerfull);
            assetMap.put("towertop", towertop);
            assetMap.put("dlgbackground", dlgbackground);
            assetMap.put("background_textbutton", background_textbutton);
            assetMap.put("bocklin_fnt", bocklin_fnt);
            assetMap.put("bocklin_png", bocklin_png);
            assetMap.put("styles_atlas", styles_atlas);
            assetMap.put("styles_json", styles_json);
            assetMap.put("styles_png", styles_png);
        }
        return assetMap;
    }

}