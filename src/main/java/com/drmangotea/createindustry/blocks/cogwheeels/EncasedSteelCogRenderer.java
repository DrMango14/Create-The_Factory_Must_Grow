package com.drmangotea.createindustry.blocks.cogwheeels;

import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogwheelBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EncasedSteelCogRenderer extends KineticBlockEntityRenderer<SimpleKineticBlockEntity> {

	private boolean large;

	public static EncasedSteelCogRenderer small(BlockEntityRendererProvider.Context context) {
		return new EncasedSteelCogRenderer(context, false);
	}

	public static EncasedSteelCogRenderer large(BlockEntityRendererProvider.Context context) {
		return new EncasedSteelCogRenderer(context, true);
	}

	public EncasedSteelCogRenderer(BlockEntityRendererProvider.Context context, boolean large) {
		super(context);
		this.large = large;
	}

	@Override
	protected void renderSafe(SimpleKineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
							  int light, int overlay) {
		super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
		if (Backend.canUseInstancing(be.getLevel()))
			return;

		BlockState blockState = be.getBlockState();
		Block block = blockState.getBlock();
		if (!(block instanceof IRotate))
			return;
		IRotate def = (IRotate) block;

		Direction.Axis axis = getRotationAxisOf(be);
		BlockPos pos = be.getBlockPos();
		float angle = large ? BracketedKineticBlockEntityRenderer.getAngleForLargeCogShaft(be, axis)
			: getAngleForTe(be, pos, axis);

		for (Direction d : Iterate.directionsInAxis(getRotationAxisOf(be))) {
			if (!def.hasShaftTowards(be.getLevel(), be.getBlockPos(), blockState, d))
				continue;
			SuperByteBuffer shaft = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), d);
			kineticRotationTransform(shaft, be, axis, angle, light);
			shaft.renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
		}
	}

	@Override
	protected SuperByteBuffer getRotatedModel(SimpleKineticBlockEntity be, BlockState state) {
		return CachedBufferer.partialFacingVertical(
				large ? TFMGPartialModels.LARGE_STEEL_COGHWEEL : TFMGPartialModels.STEEL_COGHWEEL, state,
			Direction.fromAxisAndDirection(state.getValue(EncasedCogwheelBlock.AXIS), Direction.AxisDirection.POSITIVE));
	}

}