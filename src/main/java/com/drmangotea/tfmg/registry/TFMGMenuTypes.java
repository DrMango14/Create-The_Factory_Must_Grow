package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerMenu;
import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerScreen;
import com.simibubi.create.Create;
import com.tterrag.registrate.builders.MenuBuilder.ForgeMenuFactory;
import com.tterrag.registrate.builders.MenuBuilder.ScreenFactory;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class TFMGMenuTypes {

	public static final MenuEntry<EngineControllerMenu> ENGINE_CONTROLLER =
		init("engine_controller", EngineControllerMenu::new, () -> EngineControllerScreen::new);

	
	private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> init(
		String name, ForgeMenuFactory<C> factory, NonNullSupplier<ScreenFactory<C, S>> screenFactory) {
		return TFMG.REGISTRATE
			.menu(name, factory, screenFactory)
			.register();
	}

	public static void init() {}

}
