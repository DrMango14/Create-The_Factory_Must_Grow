package com.drmangotea.tfmg.blocks.machines.oil_processing.pumpjack.hammer_holder;


import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class PumpjackHammerHolderRenderer extends KineticBlockEntityRenderer<PumpjackHammerHolderBlockEntity> {

	protected float lastAngle = Float.NaN;
	public PumpjackHammerHolderRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void renderSafe(PumpjackHammerHolderBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
							  int light, int overlay) {

	//	super.renderSafe(te, partialTicks, ms, buffer, light, overlay);


		if (Backend.canUseInstancing(be.getLevel()))
			return;

		BlockState blockState = be.getBlockState();
		PumpjackHammerHolderBlockEntity wte = (PumpjackHammerHolderBlockEntity) be;


		float speed = be.visualSpeed.getValue(partialTicks) * 3 / 10f;
		float angle = be.angle.getValue() + speed * partialTicks;

		//if (Math.abs(angle - lastAngle) < 0.001)
		//	return;
		VertexConsumer vb = buffer.getBuffer(RenderType.solid());
		renderHammer(be, ms, light, blockState, angle, vb);

		lastAngle = angle;
	}

	private void renderHammer(PumpjackHammerHolderBlockEntity be, PoseStack ms, int light, BlockState blockState, float angle,
							  VertexConsumer vb) {


		SuperByteBuffer hammer =
				CachedBufferer.partialFacing(TFMGPartialModels.PUMPJACK_HAMMER, be.getBlockState(), be.direction);
		int lightInFront = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos());

		Direction.Axis axis = blockState.getValue(FACING).getAxis();

		hammer.centre();

		hammer.rotate(be.direction.getClockWise(), (float) Math.toRadians(angle));

		hammer.unCentre();


		hammer.renderInto(ms, vb);
		//kineticRotationTransform(hammer, be, be.direction2.getAxis(), angle, lightInFront).renderInto(ms, vb);




	}




}
