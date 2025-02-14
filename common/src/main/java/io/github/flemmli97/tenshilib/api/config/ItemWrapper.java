package io.github.flemmli97.tenshilib.api.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.flemmli97.tenshilib.TenshiLib;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.lang.reflect.Type;
import java.util.List;

public class ItemWrapper implements IItemConfig<ItemWrapper> {

    protected Item item;
    protected String reg;

    public ItemWrapper(String s) {
        this.reg = s;
    }

    @Override
    public Item getItem() {
        if (this.item == null) {
            if (this.reg == null || this.reg.isEmpty()) {
                this.item = Items.AIR;
                return this.item;
            }
            this.item = PlatformUtils.INSTANCE.items().getFromId(new ResourceLocation(this.reg));
            if (this.item == Items.AIR && (this.reg.isEmpty() || !this.reg.equals("minecraft:air")))
                TenshiLib.logger.error("Faulty item registry name {}", this.reg);
        }
        return this.item;
    }

    @Override
    public ItemStack getStack() {
        return this.getItem() == Items.AIR ? ItemStack.EMPTY : new ItemStack(this.getItem());
    }

    @Override
    public List<Item> getItemList() {
        return null;
    }

    @Override
    public boolean hasList() {
        return false;
    }

    @Override
    public boolean match(ItemStack stack) {
        return stack.getItem() == this.getItem();
    }

    @Override
    public ItemWrapper readFromString(String s) {
        this.reg = s;
        this.item = null;
        return this;
    }

    @Override
    public String writeToString() {
        return this.reg;
    }

    public static String usage() {
        return "Valid values are all item registry names. Empty or minecraft:air for nothing";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ItemWrapper prop) {
            return prop.writeToString().equals(this.writeToString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.writeToString().hashCode();
    }

    @Override
    public String toString() {
        return this.writeToString();
    }

    public static class Serializer implements JsonDeserializer<ItemWrapper>, JsonSerializer<ItemWrapper> {

        @Override
        public JsonElement serialize(ItemWrapper src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.add("item", new JsonPrimitive(src.reg));
            return obj;
        }

        @Override
        public ItemWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new ItemWrapper(json.getAsJsonObject().get("item").getAsString());
        }
    }
}
