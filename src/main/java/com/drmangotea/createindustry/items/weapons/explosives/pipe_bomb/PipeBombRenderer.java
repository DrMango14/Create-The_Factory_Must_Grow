package com.drmangotea.createindustry.items.weapons.explosives.pipe_bomb;


import com.drmangotea.createindustry.registry.TFMGItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PipeBombRenderer extends EntityRenderer<PipeBomb> {
    private final ItemRenderer itemRenderer;


    public PipeBombRenderer(EntityRendererProvider.Context p_174114_) {
        super(p_174114_);
        this.itemRenderer = p_174114_.getItemRenderer();
    }


    public void render(PipeBomb grenade, float p_114657_, float p_114658_, PoseStack p_114659_, MultiBufferSource p_114660_, int p_114661_) {
        p_114659_.pushPose();
        p_114659_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        p_114659_.mulPose(Vector3f.YP.rotationDegrees(180.0F));


                this.itemRenderer.renderStatic(TFMGItems.PIPE_BOMB.get().getDefaultInstance(), ItemTransforms.TransformType.GROUND, p_114661_, OverlayTexture.NO_OVERLAY, p_114659_, p_114660_, grenade.getId());

        p_114659_.popPose();
        super.render(grenade, p_114657_, p_114658_, p_114659_, p_114660_, p_114661_);
    }

    public ResourceLocation getTextureLocation(PipeBomb p_114654_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

}