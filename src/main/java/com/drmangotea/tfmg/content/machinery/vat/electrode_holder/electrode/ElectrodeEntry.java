package com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ElectrodeEntry<T extends Electrode> extends RegistryEntry<Electrode, T> {
    public ElectrodeEntry(AbstractRegistrate<?> owner, DeferredHolder<Electrode, T> delegate) {
        super(owner, delegate);
    }

    public static <T extends Electrode> ElectrodeEntry<T> cast(RegistryEntry<Electrode, T> entry) {
        return RegistryEntry.cast(ElectrodeEntry.class, entry);
    }
}
