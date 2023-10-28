package com.drmangotea.createindustry.recipes.distillation;



import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import mezz.jei.api.constants.RecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class DistillationRecipe extends ItemlessRecipe {

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

    public ItemStack getFirstItemResult(){
        return results.get(0).getStack();
    }
    public ItemStack getSecondItemResult(){
        return results.get(1).getStack();
    }
    public ItemStack getThirdItemResult(){
        return results.get(2).getStack();
    }
}
