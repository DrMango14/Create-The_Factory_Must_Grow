package com.drmangotea.tfmg.content.machinery.vat.base.registry.types;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

@NothingNullByDefault
public class VatTypeBuilder<P> extends AbstractBuilder<VatType, VatType, P, VatTypeBuilder<P>> {
    private final Function<ResourceLocation, VatType> factory;

    public VatTypeBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, Function<ResourceLocation, VatType> factory) {
        super(owner, parent, name, callback, TFMGRegistries.VAT_TYPE);
        this.factory = factory;
    }

    @Override
    protected VatType createEntry() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(getOwner().getModid(), getName());
        return factory.apply(id);
    }

    @Override
    protected RegistryEntry<VatType, VatType> createEntryWrapper(DeferredHolder<VatType, VatType> delegate) {
        return new VatTypeEntry(getOwner(), delegate);
    }

    @Override
    public VatTypeEntry register() {
        return (VatTypeEntry) super.register();
    }
}
