package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.recipes.input.HotBlastRecipeInput;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class HotBlastRecipe extends StandardProcessingRecipe<HotBlastRecipeInput> {

    public HotBlastRecipe(ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.HOT_BLAST, params);
    }
    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }
    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }
    @Override
    protected int getMaxFluidOutputCount() {
        return 2;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 2;
    }

    public FluidStack getPrimaryResult(){
        return getFluidResults().getFirst();
    }
    public FluidStack getSecondaryResult(){
        return getFluidResults().get(1);
    }

    public SizedFluidIngredient getPrimaryIngredient(){
        return getFluidIngredients().getFirst();
    }
    public SizedFluidIngredient getSecondaryIngredient(){
        return getFluidIngredients().get(1);
    }


    @Override
    public boolean matches(HotBlastRecipeInput input, Level worldIn) {
        if (input.isEmpty())
            return false;
        return getPrimaryIngredient().test(input.air) && getSecondaryIngredient().test(input.fuel);
    }
}
