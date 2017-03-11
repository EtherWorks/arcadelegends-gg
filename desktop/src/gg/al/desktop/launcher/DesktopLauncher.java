package gg.al.desktop.launcher;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gg.al.game.ArcadeLegendsGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        LwjglApplication application = new LwjglApplication(new ArcadeLegendsGame(), config);
    }
}
