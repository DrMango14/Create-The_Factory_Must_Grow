package com.drmangotea.tfmg.mixin.accessor;


import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ProcessingRecipe.class)
public interface ProcessingRecipeAccessor {

    @Accessor("ingredients")
    NonNullList<Ingredient> tfmg$ingredients();
    @Accessor("fluidIngredients")
    NonNullList<FluidIngredient> tfmg$fluidIngredients();
    @Accessor("results")
    NonNullList<ProcessingOutput> tfmg$results();
    @Accessor("fluidResults")
    NonNullList<FluidStack> tfmg$fluidResults();
    @Accessor("typeInfo")
    IRecipeTypeInfo tfmg$typeInfo();


}
