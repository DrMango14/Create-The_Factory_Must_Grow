package com.drmangotea.tfmg.content.decoration.tanks;

import com.drmangotea.tfmg.mixin.accessor.FluidTankBlockEntityAccessor;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class TFMGFluidTankBlockEntity extends FluidTankBlockEntity {

    public TFMGFluidTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.TFMG_FLUID_TANK.get(),
                (be, context) -> {
                    if (((FluidTankBlockEntityAccessor)be).tfmg$getFluidCapability() == null)
                        ((FluidTankBlockEntityAccessor)be).tfmg$refreshCapability();
                    return ((FluidTankBlockEntityAccessor)be).tfmg$getFluidCapability();
                }
        );
    }
}
