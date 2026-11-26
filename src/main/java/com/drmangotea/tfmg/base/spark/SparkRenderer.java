package com.drmangotea.tfmg.base.spark;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@NothingNullByDefault
@OnlyIn(Dist.CLIENT)
public class SparkRenderer extends EntityRenderer<Spark> {
    public int color = 0xFFD600;

    public SparkRenderer(EntityRendererProvider.Context p_173962_) {
        super(p_173962_);
    }

    protected int getBlockLightLevel(Spark spark, BlockPos pos) {
        return 15;
    }

    public void render(
		Spark spark, float entityYaw, float partialTick, PoseStack poseStack,
		MultiBufferSource bufferSource, int packedLight
	) {
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(spark.getTexture()));

        vertex(vertexconsumer, matrix4f, packedLight, 0F, 0F, 0, 1, spark.getColor());
        vertex(vertexconsumer, matrix4f, packedLight, 1F, 0F, 1, 1, spark.getColor());
        vertex(vertexconsumer, matrix4f, packedLight, 1F, 1F, 1, 0, spark.getColor());
        vertex(vertexconsumer, matrix4f, packedLight, 0F, 1F, 0, 0, spark.getColor());
        poseStack.popPose();
        super.render(spark, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
    private void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, int light, float x, float y, float u, float v, int color) {
        vertexConsumer.addVertex(matrix4f, x - 0.5F, y - 0.25F, 0.0F)
			.setColor(color)
			.setUv(u, v)
			.setOverlay(OverlayTexture.NO_OVERLAY)
			.setLight(15728880)
			.setUv2(light,light)
			.setNormal( 0.0F, 1.0F, 0.0F);
    }
	
    public ResourceLocation getTextureLocation(Spark spark) {
        return spark.getTexture();
    }
}