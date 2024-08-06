package com.drmangotea.createindustry.recipes.distillation;



import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;

public class DistillationRecipe extends ProcessingRecipe<SmartInventory> {





    public DistillationRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.DISTILLATION, params);
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









    public int getOutputCount(DistillationRecipe recipe){
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
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }



    public boolean matches(FluidTank inv, int outputs) {

        int neededOutputs = fluidIngredients.toArray().length;

        if(outputs !=neededOutputs)
            return false;

        if (inv.getFluidAmount()==0)
            return false;
        return fluidIngredients.get(0)
                .test(inv.getFluid());
    }

    @Override
    public boolean matches(SmartInventory pContainer, Level pLevel) {
        return false;
    }
}
