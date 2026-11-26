package com.drmangotea.tfmg.mixin.accessor;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FluidTankBlockEntity.class)
public interface FluidTankBlockEntityAccessor {
    @Accessor("luminosity")
    int tfmg$getLuminosity();

    @Accessor("window")
    boolean tfmg$getWindow();

    @Accessor("height")
    int tfmg$getHeight();

    @Accessor("width")
    int tfmg$getWidth();

    @Accessor("fluidCapability")
    IFluidHandler tfmg$getFluidCapability();

    @Invoker("refreshCapability")
    void tfmg$refreshCapability();
}