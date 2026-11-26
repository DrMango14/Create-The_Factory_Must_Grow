package com.drmangotea.tfmg.content.decoration.tanks.steel;

import com.drmangotea.tfmg.mixin.accessor.FluidTankBlockEntityAccessor;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.platform.NeoForgeCatnipServices;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

public class SteelFluidTankRenderer extends SafeBlockEntityRenderer<SteelTankBlockEntity> {
    public SteelFluidTankRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(SteelTankBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (!te.isController())
			return;
		if (!((FluidTankBlockEntityAccessor)te).tfmg$getWindow()) {
			if (te.isDistillationTower)
				renderAsDistillationTower(te, partialTicks, ms, buffer, light, overlay);
			return;
		}
		LerpedFloat fluidLevel = te.getFluidLevel();
		if (fluidLevel == null)
			return;
		
		float capHeight = 1 / 4f;
		float tankHullWidth = 9 / 128f;
		float minPuddleHeight = 1 / 16f;
		float totalHeight = ((FluidTankBlockEntityAccessor)te).tfmg$getHeight() - 2 * capHeight - minPuddleHeight;
		
		float level = fluidLevel.getValue(partialTicks);
		if (level < 1 / (512f * totalHeight))
			return;
		float clampedLevel = Mth.clamp(level * totalHeight, 0, totalHeight);
		
		FluidStack fluidStack = te.getTankInventory().getFluid();
		
		if (fluidStack.isEmpty())
			return;
		boolean top = fluidStack.getFluid()
			.getFluidType()
			.isLighterThanAir();
		
		float xzMax = ((FluidTankBlockEntityAccessor)te).tfmg$getWidth() - tankHullWidth;
		float yMin = totalHeight + capHeight + minPuddleHeight - clampedLevel;
		float yMax = yMin + clampedLevel;
		
		if (top) {
			yMin += totalHeight - clampedLevel;
			yMax += totalHeight - clampedLevel;
		}
		
		ms.pushPose();
		ms.translate(0, clampedLevel - totalHeight, 0);
		NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(
			fluidStack,
			tankHullWidth, yMin, tankHullWidth,
			xzMax, yMax, xzMax,
			buffer, ms, light, false,true
		);
		ms.popPose();
    }

    protected void renderAsDistillationTower(SteelTankBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
        ms.pushPose();
        var msr = TransformStack.of(ms);
        msr.translate(((FluidTankBlockEntityAccessor)be).tfmg$getWidth() / 2f, 0.5, ((FluidTankBlockEntityAccessor)be).tfmg$getWidth() / 2f);

        float dialPivot = 5.75f / 16;

        for (Direction d : Iterate.horizontalDirections) {
            ms.pushPose();
            CachedBuffers.partial(TFMGPartialModels.TOWER_GAUGE, blockState)
                    .rotateYDegrees(d.toYRot())
                    .uncenter()
                    .translate(((FluidTankBlockEntityAccessor)be).tfmg$getWidth() / 2f - 6 / 16f, 0, 0)
                    .light(light)
                    .renderInto(ms, vb);
            float dialPivotY = 6f / 16;
            float dialPivotZ = 8f / 16;
            CachedBuffers.partial(AllPartialModels.BOILER_GAUGE_DIAL, blockState)
                    .rotateYDegrees(d.toYRot())
                    .uncenter()
                    .translate(((FluidTankBlockEntityAccessor)be).tfmg$getWidth() / 2f - 6 / 16f, 0, 0)
                    .translate(0, dialPivotY, dialPivotZ)
                    .rotateXDegrees(-be.visualGaugeRotation.getValue(partialTicks) + 90)
                    .translate(0, -dialPivotY, -dialPivotZ)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }
        ms.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(SteelTankBlockEntity te) {
        return te.isController();
    }
}
