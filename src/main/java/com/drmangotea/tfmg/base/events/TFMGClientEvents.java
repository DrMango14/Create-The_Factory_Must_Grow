package com.drmangotea.tfmg.base.events;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.measurement.MultimeterOverlayRenderer;
import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;


@EventBusSubscriber(Dist.CLIENT)
public class TFMGClientEvents {


	@SubscribeEvent
	public static void onTickPre(ClientTickEvent.Pre event) {
		onTick( true);
	}

	@SubscribeEvent
	public static void onTickPost(ClientTickEvent.Post event) {
		onTick(false);
	}


	public static void onTick(boolean isPreEvent) {
		if (!isGameActive())
			return;


		if (isPreEvent) {
			EngineControllerClientHandler.tick();

		}
	}
	@SubscribeEvent
	public static void PlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		Player player = event.getEntity();

		if (player != null)
			player.getPersistentData().remove("IsUsingEngineController");
	}

	//@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.HOTBAR, TFMG.asResource("multimeter_info"), MultimeterOverlayRenderer.OVERLAY);

	}
	protected static boolean isGameActive() {
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
	}
}
