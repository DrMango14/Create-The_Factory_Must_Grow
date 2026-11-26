package com.drmangotea.tfmg;

import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.cable_type.CableType;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelType;
import com.drmangotea.tfmg.content.engines.types.EngineType;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgrade;
import com.drmangotea.tfmg.content.items.weapons.fire_extinguisher.FireExtinguisherFuelType;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerFuelType;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperation;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.types.VatType;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.mode.MixerMode;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class TFMGRegistries {
    public static final List<Registry<?>> FOR_INIT = new ArrayList<>();

    public static final ResourceKey<Registry<CableType>> CABLE_TYPE = createRegistryKey("cable_types");
    public static final ResourceKey<Registry<Electrode>> ELECTRODE = createRegistryKey("electrodes");
	public static final ResourceKey<Registry<ElectricalPropertiesType<?>>> ELECTRIC_TYPE = createRegistryKey("electric_types");
    public static final ResourceKey<Registry<EngineType>> ENGINE_TYPE = createRegistryKey("engine_type");
    public static final ResourceKey<Registry<MixerMode>> MIXER_MODE = createRegistryKey("mixer_mode");
    public static final ResourceKey<Registry<VatOperation>> VAT_OPERATION = createRegistryKey("vat_operation");
    public static final ResourceKey<Registry<VatType>> VAT_TYPE = createRegistryKey("vat_type");
    public static final ResourceKey<Registry<EngineUpgrade>> ENGINE_UPGRADE = createRegistryKey("engine_upgrade");

    public static final ResourceKey<Registry<FlamethrowerFuelType>> FLAMETHROWER_FUEL_TYPE = createRegistryKey("fuel_type/flamethrower");
    public static final ResourceKey<Registry<EngineFuelType>> ENGINE_FUEL_TYPE = createRegistryKey("fuel_type/engine");
    public static final ResourceKey<Registry<FireExtinguisherFuelType>> FIRE_EXTINGUISHER_FUEL_TYPE = createRegistryKey("fuel_type/fire_extinguisher");

    public static final Registry<VatOperation> VAT_OPERATION_REGISTRY = makeSyncedRegistry(VAT_OPERATION);
    public static final Registry<CableType> CABLE_TYPE_REGISTRY = makeSyncedRegistry(CABLE_TYPE);
    public static final Registry<Electrode> ELECTRODE_REGISTRY = makeSyncedRegistry(ELECTRODE);
	public static final Registry<ElectricalPropertiesType<?>> ELECTRICAL_TYPE_REGISTRY = makeSyncedRegistry(ELECTRIC_TYPE);
    public static final Registry<EngineType> ENGINE_TYPE_REGISTRY = makeSyncedRegistry(ENGINE_TYPE);
    public static final Registry<MixerMode> MIXER_MODE_REGISTRY = makeSyncedRegistry(MIXER_MODE);
    public static final Registry<VatType> VAT_TYPE_REGISTRY = makeSyncedRegistry(VAT_TYPE);
    public static final Registry<EngineUpgrade> ENGINE_UPGRADE_REGISTRY = makeSyncedRegistry(ENGINE_UPGRADE);

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(TFMG.asResource(name));
    }

    private static <T> Registry<T> makeSyncedRegistry(ResourceKey<Registry<T>> registryKey) {
        var registry = new RegistryBuilder<>(registryKey).sync(true).create();
        FOR_INIT.add(registry);
        return registry;
    }

    private static <T> Registry<T> makeRegistry(ResourceKey<Registry<T>> registryKey) {
        var registry = new RegistryBuilder<>(registryKey).create();
        FOR_INIT.add(registry);
        return registry;
    }
}
