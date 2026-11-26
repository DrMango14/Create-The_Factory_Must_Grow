package com.drmangotea.tfmg.content.machinery.vat.base.registry.operations;

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
public class VatOperationBuilder<P> extends AbstractBuilder<VatOperation, VatOperation, P, VatOperationBuilder<P>> {
    private final Function<ResourceLocation, VatOperation> factory;

    public VatOperationBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, Function<ResourceLocation, VatOperation> factory) {
        super(owner, parent, name, callback, TFMGRegistries.VAT_OPERATION);
        this.factory = factory;
    }

    @Override
    protected VatOperation createEntry() {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(getOwner().getModid(), getName());
        return factory.apply(id);
    }

    @Override
    protected RegistryEntry<VatOperation, VatOperation> createEntryWrapper(DeferredHolder<VatOperation, VatOperation> delegate) {
        return new VatOperationEntry(getOwner(), delegate);
    }

    @Override
    public VatOperationEntry register() {
        return (VatOperationEntry) super.register();
    }
}
