package com.drmangotea.tfmg.content.electricity.connection.cable_type;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CableTypeEntry<T extends CableType> extends RegistryEntry<CableType, T> {
    public CableTypeEntry(AbstractRegistrate<?> owner, DeferredHolder<CableType, T> delegate) {
        super(owner, delegate);
    }

    public static <T extends CableType> CableTypeEntry<T> cast(RegistryEntry<CableType, T> entry) {
        return RegistryEntry.cast(CableTypeEntry.class, entry);
    }
}
