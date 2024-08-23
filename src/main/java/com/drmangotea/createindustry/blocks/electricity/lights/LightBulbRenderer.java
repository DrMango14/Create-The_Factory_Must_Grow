package com.drmangotea.createindustry.blocks.electricity.lights;

import com.drmangotea.createindustry.blocks.electricity.base.WallMountBlock;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.RenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class LightBulbRenderer extends SafeBlockEntityRenderer<LightBulbBlockEntity> {
    public LightBulbRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    protected void renderSafe(LightBulbBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {


        BlockState blockState = be.getBlockState();
        TransformStack msr = TransformStack.cast(ms);


        ms.pushPose();

        float glow = be.glow.getValue(partialTicks);
        int color =  Math.min(100,(int) (glow/0.2f));

        if(be.glow.getValue()!=0) {
           // if(blockState.is(TFMGBlocks.INDUSTRIAL_LIGHT.get())){
           //     CachedBufferer.partialFacing(TFMGPartialModels.INDUSTRIAL_LIGHT, blockState, blockState.getValue(WallMountBlock.FACING))
           //             .light((int) glow * 3 + 40)
           //             .color(color, color, (int) (color * 0.8), 255)
           //             .disableDiffuse()
           //             .renderInto(ms, buffer.getBuffer(RenderTypes.getAdditive()));
//
           // }else

                CachedBufferer.partialFacing(TFMGPartialModels.LIGHT_BULB, blockState, blockState.getValue(WallMountBlock.FACING))
                        .light((int) glow * 3 + 40)
                        .color(color, color, (int) (color * 0.8), 255)
                        .disableDiffuse()
                        .renderInto(ms, buffer.getBuffer(RenderTypes.getAdditive()));


        }
        ms.popPose();
    }
}
