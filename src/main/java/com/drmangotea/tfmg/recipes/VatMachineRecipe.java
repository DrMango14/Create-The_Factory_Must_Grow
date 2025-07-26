package com.drmangotea.tfmg.recipes;

import com.drmangotea.tfmg.datagen.recipes.values.tfmg.TFMGVatRecipeGen;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class VatMachineRecipe extends ProcessingRecipe<RecipeInput, VatRecipeParams> {

    public List<String> machines;
    public List<String> allowedVatTypes;
    public int minSize;
    public int heatLevel=0;

    public VatMachineRecipe(VatRecipeParams params) {
        super(TFMGRecipeTypes.VAT_MACHINE_RECIPE, params);
        machines = params.machines;
        allowedVatTypes = params.allowedVatTypes;
        minSize = params.min_size;
        heatLevel = params.heat_level;
    }

    @Override
    protected int getMaxInputCount() {
        return 4;
    }
    @Override
    protected int getMaxOutputCount() {
        return 4;
    }
    @Override
    protected int getMaxFluidInputCount() {
        return 4;
    }
    @Override
    protected int getMaxFluidOutputCount() {
        return 4;
    }


    @Override
    public boolean matches(RecipeInput inv, Level worldIn) {
        return false;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }
    @Override
    protected boolean canRequireHeat() {
        return true;
    }
    @FunctionalInterface
    public interface Factory<R extends VatMachineRecipe> extends ProcessingRecipe.Factory<VatRecipeParams, R> {
        R create(VatRecipeParams params);
    }
    public static class Builder<R extends VatMachineRecipe> extends ProcessingRecipeBuilder<VatRecipeParams, R, VatMachineRecipe.Builder<R>> {
        public Builder(VatMachineRecipe.Factory<R> factory, ResourceLocation recipeId) {
            super(factory, recipeId);
        }

        @Override
        protected VatRecipeParams createParams() {
            return new VatRecipeParams();
        }

        @Override
        public VatMachineRecipe.Builder<R> self() {
            return this;
        }

        public VatMachineRecipe.Builder<R> values(TFMGVatRecipeGen.VatRecipeValues value) {
            params.machines = value.machines;
            params.allowedVatTypes = value.allowedVatTypes;

            params.min_size = value.minSize;
            return this;
        }


    }

    public static class Serializer<R extends VatMachineRecipe> implements RecipeSerializer<R> {
        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(ProcessingRecipe.Factory<VatRecipeParams, R> factory) {
            this.codec = ProcessingRecipe.codec(factory, VatRecipeParams.CODEC);
            this.streamCodec = ProcessingRecipe.streamCodec(factory, VatRecipeParams.STREAM_CODEC);
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
