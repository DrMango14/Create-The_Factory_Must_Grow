package com.drmangotea.tfmg.base.capabilities.pressure;

import net.minecraft.world.item.ItemStack;

public interface IPressureHandlerItem extends IPressureHandler {
    ItemStack getContainer();
}
