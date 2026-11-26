package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.content.engines.upgrades.*;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgrade;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgradeEntry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;

public class TFMGEngineUpgrades {

    public static final EngineUpgradeEntry<TurboUpgradeData> TURBO = register("turbo", TurboUpgradeData::new);
    public static final EngineUpgradeEntry<GoldenTurboUpgradeData> GOLDEN_TURBO = register("golden_turbo", GoldenTurboUpgradeData::new);
    public static final EngineUpgradeEntry<GeneratorEngineUpgrade> GENERATOR = register("generator", GeneratorEngineUpgrade::new);
    public static final EngineUpgradeEntry<EnginePipingUpgrade> PIPING = register("piping", EnginePipingUpgrade::new);

    private static <T extends EngineUpgrade> EngineUpgradeEntry<T> register(String name, Function<ResourceLocation, T> factory) {
        return REGISTRATE.engineUpgrade(name, factory).defaultLang().register();
    }

    public static void init() { }
}
