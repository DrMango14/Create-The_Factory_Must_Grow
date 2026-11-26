package com.drmangotea.tfmg.base.fluid;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Simple tank with insertion and extraction restrictions that can be bypassed manually.
 */
@NothingNullByDefault
public class ForceableFluidTank extends SmartFluidTank {
    protected boolean extractionAllowed = true, insertionAllowed = true;

    public ForceableFluidTank(int capacity, Consumer<FluidStack> updateCallback) {
        super(capacity, updateCallback);
    }

    public ForceableFluidTank blockExtraction() {
        this.extractionAllowed = false;
        return this;
    }

    public ForceableFluidTank blockInsertion() {
        this.insertionAllowed = false;
        return this;
    }

    public ForceableFluidTank allowExtraction() {
        this.extractionAllowed = true;
        return this;
    }

    public ForceableFluidTank allowInsertion() {
        this.insertionAllowed = true;
        return this;
    }

    public ForceableFluidTank withValidator(Predicate<FluidStack> validator) {
        this.validator = validator;
        return this;
    }

    public ForceableFluidTank withCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (!extractionAllowed) return FluidStack.EMPTY;
        return super.drain(resource, action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (!extractionAllowed) return FluidStack.EMPTY;
        return super.drain(maxDrain, action);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (!insertionAllowed) return 0;
        return super.fill(resource, action);
    }
	
	/**Drain a {@link ForceableFluidTank}, even if {@code extractionAllowed = false}**/
	public FluidStack forceDrain(FluidStack resource, FluidAction action) {
        return super.drain(resource, action);
    }
	
	/**Drain a {@link ForceableFluidTank}, even if {@code extractionAllowed = false}**/
	public FluidStack forceDrain(int maxDrain, FluidAction action) {
        return super.drain(maxDrain, action);
    }

	/**Fill a {@link ForceableFluidTank}, even if {@code insertionAllowed = false}**/
    public int forceFill(FluidStack resource, FluidAction action) {
        return super.fill(resource, action);
    }
	
	public ForceableFluidTank read(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
		readFromNBT(lookupProvider, nbt);
		int over = fluid.getAmount() - capacity;
		if (over > 0) this.forceDrain(over, FluidAction.EXECUTE);
		return this;
	}

    public ForceableFluidTank readFromNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        this.fluid = FluidStack.parseOptional(lookupProvider, nbt.getCompound("Fluid"));
        return this;
    }

    public CompoundTag writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        if (!this.fluid.isEmpty()) {
            nbt.put("Fluid", this.fluid.save(lookupProvider));
        }
        return nbt;
    }
}
