package gg.al;

import com.badlogic.gdx.Game;
import gg.al.screen.Splash;
import gg.al.screen.TestScreen;

/**
 * Created by Thomas on 27.02.2017.
 */
public class ArcadeScreenTest extends Game {

    private Splash splash;
    private TestScreen testScreen;

    public ArcadeScreenTest() {
        splash = new Splash(this);
        testScreen = new TestScreen(this);
    }

    @Override
    public void create() {
        splash.setNextScreen(testScreen);
        setScreen(splash);
    }
}
