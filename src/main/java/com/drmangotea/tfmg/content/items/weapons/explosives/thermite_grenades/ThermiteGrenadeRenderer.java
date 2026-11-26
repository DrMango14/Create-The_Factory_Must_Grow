package com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades;


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
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


import static com.drmangotea.tfmg.content.items.weapons.explosives.thermite_grenades.ThermiteGrenade.ChemicalColor;

@OnlyIn(Dist.CLIENT)
@NothingNullByDefault
public class ThermiteGrenadeRenderer extends EntityRenderer<ThermiteGrenade> {
    private final ItemRenderer itemRenderer;
    private final ChemicalColor chemicalColor;
    public static ThermiteGrenadeRenderer regular(EntityRendererProvider.Context context) {
        return new ThermiteGrenadeRenderer(context, ChemicalColor.BASE);
    }
    public static ThermiteGrenadeRenderer green(EntityRendererProvider.Context context) {
        return new ThermiteGrenadeRenderer(context, ChemicalColor.GREEN);
    }
    public static ThermiteGrenadeRenderer blue(EntityRendererProvider.Context context) {
        return new ThermiteGrenadeRenderer(context, ChemicalColor.BLUE);
    }
    public ThermiteGrenadeRenderer(EntityRendererProvider.Context context, ChemicalColor color) {
        super(context);
        this.chemicalColor = color;
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(ThermiteGrenade grenade, float entityYaw, float partialTick, PoseStack pose, MultiBufferSource bufferSource, int light) {
        pose.pushPose();
        pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
        pose.mulPose(Axis.YP.rotationDegrees(180.0F));

		ItemStack stack = switch (chemicalColor) {
			case GREEN -> TFMGItems.ZINC_GRENADE.get().getDefaultInstance();
			case BLUE -> TFMGItems.COPPER_GRENADE.get().getDefaultInstance();
			case BASE -> TFMGItems.THERMITE_GRENADE.get().getDefaultInstance();
		};
		
		itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, pose, bufferSource, grenade.level(), grenade.getId());

        pose.popPose();
        super.render(grenade, entityYaw, partialTick, pose, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(ThermiteGrenade p_114654_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}