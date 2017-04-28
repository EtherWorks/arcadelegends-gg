public abstract class Assets {
	private static BiMap<String, AssetDescriptor> assetMap;

	public static AssetDescriptor get(String name) {
		return getAssetMap().get(name);
	}

	public static String getName(AssetDescriptor descriptor) {
		return assetMap.inverse().get(descriptor);
	}
	private static Map<String, AssetDescriptor> getAssetMap() {
		if(assetMap != null)
			return assetMap;
		else
		{
			assetMap = new HashBiMap.create();
			assetMap.put("PT_BITRUSH", PT_BITRUSH);
			assetMap.put("PT_TEST", PT_TEST);
			assetMap.put("PT_BG", PT_BG);
			assetMap.put("PT_BOOTH", PT_BOOTH);
			assetMap.put("PT_BUTTON_BG", PT_BUTTON_BG);
			assetMap.put("PT_BUTTON_PRESSED", PT_BUTTON_PRESSED);
			assetMap.put("PT_BUTTON", PT_BUTTON);
			assetMap.put("PT_FONT_EXPORT_FNT", PT_FONT_EXPORT_FNT);
			assetMap.put("PT_FONT_EXPORT_PNG", PT_FONT_EXPORT_PNG);
			assetMap.put("PT_FONT", PT_FONT);
			assetMap.put("PT_JOYSTICK_BG", PT_JOYSTICK_BG);
			assetMap.put("PT_JOYSTICK_D", PT_JOYSTICK_D);
			assetMap.put("PT_JOYSTICK_DL", PT_JOYSTICK_DL);
			assetMap.put("PT_JOYSTICK_DR", PT_JOYSTICK_DR);
			assetMap.put("PT_JOYSTICK_L", PT_JOYSTICK_L);
			assetMap.put("PT_JOYSTICK_R", PT_JOYSTICK_R);
			assetMap.put("PT_JOYSTICK_U", PT_JOYSTICK_U);
			assetMap.put("PT_JOYSTICK_UL", PT_JOYSTICK_UL);
			assetMap.put("PT_JOYSTICK_UR", PT_JOYSTICK_UR);
			assetMap.put("PT_JOYSTICK", PT_JOYSTICK);
			assetMap.put("PT_SCREEN_EXPORT_FNT", PT_SCREEN_EXPORT_FNT);
			assetMap.put("PT_SCREEN_EXPORT_PNG", PT_SCREEN_EXPORT_PNG);
			assetMap.put("PT_SCREEN", PT_SCREEN);
			assetMap.put("PT_TITLE_EXPORT_FNT", PT_TITLE_EXPORT_FNT);
			assetMap.put("PT_TITLE_EXPORT_PNG", PT_TITLE_EXPORT_PNG);
			assetMap.put("PT_TITLE", PT_TITLE);
			assetMap.put("PT_WHITE", PT_WHITE);
			assetMap.put("PT_AWDSTILESET", PT_AWDSTILESET);
			assetMap.put("PT_EZREAL", PT_EZREAL);
			assetMap.put("PT_EZ_IDLE", PT_EZ_IDLE);
			assetMap.put("PT_FACTIONS", PT_FACTIONS);
			assetMap.put("PT_KNIGHTTEMPLATE", PT_KNIGHTTEMPLATE);
			assetMap.put("PT_LAWBRINGER", PT_LAWBRINGER);
			assetMap.put("PT_LAWBRINGER32", PT_LAWBRINGER32);
			assetMap.put("PT_LOGO16", PT_LOGO16);
			assetMap.put("PT_LOGO32", PT_LOGO32);
			assetMap.put("PT_RETICLE", PT_RETICLE);
			assetMap.put("PT_RIPPEDTILESET", PT_RIPPEDTILESET);
			assetMap.put("PT_BACKGROUND", PT_BACKGROUND);
			assetMap.put("PT_SPLASH", PT_SPLASH);
			assetMap.put("PT_TESTMAINSCREEN", PT_TESTMAINSCREEN);
			assetMap.put("PT_TILESET", PT_TILESET);
			assetMap.put("PT_TOWER", PT_TOWER);
			assetMap.put("PT_TOWERFULL", PT_TOWERFULL);
			assetMap.put("PT_TOWERTOP", PT_TOWERTOP);
			assetMap.put("PT_SIDEVIEWSHEET", PT_SIDEVIEWSHEET);
			assetMap.put("PT_BACKGROUND_TEXTBUTTON", PT_BACKGROUND_TEXTBUTTON);
			assetMap.put("PT_BOCKLIN_FNT", PT_BOCKLIN_FNT);
			assetMap.put("PT_BOCKLIN_PNG", PT_BOCKLIN_PNG);
			assetMap.put("PT_STYLES_ATLAS", PT_STYLES_ATLAS);
			assetMap.put("PT_STYLES_JSON", PT_STYLES_JSON);
			assetMap.put("PT_STYLES_PNG", PT_STYLES_PNG);
			return assetMap;
		}
	}
	public static AssetDescriptor<Music> PT_BITRUSH = new AssetDescriptor<>("assets/prototype/audio/bitrush.mp3", Music.class);
	public static AssetDescriptor<TiledMap> PT_TEST = new AssetDescriptor<>("assets/prototype/map/test.tmx", TiledMap.class);
	public static AssetDescriptor<Texture> PT_BG = new AssetDescriptor<>("assets/prototype/raw/bg.png", Texture.class);
	public static AssetDescriptor<Texture> PT_BOOTH = new AssetDescriptor<>("assets/prototype/raw/booth.png", Texture.class);
	public static AssetDescriptor<Texture> PT_BUTTON_BG = new AssetDescriptor<>("assets/prototype/raw/button-bg.png", Texture.class);
	public static AssetDescriptor<Texture> PT_BUTTON_PRESSED = new AssetDescriptor<>("assets/prototype/raw/button-pressed.png", Texture.class);
	public static AssetDescriptor<Texture> PT_BUTTON = new AssetDescriptor<>("assets/prototype/raw/button.png", Texture.class);
	public static AssetDescriptor<BitmapFont> PT_FONT_EXPORT_FNT = new AssetDescriptor<>("assets/prototype/raw/font-export.fnt", BitmapFont.class);
	public static AssetDescriptor<Texture> PT_FONT_EXPORT_PNG = new AssetDescriptor<>("assets/prototype/raw/font-export.png", Texture.class);
	public static AssetDescriptor<Texture> PT_FONT = new AssetDescriptor<>("assets/prototype/raw/font.png", Texture.class);
	public static AssetDescriptor<Texture> PT_JOYSTICK_BG = new AssetDescriptor<>("assets/prototype/raw/joystick-bg.png", Texture.class);
	public static AssetDescriptor<Texture> PT_JOYSTICK_D = new AssetDescriptor<>("assets/prototype/raw/joystick-d.png", Texture.class);
	public static AssetDescriptor<Texture> PT_JOYSTICK_DL = new AssetDescriptor<>("assets/prototype/raw/joystick-dl.png", Texture.class);
	public static AssetDescriptor<Texture> PT_JOYSTICK_DR = new AssetDescriptor<>("assets/prototype/raw/joystick-dr.png", Texture.class);
	public static AssetDescriptor<Texture> PT_JOYSTICK_L = new AssetDescriptor<>("assets/prototype/raw/joystick-l.png", Texture.class);
	public static AssetDescriptor<Texture> PT_JOYSTICK_R = new AssetDescriptor<>("assets/prototype/raw/joystick-r.png", Texture.class);
	public static AssetDescriptor<Texture> PT_JOYSTICK_U = new AssetDescriptor<>("assets/prototype/raw/joystick-u.png", Texture.class);
	public static AssetDescriptor<Texture> PT_JOYSTICK_UL = new AssetDescriptor<>("assets/prototype/raw/joystick-ul.png", Texture.class);
	public static AssetDescriptor<Texture> PT_JOYSTICK_UR = new AssetDescriptor<>("assets/prototype/raw/joystick-ur.png", Texture.class);
	public static AssetDescriptor<Texture> PT_JOYSTICK = new AssetDescriptor<>("assets/prototype/raw/joystick.png", Texture.class);
	public static AssetDescriptor<BitmapFont> PT_SCREEN_EXPORT_FNT = new AssetDescriptor<>("assets/prototype/raw/screen-export.fnt", BitmapFont.class);
	public static AssetDescriptor<Texture> PT_SCREEN_EXPORT_PNG = new AssetDescriptor<>("assets/prototype/raw/screen-export.png", Texture.class);
	public static AssetDescriptor<Texture> PT_SCREEN = new AssetDescriptor<>("assets/prototype/raw/screen.png", Texture.class);
	public static AssetDescriptor<BitmapFont> PT_TITLE_EXPORT_FNT = new AssetDescriptor<>("assets/prototype/raw/title-export.fnt", BitmapFont.class);
	public static AssetDescriptor<Texture> PT_TITLE_EXPORT_PNG = new AssetDescriptor<>("assets/prototype/raw/title-export.png", Texture.class);
	public static AssetDescriptor<Texture> PT_TITLE = new AssetDescriptor<>("assets/prototype/raw/title.png", Texture.class);
	public static AssetDescriptor<Texture> PT_WHITE = new AssetDescriptor<>("assets/prototype/raw/white.png", Texture.class);
	public static AssetDescriptor<Texture> PT_AWDSTILESET = new AssetDescriptor<>("assets/prototype/sprites/awdstileset.png", Texture.class);
	public static AssetDescriptor<Texture> PT_EZREAL = new AssetDescriptor<>("assets/prototype/sprites/ezreal.png", Texture.class);
	public static AssetDescriptor<Texture> PT_EZ_IDLE = new AssetDescriptor<>("assets/prototype/sprites/ez_idle.png", Texture.class);
	public static AssetDescriptor<Texture> PT_FACTIONS = new AssetDescriptor<>("assets/prototype/sprites/Factions.png", Texture.class);
	public static AssetDescriptor<Texture> PT_KNIGHTTEMPLATE = new AssetDescriptor<>("assets/prototype/sprites/knighttemplate.png", Texture.class);
	public static AssetDescriptor<Texture> PT_LAWBRINGER = new AssetDescriptor<>("assets/prototype/sprites/lawbringer.png", Texture.class);
	public static AssetDescriptor<Texture> PT_LAWBRINGER32 = new AssetDescriptor<>("assets/prototype/sprites/lawbringer32.png", Texture.class);
	public static AssetDescriptor<Texture> PT_LOGO16 = new AssetDescriptor<>("assets/prototype/sprites/logo16.png", Texture.class);
	public static AssetDescriptor<Texture> PT_LOGO32 = new AssetDescriptor<>("assets/prototype/sprites/logo32.png", Texture.class);
	public static AssetDescriptor<Texture> PT_RETICLE = new AssetDescriptor<>("assets/prototype/sprites/reticle.png", Texture.class);
	public static AssetDescriptor<Texture> PT_RIPPEDTILESET = new AssetDescriptor<>("assets/prototype/sprites/rippedtileset.png", Texture.class);
	public static AssetDescriptor<Texture> PT_BACKGROUND = new AssetDescriptor<>("assets/prototype/sprites/splash/background.jpg", Texture.class);
	public static AssetDescriptor<Texture> PT_SPLASH = new AssetDescriptor<>("assets/prototype/sprites/splash/splash.png", Texture.class);
	public static AssetDescriptor<Texture> PT_TESTMAINSCREEN = new AssetDescriptor<>("assets/prototype/sprites/testmainscreen.jpg", Texture.class);
	public static AssetDescriptor<Texture> PT_TILESET = new AssetDescriptor<>("assets/prototype/sprites/tileset.png", Texture.class);
	public static AssetDescriptor<Texture> PT_TOWER = new AssetDescriptor<>("assets/prototype/sprites/tower.png", Texture.class);
	public static AssetDescriptor<Texture> PT_TOWERFULL = new AssetDescriptor<>("assets/prototype/sprites/towerfull.png", Texture.class);
	public static AssetDescriptor<Texture> PT_TOWERTOP = new AssetDescriptor<>("assets/prototype/sprites/towertop.png", Texture.class);
	public static AssetDescriptor<Texture> PT_SIDEVIEWSHEET = new AssetDescriptor<>("assets/prototype/sprites/walksprites/sideviewsheet.png", Texture.class);
	public static AssetDescriptor<Texture> PT_BACKGROUND_TEXTBUTTON = new AssetDescriptor<>("assets/prototype/styles/styles/background_textbutton.png", Texture.class);
	public static AssetDescriptor<BitmapFont> PT_BOCKLIN_FNT = new AssetDescriptor<>("assets/prototype/styles/styles/bocklin.fnt", BitmapFont.class);
	public static AssetDescriptor<Texture> PT_BOCKLIN_PNG = new AssetDescriptor<>("assets/prototype/styles/styles/bocklin.png", Texture.class);
	public static AssetDescriptor<TextureAtlas> PT_STYLES_ATLAS = new AssetDescriptor<>("assets/prototype/styles/styles/styles.atlas", TextureAtlas.class);
	public static AssetDescriptor<?> PT_STYLES_JSON = new AssetDescriptor<>("assets/prototype/styles/styles/styles.json", ?.class);
	public static AssetDescriptor<Texture> PT_STYLES_PNG = new AssetDescriptor<>("assets/prototype/styles/styles/styles.png", Texture.class);
}