package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TFMGBoilerHeaters {
    public static void registerDefaults() {
        BoilerHeater.REGISTRY.register(TFMGBlocks.FIREBOX.get(), FIREBOX);
    }

    public static BoilerHeater FIREBOX = TFMGBoilerHeaters::blazeBurner;

    public static int blazeBurner(Level level, BlockPos pos, BlockState state) {
        BlazeBurnerBlock.HeatLevel value = state.getValue(BlazeBurnerBlock.HEAT_LEVEL);
        if (value == BlazeBurnerBlock.HeatLevel.NONE) {
            return -1;
        }
        if (value == BlazeBurnerBlock.HeatLevel.SEETHING) {
            return 3;
        }
        if (value.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {
            return 2;
        }
        return -1;

    }
}
