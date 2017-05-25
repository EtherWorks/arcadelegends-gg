package gg.al.game.ui;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import gg.al.config.IInputConfig;
import gg.al.game.AL;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.extern.slf4j.Slf4j;


import javax.swing.*;
import java.awt.*;
import java.security.Key;


/**
 * Created by Patrick Windegger on 12.05.2017.
 */
@Slf4j
public class ALDialog extends Dialog {

    private Table table;
    private Skin skin;
    private Stage stage;
    private BitmapFont font;
    private String currentKey;
    private String inputKey;
    private char key;
    private TextButton button;


    public ALDialog(String title, WindowStyle windowStyle, Skin skin, Stage stage, BitmapFont font, String inputKey, String currentKey, TextButton button) {
        super(title, windowStyle);
        table = new Table();
        this.skin = skin;
        this.stage = stage;
        this.font = font;
        this.currentKey = currentKey;
        this.inputKey = inputKey;
        this.button = button;
        this.setSize(300, 300);
    }

    public void initDefaultDialog(Drawable background) {
        this.setBackground(background);
        Label lbKey = new Label("Key:", skin);
        table.add(new com.badlogic.gdx.scenes.scene2d.ui.Label("Input Selection", new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.BLACK))).fill().center();
        table.row();
        table.add(lbKey);

        TextField tfKey = new TextField("", skin);
        tfKey.setText(inputKey.toUpperCase());
        tfKey.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                tfKey.setText(String.format("%c", character));
                key = tfKey.getText().charAt(0);
                int keyCode = KeyStroke.getKeyStroke(key, 0).getKeyCode();
                switch (currentKey) {
                    case "up":
                        AL.getConfigEditor().setValue(IInputConfig.InputKeys.up, keyCode);
                        break;
                    case "down":
                        AL.getConfigEditor().setValue(IInputConfig.InputKeys.down, keyCode);
                        break;
                    case "left":
                        AL.getConfigEditor().setValue(IInputConfig.InputKeys.left, keyCode);
                        break;
                    case "right":
                        AL.getConfigEditor().setValue(IInputConfig.InputKeys.right, keyCode);
                        break;
                    case "ability1":
                        AL.getConfigEditor().setValue(IInputConfig.InputKeys.ability1, keyCode);
                        break;
                    case "ability2":
                        AL.getConfigEditor().setValue(IInputConfig.InputKeys.ability2, keyCode);
                        break;
                    case "ability3":
                        AL.getConfigEditor().setValue(IInputConfig.InputKeys.ability3, keyCode);
                        break;
                    case "ability4":
                        AL.getConfigEditor().setValue(IInputConfig.InputKeys.ability4, keyCode);
                        break;
                    case "inventory":
                        AL.getConfigEditor().setValue(IInputConfig.InputKeys.inventory, keyCode);
                        break;
                    case "trait":
                        AL.getConfigEditor().setValue(IInputConfig.InputKeys.trait, keyCode);
                        break;
                    default:
                        log.debug("Unkown Key");
                        break;

                }
                AL.getConfigEditor().flush();
                hide();
                button.setText(key + "");
                button.setText(button.getText().toString().toUpperCase());

                return super.keyTyped(event, character);
            }
        });
        table.add(tfKey);

        this.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (event.getKeyCode() == Input.Keys.ESCAPE) {
                    hide();
                }
                return super.keyTyped(event, character);
            }
        });

        this.add(table);
        this.show(stage);
    }

    public char getKey() {
        return key;
    }
}
