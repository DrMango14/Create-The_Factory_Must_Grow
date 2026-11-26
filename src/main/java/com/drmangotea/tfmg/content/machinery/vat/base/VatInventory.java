package com.drmangotea.tfmg.content.machinery.vat.base;

import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.world.item.ItemStack;


public class VatInventory extends SmartInventory {

    private final VatBlockEntity owner;

    public VatInventory(int slots, VatBlockEntity be) {
        super(slots, be, 64, true);
        this.owner = be;
    }


    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        int firstFreeSlot = -1;

        for (int i = 0; i < getSlots(); i++) {
            // Only insert if no other slot already has a stack of this item
            if (i != slot && ItemStack.isSameItemSameComponents(stack, inv.getStackInSlot(i)))
                return stack;
            if (inv.getStackInSlot(i)
                    .isEmpty() && firstFreeSlot == -1)
                firstFreeSlot = i;
        }

        // Only insert if this is the first empty slot, prevents overfilling in the
        // simulation pass
        if (inv.getStackInSlot(slot)
                .isEmpty() && firstFreeSlot != slot)
            return stack;

        ItemStack remainder = super.insertItem(slot, stack, simulate);
        if (!simulate && !ItemStack.matches(stack, remainder))
            owner.notifyItemContentsChanged();
        return remainder;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack extracted = super.extractItem(slot, amount, simulate);
        if (!simulate && !extracted.isEmpty())
            owner.notifyItemContentsChanged();
        return extracted;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        ItemStack previous = getStackInSlot(slot).copy();
        super.setStackInSlot(slot, stack);
        if (!ItemStack.matches(previous, stack))
            owner.notifyItemContentsChanged();
    }
}
