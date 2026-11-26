package com.drmangotea.tfmg.content.engines.base;

import com.drmangotea.tfmg.base.fluid.ForceableFluidTank;
import com.drmangotea.tfmg.registry.TFMGTags;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;
import java.util.function.Predicate;

//arguably these should just be additional constructors for ForceableFluidTank
@ParametersAreNonnullByDefault
public class EngineFluidTank extends ForceableFluidTank {
    public EngineFluidTank(int capacity, boolean extractionAllowed, boolean insertionAllowed, Consumer<FluidStack> updateCallback) {
        super(capacity, updateCallback);
        this.extractionAllowed = extractionAllowed;
        this.insertionAllowed = insertionAllowed;
    }
	
	public EngineFluidTank(int capacity, boolean extractionAllowed, boolean insertionAllowed, Consumer<FluidStack> updateCallback, Predicate<FluidStack> validator) {
		super(capacity, updateCallback);
		this.extractionAllowed = extractionAllowed;
		this.insertionAllowed = insertionAllowed;
		this.validator = validator;
	}
	
	public static EngineFluidTank exhaustTank(int capacity, Consumer<FluidStack> updateCallback) {
		return new EngineFluidTank(capacity, true, false, updateCallback);
	}
	
	public static EngineFluidTank fuelTank(int capacity, Consumer<FluidStack> updateCallback) {
		return new EngineFluidTank(capacity, false, true, updateCallback, f -> !f.is(TFMGTags.Fluids.AIR.tag));
	}
	
	public static EngineFluidTank airTank(int capacity, Consumer<FluidStack> updateCallback) {
		return new EngineFluidTank(capacity, false, true, updateCallback, f -> f.is(TFMGTags.Fluids.AIR.tag));
	}
}
