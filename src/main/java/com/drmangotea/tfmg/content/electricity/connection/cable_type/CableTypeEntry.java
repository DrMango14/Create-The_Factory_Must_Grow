package com.drmangotea.tfmg.content.electricity.connection.cable_type;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class CableTypeEntry<T extends CableType> extends RegistryEntry<T> {
    public CableTypeEntry(AbstractRegistrate<?> owner, RegistryObject<T> delegate) {
        super(owner, delegate);
    }

    public static <T extends CableType> CableTypeEntry<T> cast(RegistryEntry<T> entry) {
        return RegistryEntry.cast(CableTypeEntry.class, entry);
    }
}
