package com.drmangotea.tfmg.content.engines.types;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.datagen.TFMGDatagen;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.DeferredHolder;

@NothingNullByDefault
public class EngineTypeBuilder<T extends EngineType, P> extends AbstractBuilder<EngineType, T, P, EngineTypeBuilder<T, P>> {

    public static <T extends EngineType, P> EngineTypeBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<EngineType.Properties, T> factory) {
        return new EngineTypeBuilder<>(owner, parent, name, callback, factory);
    }

    private final NonNullFunction<EngineType.Properties, T> factory;

    private NonNullSupplier<EngineType.Properties> initialProperties = () -> new EngineType.Properties(ResourceLocation.fromNamespaceAndPath(getOwner().getModid(), getName()));
    private NonNullFunction<EngineType.Properties, EngineType.Properties> propertiesCallback = NonNullUnaryOperator.identity();

    public EngineTypeBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<EngineType.Properties, T> factory) {
        super(owner, parent, name, callback, TFMGRegistries.ENGINE_TYPE);
        this.factory = factory;
    }

    public EngineTypeBuilder<T, P> properties(NonNullUnaryOperator<EngineType.Properties> func) {
        propertiesCallback = propertiesCallback.andThen(func);
        return this;
    }

    public EngineTypeBuilder<T, P> initialProperties(NonNullSupplier<EngineType.Properties> properties) {
        initialProperties = properties;
        return this;
    }

    public EngineTypeBuilder<T, P> defaultLang() {
        return lang(EngineType::getDescriptionId);
    }

    public EngineTypeBuilder<T, P> lang(String name) {
        return lang(EngineType::getDescriptionId, name);
    }

    @SafeVarargs
    public final EngineTypeBuilder<T, P> tag(TagKey<EngineType>... tags) {
        return tag(TFMGDatagen.ENGINE_TAGS, tags);
    }

    @Override
    protected @NonnullType T createEntry() {
        EngineType.Properties properties = this.initialProperties.get();
        properties = propertiesCallback.apply(properties);
        return factory.apply(properties);
    }

    @Override
    protected RegistryEntry<EngineType, T> createEntryWrapper(DeferredHolder<EngineType, T> delegate) {
        return new EngineTypeEntry<>(getOwner(), delegate);
    }

    @Override
    public EngineTypeEntry<T> register() {
        return (EngineTypeEntry<T>) super.register();
    }
}
