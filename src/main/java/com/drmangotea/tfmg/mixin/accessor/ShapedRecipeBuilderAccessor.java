package com.drmangotea.tfmg.mixin.accessor;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(ShapedRecipeBuilder.class)
public interface ShapedRecipeBuilderAccessor {

    @Accessor
    String getGroup();

    @Accessor
    RecipeCategory getCategory();

    @Accessor
    ItemStack getResultStack();

    @Accessor
    List<String> getRows();

    @Accessor
    Map<Character, Ingredient> getKey();

    @Accessor
    Map<String, Criterion<?>> getCriteria();

    @Accessor("showNotification")
    boolean showNotification();
}
