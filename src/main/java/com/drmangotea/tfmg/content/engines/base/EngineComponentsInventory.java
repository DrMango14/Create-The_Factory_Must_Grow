package com.drmangotea.tfmg.content.engines.base;

import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class EngineComponentsInventory extends SmartInventory {

    public final List<Ingredient> components;
    public EngineComponentsInventory(SyncedBlockEntity be, List<Ingredient> components) {
        super(components.size(), be, 1, false);
        this.components = components;
    }
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return inv.insertItem(slot, stack, simulate);
    }
    public boolean insertItem(ItemStack stack){
        for(int i = 0; i<components.size();i++){
            Ingredient neededComponent = components.get(i);
            if(neededComponent.test(stack)&&getStackInSlot(i).isEmpty()){
                insertItem(i, new ItemStack(stack.getItem(),2), false);
                return true;
            }

        }
        return false;
    }

    //@Override
    //public ItemStack extractItem(int slot, int amount, boolean simulate) {
    //    if (!extractionAllowed)
    //        return ItemStack.EMPTY;
    //    if (stackNonStackables) {
    //        ItemStack extractItem = inv.extractItem(slot, amount, true);
    //        if (!extractItem.isEmpty() && extractItem.getMaxStackSize() < extractItem.getCount())
    //            amount = extractItem.getMaxStackSize();
    //    }
    //    return inv.extractItem(slot, amount, simulate);
    //}
}
