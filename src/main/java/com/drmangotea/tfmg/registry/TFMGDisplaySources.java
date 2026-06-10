package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGRegistrate;
import com.drmangotea.tfmg.content.electricity.display_link.CurrentDisplaySource;
import com.drmangotea.tfmg.content.electricity.display_link.PowerGenerationDisplaySource;
import com.drmangotea.tfmg.content.electricity.display_link.PowerUsageDisplaySource;
import com.drmangotea.tfmg.content.electricity.display_link.ResistanceDisplaySource;
import com.drmangotea.tfmg.content.electricity.display_link.VoltageDisplaySource;
import com.drmangotea.tfmg.content.electricity.display_link.VoltageGenerationDisplaySource;
import com.drmangotea.tfmg.content.electricity.display_link.network.NetConsumptionDisplaySource;
import com.drmangotea.tfmg.content.electricity.display_link.network.NetGenerationDisplaySource;
import com.drmangotea.tfmg.content.electricity.display_link.network.NetIdDisplaySource;
import com.drmangotea.tfmg.content.electricity.display_link.network.NetResistanceDisplaySource;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.tterrag.registrate.util.entry.RegistryEntry;

import java.util.function.Supplier;

public class TFMGDisplaySources {

    private static final TFMGRegistrate REGISTRATE = TFMGRegistrate.create();

    // Per-block electricity readings
    public static final RegistryEntry<CurrentDisplaySource>          CURRENT           = simple("current",           CurrentDisplaySource::new);
    public static final RegistryEntry<PowerUsageDisplaySource>       POWER_USAGE        = simple("power_usage",       PowerUsageDisplaySource::new);
    public static final RegistryEntry<ResistanceDisplaySource>       RESISTANCE         = simple("resistance",        ResistanceDisplaySource::new);
    public static final RegistryEntry<VoltageDisplaySource>          VOLTAGE            = simple("voltage",           VoltageDisplaySource::new);
    public static final RegistryEntry<PowerGenerationDisplaySource>  POWER_GENERATION   = simple("power_generation",  PowerGenerationDisplaySource::new);
    public static final RegistryEntry<VoltageGenerationDisplaySource> VOLTAGE_GENERATION = simple("voltage_generation", VoltageGenerationDisplaySource::new);

    // Network-wide readings
    public static final RegistryEntry<NetConsumptionDisplaySource>   NETWORK_CONSUMPTION = simple("network_consumption",  NetConsumptionDisplaySource::new);
    public static final RegistryEntry<NetGenerationDisplaySource>    NETWORK_GENERATION  = simple("network_generation",   NetGenerationDisplaySource::new);
    public static final RegistryEntry<NetIdDisplaySource>            NETWORK_ID          = simple("network_id",           NetIdDisplaySource::new);
    public static final RegistryEntry<NetResistanceDisplaySource>    NETWORK_RESISTANCE  = simple("network_resistance",   NetResistanceDisplaySource::new);

    private static <T extends DisplaySource> RegistryEntry<T> simple(String name, Supplier<T> supplier) {
        return REGISTRATE.displaySource(name, supplier).register();
    }

    /** Called during mod init to force class-loading and trigger static registration. */
    public static void init() {}
}
