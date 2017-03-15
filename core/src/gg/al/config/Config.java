package gg.al.config;

import org.cfg4j.provider.ConfigurationProvider;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
public class Config {

    public final IVideoConfig video;
    public final IAudioConfig audio;
    public final IGameplayConfig gameplay;
    public final IInputConfig input;

    private final ConfigEditor editor;
    private final ConfigurationProvider provider;

    public Config(ConfigurationProvider provider, ConfigEditor editor) {
        this.editor = editor;
        this.provider = provider;
        this.video = provider.bind(IVideoConfig.PREFIX, IVideoConfig.class);
        this.audio = provider.bind(IAudioConfig.PREFIX, IAudioConfig.class);
        this.gameplay = provider.bind(IGameplayConfig.PREFIX, IGameplayConfig.class);
        this.input = provider.bind(IInputConfig.PREFIX, IInputConfig.class);
    }

    public ConfigEditor buildEditor() {
        editor.setEditing(provider.allConfigurationAsProperties());
        return editor;
    }


}
