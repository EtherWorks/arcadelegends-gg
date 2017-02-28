package gg.al.anim;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Thomas on 27.02.2017.
 */
public class ScaleAnimation {
    private ScaleMode mode;
    private Texture texture;
    private Vector2[] steps;
    private float stepTime;

    public ScaleAnimation(Texture texture, float stepTime, Vector2... steps) {
        this.texture = texture;
        this.stepTime = stepTime;
        this.steps = steps;
        mode = ScaleMode.SMOOTH;
    }

    public ScaleAnimation(Texture texture, float stepTime, float... steps) {
        this.texture = texture;
        this.stepTime = stepTime;
        this.steps = new Vector2[steps.length];
        for (int i = 0; i < steps.length; i++) {
            this.steps[i] = new Vector2(steps[i], steps[i]);
        }
        mode = ScaleMode.SMOOTH;
    }

    public ScaleAnimation(Texture texture, float stepTime, ScaleMode mode, Vector2... steps) {
        this(texture, stepTime, steps);
        this.mode = mode;
    }

    public ScaleAnimation(Texture texture, float stepTime, ScaleMode mode, float... steps) {
        this(texture, stepTime, steps);
        this.mode = mode;
    }

    public void setMode(ScaleMode mode) {
        this.mode = mode;
    }

    public int getKeyFrameWidth(float stateTime) {
        return (int) (texture.getWidth() * getKeyFrameScale(stateTime).x);
    }

    public int getKeyFrameWidth(float stateTime, int baseWidth) {
        return (int) (baseWidth * getKeyFrameScale(stateTime).x);
    }

    public int getKeyFrameHeight(float stateTime) {
        return (int) (texture.getHeight() * getKeyFrameScale(stateTime).y);
    }

    public int getKeyFrameHeight(float stateTime, int baseHeight) {
        return (int) (baseHeight * getKeyFrameScale(stateTime).y);
    }

    public Vector2 getKeyFrameScale(float stateTime) {
        int ind = (int) (stateTime / stepTime);
        switch (mode) {
            case SMOOTH:
                ind = ind % steps.length;
                Vector2 curr = steps[ind];
                Vector2 next = steps[ind + 1 == steps.length ? 0 : (ind) + 1];
                float till = stateTime % stepTime;
                float percNext = till / stepTime;
                float percCurr = 1 - percNext;
                return new Vector2(percNext * next.x + percCurr * curr.x, percNext * next.y + percCurr * curr.y);
            case STEPPED:
                return steps[ind % steps.length];
            default:
                return Vector2.Zero;
        }
    }

    public void drawKeyFrame(Batch batch, float stateTime, int x, int y) {
        drawKeyFrame(batch, stateTime, x, y, texture.getWidth(), texture.getHeight());
    }

    public void drawKeyFrame(Batch batch, float stateTime, int x, int y, int baseWidth, int baseHeight) {
        Vector2 curr = getKeyFrameScale(stateTime);
        batch.draw(texture, x, y, baseWidth * curr.x, baseHeight * curr.y);
    }

    public enum ScaleMode {
        SMOOTH, STEPPED
    }
}
