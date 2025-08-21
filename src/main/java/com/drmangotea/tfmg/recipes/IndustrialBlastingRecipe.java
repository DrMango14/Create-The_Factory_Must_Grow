package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class IndustrialBlastingRecipe extends ProcessingRecipe<RecipeWrapper> {

    public int hotAirUsage;

    public IndustrialBlastingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.INDUSTRIAL_BLASTING, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 2;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }
    @Override
    protected int getMaxFluidOutputCount() {
        return 3;
    }

    @Override
	protected boolean canSpecifyDuration() {
		return true;
	}

    public FluidStack getPrimaryResult(){
        return getFluidResults().get(0);
    }
    public FluidStack getSecondaryResult(){
        return getFluidResults().get(1);
    }
    public FluidStack getGasByproduct(){
        if(getFluidResults().size() == 3) {
            return getFluidResults().get(2);
        }else return FluidStack.EMPTY;
    }

    public void readAdditional(JsonObject json) {
        super.readAdditional(json);
        this.hotAirUsage = GsonHelper.getAsInt(json, "hotAirUsage", 0);
    }

    public void writeAdditional(JsonObject json) {
        super.writeAdditional(json);
        json.addProperty("hotAirUsage", this.hotAirUsage);
    }


    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0)
                .test(inv.getItem(0));
    }



}
