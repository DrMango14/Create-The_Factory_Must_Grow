package com.drmangotea.tfmg.content.decoration.kinetics.cogs;

import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.util.Mth;

import static com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual.rotationOffset;

public class TFMGCogwheelRenderer extends KineticBlockEntityRenderer<SimpleKineticBlockEntity> {

	public TFMGCogwheelRenderer(Context context) {
		super(context);
	}

	@Override
	protected void renderSafe(SimpleKineticBlockEntity be, float partialTicks, PoseStack ms,
		MultiBufferSource buffer, int light, int overlay) {

		if (VisualizationManager.supportsVisualization(be.getLevel()))
			return;

		if (!AllBlocks.LARGE_COGWHEEL.has(be.getBlockState())) {
			super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
			return;
		}

		Axis axis = getRotationAxisOf(be);
		Direction facing = Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE);
		PartialModel model = be.getBlockState().is(TFMGBlocks.LARGE_ALUMINUM_COGWHEEL.get()) ? TFMGPartialModels.LARGE_ALUMINUM_COGWHEEL : TFMGPartialModels.LARGE_STEEL_COGWHEEL;
		renderRotatingBuffer(be,
			CachedBuffers.partialFacingVertical(model, be.getBlockState(), facing),
			ms, buffer.getBuffer(RenderType.cutoutMipped()), light);

		float angle = getAngleForLargeCogShaft(be, axis);
		SuperByteBuffer shaft =
			CachedBuffers.partialFacingVertical(AllPartialModels.COGWHEEL_SHAFT, be.getBlockState(), facing);
		kineticRotationTransform(shaft, be, axis, angle, light);
		shaft.renderInto(ms, buffer.getBuffer(RenderType.solid()));
	}

	public static float getAngleForLargeCogShaft(SimpleKineticBlockEntity be, Axis axis) {
		BlockPos pos = be.getBlockPos();
		float offset = rotationOffset(be.getBlockState(), axis, pos);
		float time = AnimationTickHolder.getRenderTime(be.getLevel());
        return ((time * be.getSpeed() * .3f + offset) % 360) * Mth.DEG_TO_RAD;
	}
}