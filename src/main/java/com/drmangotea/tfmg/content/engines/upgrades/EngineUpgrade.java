package com.drmangotea.tfmg.content.engines.upgrades;

import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class EngineUpgrade {


    public EngineUpgrade(){

    }

    public abstract Optional<? extends EngineUpgrade> createUpgrade();


    public void tickUpgrade(AbstractSmallEngineBlockEntity engine) {}
    public void lazyTickUpgrade(AbstractSmallEngineBlockEntity engine) {}
    public void render(AbstractSmallEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {}
    public float getTorqueModifier(AbstractSmallEngineBlockEntity engine) {
        return 1;
    }

    public void updateUpgrade(AbstractSmallEngineBlockEntity be ){}

    public abstract Item getItem();
    public float getSpeedModifier(AbstractSmallEngineBlockEntity engine) {
        return 1;
    }

    public float getEfficiencyModifier(AbstractSmallEngineBlockEntity engine) {
        return 1;
    }

    public static Map<Item, EngineUpgrade> getUpgrades(){
        Map<Item, EngineUpgrade> map = new HashMap<>();

        map.put(TFMGItems.TURBO.get(), new TurboUpgradeData());
        map.put(TFMGItems.GOLDEN_TURBO.get(), new GoldenTurboUpgradeData());
        map.put(TFMGBlocks.GENERATOR.asItem(), new GeneratorEngineUpgrade());
        map.put(TFMGBlocks.INDUSTRIAL_PIPE.asItem(), new EnginePipingUpgrade());
        map.put(TFMGItems.TRANSMISSION.get(), new TransmissionUpgrade());

        return map;
    }
}
