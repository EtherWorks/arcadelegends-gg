package gg.al.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;

import java.util.Comparator;

/**
 * Created by Thomas Neumann on 21.04.2017.<br />
 * {@link GroupStrategy} which is the exact same as {@link com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy}, only setting an additional OpenGL flag.<br>
 * Additional flag is set since libGDX version 1.9.5, therefore technically @{@link Deprecated}
 */
@Deprecated
public class CameraLayerGroupStrategy implements GroupStrategy, Disposable {
    private static final int GROUP_OPAQUE = 0;
    private static final int GROUP_BLEND = 1;
    private final Comparator<Decal> cameraSorter;
    Pool<Array<Decal>> arrayPool = new Pool<Array<Decal>>(16) {
        @Override
        protected Array<Decal> newObject() {
            return new Array();
        }
    };
    Array<Array<Decal>> usedArrays = new Array<Array<Decal>>();
    ObjectMap<DecalMaterial, Array<Decal>> materialGroups = new ObjectMap<DecalMaterial, Array<Decal>>();
    Camera camera;
    ShaderProgram shader;

    public CameraLayerGroupStrategy(final Camera camera) {
        this(camera, (o1, o2) -> {
            float dist1 = camera.position.dst(o1.getPosition());
            float dist2 = camera.position.dst(o2.getPosition());
            return (int) Math.signum(dist2 - dist1);
        });
    }

    public CameraLayerGroupStrategy(Camera camera, Comparator<Decal> sorter) {
        this.camera = camera;
        this.cameraSorter = sorter;
        createDefaultShader();

    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public int decideGroup(Decal decal) {
        return decal.getMaterial().isOpaque() ? GROUP_OPAQUE : GROUP_BLEND;
    }

    @Override
    public void beforeGroup(int group, Array<Decal> contents) {


        if (group == GROUP_BLEND)
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        if (group == GROUP_BLEND) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            contents.sort(cameraSorter);
        } else {
            for (int i = 0, n = contents.size; i < n; i++) {
                Decal decal = contents.get(i);
                Array<Decal> materialGroup = materialGroups.get(decal.getMaterial());
                if (materialGroup == null) {
                    materialGroup = arrayPool.obtain();
                    materialGroup.clear();
                    usedArrays.add(materialGroup);
                    materialGroups.put(decal.getMaterial(), materialGroup);
                }
                materialGroup.add(decal);
            }

            contents.clear();
            for (Array<Decal> materialGroup : materialGroups.values()) {
                contents.addAll(materialGroup);
            }

            materialGroups.clear();
            arrayPool.freeAll(usedArrays);
            usedArrays.clear();
        }
    }

    @Override
    public void afterGroup(int group) {
        if (group == GROUP_BLEND) {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void beforeGroups() {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        shader.begin();
        shader.setUniformMatrix("u_projectionViewMatrix", camera.combined);
        shader.setUniformi("u_texture", 0);

//        shader.setUniformf("u_viewportInverse", new Vector2(1f / camera.viewportWidth, 1f / camera.viewportHeight));
//        shader.setUniformf("u_offset", 1.0f);
//        shader.setUniformf("u_step", Math.min(1f, camera.viewportWidth / 70f));
//        shader.setUniformf("u_color", new Vector3(1, 0, 0));
    }

    @Override
    public void afterGroups() {
        shader.end();
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }

    private void createDefaultShader() {
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projectionViewMatrix;\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "   v_color.a = v_color.a * (255.0/254.0);\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projectionViewMatrix * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n" //
                + "precision mediump float;\n" //
                + "#endif\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
                + "}";
//        String fragmentShader = "#ifdef GL_ES\n" +
//                "precision mediump float;\n" +
//                "precision mediump int;\n" +
//                "#endif\n" +
//                "\n" +
//                "uniform sampler2D u_texture;\n" +
//                "\n" +
//                "// The inverse of the viewport dimensions along X and Y\n" +
//                "uniform vec2 u_viewportInverse;\n" +
//                "\n" +
//                "// Color of the outline\n" +
//                "uniform vec3 u_color;\n" +
//                "\n" +
//                "// Thickness of the outline\n" +
//                "uniform float u_offset;\n" +
//                "\n" +
//                "// Step to check for neighbors\n" +
//                "uniform float u_step;\n" +
//                "\n" +
//                "varying vec4 v_color;\n" +
//                "varying vec2 v_texCoord;\n" +
//                "\n" +
//                "#define ALPHA_VALUE_BORDER 0.5\n" +
//                "\n" +
//                "void main() {\n" +
//                "   vec2 T = v_texCoord.xy;\n" +
//                "\n" +
//                "   float alpha = 0.0;\n" +
//                "   bool allin = true;\n" +
//                "   for( float ix = -u_offset; ix < u_offset; ix += u_step )\n" +
//                "   {\n" +
//                "      for( float iy = -u_offset; iy < u_offset; iy += u_step )\n" +
//                "       {\n" +
//                "          float newAlpha = texture2D(u_texture, T + vec2(ix, iy) * u_viewportInverse).a;\n" +
//                "          allin = allin && newAlpha > ALPHA_VALUE_BORDER;\n" +
//                "          if (newAlpha > ALPHA_VALUE_BORDER && newAlpha >= alpha)\n" +
//                "          {\n" +
//                "             alpha = newAlpha;\n" +
//                "          }\n" +
//                "      }\n" +
//                "   }\n" +
//                "   if (allin)\n" +
//                "   {\n" +
//                "      alpha = 0.0;\n" +
//                "   }\n" +
//                "\n" +
//                "   gl_FragColor = vec4(u_color,alpha);\n" +
//                "}";
//
//        String vertexShader = "uniform mat4 u_projectionViewMatrix;\n" +
//                "\n" +
//                "attribute vec4 a_position;\n" +
//                "attribute vec2 a_texCoord0;\n" +
//                "attribute vec4 a_color;\n" +
//                "\n" +
//                "varying vec4 v_color;\n" +
//                "varying vec2 v_texCoord;\n" +
//                "\n" +
//                "uniform vec2 u_viewportInverse;\n" +
//                "\n" +
//                "void main() {\n" +
//                "    gl_Position = u_projectionViewMatrix * a_position;\n" +
//                "    v_texCoord = a_texCoord0;\n" +
//                "    v_color = a_color;\n" +
//                "}";

        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false)
            throw new IllegalArgumentException("couldn't compile shader: " + shader.getLog());
    }

    @Override
    public ShaderProgram getGroupShader(int group) {
        return shader;
    }

    @Override
    public void dispose() {
        if (shader != null) shader.dispose();
    }

}
