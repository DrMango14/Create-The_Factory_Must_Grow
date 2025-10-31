package com.drmangotea.tfmg.content.engines.engine_controller;

import com.drmangotea.tfmg.registry.TFMGMenuTypes;

import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class EngineControllerMenu extends GhostItemMenu<EngineControllerBlockEntity> {

	public EngineControllerMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
		super(type, id, inv, extraData);
	}

	public EngineControllerMenu(MenuType<?> type, int id, Inventory inv, EngineControllerBlockEntity be) {
		super(type, id, inv, be);
	}

	@Override
	protected EngineControllerBlockEntity createOnClient(RegistryFriendlyByteBuf extraData) {
		BlockPos readBlockPos = extraData.readBlockPos();
		ClientLevel world = Minecraft.getInstance().level;
		BlockEntity blockEntity = world.getBlockEntity(readBlockPos);
		if (blockEntity instanceof EngineControllerBlockEntity controller)
			return controller;
		return null;
	}


	public static EngineControllerMenu create(int id, Inventory inv, EngineControllerBlockEntity be) {
		return new EngineControllerMenu(TFMGMenuTypes.ENGINE_CONTROLLER.get(), id, inv, be);
	}


	@Override
	protected ItemStackHandler createGhostInventory() {
		return EngineControllerBlockEntity.getFrequencyItems(contentHolder);
	}

	@Override
	protected void addSlots() {
		addPlayerSlots(8-49, 131);

		int x = 12;
		int y = 34;
		int slot = 0;

		for (int column = 0; column < 3; column++) {
			for (int row = 0; row < 2; ++row)
				addSlot(new SlotItemHandler(ghostInventory, slot++, x, y + row * 18));
			x += 24;
		}
	}

	@Override
	protected void saveData(EngineControllerBlockEntity be) {
		//be.getOrCreateTag()
		//	.put("Items", ghostInventory.serializeNBT());
		be.frequencyItems = ghostInventory;

	}

	@Override
	protected boolean allowRepeats() {
		return true;
	}

	@Override
	public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
		if (slotId == playerInventory.selected && clickTypeIn != ClickType.THROW)
			return;
		super.clicked(slotId, dragType, clickTypeIn, player);
	}



}
