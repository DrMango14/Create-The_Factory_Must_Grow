package com.drmangotea.createindustry.recipes.distillation;

import com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.distillation_tower.DistillationControllerBlockEntity;
import com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.distillery.DistilleryControllerBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@SuppressWarnings("removal")
public class AbstractDistillationRecipe extends ProcessingRecipe<SmartInventory> {

    public static boolean match(DistilleryControllerBlockEntity controller, Recipe<?> recipe) {




        if(recipe instanceof AbstractDistillationRecipe) {
            return apply(controller, recipe, true);
        }


        return false;

    }

    public static boolean apply(DistilleryControllerBlockEntity controller, Recipe<?> recipe) {
        return apply(controller, recipe, false);
    }

    private static boolean apply(DistilleryControllerBlockEntity controller, Recipe<?> recipe, boolean test) {
        boolean isItemlessRecipe = recipe instanceof AbstractDistillationRecipe;
        IItemHandler availableItems = controller.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .orElse(null);
        IFluidHandler availableFluids = controller.getCapability(ForgeCapabilities.FLUID_HANDLER)
                .orElse(null);

        if (availableItems == null || availableFluids == null)
            return false;

        BlazeBurnerBlock.HeatLevel heat = DistilleryControllerBlockEntity.getHeatLevelOf(controller.getLevel()
                .getBlockState(controller.getBlockPos()
                        .below(1)));
        if (isItemlessRecipe && !((AbstractDistillationRecipe) recipe).getRequiredHeat()
                .testBlazeBurner(heat))
            return false;

        List<ItemStack> recipeOutputItems = new ArrayList<>();
        List<FluidStack> recipeOutputFluids = new ArrayList<>();


        List<FluidIngredient> fluidIngredients =
                isItemlessRecipe ? ((AbstractDistillationRecipe) recipe).getFluidIngredients() : Collections.emptyList();
        if(!fluidIngredients.isEmpty())




        for (boolean simulate : Iterate.trueAndFalse) {

            if (!simulate && test)
                return true;

            int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];


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
                if (recipe instanceof AbstractDistillationRecipe ItemlessRecipe) {
                    recipeOutputItems.addAll(ItemlessRecipe.rollResults());
                    recipeOutputFluids.addAll(ItemlessRecipe.getFluidResults());

                } else {
                    RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
                    recipeOutputItems.add(recipe.getResultItem(registryAccess));
                }
            }

        }

        return true;
    }
    public static boolean match2(DistillationControllerBlockEntity controller, Recipe<?> recipe) {


        if(recipe instanceof AbstractDistillationRecipe) {
            return apply(controller, recipe, true);
        }


        return false;

    }
    private static boolean apply2(DistillationControllerBlockEntity controller, Recipe<?> recipe, boolean test) {
        boolean isItemlessRecipe = recipe instanceof AbstractDistillationRecipe;
        IItemHandler availableItems = controller.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .orElse(null);
        IFluidHandler availableFluids = controller.getCapability(ForgeCapabilities.FLUID_HANDLER)
                .orElse(null);

        if (availableItems == null || availableFluids == null)
            return false;

        BlazeBurnerBlock.HeatLevel heat = DistilleryControllerBlockEntity.getHeatLevelOf(controller.getLevel()
                .getBlockState(controller.getBlockPos()
                        .below(1)));
        if (isItemlessRecipe && !((AbstractDistillationRecipe) recipe).getRequiredHeat()
                .testBlazeBurner(heat))
            return false;

        List<ItemStack> recipeOutputItems = new ArrayList<>();
        List<FluidStack> recipeOutputFluids = new ArrayList<>();


        List<FluidIngredient> fluidIngredients =
                isItemlessRecipe ? ((AbstractDistillationRecipe) recipe).getFluidIngredients() : Collections.emptyList();
        if(!fluidIngredients.isEmpty())




            for (boolean simulate : Iterate.trueAndFalse) {

                if (!simulate && test)
                    return true;

                int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];


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
                    if (recipe instanceof AbstractDistillationRecipe ItemlessRecipe) {
                        recipeOutputItems.addAll(ItemlessRecipe.rollResults());
                        recipeOutputFluids.addAll(ItemlessRecipe.getFluidResults());

                    } else {
                        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
                        recipeOutputItems.add(recipe.getResultItem(registryAccess));
                    }
                }

            }

        return true;
    }

    protected AbstractDistillationRecipe(IRecipeTypeInfo type, ProcessingRecipeBuilder.ProcessingRecipeParams params) {
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
