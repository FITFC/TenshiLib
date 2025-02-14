package io.github.flemmli97.tenshilib.common.entity;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Predicate;

public class EntityUtil {

    @Nullable
    public static <T extends Entity> T findFromUUID(Class<T> clss, Level world, UUID uuid) {
        return findFromUUID(clss, world, uuid, t -> true);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends Entity> T findFromUUID(Class<T> clss, Level world, UUID uuid, Predicate<T> pred) {
        if (world instanceof ServerLevel) {
            Entity e = ((ServerLevel) world).getEntity(uuid);
            if (e != null && clss.isAssignableFrom(e.getClass()) && pred.test((T) e))
                return (T) e;
        } else {
            for (Entity e : ((ClientLevel) world).entitiesForRendering()) {
                if (e.getUUID().equals(uuid) && clss.isAssignableFrom(e.getClass()) && pred.test((T) e))
                    return (T) e;
            }
        }
        return null;
    }

    public static ItemStack findItem(Player player, Predicate<ItemStack> pred, boolean searchArmor, boolean offHand) {
        for (ItemStack stack : player.getInventory().items)
            if (pred.test(stack))
                return stack;
        if (searchArmor)
            for (ItemStack stack : player.getInventory().armor)
                if (pred.test(stack))
                    return stack;
        if (offHand)
            for (ItemStack stack : player.getInventory().offhand)
                if (pred.test(stack))
                    return stack;
        return ItemStack.EMPTY;
    }
}
