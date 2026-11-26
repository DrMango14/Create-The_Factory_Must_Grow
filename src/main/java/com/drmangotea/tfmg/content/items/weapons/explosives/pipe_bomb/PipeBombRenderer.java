package com.drmangotea.tfmg.content.items.weapons.explosives.pipe_bomb;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT) @NothingNullByDefault
public class PipeBombRenderer extends EntityRenderer<PipeBomb> {
    private final ItemRenderer itemRenderer;

    public PipeBombRenderer(EntityRendererProvider.Context p_174114_) {
        super(p_174114_);
        this.itemRenderer = p_174114_.getItemRenderer();
    }

    public void render(PipeBomb grenade, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		
		this.itemRenderer.renderStatic(TFMGItems.PIPE_BOMB.get().getDefaultInstance(), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource,grenade.level(), grenade.getId());
		
        poseStack.popPose();
        super.render(grenade, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    public ResourceLocation getTextureLocation(PipeBomb p_114654_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

}