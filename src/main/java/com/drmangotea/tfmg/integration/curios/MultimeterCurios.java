package com.drmangotea.tfmg.integration.curios;

import com.drmangotea.tfmg.content.electricity.content.measuring_instruments.MultimeterItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.Mods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Optional;

public class MultimeterCurios {

    public static MultimeterItem getHeldByPlayer(Player player) {
		if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof MultimeterItem meter)
			return meter;
		if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof MultimeterItem meter)
			return meter;
        if (!Mods.CURIOS.isLoaded()) return null;
        return LoadedOnly.getHeldByPlayer(player);
    }

	public static void registerRenderer() {
		if (!Mods.CURIOS.isLoaded()) return;
		LoadedOnly.registerRenderer();
	}

    public static class LoadedOnly {

        public static MultimeterItem getHeldByPlayer (Player player) {
			Optional<ICuriosItemHandler> curios = CuriosApi.getCuriosInventory(player);
			if (curios.isEmpty()) return null;
			
			Optional<ICurioStacksHandler> maybeBelt = curios.get().getStacksHandler("belt");
			if (maybeBelt.isEmpty()) return null;
			
			IDynamicStackHandler belt = maybeBelt.get().getStacks();
			int slots = belt.getSlots();
			for (int i = 0; i < slots; i++) {
				ItemStack stack = belt.getStackInSlot(i);
				if (stack.getItem() instanceof MultimeterItem meter)
					return meter;
			}
			return null;
        }

		public static void registerRenderer() {
			BuiltInRegistries.ITEM.stream().filter(item -> item instanceof MultimeterItem).forEach(item -> CuriosRendererRegistry.register(item, Renderer::new));
		}
    }

	@OnlyIn(Dist.CLIENT)
	public static class Renderer implements ICurioRenderer {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		static final Quaternionf orientation = //rotate 180deg around Z, then 90deg around Y
			new Quaternionf(-0.5f * Mth.SQRT_OF_TWO, 0, 0.5f * Mth.SQRT_OF_TWO, 0);

        @Override
		public <T extends LivingEntity, M extends EntityModel<T>> void render(
			ItemStack itemStack, SlotContext slotContext, PoseStack poseStack,
			RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
			int light, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch
		) {
			if (!(renderLayerParent.getModel() instanceof HumanoidModel<? extends LivingEntity> humanoidModel))
				return;
			
			poseStack.pushPose();
			humanoidModel.body.translateAndRotate(poseStack);
			poseStack.scale(0.35f, 0.35f, 0.35f);
			float offset = !slotContext.entity().getItemBySlot(EquipmentSlot.LEGS).isEmpty() ?  0.1f : 0f;
			poseStack.translate(-0.75 - offset, 2.0, 0.0);
			poseStack.mulPose(orientation);

			itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, poseStack, renderTypeBuffer, null, 0);
			poseStack.popPose();
		}
	}
}
