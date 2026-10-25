package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.accumulator_rack.AccumulatorRackMenu;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.accumulator_rack.AccumulatorRackScreen;
import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.filter.FilterScreen;
import com.tterrag.registrate.builders.MenuBuilder.ForgeMenuFactory;
import com.tterrag.registrate.builders.MenuBuilder.ScreenFactory;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class TFMGMenuTypes {

	//public static final MenuEntry<EngineControllerMenu> ENGINE_CONTROLLER =
	//	init("engine_controller", EngineControllerMenu::new, () -> EngineControllerScreen::new);

	public static final MenuEntry<AccumulatorRackMenu> ACCUMULATOR_RACK =
			register("accumulator_rack", AccumulatorRackMenu::new, () -> AccumulatorRackScreen::new);

	private static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> MenuEntry<C> register(
			String name, ForgeMenuFactory<C> factory, NonNullSupplier<ScreenFactory<C, S>> screenFactory) {
		return TFMG.REGISTRATE
				.menu(name, factory, screenFactory)
				.register();
	}

	public static void init() {}

}
