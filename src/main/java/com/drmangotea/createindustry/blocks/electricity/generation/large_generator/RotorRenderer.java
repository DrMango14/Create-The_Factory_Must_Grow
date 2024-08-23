package com.drmangotea.createindustry.blocks.electricity.generation.large_generator;


import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock.AXIS;

public class RotorRenderer extends KineticBlockEntityRenderer<RotorBlockEntity> {

	public RotorRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void renderSafe(RotorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
							  int light, int overlay) {
		super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

		if (Backend.canUseInstancing(be.getLevel()))
			return;

		BlockState blockState = be.getBlockState();

		float speed = be.visualSpeed.getValue(partialTicks) * 3 / 10f;
		float angle = be.angle + speed * partialTicks;

		VertexConsumer vb = buffer.getBuffer(RenderType.solid());

		renderFrame(be, ms, light, blockState, angle, vb);

		renderFlywheel(be, ms, light, blockState, angle, vb);
	}

	private void renderFlywheel(RotorBlockEntity be, PoseStack ms, int light, BlockState blockState, float angle,
								VertexConsumer vb) {
		SuperByteBuffer wheel = CachedBufferer.block(blockState);
		kineticRotationTransform(wheel, be, getRotationAxisOf(be), AngleHelper.rad(angle), light);
		wheel.renderInto(ms, vb);
	}

	private void renderFrame(RotorBlockEntity be, PoseStack ms, int light, BlockState blockState, float angle,
								VertexConsumer vb) {
		ms.pushPose();


		Direction direction=null;
		Direction.Axis axis = blockState.getValue(AXIS);

		if(axis == Direction.Axis.X)
			direction = Direction.WEST;
		if(axis == Direction.Axis.Y)
			direction = Direction.UP;
		if(axis == Direction.Axis.Z)
			direction = Direction.NORTH;

		//SuperByteBuffer frame = CachedBufferer.partialFacing(TFMGPartialModels.ROTOR_FRAME, blockState,direction);
		//if(be.valid) {
		//	frame.renderInto(ms, vb);
		//}

		ms.popPose();
	}

	@Override
	protected BlockState getRenderedBlockState(RotorBlockEntity be) {
		return shaft(getRotationAxisOf(be));
	}

}
