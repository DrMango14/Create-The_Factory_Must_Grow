package com.drmangotea.tfmg.content.electricity.content.devices.polarizer;

import com.drmangotea.tfmg.base.spark.ElectricitySparkPacket;
import com.drmangotea.tfmg.recipes.PolarizingRecipe;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class PolarizerCommons {

    public static boolean canBePolarized(Level level, ItemStack item) {
        return getRecipe(level, item).isPresent();
    }

    public static Optional<RecipeHolder<PolarizingRecipe>> getRecipe(Level level, ItemStack item) {
        if (level == null)
            return Optional.empty();
        Optional<RecipeHolder<PolarizingRecipe>> recipe = SequencedAssemblyRecipe.getRecipe(level, item, TFMGRecipeTypes.POLARIZING.getType(), PolarizingRecipe.class);
        if (recipe.isPresent()) {
            return recipe;
        } else {
            return TFMGRecipeTypes.POLARIZING.find(new SingleRecipeInput(item), level);
        }
    }

    public static ItemStack assembleResult(Level level, Vec3 pos, PolarizingRecipe recipe) {
        if (level == null) return ItemStack.EMPTY;
        ItemStack result = recipe.getRollableResults().getFirst().rollOutput(level.random);
        ElectricitySparkPacket.send(level, pos);
        return result;
    }
}
