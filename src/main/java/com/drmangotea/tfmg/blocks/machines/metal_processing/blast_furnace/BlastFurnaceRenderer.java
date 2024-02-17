package com.drmangotea.tfmg.blocks.machines.metal_processing.blast_furnace;


import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static com.drmangotea.tfmg.blocks.machines.metal_processing.blast_furnace.BlastFurnaceOutputBlockEntity.MIN_CONTENT_DISPLAY_HEIGHT;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class BlastFurnaceRenderer extends SafeBlockEntityRenderer<BlastFurnaceOutputBlockEntity> {

    public BlastFurnaceRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(BlastFurnaceOutputBlockEntity be,
                              float partialTicks,
                              PoseStack ms,
                              MultiBufferSource buffer,
                              int light,
                              int overlay) {

        ms.pushPose();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(1 / 2f, 0.5, 1 / 2f);



        int lightInside = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above().relative(be.getBlockState().getValue(FACING).getOpposite()));

        if (be.isValid()) {
            var dir = be.getBlockState().getValue(FACING);
            if (dir == Direction.NORTH || dir == Direction.SOUTH || dir == Direction.WEST || dir == Direction.EAST) {

                float coalCokeLevel = be.contentLevel();
                if (coalCokeLevel >= MIN_CONTENT_DISPLAY_HEIGHT)
                    this.renderPile(be, partialTicks, ms, buffer, lightInside, overlay, coalCokeLevel, dir, false);

                float fluidLevel = be.contentFluidHeight();
                if (fluidLevel >= MIN_CONTENT_DISPLAY_HEIGHT)
                    this.renderPile(be, partialTicks, ms, buffer, lightInside, overlay, fluidLevel, dir, true);
            }
        }

        ms.popPose();

    }

    protected void renderPile(BlastFurnaceOutputBlockEntity be,
                              float partialTicks,
                              PoseStack ms,
                              MultiBufferSource buffer,
                              int light,
                              int overlay,
                              float height,
                              Direction direction,
                              boolean isMolten) {
        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        if (height >= MIN_CONTENT_DISPLAY_HEIGHT) {
            var model = isMolten ? TFMGPartialModels.MOLTEN_METAL_LAYER : TFMGPartialModels.COAL_COKE_DUST_LAYER;

            var area = be.getAlignedInteriorArea();
            var pos = be.getBlockPos();

            for (int x = area.minX(); x < area.maxX(); ++x) {
                for (int z = area.minZ(); z < area.maxZ(); ++z) {
                    CachedBufferer.partial(model, blockState)
                            .centre()
                            .translate(new Vec3(x - pos.getX()-1, height, z - pos.getZ()-1))
                            .light(light).renderInto(ms, vb);
                }
            }
        }
    }

    @Override
    public boolean shouldRenderOffScreen(BlastFurnaceOutputBlockEntity te) {
        return true;
    }

}
