package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;


@EventBusSubscriber
public class TFMGRemapper {
    /**
     * @see String The old name of the block/item (String)
     * <p>
     * @see ResourceLocation The new name of the block/item (ResourceLocation)
     */
    private static final Map<String, ResourceLocation> reMapBlock = new HashMap<>();
    private static final Map<String, ResourceLocation> reMapItem = new HashMap<>();
    private static final Map<String, ResourceLocation> reMapFluid = new HashMap<>();

    static {
        //Blocks
        reMapBlock.put("copper_encased_brass_pipe", TFMG.asResource("encased_brass_pipe"));
        reMapBlock.put("copper_encased_steel_pipe", TFMG.asResource("encased_steel_pipe"));
        reMapBlock.put("copper_encased_aluminum_pipe", TFMG.asResource("encased_aluminum_pipe"));
        reMapBlock.put("copper_encased_cast_iron_pipe", TFMG.asResource("encased_cast_iron_pipe"));
        reMapBlock.put("copper_encased_plastic_pipe", TFMG.asResource("encased_plastic_pipe"));

        //Items
    }

    @SubscribeEvent
    public static void remap(RegisterEvent event) {
        Registry<?> registry = event.getRegistry();

        if (registry.key() == Registries.BLOCK) {
            reMapBlock.forEach((string, resourceLocation) -> registry.addAlias(TFMG.asResource(string), resourceLocation));
            TFMG.LOGGER.info("[TFMG Remapper] Remapped {} blocks", reMapBlock.size());
        }
        if (registry.key() == Registries.ITEM) {
            reMapBlock.forEach((string, resourceLocation) -> registry.addAlias(TFMG.asResource(string), resourceLocation));
            reMapItem.forEach((string, resourceLocation) -> registry.addAlias(TFMG.asResource(string), resourceLocation));
            int reMapSize = reMapBlock.size() + reMapItem.size();
            TFMG.LOGGER.info("[TFMG Remapper] Remapped {} items", reMapSize);
        }
        if (registry.key() == Registries.FLUID) {
            reMapFluid.forEach((string, resourceLocation) -> registry.addAlias(TFMG.asResource(string), resourceLocation));
            TFMG.LOGGER.info("[TFMG Remapper] Remapped {} fluids", reMapFluid.size());
        }
    }
}
