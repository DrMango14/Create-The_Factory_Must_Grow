package com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.mode;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MixerModeEntry<T extends MixerMode> extends RegistryEntry<MixerMode, T> {
    public MixerModeEntry(AbstractRegistrate<?> owner, DeferredHolder<MixerMode, T> delegate) {
        super(owner, delegate);
    }

    public static <T extends MixerMode> MixerModeEntry<T> cast(RegistryEntry<MixerMode, T> entry) {
        return RegistryEntry.cast(MixerModeEntry.class, entry);
    }
}
