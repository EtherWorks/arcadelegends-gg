package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.TimeUtils;
import gg.al.config.IVideoConfig;
import gg.al.game.ArcadeLegendsGame;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
@Slf4j
public class TestScreen implements Screen, InputProcessor {

    private final ArcadeLegendsGame game;

    private long time;

    public TestScreen(ArcadeLegendsGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        time = TimeUtils.millis();
        Gdx.input.setInputProcessor(this);
    }


    @Override
    public void render(float delta) {
        if (TimeUtils.timeSinceMillis(time) > 1000) {
            log.debug("FPS: {}", Gdx.graphics.getFramesPerSecond());
            time = TimeUtils.millis();
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.F11:
                game.config.editor.setValue(IVideoConfig.VideoKeys.FULLSCREEN, !game.config.video.fullscreen());
                game.config.editor.flush();
                break;
            case Input.Keys.F6:
                game.config.editor.setValue(IVideoConfig.VideoKeys.VSYNC, !game.config.video.vsyncEnabled());
                game.config.editor.flush();
                break;
            case Input.Keys.F5:
                game.config.editor.setValue(IVideoConfig.VideoKeys.FOREGROUNDFPS, game.config.video.foregroundFPS() + 10);
                game.config.editor.flush();
                break;
            case Input.Keys.F3:
                game.config.editor.setValue(IVideoConfig.VideoKeys.WIDTH, 1920);
                game.config.editor.setValue(IVideoConfig.VideoKeys.HEIGHT, 1080);
                game.config.editor.flush();
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
