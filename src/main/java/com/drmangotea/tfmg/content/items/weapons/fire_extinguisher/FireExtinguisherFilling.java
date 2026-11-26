package com.drmangotea.tfmg.content.items.weapons.fire_extinguisher;

import com.drmangotea.tfmg.registry.TFMGDataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class FireExtinguisherFilling {
    private final FluidStack testingFluid;

    public FireExtinguisherFilling(FluidStack testingFluid) {
        this.testingFluid = testingFluid;
    }

    public FluidStack requiredAmount(ItemStack stack) {
        FireExtinguisherFuel currentFuel = stack.getOrDefault(TFMGDataComponents.FIRE_EXTINGUISHER, FireExtinguisherFuel.EMPTY);
        int space = FireExtinguisherItem.DRY_ICE_CAPACITY - currentFuel.amount();
        return space > 0 ? testingFluid.copyWithAmount(Math.min(testingFluid.getAmount(), space)) : FluidStack.EMPTY;
    }

    public boolean matches(ItemStack stack, Level level) {
        if (!stack.has(TFMGDataComponents.FIRE_EXTINGUISHER)) return false;
        FireExtinguisherFuel testingFuel = FireExtinguisherFuel.createForType(level.registryAccess(), testingFluid);
        FireExtinguisherFuel currentFuel = stack.getOrDefault(TFMGDataComponents.FIRE_EXTINGUISHER, FireExtinguisherFuel.EMPTY);
        if (testingFuel.isEmpty() || (!currentFuel.isEmpty() && (currentFuel.fuelType() != testingFuel.fuelType()))) return false;
        return currentFuel.amount() + testingFuel.amount() <= FireExtinguisherItem.DRY_ICE_CAPACITY;
    }
}
