package com.drmangotea.createindustry.recipes.polarizing;

import com.drmangotea.createindustry.recipes.jei.PolarizingCategory;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.google.gson.JsonObject;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
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
    private int energy;
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
    
    public int getEnergy() {
        return this.energy;
    }
    
    @Override
    public Component getDescriptionForAssembly() {
        return Lang.translateDirect("recipe.assembly.polarizing");
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
    
    @Override
    public void readAdditional(JsonObject json) {
        super.readAdditional(json);
        this.energy = GsonHelper.getAsInt(json,"energy", 250);
    }
    @Override
    public void writeAdditional(JsonObject json) {
        super.writeAdditional(json);
        json.addProperty("energy", energy);
    }
    @Override
    public void readAdditional(FriendlyByteBuf buffer) {
        super.readAdditional(buffer);
        energy = buffer.readInt();
    }
    @Override
    public void writeAdditional(FriendlyByteBuf buffer) {
        super.writeAdditional(buffer);
        buffer.writeInt(energy);
    }
}
