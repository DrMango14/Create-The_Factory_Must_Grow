package com.drmangotea.tfmg.base.events;

import com.drmangotea.tfmg.content.electricity.measurement.MultimeterOverlayRenderer;
import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;




@EventBusSubscriber(Dist.CLIENT)
public class TFMGClientEvents {

	@SubscribeEvent
	public static void onTick(ClientTickEvent event) {
		if (!isGameActive())
			return;


		if (event.phase == Phase.START) {
			EngineControllerClientHandler.tick();

		}
	}
	@SubscribeEvent
	public static void PlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		Player player = event.getEntity();

		if (player != null)
			player.getPersistentData().remove("IsUsingEngineController");
	}

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
		event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "multimeter_info", MultimeterOverlayRenderer.OVERLAY);

	}
	protected static boolean isGameActive() {
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
	}
}
