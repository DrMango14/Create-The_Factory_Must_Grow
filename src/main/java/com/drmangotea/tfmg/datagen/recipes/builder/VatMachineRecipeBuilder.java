package com.drmangotea.tfmg.datagen.recipes.builder;

import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VatMachineRecipeBuilder extends ProcessingRecipeBuilder<VatMachineRecipe>{

    protected VatRecipeParams vatParams;

    public VatMachineRecipeBuilder(ProcessingRecipeFactory factory, VatRecipeParams params, ResourceLocation recipeId) {
        super(factory, recipeId);
        this.vatParams = params;
    }

    public VatMachineRecipe build() {
        VatMachineRecipe recipe = factory.create(params);
        recipe.machines = vatParams.machines;
        recipe.allowedVatTypes = vatParams.allowedVatTypes;
        recipe.minSize = vatParams.minSize;
        return recipe;
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new DataGenResult<>(build(), recipeConditions));
    }

    public static class VatRecipeParams{

        public List<String> machines;
        public int minSize;
        public List<String> allowedVatTypes;

        public VatRecipeParams(){
            machines = new ArrayList<>();
            minSize = 1;
            allowedVatTypes = new ArrayList<>();
            allowedVatTypes.add("tfmg:steel_vat");
            allowedVatTypes.add("tfmg:cast_iron_vat");
            allowedVatTypes.add("tfmg:firebrick_lined_vat");
        }


    }


}
