package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.HashMap;
import java.util.Map;

public class TFMGRegistries {
    public static final Map<ResourceLocation, CableType> registeredCableTypes = new HashMap<>();
    public static final Map<ResourceLocation, Electrode> registeredElectrodes = new HashMap<>();

    public static final ResourceKey<Registry<CableType>> CABLE_TYPE = TFMG.REGISTRATE.makeRegistry("cable_type", () -> new RegistryBuilder<CableType>().hasTags().allowModification().setDefaultKey(TFMG.asResource("empty")));
    public static final ResourceKey<Registry<Electrode>> ELECTRODE = TFMG.REGISTRATE.makeRegistry("electrode", () -> new RegistryBuilder<Electrode>().hasTags().allowModification().setDefaultKey(TFMG.asResource("none")));

    public static void register() {
    }
}
