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

    int width();

    int height();

    interface VideoKeyNames {
        String VSYNC = PREFIX + ".vsyncEnabled";
        String BACKGRFPS = PREFIX + ".backgroundFPS";
        String FOREGRFPS = PREFIX + ".foregroundFPS";
        String FULLSCREEN = PREFIX + ".fullscreen";
        String WIDTH = PREFIX + ".width";
        String HEIGHT = PREFIX + ".height";
    }
}
