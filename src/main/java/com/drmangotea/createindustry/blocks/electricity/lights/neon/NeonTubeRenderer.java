package com.drmangotea.createindustry.blocks.electricity.lights.neon;

import com.drmangotea.createindustry.blocks.electricity.base.WallMountBlock;
import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.RenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.RotatedPillarBlock.AXIS;

public class NeonTubeRenderer extends SafeBlockEntityRenderer<NeonTubeBlockEntity> {



    public NeonTubeRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(NeonTubeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {



        BlockState blockState = be.getBlockState();
        TransformStack msr = TransformStack.cast(ms);


        ms.pushPose();

        float glow = be.glow.getValue(partialTicks);


        int color =  be.color.getTextColor();




        if(be.glow.getValue()!=0)
            if(be.color != DyeColor.BLACK) {
                CachedBufferer.partialFacing(TFMGPartialModels.NEON_TUBE_LIGHT, blockState, Direction.fromAxisAndDirection(blockState.getValue(AXIS), Direction.AxisDirection.POSITIVE))
                        .light((int) glow * 3 + 40)
                        .color(color)
                        .disableDiffuse()
                        .renderInto(ms, bufferSource.getBuffer(RenderTypes.getAdditive()));
            } else
                CachedBufferer.partialFacing(TFMGPartialModels.NEON_TUBE_LIGHT, blockState, Direction.fromAxisAndDirection(blockState.getValue(AXIS), Direction.AxisDirection.POSITIVE))
                        .light((int) glow * 3 + 40)
                        .color(be.red,be.green,be.blue,255)
                        .disableDiffuse()
                        .renderInto(ms, bufferSource.getBuffer(RenderTypes.getAdditive()));

        ms.popPose();
    }
}
