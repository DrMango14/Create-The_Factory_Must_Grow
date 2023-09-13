package com.drmangotea.tfmg.recipes.distillation;

import com.drmangotea.tfmg.CreateTFMG;
import com.drmangotea.tfmg.content.machines.oil_processing.distillation.DistillationControllerBlockEntity;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ItemlessRecipe extends ProcessingRecipe<SmartInventory> {

    public static boolean match(DistillationControllerBlockEntity controller, Recipe<?> recipe) {




        if(recipe instanceof ItemlessRecipe) {



            return apply(controller, recipe, true);
        }


        return false;

    }

    public static boolean apply(DistillationControllerBlockEntity basin, Recipe<?> recipe) {
        return apply(basin, recipe, false);
    }

    private static boolean apply(DistillationControllerBlockEntity controller, Recipe<?> recipe, boolean test) {
        boolean isItemlessRecipe = recipe instanceof ItemlessRecipe;
        IItemHandler availableItems = controller.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                .orElse(null);
        IFluidHandler availableFluids = controller.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                .orElse(null);

        if (availableItems == null || availableFluids == null)
            return false;

        BlazeBurnerBlock.HeatLevel heat = DistillationControllerBlockEntity.getHeatLevelOf(controller.getLevel()
                .getBlockState(controller.getBlockPos()
                        .below(1)));
        if (isItemlessRecipe && !((ItemlessRecipe) recipe).getRequiredHeat()
                .testBlazeBurner(heat))
            return false;

        List<ItemStack> recipeOutputItems = new ArrayList<>();
        List<FluidStack> recipeOutputFluids = new ArrayList<>();

        List<Ingredient> ingredients = new LinkedList<>(recipe.getIngredients());
        List<FluidIngredient> fluidIngredients =
                isItemlessRecipe ? ((ItemlessRecipe) recipe).getFluidIngredients() : Collections.emptyList();
        if(!fluidIngredients.isEmpty())




        for (boolean simulate : Iterate.trueAndFalse) {

            if (!simulate && test)
                return true;

            int[] extractedItemsFromSlot = new int[availableItems.getSlots()];
            int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];

            Ingredients: for (int i = 0; i < ingredients.size(); i++) {
                Ingredient ingredient = ingredients.get(i);

                for (int slot = 0; slot < availableItems.getSlots(); slot++) {
                    if (simulate && availableItems.getStackInSlot(slot)
                            .getCount() <= extractedItemsFromSlot[slot])
                        continue;
                    ItemStack extracted = availableItems.extractItem(slot, 1, true);
                    if (!ingredient.test(extracted))
                        continue;
                    if (!simulate)
                        availableItems.extractItem(slot, 1, false);
                    extractedItemsFromSlot[slot]++;
                    continue Ingredients;
                }

                // something wasn't found
                return false;
            }

            boolean fluidsAffected = false;
            FluidIngredients: for (int i = 0; i < fluidIngredients.size(); i++) {
                FluidIngredient fluidIngredient = fluidIngredients.get(i);
                int amountRequired = fluidIngredient.getRequiredAmount();


                for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
                    FluidStack fluidStack = availableFluids.getFluidInTank(tank);
                    if (simulate && fluidStack.getAmount() <= extractedFluidsFromTank[tank])
                        continue;
                    if (!fluidIngredient.test(fluidStack))
                        continue;
                    int drainedAmount = Math.min(amountRequired, fluidStack.getAmount());
                    if (!simulate) {
                        fluidStack.shrink(drainedAmount);
                        fluidsAffected = true;
                    }
                    amountRequired -= drainedAmount;
                    if (amountRequired != 0)
                        continue;
                    extractedFluidsFromTank[tank] += drainedAmount;
                    continue FluidIngredients;
                }

                // something wasn't found
                return false;
            }

            if (fluidsAffected) {
                controller.getBehaviour(SmartFluidTankBehaviour.INPUT)
                        .forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
                controller.getBehaviour(SmartFluidTankBehaviour.OUTPUT)
                        .forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
            }

            if (simulate) {
                if (recipe instanceof ItemlessRecipe ItemlessRecipe) {
                    recipeOutputItems.addAll(ItemlessRecipe.rollResults());
                    recipeOutputFluids.addAll(ItemlessRecipe.getFluidResults());

                } else {
                    recipeOutputItems.add(recipe.getResultItem());
/*
                    if (recipe instanceof CraftingRecipe craftingRecipe) {
                        recipeOutputItems.addAll(craftingRecipe.getRemainingItems(new DummyCraftingContainer(availableItems, extractedItemsFromSlot)));
                    }

 */
                }
            }

            if (!controller.acceptOutputs(recipeOutputItems, recipeOutputFluids, simulate))
                return false;
        }

        return true;
    }



    protected ItemlessRecipe(IRecipeTypeInfo type, ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(type, params);
    }


    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxOutputCount() {
        return 2;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 3;
    }

    @Override
    protected boolean canRequireHeat() {
        return true;
    }

    @Override
    public boolean matches(SmartInventory inv, @Nonnull Level worldIn) {
        return false;
    }

}
