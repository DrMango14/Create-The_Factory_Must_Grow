package com.drmangotea.tfmg.base.fluid;

import com.drmangotea.tfmg.registry.TFMGBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidInteractionRegistry;

import static com.drmangotea.tfmg.registry.TFMGFluids.COOLING_FLUID;
import static com.drmangotea.tfmg.registry.TFMGFluids.CRUDE_OIL;
import static com.drmangotea.tfmg.registry.TFMGFluids.DIESEL;
import static com.drmangotea.tfmg.registry.TFMGFluids.GASOLINE;
import static com.drmangotea.tfmg.registry.TFMGFluids.HEAVY_OIL;
import static com.drmangotea.tfmg.registry.TFMGFluids.KEROSENE;
import static com.drmangotea.tfmg.registry.TFMGFluids.LUBRICATION_OIL;
import static com.drmangotea.tfmg.registry.TFMGFluids.NAPHTHA;

public class TFMGFluidInteractions {
    public static void registerFluidInteractions() {
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                CRUDE_OIL.get().getFluidType(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return TFMGBlocks.FOSSILSTONE.get().defaultBlockState();
                    } else {
                        return Blocks.SMOOTH_BASALT.defaultBlockState();
                    }
                }
        ));
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                HEAVY_OIL.get().getFluidType(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return TFMGBlocks.FOSSILSTONE.get().defaultBlockState();
                    } else {
                        return Blocks.SMOOTH_BASALT.defaultBlockState();
                    }
                }
        ));
        //
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                GASOLINE.get().getFluidType(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return TFMGBlocks.FOSSILSTONE.get().defaultBlockState();
                    } else {
                        return Blocks.SMOOTH_BASALT.defaultBlockState();
                    }
                }
        ));
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                DIESEL.get().getFluidType(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return TFMGBlocks.FOSSILSTONE.get().defaultBlockState();
                    } else {
                        return Blocks.SMOOTH_BASALT.defaultBlockState();
                    }
                }
        ));
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                NAPHTHA.get().getFluidType(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return TFMGBlocks.FOSSILSTONE.get().defaultBlockState();
                    } else {
                        return Blocks.SMOOTH_BASALT.defaultBlockState();
                    }
                }
        ));
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                KEROSENE.get().getFluidType(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return TFMGBlocks.FOSSILSTONE.get().defaultBlockState();
                    } else {
                        return Blocks.SMOOTH_BASALT.defaultBlockState();
                    }
                }
        ));
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                LUBRICATION_OIL.get().getFluidType(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return TFMGBlocks.FOSSILSTONE.get().defaultBlockState();
                    } else {
                        return Blocks.SMOOTH_BASALT.defaultBlockState();
                    }
                }
        ));
        FluidInteractionRegistry.addInteraction(ForgeMod.LAVA_TYPE.get(), new FluidInteractionRegistry.InteractionInformation(
                COOLING_FLUID.get().getFluidType(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return Blocks.BASALT.defaultBlockState();
                    } else {
                        return Blocks.SMOOTH_BASALT.defaultBlockState();
                    }
                }
        ));
    }
}
