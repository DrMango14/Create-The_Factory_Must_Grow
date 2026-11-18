package com.drmangotea.tfmg.base.events;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGClient;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.content.measuring_instruments.MultimeterOverlayRenderer;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.small.TransformerBlockEntity;
import com.drmangotea.tfmg.content.items.ScrewdriverItem;
import com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonItemRenderer;
import com.drmangotea.tfmg.content.items.weapons.quad_potato_cannon.QuadPotatoCannonItemRenderer;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatCategoryEvent;
import com.drmangotea.tfmg.registry.*;
import net.createmod.ponder.api.PonderPalette;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;

@SuppressWarnings("unused")
@EventBusSubscriber(Dist.CLIENT)
public class TFMGClientEvents {

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		Item.TooltipContext context = event.getContext();
		List<Component> tooltip = event.getToolTip();
		TooltipFlag flag = event.getFlags();
		for (var type : TFMGDataComponents.DATA_COMPONENTS.getEntries()) {
			var comp = stack.get(type);
			if (comp instanceof TooltipProvider tooltipProvider) {
				tooltipProvider.addToTooltip(context, tooltip::add, flag);
			}
		}
	}

	@SubscribeEvent
	public static void onTickPre(ClientTickEvent.Pre event) {
		onTick( true);
	}

	@SubscribeEvent
	public static void onTickPost(ClientTickEvent.Post event) {
		onTick(false);
	}

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        TFMGClient.ELECTRIC_NETWORK_RENDERER.tickRender(event);
    }

    public static void onTick(boolean isPreEvent) {
        if (!isGameActive())
            return;

		TFMGClient.QUAD_POTATO_CANNON_RENDER_HANDLER.tick();
		TFMGClient.ADVANCED_POTATO_CANNON_RENDER_HANDLER.tick();
		TFMGClient.FLAMETHROWER_RENDER_HANDLER.tick();

		TransformerBlockEntity.tickOutliner();
		ScrewdriverItem.clientTick();
    }


    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath("tfmg", "item/graphite_electrode_model")));
    }

    @SubscribeEvent
    public static void PlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ElectricalNetwork network = ElectricNetworkManager.getNetwork(event.getEntity().level());
        network.update();
    }

    @SubscribeEvent
    public static void PlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();

		if (player != null)
			player.getPersistentData().remove("IsUsingEngineController");
	}

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.HOTBAR, TFMG.asResource("multimeter_info"), MultimeterOverlayRenderer.OVERLAY);
	}

	protected static boolean isGameActive() {
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
	}

	@EventBusSubscriber(value = Dist.CLIENT)
	public static class ModBusEvents {
		@SubscribeEvent
		public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
			event.register(TFMGItems.QUAD_POTATO_CANNON, QuadPotatoCannonItemRenderer.DECORATOR);
			event.register(TFMGItems.ADVANCED_POTATO_CANNON, AdvancedPotatoCannonItemRenderer.DECORATOR);
		}

		@SubscribeEvent
		public static void vatOperations(VatCategoryEvent event) {
			event.addDrawableOperation(TFMGVatOperations.MIXER.get(), (recipe, graphics, mouseX, mouseY) -> {
				TFMGGuiTextures.VAT_MACHINE.render(graphics, 55 - 12, 0);
				TFMGGuiTextures.MIXER.render(graphics, 55 - 19, 32);
			});
			event.addOperationTooltip(TFMGVatOperations.MIXER.get(), (recipe, tooltip, mouseX, mouseY) -> {
				if (mouseY > -3 && mouseY < 60 && mouseX > 43 && mouseX < 67) {
					tooltip.accept(TFMGLang.translate("recipe.vat.mixing").component()
							.withColor(PonderPalette.INPUT.getColor()));
				}
			});
			event.addDrawableOperation(TFMGVatOperations.CENTRIFUGE.get(), (recipe, graphics, mouseX, mouseY) -> {
				TFMGGuiTextures.VAT_MACHINE.render(graphics, 55 - 12, 0);
				TFMGGuiTextures.CENTRIFUGE.render(graphics, 55 - 12, 32);
			});
			event.addOperationTooltip(TFMGVatOperations.CENTRIFUGE.get(), (recipe, tooltip, mouseX, mouseY) -> {
				if (mouseY > -3 && mouseY < 60 && mouseX > 43 && mouseX < 67) {
					tooltip.accept(TFMGLang.translate("recipe.vat.centrifuge").component()
							.withColor(PonderPalette.INPUT.getColor()));
				}
			});
			event.addDrawableOperation(TFMGVatOperations.ELECTRODE.get(), (recipe, graphics, mouseX, mouseY) -> {
				TFMGGuiTextures.VAT_MACHINE.render(graphics, 55 - 12 - 32, 0);
				TFMGGuiTextures.VAT_MACHINE.render(graphics, 55 - 12 + 32, 0);
				TFMGGuiTextures.ELECTRODE.render(graphics, 55 - 3 - 32, 32);
				TFMGGuiTextures.ELECTRODE.render(graphics, 55 - 3 + 32, 32);
			});
			event.addOperationTooltip(TFMGVatOperations.ELECTRODE.get(), (recipe, tooltip, mouseX, mouseY) -> {
				boolean xCheck = mouseX > 11 && mouseX < 35 || mouseX > 75 && mouseX < 99;
				if (mouseY > -3 && mouseY < 60 && xCheck) {
					tooltip.accept(TFMGLang.translate("recipe.vat.electrode").component()
							.withColor(PonderPalette.INPUT.getColor()));
				}
			});
			event.addDrawableOperation(TFMGVatOperations.GRAPHITE_ELECTRODE.get(), (recipe, graphics, mouseX, mouseY) -> {
				TFMGGuiTextures.VAT_MACHINE.render(graphics, 55 - 12 - 32, 0);
				TFMGGuiTextures.VAT_MACHINE.render(graphics, 55 - 12 + 32, 0);
				TFMGGuiTextures.VAT_MACHINE.render(graphics, 55 - 12, 0);
				TFMGGuiTextures.GRAPHITE_ELECTRODE.render(graphics, 55 - 4 - 32, 32);
				TFMGGuiTextures.GRAPHITE_ELECTRODE.render(graphics, 55 - 4 + 32, 32);
				TFMGGuiTextures.GRAPHITE_ELECTRODE.render(graphics, 55 - 4, 32);
			});
			event.addOperationTooltip(TFMGVatOperations.GRAPHITE_ELECTRODE.get(), (recipe, tooltip, mouseX, mouseY) -> {
				if (mouseY > -3 && mouseY < 60 && mouseX > 11 && mouseX < 99) {
					tooltip.accept(TFMGLang.translate("recipe.vat.graphite_electrode").component()
							.withColor(PonderPalette.INPUT.getColor()));
				}
			});

			event.addDrawableVatType(TFMGVatTypes.FIREPROOF.get(), (vatType, graphics, mouseX, mouseY) -> TFMGGuiTextures.FIREPROOF_BRICK_OVERLAY.render(graphics, 55 - 48, 32));
			event.addDrawableVatType(TFMGVatTypes.CAST_IRON.get(), (vatType, graphics, mouseX, mouseY) -> TFMGGuiTextures.CAST_IRON_VAT_OVERLAY.render(graphics, 0, 24));
			event.addDrawableVatType(TFMGVatTypes.STEEL.get(), (vatType, graphics, mouseX, mouseY) -> TFMGGuiTextures.STEEL_VAT_OVERLAY.render(graphics, 0, 24));
		}
	}
}
