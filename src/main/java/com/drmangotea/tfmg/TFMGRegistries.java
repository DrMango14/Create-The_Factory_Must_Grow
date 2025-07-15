package com.drmangotea.tfmg;

import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGRegistries {
    public static final ResourceKey<Registry<CableType>> CABLE_TYPE = createRegistryKey("cable_types");
    public static final ResourceKey<Registry<Electrode>> ELECTRODE = createRegistryKey("electrodes");

    public static final Registry<CableType> CABLE_TYPE_REGISTRY = makeSyncedRegistry(CABLE_TYPE);
    public static final Registry<Electrode> ELECTRODE_REGISTRY = makeSyncedRegistry(ELECTRODE);


    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(TFMG.asResource(name));
    }

    private static <T> Registry<T> makeSyncedRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).sync(true).create();
    }

    private static <T> Registry<T> makeRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).create();
    }
}
