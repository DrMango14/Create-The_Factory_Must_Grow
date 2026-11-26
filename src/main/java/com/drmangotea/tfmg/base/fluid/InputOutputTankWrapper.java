package com.drmangotea.tfmg.base.fluid;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;


@NothingNullByDefault
public class InputOutputTankWrapper implements IFluidHandler {
	protected final IFluidHandler input, output;
	
	public InputOutputTankWrapper(IFluidHandler output, IFluidHandler input) {
		this.input = input;
		this.output = output;
	}
	
	@Override
	public int getTanks() {
		return 2;
	}
	
	@Override
	public FluidStack getFluidInTank(int tank) {
		return getHandlerFromIndex(tank).getFluidInTank(tank);
	}
	
	@Override
	public int getTankCapacity(int tank) {
		return getHandlerFromIndex(tank).getTankCapacity(tank);
	}
	
	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return getHandlerFromIndex(tank).isFluidValid(tank, stack);
	}
	
	@Override
	public int fill(FluidStack resource, FluidAction action) {
		if (resource.isEmpty())
			return 0;
		return input.fill(resource, action);
	}
	
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.isEmpty())
			return resource;
		return output.drain(resource, action);
	}
	
	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		if (maxDrain == 0) return FluidStack.EMPTY;
		return output.drain(maxDrain, action);
	}
	
	protected IFluidHandler getHandlerFromIndex(int index) {
		return switch (index) {
			case 0 -> output;
			case 1 -> input;
			default -> throw new IllegalStateException("Unexpected index for IO Tank Wrapper: " + index + "=/= 0, 1");
		};
	}
}
