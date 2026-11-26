package com.drmangotea.tfmg.mixin.accessor;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ShapelessRecipeBuilder.class)
public interface ShapelessRecipeBuilderAccessor {

    @Accessor
    String getGroup();

    @Accessor
    RecipeCategory getCategory();

    @Accessor
    ItemStack getResultStack();

    @Accessor
    NonNullList<Ingredient> getIngredients();

    @Accessor
    Map<String, Criterion<?>> getCriteria();
}
