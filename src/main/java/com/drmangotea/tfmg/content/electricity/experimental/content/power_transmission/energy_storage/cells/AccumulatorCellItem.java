package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.cells;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.Create;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class AccumulatorCellItem extends Item {

    public final int capacity;

    public AccumulatorCellItem(Properties properties, int capacity) {
        super(properties);
        this.capacity = capacity;

    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        super.use(level, player, usedHand);
        ItemStack stack = player.getItemInHand(usedHand);

        TFMGItemEnergyStorage storage = (TFMGItemEnergyStorage) stack.getCapability(Capabilities.EnergyStorage.ITEM);

        int numero = player.isShiftKeyDown() ? storage.extractEnergy(500,false) :  storage.receiveEnergy(500,false);

        TFMG.LOGGER.debug("Storage " + stack.getOrDefault(TFMGDataComponents.ACCUMULATOR_STORAGE,0)+" "+numero+" "+storage.getMaxEnergyStored());

        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x6DC4C4;
    }

    @Override
    public int getBarWidth(ItemStack stack) {

        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);

        return (int) (((float) stack.getOrDefault(TFMGDataComponents.ACCUMULATOR_STORAGE,0) / (float) storage.getMaxEnergyStored()) * 13f);
    }


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

        event.registerItem(
                Capabilities.EnergyStorage.ITEM, (stack, ctx) -> new TFMGItemEnergyStorage(((AccumulatorCellItem) stack.getItem()).capacity, 50000,stack) {
                    @Override
                    public void onEnergyChanged(int amount, int oldAmount) {

                    }
                },
                TFMGItems.LEAD_ACID_BATTERY,
                TFMGItems.LITHIUM_CELL,
                TFMGItems.SINGLE_USE_BATTERY,
                TFMGItems.SUPERCAPACITOR

        );

    }
}
