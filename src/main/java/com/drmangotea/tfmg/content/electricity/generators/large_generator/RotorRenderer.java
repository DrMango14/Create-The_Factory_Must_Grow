package com.drmangotea.tfmg.content.electricity.generators.large_generator;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
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

		if (VisualizationManager.supportsVisualization(be.getLevel()))
			return;

		BlockState blockState = be.getBlockState();

		float speed = be.visualSpeed.getValue(partialTicks) * 3 / 10f;
		float angle = be.angle + speed * partialTicks;

		VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

		ms.pushPose();

		renderRotor(be, ms, light, blockState, angle, vb);

		ms.popPose();
	}

	private void renderRotor(RotorBlockEntity be, PoseStack ms, int light, BlockState blockState, float angle,
							 VertexConsumer vb) {
		SuperByteBuffer wheel = CachedBuffers.block(blockState);
		kineticRotationTransform(wheel, be, getRotationAxisOf(be), AngleHelper.rad(angle), light);
		wheel.renderInto(ms, vb);
	}

	@Override
	protected BlockState getRenderedBlockState(RotorBlockEntity be) {
		return shaft(getRotationAxisOf(be));
	}

}
