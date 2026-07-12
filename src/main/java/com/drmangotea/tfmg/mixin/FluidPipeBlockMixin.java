package com.drmangotea.tfmg.mixin;

import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipeBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FluidPipeBlock.class)
public class FluidPipeBlockMixin {

	@WrapOperation(
		method = "updateBlockState",
		at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/fluids/pipes/FluidPipeBlock;canConnectTo(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z")
	)
	private boolean tfmg$filterLockedPipes(BlockAndTintGetter neighbourWorld, BlockPos neighbourPos, BlockState neighbourState, Direction direction, Operation<Boolean> original, BlockState state, Direction preferredDirection, Direction ignore, BlockAndTintGetter world, BlockPos pos) {
		boolean shouldConnect = original.call(neighbourWorld, neighbourPos, neighbourState, direction);

		BlockEntity be = world.getBlockEntity(neighbourPos);

		if (be instanceof TFMGPipeBlockEntity tfmgPipe) {
			if (tfmgPipe.locked) {
				var oppositeProperty = FluidPipeBlock.PROPERTY_BY_DIRECTION.get(direction.getOpposite());
				shouldConnect =
					neighbourState.hasProperty(oppositeProperty) &&
					neighbourState.getValue(oppositeProperty);
			}
		}

		return shouldConnect;
	}
}