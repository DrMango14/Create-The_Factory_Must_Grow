package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.recipes.jei.PolarizingCategory;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PolarizingRecipe extends StandardProcessingRecipe<RecipeInput> implements IAssemblyRecipe {
    public PolarizingRecipe(ProcessingRecipeParams params) {
        super(TFMGRecipeTypes.POLARIZING, params);
    }
    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }
    @Override
    protected int getMaxInputCount() {
        return 1;
    }
    
    @Override
    protected int getMaxOutputCount() {
        return 1;
    }
    
    public boolean matches(RecipeInput inv, Level worldIn) {
        return !inv.isEmpty() && ((Ingredient)this.ingredients.get(0)).test(inv.getItem(0));
    }
    
    @Override
    public Component getDescriptionForAssembly() {
        return TFMGLang.translateDirect("recipe.assembly.polarizing");
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