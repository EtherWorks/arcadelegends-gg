package gg.al.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.al.logic.component.*;
import gg.al.logic.component.data.Template;
import io.gsonfire.GsonFireBuilder;
import io.gsonfire.PostProcessor;

import java.util.*;

/**
 * Created by Thomas Neumann on 26.04.2017.<br />
 */
public class GsonUtil {

    private static final Set<Class<? extends Template>> TEMPLATES = ImmutableSet.of(
            StatComponent.StatTemplate.class,
            PositionComponent.PositionTemplate.class,
            RenderComponent.RenderTemplate.class,
            CharacterComponent.CharacterTemplate.class
    );
    private static Gson GSON;

    public static Gson getGSON() {
        return GSON == null ?
                GSON = new GsonFireBuilder()
                        .registerPostProcessor(Template.class, new PostProcessor<Template>() {
                            @Override
                            public void postDeserialize(Template result, JsonElement src, Gson gson) {

                            }

                            @Override
                            public void postSerialize(JsonElement result, Template src, Gson gson) {
                                result.getAsJsonObject().addProperty("type", src.getClass().getSimpleName());
                            }
                        })
                        .registerTypeSelector(Template.class, readElement -> {
                            JsonObject jsonObject = readElement.getAsJsonObject();
                            String type = jsonObject.get("type").getAsString();
                            for (Class<? extends Template> template : TEMPLATES) {
                                if (template.getSimpleName().equals(type))
                                    return template;
                            }
                            return null;
                        })
                        .createGsonBuilder()
                        .setPrettyPrinting()
                        .create()
                : GSON;
    }
}
