package com.drmangotea.tfmg.base.spark;

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
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class SparkRenderer extends EntityRenderer<Spark> {



    public int color = 0xFFD600;





    public SparkRenderer(EntityRendererProvider.Context p_173962_) {
        super(p_173962_);
    }

    protected int getBlockLightLevel(Spark spark, BlockPos pos) {
        return 15;
    }

    public void render(Spark spark, float p_114081_, float p_114082_, PoseStack poseStack, MultiBufferSource bufferSource, int p_114085_) {
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(spark.getTexture()));



        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 0, 0, 1,spark.getColor());
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 0, 1, 1,spark.getColor());
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 1, 1, 0,spark.getColor());
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 1, 0, 0,spark.getColor());
        poseStack.popPose();
        super.render(spark, p_114081_, p_114082_, poseStack, bufferSource, p_114085_);
    }
    private void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int p_114093_, float p_114094_, int p_114095_, int p_114096_, int p_114097_,int color) {
        vertexConsumer.addVertex(matrix4f, p_114094_ - 0.5F, (float)p_114095_ - 0.25F, 0.0F).setColor(color).setUv((float)p_114096_, (float)p_114097_).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setUv2(p_114093_,p_114093_).setNormal( 0.0F, 1.0F, 0.0F);
    }
    public @NotNull ResourceLocation getTextureLocation(Spark spark) {
        return spark.getTexture();
    }


}