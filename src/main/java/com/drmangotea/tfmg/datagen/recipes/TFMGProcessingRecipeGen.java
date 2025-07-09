package com.drmangotea.tfmg.datagen.recipes;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.datagen.recipes.values.create.*;
import com.drmangotea.tfmg.datagen.recipes.values.tfmg.*;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class TFMGProcessingRecipeGen extends TFMGRecipeProvider {

	protected static final List<TFMGProcessingRecipeGen> GENERATORS = new ArrayList<>();


	public static void registerAll(DataGenerator gen, PackOutput output) {

		GENERATORS.add(new CokingRecipeGen(output));
		GENERATORS.add(new DistillationRecipeGen(output));
		GENERATORS.add(new WindingRecipeGen(output));
		GENERATORS.add(new PolarizingRecipeGen(output));
		GENERATORS.add(new HotBlastRecipeGen(output));
		GENERATORS.add(new TFMGItemApplicationRecipeGen(output));
		GENERATORS.add(new TFMGFillingRecipeGen(output));
		GENERATORS.add(new TFMGMixingRecipeGen(output));
		GENERATORS.add(new TFMGCompactingRecipeGen(output));
		GENERATORS.add(new TFMGPressingRecipeGen(output));
		GENERATORS.add(new TFMGCrushingRecipeGen(output));
		GENERATORS.add(new TFMGDeployingRecipeGen(output));


		gen.addProvider(true, new DataProvider() {

			@Override
			public String getName() {
				return "TFMG's Processing Recipes";
			}

			@Override
			public CompletableFuture<?> run(CachedOutput dc) {
				return CompletableFuture.allOf(GENERATORS.stream()
					.map(gen -> gen.run(dc))
					.toArray(CompletableFuture[]::new));
			}
		});
	}

	public TFMGProcessingRecipeGen(PackOutput generator) {
		super(generator);
	}



	public  <T extends ProcessingRecipe<?>> GeneratedRecipe create(String namespace,
		Supplier<ItemLike> singleIngredient, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
		ProcessingRecipeSerializer<T> serializer = getSerializer();
		GeneratedRecipe generatedRecipe = c -> {
			ItemLike itemLike = singleIngredient.get();
			transform
				.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(),
					new ResourceLocation(namespace, CatnipServices.REGISTRIES.getKeyOrThrow(itemLike.asItem())
						.getPath())).withItemIngredients(Ingredient.of(itemLike)))
				.build(c);
		};
		all.add(generatedRecipe);
		return generatedRecipe;
	}



	public  <T extends ProcessingRecipe<?>> GeneratedRecipe create(Supplier<ItemLike> singleIngredient,
		UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
		return create(TFMG.MOD_ID, singleIngredient, transform);
	}

	protected <T extends ProcessingRecipe<?>> GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name,
		UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
		ProcessingRecipeSerializer<T> serializer = getSerializer();
		GeneratedRecipe generatedRecipe =
			c -> transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), name.get()))
				.build(c);
		all.add(generatedRecipe);
		return generatedRecipe;
	}


	public  <T extends ProcessingRecipe<?>> GeneratedRecipe create(ResourceLocation name,
		UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
		return createWithDeferredId(() -> name, transform);
	}


	public  <T extends ProcessingRecipe<?>> GeneratedRecipe create(String name,
		UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
		return create(TFMG.asResource(name), transform);
	}

	protected abstract IRecipeTypeInfo getRecipeType();

	protected <T extends ProcessingRecipe<?>> ProcessingRecipeSerializer<T> getSerializer() {
		return getRecipeType().getSerializer();
	}

	protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
		return () -> {
			ResourceLocation registryName = CatnipServices.REGISTRIES.getKeyOrThrow(item.get()
				.asItem());
			return TFMG.asResource(registryName.getPath() + suffix);
		};
	}



}
