package com.drmangotea.tfmg.content.electricity.connection.cable_type;

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

public class CableTypeBuilder<T extends CableType, P> extends AbstractBuilder<CableType, T, P, CableTypeBuilder<T, P>> {

    public static <T extends CableType, P> CableTypeBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<CableType.Properties, T> factory) {
        return new CableTypeBuilder<>(owner, parent, name, callback, factory);
    }

    private final NonNullFunction<CableType.Properties, T> factory;

    private NonNullSupplier<CableType.Properties> initialProperties = () -> new CableType.Properties(new ResourceLocation(getOwner().getModid(), getName()));
    private NonNullFunction<CableType.Properties, CableType.Properties> propertiesCallback = NonNullUnaryOperator.identity();

    public CableTypeBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<CableType.Properties, T> factory) {
        super(owner, parent, name, callback, TFMGRegistries.CABLE_TYPE);
        this.factory = factory;
    }

    public CableTypeBuilder<T, P> properties(NonNullUnaryOperator<CableType.Properties> func) {
        propertiesCallback = propertiesCallback.andThen(func);
        return this;
    }

    public CableTypeBuilder<T, P> initialProperties(NonNullSupplier<CableType.Properties> properties) {
        initialProperties = properties;
        return this;
    }

    public CableTypeBuilder<T, P> defaultLang() {
        return lang(CableType::getDescriptionId);
    }

    public CableTypeBuilder<T, P> lang(String name) {
        return lang(CableType::getDescriptionId, name);
    }

    @Override
    protected @NonnullType T createEntry() {
        CableType.Properties properties = this.initialProperties.get();
        properties = propertiesCallback.apply(properties);
        return factory.apply(properties);
    }

    @Override
    protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
        return new CableTypeEntry<>(getOwner(), delegate);
    }

    @Override
    public CableTypeEntry<T> register() {
        TFMGRegistries.registeredCableTypes.put(new ResourceLocation(getOwner().getModid(), getName()), createEntry());
        return (CableTypeEntry<T>) super.register();
    }
}
