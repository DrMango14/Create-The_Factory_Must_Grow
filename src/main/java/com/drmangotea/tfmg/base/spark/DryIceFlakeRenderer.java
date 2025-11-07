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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class DryIceFlakeRenderer extends EntityRenderer<DryIceFlake> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.parse("tfmg:textures/entity/dry_ice_flake.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);
    public DryIceFlakeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    protected int getBlockLightLevel(DryIceFlake flake, BlockPos blockPos) {
        return 15;
    }

    public void render(DryIceFlake flake, float p_114081_, float p_114082_, PoseStack stack, MultiBufferSource bufferSource, int p_114085_) {
        stack.pushPose();
        stack.scale(0.5F, 0.5F, 0.5F);
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose posestack$pose = stack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RENDER_TYPE);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 0, 0, 1);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 0, 1, 1);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 1, 1, 0);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 1, 0, 0);
        stack.popPose();
        super.render(flake, p_114081_, p_114082_, stack, bufferSource, p_114085_);
    }
    private static void vertex(VertexConsumer vertexConsumer, Matrix4f p_114091_, Matrix3f p_114092_, int p_114093_, float p_114094_, int p_114095_, int p_114096_, int p_114097_) {
        vertexConsumer.vertex(p_114091_, p_114094_ - 0.5F, (float)p_114095_ - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float)p_114096_, (float)p_114097_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_114093_).normal(p_114092_, 0.0F, 1.0F, 0.0F).endVertex();
    }
    public ResourceLocation getTextureLocation(DryIceFlake p_114078_) {
        return TEXTURE_LOCATION;
    }
}
