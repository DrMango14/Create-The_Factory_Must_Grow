package com.drmangotea.tfmg.recipes.distillation;



import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class AdvancedDistillationRecipe extends ItemlessRecipe {

    public AdvancedDistillationRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.ADVANCED_DISTILLATION, params);
    }

    public FluidIngredient getInputFluid(){
        return getFluidIngredients().get(0);
    }

    public FluidStack getFirstFluidResult(){
        return fluidResults.get(0);
    }
    public FluidStack getSecondFluidResult(){
        return fluidResults.get(1);
    }
    public FluidStack getThirdFluidResult(){
        return fluidResults.get(2);
    }
    public FluidStack getFourthFluidResult(){
        return fluidResults.get(3);
    }
    public FluidStack getFifthFluidResult(){
        return fluidResults.get(4);
    }
    public FluidStack getSixthFluidResult(){
        return fluidResults.get(5);
    }


    public int getOutputCount(AdvancedDistillationRecipe recipe){
        return recipe.fluidResults.toArray().length;
    }
    public NonNullList<FluidStack> getResults(){
        return fluidResults;
    }


    @Override
    protected int getMaxFluidOutputCount() {
        return 6;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }
}
