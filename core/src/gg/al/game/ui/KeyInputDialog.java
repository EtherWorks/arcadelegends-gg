package gg.al.game.ui;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import gg.al.config.IInputConfig;
import gg.al.game.AL;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by Patrick Windegger on 12.05.2017.
 * Class responsible for providing a basic Input Dialog for the key config {@link IInputConfig} of the game.
 */
@Slf4j
public class KeyInputDialog extends Dialog {

    private Table table;
    private Skin skin;
    private Stage stage;
    private BitmapFont font;
    private String currentKey;
    private String inputKey;
    private TextButton button;


    public KeyInputDialog(String title, WindowStyle windowStyle, Skin skin, Stage stage, BitmapFont font, String inputKey, String currentKey, TextButton button) {
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

    /**
     * Method responsible for initializing a Dialog to provide the option to set input-keys
     *
     * @param background - background of the dialog
     */
    public void initDialog(Drawable background) {
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
                int keyCode = event.getKeyCode();
                if (keyCode == Input.Keys.ESCAPE) {
                    return false;
                }
                IInputConfig.InputKeys[] allKeys = IInputConfig.InputKeys.values();
                for (int i = 0; i < allKeys.length; i++) {

                    if (IInputConfig.InputKeys.getFromKey(allKeys[i], AL.getInputConfig()) == keyCode) {
                        tfKey.setText(inputKey.toUpperCase());
                        hide();
                        return false;
                    }
                }
                tfKey.setText(Input.Keys.toString(keyCode));
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
                button.setText(Input.Keys.toString(keyCode));
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


}
