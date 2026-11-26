package com.drmangotea.tfmg.mixin;

import com.drmangotea.tfmg.content.items.ScrewdriverItem;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidPipeBlock.class)
public class FluidPipeBlockMixin {
	@Inject(method = "canConnectTo", at = @At(value = "HEAD"), cancellable = true)
	private static void tfmg$filterLockedPipes(BlockAndTintGetter world, BlockPos neighbourPos, BlockState neighbour, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		if (world.getBlockEntity(neighbourPos) instanceof FluidPipeBlockEntity fp && ScrewdriverItem.isLocked(fp)) {
			FluidTransportBehaviour transport = BlockEntityBehaviour.get(world, neighbourPos, FluidTransportBehaviour.TYPE);
			if (transport == null) {
				cir.setReturnValue(false);
			} else {
				cir.setReturnValue(transport.canHaveFlowToward(neighbour, direction.getOpposite()));
			}
			cir.cancel();
		}
	}

	@Inject(method = "updateBlockState", at = @At("HEAD"), cancellable = true)
	private void updateBlockState(BlockState state, Direction preferredDirection, Direction ignore, BlockAndTintGetter world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		if (world.getBlockEntity(pos) instanceof FluidPipeBlockEntity fp && ScrewdriverItem.isLocked(fp)) {
			cir.setReturnValue(state);
			cir.cancel();
		}
	}
}
