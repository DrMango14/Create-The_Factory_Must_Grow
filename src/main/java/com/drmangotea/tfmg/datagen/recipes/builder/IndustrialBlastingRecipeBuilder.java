package com.drmangotea.tfmg.datagen.recipes.builder;

import com.drmangotea.tfmg.recipes.IndustrialBlastingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class IndustrialBlastingRecipeBuilder extends ProcessingRecipeBuilder<IndustrialBlastingRecipe>{

    protected int hotAirUsage;

    public IndustrialBlastingRecipeBuilder(ProcessingRecipeFactory factory,int hotAirUsage, ResourceLocation recipeId) {
        super(factory, recipeId);
        this.hotAirUsage = hotAirUsage;
    }

    public IndustrialBlastingRecipe build() {
        IndustrialBlastingRecipe recipe = factory.create(params);
        recipe.hotAirUsage = hotAirUsage;
        return recipe;
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new DataGenResult<>(build(), recipeConditions));
    }




}
