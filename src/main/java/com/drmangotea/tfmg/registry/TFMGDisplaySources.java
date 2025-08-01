package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGRegistrate;
import com.drmangotea.tfmg.content.electricity.display_link.*;
import com.drmangotea.tfmg.content.electricity.display_link.network.NetConsumptionDisplaySource;
import com.drmangotea.tfmg.content.electricity.display_link.network.NetGenerationDisplaySource;
import com.drmangotea.tfmg.content.electricity.display_link.network.NetIdDisplaySource;
import com.drmangotea.tfmg.content.electricity.display_link.network.NetResistanceDisplaySource;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.tterrag.registrate.util.entry.RegistryEntry;

import java.util.function.Supplier;

public class TFMGDisplaySources {
    private static final TFMGRegistrate REGISTRATE = TFMG.registrate();

    // Electricity Display Sources
    public static final RegistryEntry<DisplaySource, CurrentDisplaySource> CURRENT = simple("current", CurrentDisplaySource::new);
    public static final RegistryEntry<DisplaySource, PowerUsageDisplaySource> POWER_USAGE = simple("power_usage", PowerUsageDisplaySource::new);
    public static final RegistryEntry<DisplaySource, ResistanceDisplaySource> RESISTANCE = simple("resistance", ResistanceDisplaySource::new);
    public static final RegistryEntry<DisplaySource, VoltageDisplaySource> VOLTAGE = simple("voltage", VoltageDisplaySource::new);
    public static final RegistryEntry<DisplaySource, PowerGenerationDisplaySource> POWER_GENERATION = simple("power_generation", PowerGenerationDisplaySource::new);
    public static final RegistryEntry<DisplaySource, VoltageGenerationDisplaySource> VOLTAGE_GENERATION = simple("voltage_generation", VoltageGenerationDisplaySource::new);
    // Electricity Network Display Sources
    public static final RegistryEntry<DisplaySource, NetConsumptionDisplaySource> NETWORK_CONSUMPTION = simple("network_consumption", NetConsumptionDisplaySource::new);
    public static final RegistryEntry<DisplaySource, NetGenerationDisplaySource> NETWORK_GENERATION = simple("network_generation", NetGenerationDisplaySource::new);
    public static final RegistryEntry<DisplaySource, NetIdDisplaySource> NETWORK_ID = simple("network_id", NetIdDisplaySource::new);
    public static final RegistryEntry<DisplaySource, NetResistanceDisplaySource> NETWORK_RESISTANCE = simple("network_resistance", NetResistanceDisplaySource::new);


    private static <T extends DisplaySource> RegistryEntry<DisplaySource, T> simple(String name, Supplier<T> supplier) {
        return REGISTRATE.displaySource(name, supplier).register();
    }

    public static void init() {
    }
}
