package gg.al.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gg.al.logic.component.Abilities;
import gg.al.logic.component.Position;
import gg.al.logic.component.Render;
import gg.al.logic.component.Stats;
import gg.al.logic.data.IComponentDef;
import io.gsonfire.GsonFireBuilder;
import io.gsonfire.PostProcessor;

/**
 * Created by Thomas Neumann on 26.04.2017.<br />
 */
public class GsonUtil {
    private static Gson GSON;

    public static Gson getGSON() {
        return GSON == null ?
                GSON = new GsonFireBuilder()
                        .registerPostProcessor(IComponentDef.class, new PostProcessor<IComponentDef>() {
                            @Override
                            public void postDeserialize(IComponentDef result, JsonElement src, Gson gson) {

                            }

                            @Override
                            public void postSerialize(JsonElement result, IComponentDef src, Gson gson) {
                                result.getAsJsonObject().addProperty("type", src.getClass().getSimpleName());
                            }
                        })
                        .registerTypeSelector(IComponentDef.class, readElement -> {
                            JsonObject jsonObject = readElement.getAsJsonObject();
                            String type = jsonObject.get("type").getAsString();
                            switch (type) {
                                case "StatsDef":
                                    return Stats.StatsDef.class;
                                case "PositionDef":
                                    return Position.PositionDef.class;
                                case "RenderDef":
                                    return Render.RenderDef.class;
                                case "AbilitiesDef":
                                    return Abilities.AbilitiesDef.class;
                                default:
                                    return null;
                            }
                        })
                        .createGsonBuilder()
                        .setPrettyPrinting()
                        .create()
                : GSON;
    }
}
