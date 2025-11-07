package com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode;

import com.drmangotea.tfmg.base.TFMGRegistries;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class ElectrodeBuilder<T extends Electrode, P> extends AbstractBuilder<Electrode, T, P, ElectrodeBuilder<T, P>> {

    public static <T extends Electrode, P> ElectrodeBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Electrode.Properties, T> factory) {
        return new ElectrodeBuilder<>(owner, parent, name, callback, factory);
    }

    private final NonNullFunction<Electrode.Properties, T> factory;

    private NonNullSupplier<Electrode.Properties> initialProperties = () -> new Electrode.Properties(ResourceLocation.fromNamespaceAndPath(getOwner().getModid(), getName()));
    private NonNullFunction<Electrode.Properties, Electrode.Properties> propertiesCallback = NonNullUnaryOperator.identity();

    public ElectrodeBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Electrode.Properties, T> factory) {
        super(owner, parent, name, callback, TFMGRegistries.ELECTRODE);
        this.factory = factory;
    }

    public ElectrodeBuilder<T, P> properties(NonNullUnaryOperator<Electrode.Properties> func) {
        propertiesCallback = propertiesCallback.andThen(func);
        return this;
    }

    public ElectrodeBuilder<T, P> initialProperties(NonNullSupplier<Electrode.Properties> properties) {
        initialProperties = properties;
        return this;
    }

    public ElectrodeBuilder<T, P> defaultLang() {
        return lang(Electrode::getDescriptionId);
    }

    public ElectrodeBuilder<T, P> lang(String name) {
        return lang(Electrode::getDescriptionId, name);
    }

    @Override
    protected @NonnullType T createEntry() {
        Electrode.Properties properties = this.initialProperties.get();
        properties = propertiesCallback.apply(properties);
        return factory.apply(properties);
    }

    @Override
    protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
        return new ElectrodeEntry<>(getOwner(), delegate);
    }

    @Override
    public ElectrodeEntry<T> register() {
        TFMGRegistries.registeredElectrodes.put(ResourceLocation.fromNamespaceAndPath(getOwner().getModid(), getName()), createEntry());
        return (ElectrodeEntry<T>) super.register();
    }
}
