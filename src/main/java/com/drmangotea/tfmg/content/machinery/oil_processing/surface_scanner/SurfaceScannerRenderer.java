package com.drmangotea.tfmg.content.machinery.oil_processing.surface_scanner;

import com.drmangotea.tfmg.integration.sable.SurfaceScannerSable;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.RenderTypes;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaterniond;
import org.joml.Quaternionf;

public class SurfaceScannerRenderer extends SafeBlockEntityRenderer<SurfaceScannerBlockEntity> {
	private static final int COLOR = 0xffff4560; //#ff4560, transparency comes first
	private static final float OFFSET = 2.285f / 16f;
	
    public SurfaceScannerRenderer(BlockEntityRendererProvider.Context context) {}

	private Quaternionf getFacingQuat(SurfaceScannerBlockEntity be) {
		Quaterniond rot = SurfaceScannerSable.getSublevelRot(be);
		return new Quaternionf(0,-rot.y,0,rot.w).normalize();
	}
	
    @Override
    protected void renderSafe(SurfaceScannerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        ms.pushPose();
		ms.rotateAround(getFacingQuat(be), 0.5f, 0.5f, 0.5f);
		ms.translate(0f, 1f / 16f, 0f);
		
		CachedBuffers.partial(TFMGPartialModels.SURFACE_SCANNER_BASE, blockState)
			.light(light)
			.renderInto(ms, bufferSource.getBuffer(RenderType.SOLID));
  
		for (int x = 0 ; x < 7; x++) { for (int z = 0; z < 7; z++) {
			CachedBuffers.partial(TFMGPartialModels.SURFACE_SCANNER_BULB, blockState)
				.translate((x - 3)*OFFSET, 0f, (z - 3)*OFFSET)
				.light(light)
				.renderInto(ms, bufferSource.getBuffer(RenderType.TRANSLUCENT));
			
			if (be.grid[x][z]) {
				CachedBuffers.partial(TFMGPartialModels.SURFACE_SCANNER_LIGHT, blockState)
					.translate((x - 3)*OFFSET, 0f, (z - 3)*OFFSET)
					.light(LightTexture.FULL_BRIGHT)
					.color(COLOR)
					.renderInto(ms, bufferSource.getBuffer(RenderTypes.additive()));
			}
		} }
        ms.popPose();
    }
}