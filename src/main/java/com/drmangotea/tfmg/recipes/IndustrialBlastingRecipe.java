package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.recipes.input.IndustrialBlastingRecipeInput;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class IndustrialBlastingRecipe extends ProcessingRecipe<IndustrialBlastingRecipeInput, IndustrialBlastingRecipeParams> {

    public int hotAirUsage;

    public IndustrialBlastingRecipe(IndustrialBlastingRecipeParams params) {
        super(TFMGRecipeTypes.INDUSTRIAL_BLASTING, params);
        hotAirUsage = params.hotAirUsage;
    }

    @Override
    protected int getMaxInputCount() {
        return 2;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }
    @Override
    protected int getMaxFluidOutputCount() {
        return 3;
    }

    public FluidStack getPrimaryResult(){
        return getFluidResults().get(0);
    }
    public FluidStack getSecondaryResult(){
        return getFluidResults().get(1);
    }
    public FluidStack getGasByproduct(){
        if(getFluidResults().size() == 3) {
            return getFluidResults().get(2);
        }else return FluidStack.EMPTY;
    }


    @FunctionalInterface
    public interface Factory<R extends IndustrialBlastingRecipe> extends ProcessingRecipe.Factory<IndustrialBlastingRecipeParams, R> {
        R create(IndustrialBlastingRecipeParams params);
    }
    public static class Builder<R extends IndustrialBlastingRecipe> extends ProcessingRecipeBuilder<IndustrialBlastingRecipeParams, R, IndustrialBlastingRecipe.Builder<R>> {
        public Builder(IndustrialBlastingRecipe.Factory<R> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        @Override
        protected IndustrialBlastingRecipeParams createParams() {
            return new IndustrialBlastingRecipeParams();
        }

        @Override
        public IndustrialBlastingRecipe.Builder<R> self() {
            return this;
        }

        public IndustrialBlastingRecipe.Builder<R> hotAirUsage(int value) {
            params.hotAirUsage = value;
            return this;
        }
   
    }


    @Override
    public boolean matches(IndustrialBlastingRecipeInput inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        if (!ingredients.getFirst().test(inv.input))
            return false;
        return ingredients.size() < 2 || ingredients.get(1).test(inv.flux);
    }

    public static class Serializer<R extends IndustrialBlastingRecipe> implements RecipeSerializer<R> {
        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(ProcessingRecipe.Factory<IndustrialBlastingRecipeParams, R> factory) {
            this.codec = ProcessingRecipe.codec(factory, IndustrialBlastingRecipeParams.CODEC);
            this.streamCodec = ProcessingRecipe.streamCodec(factory, IndustrialBlastingRecipeParams.STREAM_CODEC);
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
