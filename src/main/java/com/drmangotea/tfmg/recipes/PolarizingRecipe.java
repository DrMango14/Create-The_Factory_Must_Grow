package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.integration.jei.category.PolarizingCategory;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PolarizingRecipe extends ProcessingRecipe<SingleRecipeInput, PolarizingRecipeParams> implements IAssemblyRecipe {
    public int energy;

    public PolarizingRecipe(PolarizingRecipeParams params) {
        super(TFMGRecipeTypes.POLARIZING, params);
        this.energy = params.energy;
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
    
    public boolean matches(SingleRecipeInput input, Level worldIn) {
        return !input.isEmpty() && this.ingredients.getFirst().test(input.getItem(0));
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

    @FunctionalInterface
    public interface Factory<R extends PolarizingRecipe> extends ProcessingRecipe.Factory<PolarizingRecipeParams, R> {
        R create(PolarizingRecipeParams params);
    }

    public static class Builder<R extends PolarizingRecipe> extends ProcessingRecipeBuilder<PolarizingRecipeParams, R, PolarizingRecipe.Builder<R>> {
        public Builder(PolarizingRecipe.Factory<R> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        @Override
        protected PolarizingRecipeParams createParams() {
            return new PolarizingRecipeParams();
        }

        @Override
        public Builder<R> self() {
            return this;
        }

        public PolarizingRecipe.Builder<R> energy(int energy) {
            if (energy < 0) {
                throw new IllegalArgumentException("Energy can not be less than 0!");
            }
            params.energy = energy;
            return this;
        }
    }

    public static class Serializer<R extends PolarizingRecipe> implements RecipeSerializer<R> {
        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(ProcessingRecipe.Factory<PolarizingRecipeParams, R> factory) {
            this.codec = ProcessingRecipe.codec(factory, PolarizingRecipeParams.CODEC);
            this.streamCodec = ProcessingRecipe.streamCodec(factory, PolarizingRecipeParams.STREAM_CODEC);
        }

        @Override
        public MapCodec<R> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
            return streamCodec;
        }
    }
}
