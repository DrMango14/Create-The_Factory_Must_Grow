package com.drmangotea.tfmg.blocks.machines.metal_processing.casting_spout;



import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour.TankSegment;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;

public class CastingSpoutRenderer extends SafeBlockEntityRenderer<CastingSpoutBlockEntity> {

    public CastingSpoutRenderer(BlockEntityRendererProvider.Context context) {
    }

    static final PartialModel[] BITS =
            {TFMGPartialModels.CASTING_SPOUT_CONNECTOR, TFMGPartialModels.CASTING_SPOUT_BOTTOM };

    @Override
    protected void renderSafe(CastingSpoutBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        SmartFluidTankBehaviour tank = be.tank1;
        if (tank == null)
            return;

        TankSegment primaryTank = be.tank1.getPrimaryTank();
        FluidStack fluidStack = primaryTank.getRenderedFluid();
        float level = primaryTank.getFluidLevel()
                .getValue(partialTicks);

        if (!fluidStack.isEmpty() && level != 0) {
            boolean top = fluidStack.getFluid()
                    .getFluidType()
                    .isLighterThanAir();

            level = Math.max(level, 0.175f);
            float min = 2.5f / 16f;
            float max = min + (11 / 16f);
            float yOffset = (11 / 16f) * level;

            ms.pushPose();
            if (!top) ms.translate(0, yOffset, 0);
            else ms.translate(0, max - min, 0);

            FluidRenderer.renderFluidBox(fluidStack,
                    min, min - yOffset, min,
                    max, min, max,
                    buffer, ms, light, false);

            ms.popPose();
        }

        int processingTicks;
    if(be.basin==null){
        processingTicks = 0;
    }else
        processingTicks = be.basin.timer;

        float processingPT = processingTicks - partialTicks;
        float processingProgress = 1 - (processingPT - 5) / 10;
        processingProgress = Mth.clamp(processingProgress, 0, 1);
        float radius = 0;

        if (be.isRunning) {
            radius = (float) (Math.pow(((0.3) - 1), 2) - 1);
            AABB bb = new AABB(0.5, .5, 0.5, 0.5, -1.2, 0.5).inflate(radius / 32f);
            FluidRenderer.renderFluidBox(fluidStack, (float) bb.minX, (float) bb.minY, (float) bb.minZ,
                    (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ, buffer, ms, light, true);
        }


        ms.pushPose();
        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        CachedBufferer.partial(TFMGPartialModels.CASTING_SPOUT_BOTTOM, blockState)
                .translateY(be.movement.getValue(partialTicks)*2)
                .light(light)
                .renderInto(ms, vb);

        CachedBufferer.partial(TFMGPartialModels.CASTING_SPOUT_CONNECTOR, blockState)
                .translateY(be.movement.getValue(partialTicks))
                .light(light)
                .renderInto(ms, vb);

        ms.popPose();

    }

}