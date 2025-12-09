package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.recipes.jei.PolarizingCategory;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PolarizingRecipe extends ProcessingRecipe<RecipeWrapper> implements IAssemblyRecipe {
    public PolarizingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.POLARIZING, params);
    }
    
    @Override
    protected int getMaxInputCount() {
        return 1;
    }
    
    @Override
    protected int getMaxOutputCount() {
        return 1;
    }
    
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        return !inv.isEmpty() && this.ingredients.get(0).test(inv.getItem(0));
    }
    
    @Override
    public Component getDescriptionForAssembly() {
        return CreateLang.translateDirect("recipe.assembly.polarizing");
    }
    
    @Override
    public void addRequiredMachines(Set<ItemLike> set) {

        set.add(TFMGBlocks.POLARIZER.get());
    }
    
    @Override
    public void addAssemblyIngredients(List<Ingredient> list) {
    
    }
    
    @Override
    public Supplier<Supplier<SequencedAssemblySubCategory>> getJEISubCategory() {
        return () -> PolarizingCategory.AssemblyPolarizing::new;
    }


}