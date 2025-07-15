package com.drmangotea.tfmg.mixin.accessor;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SmartFluidTankBehaviour.TankSegment.class)
public interface TankSegmentAccessor {

    @Accessor("tank")
    SmartFluidTank tfmg$tank();


}
