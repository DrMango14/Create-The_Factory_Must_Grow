package com.drmangotea.tfmg.content.engines.upgrades.base;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.neoforged.neoforge.registries.DeferredHolder;

@MethodsReturnNonnullByDefault
public class EngineUpgradeEntry<T extends EngineUpgrade> extends RegistryEntry<EngineUpgrade, T> {
    public EngineUpgradeEntry(AbstractRegistrate<?> owner, DeferredHolder<EngineUpgrade, T> key) {
        super(owner, key);
    }

    public static <T extends EngineUpgrade> EngineUpgradeEntry<T> cast(RegistryEntry<EngineUpgrade, T> entry) {
        return RegistryEntry.cast(EngineUpgradeEntry.class, entry);
    }
}
