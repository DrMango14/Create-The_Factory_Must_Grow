package com.drmangotea.createindustry.blocks.electricity.lights.rgb;

import com.drmangotea.createindustry.blocks.electricity.base.WallMountBlock;
import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.RenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class RGBLightBulbRenderer extends SafeBlockEntityRenderer<RGBLightBulbBlockEntity> {
    public RGBLightBulbRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    protected void renderSafe(RGBLightBulbBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {


        BlockState blockState = be.getBlockState();
        TransformStack msr = TransformStack.cast(ms);


        ms.pushPose();



        float glow = be.glow.getValue(partialTicks);
        int color =  Math.min(200,(int) (glow/0.8f));

        int seed = (int) glow;
        Random random = new Random(seed);


       // CachedBufferer.partial(AllPartialModels.DISPLAY_LINK_TUBE, blockState)
       //         .light(LightTexture.FULL_BRIGHT)
       //         .renderInto(ms, buffer.getBuffer(RenderType.translucent()));
        if(be.glow.getValue()!=0)
            CachedBufferer.partialFacing(TFMGPartialModels.LIGHT_BULB, blockState,blockState.getValue(WallMountBlock.FACING))
                    .light((int) glow*3+40)
                    .color(be.red,be.green,be.blue,255)
                    .disableDiffuse()
                    .renderInto(ms, buffer.getBuffer(RenderTypes.getAdditive()));



        ms.popPose();
    }
}
