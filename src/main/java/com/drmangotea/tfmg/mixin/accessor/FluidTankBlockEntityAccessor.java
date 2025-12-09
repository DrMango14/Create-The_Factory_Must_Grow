package com.drmangotea.tfmg.mixin.accessor;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FluidTankBlockEntity.class)
public interface FluidTankBlockEntityAccessor {

    @Accessor("luminosity")
    int tfmg$getLuminosity();

    @Accessor("window")
    boolean tfmg$getWindow();

    @Invoker("updateConnectivity")
    void tfmg$updateConnectivity();
}
