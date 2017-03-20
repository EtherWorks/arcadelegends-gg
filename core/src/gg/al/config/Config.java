package gg.al.config;

import org.cfg4j.provider.ConfigurationProvider;

import java.util.Properties;

/**
 * Created by Thomas Neumann on 15.03.2017.<br />
 * Config class holding all relevant config objects, as well as an
 * {@link ConfigEditor} object, which holds the ability to change the current configuration.
 */
public class Config {

    public final IVideoConfig video;
    public final IAudioConfig audio;
    public final IGameplayConfig gameplay;
    public final IInputConfig input;
    public final IMiscellaneousConfig miscellaneous;

    public final ConfigEditor editor;
    private final ConfigurationProvider provider;

    public Config(ConfigurationProvider provider, ConfigEditor editor) {
        this.editor = editor;
        this.provider = provider;
        this.editor.setEditing(this.provider.allConfigurationAsProperties());
        this.video = provider.bind(IVideoConfig.PREFIX, IVideoConfig.class);
        this.audio = provider.bind(IAudioConfig.PREFIX, IAudioConfig.class);
        this.gameplay = provider.bind(IGameplayConfig.PREFIX, IGameplayConfig.class);
        this.input = provider.bind(IInputConfig.PREFIX, IInputConfig.class);
        this.miscellaneous = provider.bind(IMiscellaneousConfig.PREFIX, IMiscellaneousConfig.class);
    }

    public Properties asProperties() {
        return provider.allConfigurationAsProperties();
    }

    public void resetEditor() {
        editor.setEditing(asProperties());
    }

}
