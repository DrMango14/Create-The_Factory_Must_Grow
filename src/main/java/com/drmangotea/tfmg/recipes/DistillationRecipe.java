package com.drmangotea.tfmg.recipes;


import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class DistillationRecipe extends ProcessingRecipe<SmartInventory> {

    public DistillationRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.DISTILLATION, params);
    }

    public FluidIngredient getInputFluid(){
        return getFluidIngredients().get(0);
    }

    @Override
    public int getMaxFluidOutputCount() {
        return 6;
    }

    @Override
    public int getMaxInputCount() {
        return 0;
    }

    @Override
    public int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    public int getMaxOutputCount() {
        return 0;
    }

    public FluidStack getFirstFluidResult(){
        return fluidResults.get(0);
    }

    public int getOutputCount(DistillationRecipe recipe){
        return recipe.fluidResults.toArray().length;
    }

    public boolean matches(FluidTank inv,int outputs) {

        int neededOutputs = fluidIngredients.toArray().length;

        if(outputs !=neededOutputs)
            return false;

        if (inv.getFluidInTank(0).getAmount()==0)
            return false;
        return fluidIngredients.get(0)
                .test(inv.getFluidInTank(0));
    }

    @Override
    public boolean matches(SmartInventory pContainer, Level pLevel) {
        return false;
    }
}
