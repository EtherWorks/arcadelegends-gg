package gg.al.config;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
public interface IVideoConfig {

    String PREFIX = "video";

    boolean vsyncEnabled();

    int foregroundFPS();

    int backgroundFPS();

    boolean fullscreen();

    interface VideoKeyNames {
        String VSYNC = PREFIX + ".vsyncEnabled";
        String BACKGRFPS = PREFIX + ".backgroundFPS";
        String FOREGRFPS = PREFIX + ".backgroundFPS";
        String FULLSCREEN = PREFIX + ".fullscreen";
    }
}
