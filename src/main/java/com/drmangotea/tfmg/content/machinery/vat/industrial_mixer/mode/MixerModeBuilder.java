package com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.mode;

import com.drmangotea.tfmg.TFMGRegistries;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MixerModeBuilder<T extends MixerMode, P> extends AbstractBuilder<MixerMode, T, P, MixerModeBuilder<T, P>> {
    public static <T extends MixerMode, P> MixerModeBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<MixerMode.Properties, T> factory) {
        return new MixerModeBuilder<>(owner, parent, name, callback, factory);
    }

    private final NonNullFunction<MixerMode.Properties, T> factory;

    private NonNullSupplier<MixerMode.Properties> initialProperties = () -> new MixerMode.Properties(ResourceLocation.fromNamespaceAndPath(getOwner().getModid(), getName()));
    private NonNullFunction<MixerMode.Properties, MixerMode.Properties> propertiesCallback = NonNullUnaryOperator.identity();

    public MixerModeBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<MixerMode.Properties, T> factory) {
        super(owner, parent, name, callback, TFMGRegistries.MIXER_MODE);
        this.factory = factory;
    }

    public MixerModeBuilder<T, P> properties(NonNullUnaryOperator<MixerMode.Properties> func) {
        propertiesCallback = propertiesCallback.andThen(func);
        return this;
    }

    public MixerModeBuilder<T, P> initialProperties(NonNullSupplier<MixerMode.Properties> properties) {
        initialProperties = properties;
        return this;
    }

    public MixerModeBuilder<T, P> defaultLang() {
        return lang(MixerMode::getDescriptionId);
    }

    public MixerModeBuilder<T, P> lang(String name) {
        return lang(MixerMode::getDescriptionId, name);
    }

    @Override
    protected @NonnullType T createEntry() {
        MixerMode.Properties properties = this.initialProperties.get();
        properties = propertiesCallback.apply(properties);
        return factory.apply(properties);
    }

    @Override
    protected RegistryEntry<MixerMode, T> createEntryWrapper(DeferredHolder<MixerMode, T> delegate) {
        return new MixerModeEntry<>(getOwner(), delegate);
    }

    @Override
    public MixerModeEntry<T> register() {
        return (MixerModeEntry<T>) super.register();
    }
}
