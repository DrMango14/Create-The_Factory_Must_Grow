package com.drmangotea.tfmg.content.engines.upgrades;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class EnginePipingUpgrade extends EngineUpgrade {
	private BlockCapabilityCache<IFluidHandler, Direction> cache;

	public EnginePipingUpgrade(ResourceLocation id) {
		super(id);
	}

	public void findTank(AbstractSmallEngineBlockEntity be) {
		if (!(be.getLevel() instanceof ServerLevel level)) return;
        for (Direction direction : Direction.values()) {
            BlockPos pos = be.getBlockPos().relative(direction);
			if (level.getBlockEntity(pos) instanceof AbstractSmallEngineBlockEntity) continue;
			Direction opposite = direction.getOpposite();
            if (level.getCapability(Capabilities.FluidHandler.BLOCK, pos, opposite) != null) {
				cache = BlockCapabilityCache.create(Capabilities.FluidHandler.BLOCK, level, pos, opposite);
                return;
            }
        }
		cache = null;
    }

    @Override
    public void updateUpgrade(AbstractSmallEngineBlockEntity be) {
        findTank(be);
    }

	//TODO: Fluid extraction is being funky
    @Override
    public void lazyTickUpgrade(AbstractSmallEngineBlockEntity engine) {
        Level level = engine.getLevel();
        if (level == null || cache == null) return;
        IFluidHandler fluidHandler = cache.getCapability();
		if (fluidHandler == null) {
			findTank(engine);
			return;
		}
		
		AbstractSmallEngineBlockEntity controller = engine.getControllerBE();
		if(controller == null || controller.fuelTank == null)
			return;
		
		FluidStack toDrain = FluidStack.EMPTY;
		FluidStack contained = controller.fuelTank.getFluid();
		for (int i = 0; i < fluidHandler.getTanks(); i++) {
			FluidStack fluidStack = fluidHandler.getFluidInTank(i);
			if (fluidStack.isEmpty()) continue;
			if (!contained.isEmpty() && FluidStack.isSameFluidSameComponents(contained, fluidStack)) {
				toDrain = fluidStack.copy();
				break;
			}
			toDrain = fluidStack.copy();
		}
		if (toDrain.isEmpty()) return;
		
		int maxInput = controller.fuelTank.fill(toDrain.copyWithAmount(500), IFluidHandler.FluidAction.SIMULATE);
		int maxOutput = fluidHandler.drain(maxInput, IFluidHandler.FluidAction.SIMULATE).getAmount();
		
		int amount = Math.min(maxInput, Math.min(maxOutput, controller.fuelTank.getSpace()));
		FluidStack drained = fluidHandler.drain(toDrain.copyWithAmount(amount), IFluidHandler.FluidAction.EXECUTE);
		controller.fuelTank.fill(drained, IFluidHandler.FluidAction.EXECUTE);
	}
}
