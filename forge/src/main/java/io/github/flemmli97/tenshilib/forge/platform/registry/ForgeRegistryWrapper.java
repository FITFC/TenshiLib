package io.github.flemmli97.tenshilib.forge.platform.registry;

import io.github.flemmli97.tenshilib.platform.registry.SimpleRegistryWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collection;

public record ForgeRegistryWrapper<T>(
        IForgeRegistry<T> registry) implements SimpleRegistryWrapper<T> {

    @Override
    public T getFromId(ResourceLocation id) {
        return this.registry.getValue(id);
    }

    @Override
    public ResourceLocation getIDFrom(T entry) {
        return this.registry.getKey(entry);
    }

    @Override
    public Iterable<T> getIterator() {
        return this.registry;
    }

    @Override
    public Collection<T> values() {
        return this.registry.getValues();
    }

    @Override
    public boolean contains(ResourceLocation id) {
        return this.registry.containsKey(id);
    }
}
