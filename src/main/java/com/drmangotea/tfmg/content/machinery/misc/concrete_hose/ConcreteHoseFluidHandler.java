package com.drmangotea.tfmg.content.machinery.misc.concrete_hose;

import com.drmangotea.tfmg.registry.TFMGFluids;
import com.simibubi.create.content.fluids.transfer.FluidDrainingBehaviour;
import com.simibubi.create.content.fluids.transfer.FluidFillingBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;


import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ConcreteHoseFluidHandler implements IFluidHandler {


	@Override
	@SuppressWarnings("removal")
	public int fill(FluidStack resource, FluidAction action) {
		if(!resource.getFluid().isSame(TFMGFluids.LIQUID_CONCRETE.getSource()))
			return 0;
		if (!internalTank.isEmpty() && !resource.isFluidEqual(internalTank.getFluid()))
			return 0;
		if (resource.isEmpty() || !FluidHelper.hasBlockState(resource.getFluid()))
			return 0;

		int diff = resource.getAmount();
		int totalAmountAfterFill = diff + internalTank.getFluidAmount();
		FluidStack remaining = resource.copy();
		boolean deposited = false;

		if (predicate.get() && totalAmountAfterFill >= 1000) {
			if (filler.tryDeposit(resource.getFluid(), rootPosGetter.get(), action.simulate())) {
				remaining.shrink(1000);
				diff -= 1000;
				deposited = true;
			}
		}

		if (action.simulate())
			return diff <= 0 ? resource.getAmount() : internalTank.fill(remaining, action);
		if (diff <= 0) {
			internalTank.drain(-diff, FluidAction.EXECUTE);
			return resource.getAmount();
		}

		return internalTank.fill(remaining, action) + (deposited ? 1000 : 0);
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		return internalTank.getFluidInTank(tank);
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		return FluidStack.EMPTY;
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		return FluidStack.EMPTY;
	}



	//

	private SmartFluidTank internalTank;
	private ConcreteFillingBehavior filler;
	private Supplier<BlockPos> rootPosGetter;
	private Supplier<Boolean> predicate;

	public ConcreteHoseFluidHandler(SmartFluidTank internalTank, ConcreteFillingBehavior filler, Supplier<BlockPos> rootPosGetter, Supplier<Boolean> predicate) {
		this.internalTank = internalTank;
		this.filler = filler;
		this.rootPosGetter = rootPosGetter;
		this.predicate = predicate;
	}

	@Override
	public int getTanks() {
		return internalTank.getTanks();
	}

	@Override
	public int getTankCapacity(int tank) {
		return internalTank.getTankCapacity(tank);
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return internalTank.isFluidValid(tank, stack);
	}

}