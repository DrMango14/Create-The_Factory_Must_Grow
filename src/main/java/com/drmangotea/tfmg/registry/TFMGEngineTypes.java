package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.content.engines.types.EngineType;
import com.drmangotea.tfmg.content.engines.types.EngineTypeEntry;
import net.minecraft.resources.ResourceLocation;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;
import static com.drmangotea.tfmg.content.engines.base.EngineProperties.*;

public class TFMGEngineTypes {

    public static final EngineTypeEntry<EngineType> I = REGISTRATE.engineType("i", EngineType::new)
            .properties((p) -> p
                    .speed(1).torque(1).efficiency(1)
                    .pistons(pistonsI())
            ).defaultLang()
            .tag(TFMGTags.Engines.UPGRADES_ON_SIDE.tag)
            .register();
    public static final EngineTypeEntry<EngineType> V = REGISTRATE.engineType("v", EngineType::new)
            .properties((p) -> p
                    .speed(1.2f).torque(1.3f).efficiency(0.8f)
                    .pistons(pistonsV())
                    .cylinderModel(TFMGPartialModels.CYLINDER)
            ).defaultLang()
            .register();
    public static final EngineTypeEntry<EngineType> W = REGISTRATE.engineType("w", EngineType::new)
            .properties((p) -> p
                    .speed(1.3f).torque(1.1f).efficiency(0.5f)
                    .pistons(pistonsW())
                    .cylinderModel(TFMGPartialModels.CYLINDER)
            ).defaultLang()
            .register();
    public static final EngineTypeEntry<EngineType> U = REGISTRATE.engineType("u", EngineType::new)
            .properties((p) -> p
                    .speed(1).torque(1.5f).efficiency(0.9f)
                    .pistons(pistonsU())
            ).defaultLang()
            .tag(TFMGTags.Engines.UPGRADES_ON_SIDE.tag)
            .register();
    public static final EngineTypeEntry<EngineType> BOXER = REGISTRATE.engineType("boxer", EngineType::new)
            .properties((p) -> p
                    .speed(1).torque(0.8f).efficiency(1.2f)
                    .pistons(pistonsBoxer())
            ).defaultLang()
            .register();
    public static final EngineTypeEntry<EngineType> RADIAL = REGISTRATE.engineType("radial", EngineType::new)
            .properties((p) -> p
                    .speed(1).torque(0.8f).efficiency(1.2f)
                    .pistons(pistonsRadial())
                    .cylinderModel(TFMGPartialModels.RADIAL_ENGINE_CYLINDER)
            ).defaultLang()
            .tag(TFMGTags.Engines.SCHEMATIC_CYCLE_BLACKLIST.tag)
            .register();
    public static final EngineTypeEntry<EngineType> TURBINE = REGISTRATE.engineType("turbine", EngineType::new)
            .properties((p) -> p
                    .speed(1.5f).torque(1.5f).efficiency(0.5f)
                    .pistons(pistonsTurbine())
                    .lastRequirement("turbines")
            ).defaultLang()
            .tag(TFMGTags.Engines.SCHEMATIC_CYCLE_BLACKLIST.tag)
            .register();

    public static void init() { }

    public static EngineType decodeType(String name, EngineType defaultType) {
        EngineType type = TFMGRegistries.ENGINE_TYPE_REGISTRY.get(ResourceLocation.parse(name));
        if (type == null) try {
            return switch (name) {
                case "engine_i" -> I.get();
                case "engine_v" -> V.get();
                case "engine_w" -> W.get();
                case "engine_u" -> U.get();
                case "engine_boxer" -> BOXER.get();
                case "radial" -> RADIAL.get();
                case "turbine" -> TURBINE.get();
                default -> defaultType;
            };
        } catch (Exception ignored) {}
        return type;
    }
}
