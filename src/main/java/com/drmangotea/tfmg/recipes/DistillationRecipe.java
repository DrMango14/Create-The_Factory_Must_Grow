package com.drmangotea.tfmg.recipes;


import com.drmangotea.tfmg.recipes.input.DistillationRecipeInput;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;


public class DistillationRecipe extends StandardProcessingRecipe<DistillationRecipeInput> {

    public DistillationRecipe(ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.DISTILLATION, params);
    }
    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    protected boolean canRequireHeat() {
        return true;
    }

    public SizedFluidIngredient getInputFluid(){
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
        return fluidResults.getFirst();
    }

    public int getOutputCount(DistillationRecipe recipe){
        return recipe.fluidResults.toArray().length;
    }

    public boolean matches(FluidTank inv, int outputs) {
        int neededOutputs = fluidIngredients.toArray().length;
        if(outputs != neededOutputs)
            return false;
        if (inv.getFluidInTank(0).getAmount()==0)
            return false;
        return fluidIngredients.getFirst().test(inv.getFluidInTank(0));
    }

    @Override
    public boolean matches(DistillationRecipeInput input, Level pLevel) {
        int neededOutputs = fluidResults.toArray().length;
        if(input.outputs != neededOutputs)
            return false;
        if (input.fluid.getAmount() == 0)
            return false;
        return fluidIngredients.getFirst().test(input.fluid);
    }
}
