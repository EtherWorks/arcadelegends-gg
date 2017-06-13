package gg.al.util;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Created by Thomas Neumann on 02.06.2017.<br>
 * Main class for packing the textures into an .atlas.
 */
public class TexturePack {
    public static void main(String[] args) {
        TexturePacker.process("characters", "out", "char");
    }
}
