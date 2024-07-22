package com.drmangotea.createindustry.recipes.gas_blasting;

import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class GasBlastingRecipe extends ProcessingRecipe<SmartInventory> {
    
    public GasBlastingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.GAS_BLASTING, params);
    }
    
    public FluidIngredient getInputFluid(){
        return getFluidIngredients().get(0);
    }
    public FluidIngredient getSecondInputFluid(){
        return getFluidIngredients().get(1);
    }
    
    public FluidStack getFirstFluidResult(){
        return fluidResults.get(0);
    }
    public FluidStack getSecondFluidResult(){
        return fluidResults.get(1);
    }
    
    public int getOutputCount(GasBlastingRecipe recipe){
        return recipe.fluidResults.toArray().length;
    }
    public NonNullList<FluidStack> getResults(){
        return fluidResults;
    }
    
    @Override
    protected int getMaxFluidOutputCount() {
        return 2;
    }
    
    @Override
    protected int getMaxInputCount() {
        return 0;
    }
    
    @Override
    protected int getMaxFluidInputCount() {
        return 2;
    }
    
    @Override
    protected int getMaxOutputCount() {
        return 0;
    }
    
    public boolean matches(FluidTank mainInv, FluidTank secondaryInv) {
        if (mainInv.isEmpty() || secondaryInv.isEmpty())
            return false;
        return fluidIngredients.get(0).test(mainInv.getFluid()) && fluidIngredients.get(1).test(secondaryInv.getFluid());
    }
    public boolean matches(CombinedTankWrapper inv, Level worldIn) {
        if (inv.getFluidInTank(0).getAmount()==0)
            return false;
        return fluidIngredients.get(0).test(inv.getFluidInTank(0));
    }
    
    @Override
    public boolean matches(SmartInventory pContainer, Level pLevel) {
        return false;
    }
    
}
