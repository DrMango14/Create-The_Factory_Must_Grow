package com.drmangotea.tfmg.datagen.recipes.values.create;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.api.data.recipe.ItemApplicationRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.*;

public class TFMGItemApplicationRecipeGen extends ItemApplicationRecipeGen {

    GeneratedRecipe STEEL = casing("steel", () -> Ingredient.of(steelIngot()), TFMGBlocks.STEEL_CASING::get, TFMGBlocks.HARDENED_PLANKS::get);
    GeneratedRecipe HEAVY_CASING = casing("heavy_machinery", () -> Ingredient.of(steelSheet()), TFMGBlocks.HEAVY_MACHINERY_CASING::get, TFMGBlocks.STEEL_CASING::get);
    GeneratedRecipe ALUMINUM = casing("aluminum", () -> Ingredient.of(aluminumSheet()), TFMGBlocks.ALUMINUM_CASING::get, TFMGBlocks.STEEL_CASING::get);

    protected GeneratedRecipe casing(String type, Supplier<Ingredient> ingredient,
                                                        Supplier<ItemLike> output, Supplier<ItemLike> block) {
        return create(type + "_casing", b -> b
                .require(block.get())
                .require(ingredient.get())
                .output(output.get()));
    }

    public TFMGItemApplicationRecipeGen(PackOutput generator, CompletableFuture<HolderLookup.Provider> registries) {
        super(generator, registries, TFMG.MOD_ID);
    }

}