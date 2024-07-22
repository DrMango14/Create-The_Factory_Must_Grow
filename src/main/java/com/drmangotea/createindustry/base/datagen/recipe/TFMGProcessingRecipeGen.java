package com.drmangotea.createindustry.base.datagen.recipe;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.create.*;
import com.drmangotea.createindustry.base.datagen.recipe.tfmg.*;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class TFMGProcessingRecipeGen extends TFMGRecipeProvider {
    protected static final List<TFMGProcessingRecipeGen> GENERATORS = new ArrayList<>();
    protected static final int BUCKET = FluidType.BUCKET_VOLUME;
    protected static final int BOTTLE = 250;
    
    public TFMGProcessingRecipeGen(DataGenerator generator) {
        super(generator);
    }
    
    public static void registerAll(DataGenerator gen) {
        //TFMG
        GENERATORS.add(new CastingGen(gen));
        GENERATORS.add(new CokingGen(gen));
        GENERATORS.add(new DistillationGen(gen));
        GENERATORS.add(new IndustrialBlastingGen(gen));
        GENERATORS.add(new GasBlastingGen(gen));
        
        //Create
        GENERATORS.add(new CompactingGen(gen));
        GENERATORS.add(new CrushingGen(gen));
        GENERATORS.add(new FillingGen(gen));
        GENERATORS.add(new ItemApplicationGen(gen));
        GENERATORS.add(new MillingGen(gen));
        GENERATORS.add(new MixingGen(gen));
        GENERATORS.add(new PressingGen(gen));
        
        gen.addProvider(true, new DataProvider() {
            
            @Override
            public String getName() {
                return "TFMG's Processing Recipes";
            }
            
            @Override
            public void run(@NotNull CachedOutput dc) {
                GENERATORS.forEach(g -> {
                    try {
                        g.run(dc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
    
    /**
     * Create a processing recipe with a single itemstack ingredient, using its id
     * as the name of the recipe
     */
    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = getSerializer();
        GeneratedRecipe generatedRecipe = c -> {
            ItemLike itemLike = singleIngredient.get();
            transform
                    .apply(new ProcessingRecipeBuilder<>(serializer.getFactory(),
                            new ResourceLocation(namespace, RegisteredObjects.getKeyOrThrow(itemLike.asItem())
                                    .getPath())).withItemIngredients(Ingredient.of(itemLike)))
                    .build(c);
        };
        all.add(generatedRecipe);
        return generatedRecipe;
    }
    
    /**
     * Create a processing recipe with a single itemstack ingredient, using its id
     * as the name of the recipe
     */
    <T extends ProcessingRecipe<?>> GeneratedRecipe create(Supplier<ItemLike> singleIngredient,
                                                           UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(CreateTFMG.MOD_ID, singleIngredient, transform);
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
    
    /**
     * Create a new processing recipe, with recipe definitions provided by the
     * function
     */
    protected <T extends ProcessingRecipe<?>> GeneratedRecipe create(ResourceLocation name,
                                                                     UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return createWithDeferredId(() -> name, transform);
    }
    
    /**
     * Create a new processing recipe, with recipe definitions provided by the
     * function
     */
    <T extends ProcessingRecipe<?>> GeneratedRecipe create(String name,
                                                           UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return create(CreateTFMG.asResource(name), transform);
    }
    
    protected abstract IRecipeTypeInfo getRecipeType();
    
    protected <T extends ProcessingRecipe<?>> ProcessingRecipeSerializer<T> getSerializer() {
        return getRecipeType().getSerializer();
    }
    
    protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
        return () -> {
            ResourceLocation registryName = RegisteredObjects.getKeyOrThrow(item.get()
                    .asItem());
            return CreateTFMG.asResource(registryName.getPath() + suffix);
        };
    }
    
    
    
    @Override
    public String getName() {
        return "TFMG's Processing Recipes: " + getRecipeType().getId()
                .getPath();
    }
}
