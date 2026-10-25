package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.accumulator_rack;

import com.drmangotea.tfmg.registry.TFMGMenuTypes;
import com.simibubi.create.AllMenuTypes;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.animatedContainer.AnimatedContainerBehaviour;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AccumulatorRackMenu extends MenuBase<AccumulatorRackBlockEntity> {

	public AccumulatorRackMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
		super(type, id, inv, extraData);
	}

	public AccumulatorRackMenu(MenuType<?> type, int id, Inventory inv, AccumulatorRackBlockEntity be) {
		super(type, id, inv, be);
		BlockEntityBehaviour.get(be, AnimatedContainerBehaviour.TYPE)
			.startOpen(player);
	}

	public static AccumulatorRackMenu create(int id, Inventory inv, AccumulatorRackBlockEntity be) {
		return new AccumulatorRackMenu(TFMGMenuTypes.ACCUMULATOR_RACK.get(), id, inv, be);
	}

	@Override
	protected AccumulatorRackBlockEntity createOnClient(RegistryFriendlyByteBuf extraData) {
		BlockPos readBlockPos = extraData.readBlockPos();
		ClientLevel world = Minecraft.getInstance().level;
		BlockEntity blockEntity = world.getBlockEntity(readBlockPos);
		if (blockEntity instanceof AccumulatorRackBlockEntity ppbe)
			return ppbe;
		return null;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {

		Slot slot = this.slots.get(index);
		if (!slot.hasItem()) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getItem().copy();
		ItemStack moved = stack.copy();

		int size = contentHolder.inventory.getSlots();
		if (index < size) {
			// move into player inventory
			if (!this.moveItemStackTo(stack, size, this.slots.size(), true)) {
				return ItemStack.EMPTY;
			}
		} else {
			if (!this.moveItemStackTo(stack, 0, size, false)) {
				return ItemStack.EMPTY;
			}
		}

		if (stack.isEmpty()) {
			slot.setByPlayer(ItemStack.EMPTY);
		} else {
			slot.setByPlayer(stack.copy());
		}

		return moved;
	}

	@Override
	protected void initAndReadInventory(AccumulatorRackBlockEntity contentHolder) {}

	@Override
	protected void addSlots() {
		SmartInventory inventory = contentHolder.inventory;
		int x = 90;
		int y = 4;

		int i = 0;
		for (int row = 0; row < 2; row++)
			for (int col = 0; col < 2; col++) {
				addSlot(new SlotItemHandler(inventory, i, x + col * 18, y + row * 18));
				i++;
			}
		addPlayerSlots(38, 108);
	}

	@Override
	protected void saveData(AccumulatorRackBlockEntity contentHolder) {}

	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
		if (!playerIn.level().isClientSide)
			BlockEntityBehaviour.get(contentHolder, AnimatedContainerBehaviour.TYPE)
				.stopOpen(playerIn);
	}


}
