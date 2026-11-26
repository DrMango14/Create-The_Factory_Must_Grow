package com.drmangotea.tfmg.content.engines.upgrades.base;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.datagen.TFMGDatagen;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

@NothingNullByDefault
public class EngineUpgradeBuilder<T extends EngineUpgrade, P> extends AbstractBuilder<EngineUpgrade, T, P, EngineUpgradeBuilder<T, P>> {
    private final Function<ResourceLocation, T> factory;

    public EngineUpgradeBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, Function<ResourceLocation, T> factory) {
        super(owner, parent, name, callback, TFMGRegistries.ENGINE_UPGRADE);
        this.factory = factory;
    }

    public EngineUpgradeBuilder<T, P> defaultLang() {
        return lang(EngineUpgrade::getDescriptionId);
    }

    public EngineUpgradeBuilder<T, P> lang(String name) {
        return lang(EngineUpgrade::getDescriptionId, name);
    }

    @SafeVarargs
    public final EngineUpgradeBuilder<T, P> tag(TagKey<EngineUpgrade>... tags) {
        return tag(TFMGDatagen.ENGINE_UPGRADE_TAGS, tags);
    }

    @Override
    protected T createEntry() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(getOwner().getModid(), getName());
        return factory.apply(id);
    }

    @Override
    protected RegistryEntry<EngineUpgrade, T> createEntryWrapper(DeferredHolder<EngineUpgrade, T> delegate) {
        return new EngineUpgradeEntry<>(getOwner(), delegate);
    }

    @Override
    public EngineUpgradeEntry<T> register() {
        return (EngineUpgradeEntry<T>) super.register();
    }
}
