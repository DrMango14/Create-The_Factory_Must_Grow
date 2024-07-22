package com.drmangotea.createindustry.base.datagen.recipe.vanilla;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.datagen.recipe.TFMGRecipeProvider;
import com.google.common.base.Supplier;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class TFMGStandardRecipeGen extends TFMGRecipeProvider {
    String currentFolder = "";
    
    Marker enterFolder(String folder) {
        currentFolder = folder;
        return new Marker();
    }
    public GeneratedRecipeBuilder create(String path, Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(path, result);
    }
    public GeneratedRecipeBuilder create(String path, ResourceLocation result) {
        return new GeneratedRecipeBuilder(path, result);
    }
    public GeneratedRecipeBuilder create(String path, ItemProviderEntry<? extends ItemLike> result) {
        return create(path, result::get);
    }
    
    public GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }
    
    public GeneratedRecipeBuilder create(ResourceLocation result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }
    
    public GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike> result) {
        return create(result::get);
    }
    
    public class GeneratedRecipeBuilder {
        
        private String path;
        private String suffix;
        private Supplier<? extends ItemLike> result;
        private ResourceLocation compatDatagenOutput;
        List<ICondition> recipeConditions;
        
        private Supplier<ItemPredicate> unlockedBy;
        private int amount;
        
        private GeneratedRecipeBuilder(String path) {
            this.path = path;
            this.recipeConditions = new ArrayList<>();
            this.suffix = "";
            this.amount = 1;
        }
        
        public GeneratedRecipeBuilder(String path, Supplier<? extends ItemLike> result) {
            this(path);
            this.result = result;
        }
        
        public GeneratedRecipeBuilder(String path, ResourceLocation result) {
            this(path);
            this.compatDatagenOutput = result;
        }
        
        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }
        
        GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(item.get())
                    .build();
            return this;
        }
        
        GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(tag.get())
                    .build();
            return this;
        }
        
        GeneratedRecipeBuilder whenModLoaded(String modid) {
            return withCondition(new ModLoadedCondition(modid));
        }
        
        GeneratedRecipeBuilder whenModMissing(String modid) {
            return withCondition(new NotCondition(new ModLoadedCondition(modid)));
        }
        
        GeneratedRecipeBuilder withCondition(ICondition condition) {
            recipeConditions.add(condition);
            return this;
        }
        
        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }
        
        GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
            return register(consumer -> {
                ShapedRecipeBuilder b = builder.apply(ShapedRecipeBuilder.shaped(result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }
        
        GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
            return register(consumer -> {
                ShapelessRecipeBuilder b = builder.apply(ShapelessRecipeBuilder.shapeless(result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }
        
        GeneratedRecipe viaSmithing(Supplier<? extends Item> base, Supplier<Ingredient> upgradeMaterial) {
            return register(consumer -> {
                UpgradeRecipeBuilder b =
                        UpgradeRecipeBuilder.smithing(Ingredient.of(base.get()), upgradeMaterial.get(), result.get()
                                .asItem());
                b.unlocks("has_item", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(base.get())
                        .build()));
                b.save(consumer, createLocation("crafting"));
            });
        }
        
        private ResourceLocation createSimpleLocation(String recipeType) {
            return CreateTFMG.asResource(recipeType + "/" + getRegistryName().getPath() + suffix);
        }
        
        private ResourceLocation createLocation(String recipeType) {
            return CreateTFMG.asResource(recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix);
        }
        
        private ResourceLocation getRegistryName() {
            return compatDatagenOutput == null ? RegisteredObjects.getKeyOrThrow(result.get()
                    .asItem()) : compatDatagenOutput;
        }
        
        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item) {
            return unlockedBy(item).viaCookingIngredient(() -> Ingredient.of(item.get()));
        }
        
        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag) {
            return unlockedByTag(tag).viaCookingIngredient(() -> Ingredient.of(tag.get()));
        }
        
        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient) {
            return new GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder(ingredient);
        }
        
        class GeneratedCookingRecipeBuilder {
            
            private Supplier<Ingredient> ingredient;
            private float exp;
            private int cookingTime;
            
            private final SimpleCookingSerializer<?> FURNACE = RecipeSerializer.SMELTING_RECIPE,
                    SMOKER = RecipeSerializer.SMOKING_RECIPE, BLAST = RecipeSerializer.BLASTING_RECIPE,
                    CAMPFIRE = RecipeSerializer.CAMPFIRE_COOKING_RECIPE;
            
            GeneratedCookingRecipeBuilder(Supplier<Ingredient> ingredient) {
                this.ingredient = ingredient;
                cookingTime = 200;
                exp = 0;
            }
            
            GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder forDuration(int duration) {
                cookingTime = duration;
                return this;
            }
            
            GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder rewardXP(float xp) {
                exp = xp;
                return this;
            }
            
            GeneratedRecipe inFurnace() {
                return inFurnace(b -> b);
            }
            
            GeneratedRecipe inFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                return create(FURNACE, builder, 1);
            }
            
            GeneratedRecipe inSmoker() {
                return inSmoker(b -> b);
            }
            GeneratedRecipe inSmoker(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(FURNACE, builder, 1);
                create(CAMPFIRE, builder, 3);
                return create(SMOKER, builder, .5f);
            }
            GeneratedRecipe inSmokerOnly() {
                return inSmokerOnly(b -> b);
            }
            GeneratedRecipe inSmokerOnly(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(CAMPFIRE, builder, 3);
                return create(SMOKER, builder, 1f);
            }
            
            GeneratedRecipe inBlastFurnace() {
                return inBlastFurnace(b -> b);
            }
            GeneratedRecipe inBlastFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(FURNACE, builder, 1);
                return create(BLAST, builder, .5f);
            }
            GeneratedRecipe inBlastFurnaceOnly() {
                return inBlastFurnaceOnly(b -> b);
            }
            GeneratedRecipe inBlastFurnaceOnly(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                return create(BLAST, builder, 1f);
            }
            
            private GeneratedRecipe create(SimpleCookingSerializer<?> serializer,
                                           UnaryOperator<SimpleCookingRecipeBuilder> builder, float cookingTimeModifier) {
                return register(consumer -> {
                    boolean isOtherMod = compatDatagenOutput != null;
                    
                    SimpleCookingRecipeBuilder b = builder.apply(
                            SimpleCookingRecipeBuilder.cooking(ingredient.get(), isOtherMod ? Items.DIRT : result.get(),
                                    exp, (int) (cookingTime * cookingTimeModifier), serializer));
                    if (unlockedBy != null)
                        b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                    b.save(result -> {
                        consumer.accept(
                                isOtherMod ? new ModdedCookingRecipeResult(result, compatDatagenOutput, recipeConditions)
                                        : result);
                    }, createSimpleLocation(RegisteredObjects.getKeyOrThrow(serializer)
                            .getPath()));
                });
            }
        }
    }
    
    private static class ModdedCookingRecipeResult implements FinishedRecipe {
        
        private FinishedRecipe wrapped;
        private ResourceLocation outputOverride;
        private List<ICondition> conditions;
        
        public ModdedCookingRecipeResult(FinishedRecipe wrapped, ResourceLocation outputOverride,
                                         List<ICondition> conditions) {
            this.wrapped = wrapped;
            this.outputOverride = outputOverride;
            this.conditions = conditions;
        }
        
        @Override
        public ResourceLocation getId() {
            return wrapped.getId();
        }
        
        @Override
        public RecipeSerializer<?> getType() {
            return wrapped.getType();
        }
        
        @Override
        public JsonObject serializeAdvancement() {
            return wrapped.serializeAdvancement();
        }
        
        @Override
        public ResourceLocation getAdvancementId() {
            return wrapped.getAdvancementId();
        }
        
        @Override
        public void serializeRecipeData(JsonObject object) {
            wrapped.serializeRecipeData(object);
            object.addProperty("result", outputOverride.toString());
            
            JsonArray conds = new JsonArray();
            conditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
            object.add("conditions", conds);
        }
        
    }
    
    @Override
    public String getName() {
        return "TFMG's Standard Recipes";
    }
    
    public TFMGStandardRecipeGen(DataGenerator generator) {
        super(generator);
    }
}
