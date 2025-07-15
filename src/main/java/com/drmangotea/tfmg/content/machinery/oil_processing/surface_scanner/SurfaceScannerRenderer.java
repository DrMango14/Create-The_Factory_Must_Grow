package com.drmangotea.tfmg.content.machinery.oil_processing.surface_scanner;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import com.simibubi.create.foundation.render.RenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class SurfaceScannerRenderer extends SafeBlockEntityRenderer<SurfaceScannerBlockEntity> {
    public SurfaceScannerRenderer(BlockEntityRendererProvider.Context context) {

    }
    @Override
    protected void renderSafe(SurfaceScannerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        ms.pushPose();
        for(int x = 0;x<5;x++) {
            for (int z = 0; z < 5; z++) {

                    if(be.grid[x][z]!=null && be.grid[x][z])
                        CachedBuffers.partial(TFMGPartialModels.SURFACE_SCANNER_LIGHT, blockState)
                                .translate((x - 2)*0.19, 0, (z - 2)*0.19)
                                .light(LightTexture.FULL_BRIGHT)
                                .color(255, 69, 96, 255)
                                .renderInto(ms, bufferSource.getBuffer(RenderTypes.additive()));

            }
        }
        ms.popPose();
    }
}
