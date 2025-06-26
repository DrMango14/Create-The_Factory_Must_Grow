package com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode;

import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableTypeEntry;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class ElectrodeEntry<T extends Electrode> extends RegistryEntry<T> {
    public ElectrodeEntry(AbstractRegistrate<?> owner, RegistryObject<T> delegate) {
        super(owner, delegate);
    }

    public static <T extends Electrode> ElectrodeEntry<T> cast(RegistryEntry<T> entry) {
        return RegistryEntry.cast(ElectrodeEntry.class, entry);
    }
}
