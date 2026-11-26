package com.drmangotea.tfmg.content.decoration.tanks;

import com.drmangotea.tfmg.base.TFMGBlockConnectivityHandler;
import com.drmangotea.tfmg.content.decoration.tanks.aluminum.AluminumTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.cast_iron.CastIronTankBlock;
import com.drmangotea.tfmg.mixin.accessor.FluidTankBlockEntityAccessor;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlock.Shape;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import static java.lang.Math.abs;

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
	
	@Override
	public TFMGFluidTankBlockEntity getControllerBE() {
		if (isController()) return this;
		if (level != null && level.getBlockEntity(controller) instanceof TFMGFluidTankBlockEntity be)
			return be;
		return null;
	}
	
	@Override
	protected void updateConnectivity() {
		updateConnectivity = false;
		if (level.isClientSide || !isController())
			return;
		TFMGBlockConnectivityHandler.formMulti(this);
	}
	
	@Override
	public void notifyMultiUpdated() {
		BlockState state = this.getBlockState();
		if (isTank(state)) {
			state = state
				.setValue(FluidTankBlock.BOTTOM, getController().getY() == getBlockPos().getY())
				.setValue(FluidTankBlock.TOP, getController().getY() + height - 1 == getBlockPos().getY());
			level.setBlock(getBlockPos(), state, 6);
		}
		if (isController())
			setWindows(window);
		onFluidStackChanged(tankInventory.getFluid());
		updateBoilerState();
		setChanged();
	}
	
	@Override
	public void removeController(boolean keepFluids) {
		if (level.isClientSide)
			return;
		updateConnectivity = true;
		if (!keepFluids) applyFluidTankSize(1);
		controller = null;
		width = 1;
		height = 1;
		
		BlockState state = getBlockState();
		if (isTank(state)) {
			state = state
				.setValue(FluidTankBlock.BOTTOM, true)
				.setValue(FluidTankBlock.TOP, true)
				.setValue(FluidTankBlock.SHAPE, window ? Shape.WINDOW : Shape.PLAIN);
			level.setBlock(worldPosition, state, 22);
		}
		((FluidTankBlockEntityAccessor)this).tfmg$refreshCapability();
		setChanged();
		sendData();
	}
	
	@Override
	public void setWindows(boolean window) {
		this.window = window;
		for (int Y = 0; Y < height; Y++) { for (int X = 0; X < width; X++) { for (int Z = 0; Z < width; Z++) {
			BlockPos pos = this.worldPosition.offset(X, Y, Z);
			BlockState state = level.getBlockState(pos);
						
			if (!isTank(state)) continue;
			
			Shape shape = Shape.PLAIN;
			if (window) { shape = switch (width) {
				// SIZE 1: Every tank has a window
				case 1 -> Shape.WINDOW;
				// SIZE 2: Every tank has a corner window
				case 2 -> X == 0 ?
					  Z == 0 ? Shape.WINDOW_NW : Shape.WINDOW_SW
					: Z == 0 ? Shape.WINDOW_NE : Shape.WINDOW_SE;
				// SIZE 3: Tanks in the center have a window
				case 3 -> (abs(X - Z) == 1) ? Shape.WINDOW : Shape.PLAIN;
				default -> Shape.PLAIN;
			}; }
			
			level.setBlock(pos, state.setValue(FluidTankBlock.SHAPE, shape), 22);
			level.getChunkSource().getLightEngine().checkBlock(pos);
		}}}
	}
	
	public boolean isTank(BlockState state) { return AluminumTankBlock.isTank(state) || CastIronTankBlock.isTank(state); }
}