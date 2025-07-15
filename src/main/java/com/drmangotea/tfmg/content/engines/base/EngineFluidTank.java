package com.drmangotea.tfmg.content.engines.base;

import com.drmangotea.tfmg.TFMG;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.function.Consumer;

public class EngineFluidTank extends SmartFluidTank {


    final boolean extractionAllowed;
    final boolean insertionAllowed;

    final TagKey<Fluid> allowedFluids;
    final TagKey<Fluid> fluidBlacklist;

    public EngineFluidTank(int capacity, boolean extractionAllowed, boolean insertionAllowed, Consumer<FluidStack> updateCallback) {
        super(capacity, updateCallback);
        this.extractionAllowed = extractionAllowed;
        this.insertionAllowed = insertionAllowed;
        this.allowedFluids = null;
        this.fluidBlacklist = null;
    }

    public EngineFluidTank(int capacity, boolean extractionAllowed, boolean insertionAllowed, TagKey<Fluid> allowedFluid, Consumer<FluidStack> updateCallback) {
        super(capacity, updateCallback);
        this.extractionAllowed = extractionAllowed;
        this.insertionAllowed = insertionAllowed;
        this.allowedFluids = allowedFluid;
        this.fluidBlacklist = null;
    }

    public EngineFluidTank(int capacity, boolean extractionAllowed, boolean insertionAllowed,  Consumer<FluidStack> updateCallback,TagKey<Fluid> fluidBlacklist) {
        super(capacity, updateCallback);
        this.extractionAllowed = extractionAllowed;
        this.insertionAllowed = insertionAllowed;
        this.allowedFluids = null;
        this.fluidBlacklist = fluidBlacklist;
    }


    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (!extractionAllowed) return FluidStack.EMPTY;
        return super.drain(resource, action);
    }

    public FluidStack forceDrain(FluidStack resource, FluidAction action) {
        return super.drain(resource, action);
    }


    public FluidStack forceDrain(int resource, FluidAction action) {
        return super.drain(resource, action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (!extractionAllowed) return FluidStack.EMPTY;
        return super.drain(maxDrain, action);
    }

    public int forceFill(FluidStack resource, FluidAction action) {
        return super.fill(resource, action);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {



        if(fluidBlacklist!=null&&resource.getFluid().is(fluidBlacklist))
            return 0;

        if (allowedFluids != null&& !resource.getFluid().is(allowedFluids))
            return 0;

        if (!insertionAllowed) return 0;
        return super.fill(resource, action);
    }
}
