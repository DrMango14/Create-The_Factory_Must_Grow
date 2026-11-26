package com.drmangotea.tfmg.integration.jei;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.integration.jei.category.*;
import com.drmangotea.tfmg.integration.jei.util.ClientEngineFuels;
import com.drmangotea.tfmg.integration.jei.util.EngineFuelsRecipe;
import com.drmangotea.tfmg.recipes.*;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.item.ItemHelper;
import dev.metallurgists.rutile.compat.jei.category.ElementCompositionCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IIngredientAliasRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@JeiPlugin
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class TFMGJei implements IModPlugin {
    private static final ResourceLocation ID = TFMG.asResource("jei_plugin");

    private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();
    public static IJeiRuntime runtime;

    private void loadCategories() {
        allCategories.clear();

        CreateRecipeCategory<?>

                advancedDistillation = builder(DistillationRecipe.class)
                .addTypedRecipes(TFMGRecipeTypes.DISTILLATION)
                .catalyst(TFMGBlocks.STEEL_DISTILLATION_CONTROLLER::get)
                .catalyst(TFMGBlocks.STEEL_DISTILLATION_OUTPUT::get)
                .itemIcon(TFMGBlocks.STEEL_DISTILLATION_CONTROLLER.get())
                .emptyBackground(177, 150)
                .build("advanced_distillation", DistillationCategory::new),

                coking = builder(CokingRecipe.class)
                        .addTypedRecipes(TFMGRecipeTypes.COKING)
                        .catalyst(TFMGBlocks.COKE_OVEN::get)
                        .itemIcon(TFMGBlocks.COKE_OVEN.get())
                        .emptyBackground(177, 123)
                        .build("coking", CokingCategory::new),

                chemical_vat = builder(VatMachineRecipe.class)
                        .addTypedRecipes(TFMGRecipeTypes.VAT)
                        .catalyst(TFMGBlocks.STEEL_CHEMICAL_VAT::get)
                        .catalyst(TFMGBlocks.CAST_IRON_CHEMICAL_VAT::get)
                        .catalyst(TFMGBlocks.FIREPROOF_CHEMICAL_VAT::get)
                        .catalyst(TFMGBlocks.INDUSTRIAL_MIXER::get)
                        .catalyst(TFMGBlocks.ELECTRODE_HOLDER::get)
                        .itemIcon(TFMGBlocks.STEEL_CHEMICAL_VAT.get())
                        .emptyBackground(177, 123)
                        .build("chemical_vat", ChemicalVatCategory::new),

                industrial_blasting = builder(IndustrialBlastingRecipe.class)
                        .addTypedRecipes(TFMGRecipeTypes.INDUSTRIAL_BLASTING)
                        .catalyst(TFMGBlocks.BLAST_FURNACE_OUTPUT::get)
                        .catalyst(TFMGBlocks.FIREPROOF_BRICKS::get)
                        .catalyst(TFMGBlocks.FIREPROOF_BRICK_REINFORCEMENT::get)
                        .catalyst(TFMGBlocks.BLAST_FURNACE_REINFORCEMENT::get)
                        .catalyst(TFMGBlocks.BLAST_FURNACE_HATCH::get)
                        .itemIcon(TFMGBlocks.BLAST_FURNACE_OUTPUT.get())
                        .emptyBackground(177, 150)
                        .build("industrial_blasting", IndustrialBlastingCategory::new),

                casting = builder(CastingRecipe.class)
                        .addTypedRecipes(TFMGRecipeTypes.CASTING)
                        .catalyst(TFMGBlocks.CASTING_BASIN::get)
                        .catalyst(TFMGItems.STEEL_INGOT::get)
                        .itemIcon(TFMGBlocks.CASTING_BASIN.get())
                        .emptyBackground(177, 53)
                        .build("casting", CastingCategory::new),

                hot_blast = builder(HotBlastRecipe.class)
                        .addTypedRecipes(TFMGRecipeTypes.HOT_BLAST)
                        .catalyst(TFMGBlocks.BLAST_STOVE::get)
                        .itemIcon(TFMGBlocks.BLAST_STOVE.get())
                        .emptyBackground(177, 110)
                        .build("hot_blast", HotBlastCategory::new),

                polarizing = builder(PolarizingRecipe.class)
                        .addTypedRecipes(TFMGRecipeTypes.POLARIZING)
                        .catalyst(TFMGBlocks.POLARIZER::get)
                        .catalyst(() -> Blocks.LIGHTNING_ROD)
                        .itemIcon(TFMGBlocks.POLARIZER.get())
                        .emptyBackground(177, 53)
                        .build("polarizing", PolarizingCategory::new),

                winding = builder(WindingRecipe.class)
                        .addTypedRecipes(TFMGRecipeTypes.WINDING)
                        .catalyst(TFMGBlocks.WINDING_MACHINE::get)
                        .itemIcon(TFMGBlocks.WINDING_MACHINE.get())
                        .emptyBackground(177, 53)
                        .build("winding", WindingCategory::new);

    }

    private <T extends Recipe<? extends RecipeInput>> TFMGJei.CategoryBuilder<T> builder(Class<T> recipeClass) {
        return new TFMGJei.CategoryBuilder<>(recipeClass);
    }

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories();
        registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
        registration.addRecipeCategories(new EngineFuelsCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IIngredientManager ingredientManager = registration.getIngredientManager();
        allCategories.forEach(c -> c.registerRecipes(registration));
        registration.addRecipes(EngineFuelsCategory.RECIPE_TYPE, EngineFuelsRecipe.fromClient(ClientEngineFuels.INSTANCE.getFuels()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));
    }

    @Override
    public void registerIngredientAliases(IIngredientAliasRegistration registration) {
        TFMGJeiConstants.engineCylinders().forEach(stack -> registration.addAlias(stack, "piston"));
        TFMGJeiConstants.engineTurbines().forEach(stack -> registration.addAlias(stack, "turbine"));
    }

    private class CategoryBuilder<T extends Recipe<?>> extends CreateRecipeCategory.Builder<T> {
        public CategoryBuilder(Class<? extends T> recipeClass) {
            super(recipeClass);
        }
        @Override
        public CreateRecipeCategory<T> build(String id, CreateRecipeCategory.Factory<T> factory) {
            CreateRecipeCategory<T> category = super.build(TFMG.asResource(id), factory);
            allCategories.add(category);
            return category;
        }
    }

    public static void consumeAllRecipes(Consumer<? super RecipeHolder<?>> consumer) {
        Minecraft.getInstance()
                .getConnection()
                .getRecipeManager()
                .getRecipes()
                .forEach(consumer);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Recipe<?>> void consumeTypedRecipes(Consumer<RecipeHolder<?>> consumer, RecipeType<?> type) {
        List<? extends RecipeHolder<?>> map = Minecraft.getInstance()
                .getConnection()
                .getRecipeManager().getAllRecipesFor((RecipeType) type);
        if (!map.isEmpty())
            map.forEach(consumer);
    }

    public static List<RecipeHolder<?>> getTypedRecipes(RecipeType<?> type) {
        List<RecipeHolder<?>> recipes = new ArrayList<>();
        consumeTypedRecipes(recipes::add, type);
        return recipes;
    }

    public static List<RecipeHolder<?>> getTypedRecipesExcluding(RecipeType<?> type, Predicate<RecipeHolder<?>> exclusionPred) {
        List<RecipeHolder<?>> recipes = getTypedRecipes(type);
        recipes.removeIf(exclusionPred);
        return recipes;
    }

    public static boolean doInputsMatch(Recipe<?> recipe1, Recipe<?> recipe2) {
        if (recipe1.getIngredients()
                .isEmpty()
                || recipe2.getIngredients()
                .isEmpty()) {
            return false;
        }
        ItemStack[] matchingStacks = recipe1.getIngredients()
                .getFirst()
                .getItems();
        if (matchingStacks.length == 0) {
            return false;
        }
        return recipe2.getIngredients()
                .getFirst()
                .test(matchingStacks[0]);
    }

    public static boolean doOutputsMatch(Recipe<?> recipe1, Recipe<?> recipe2) {
        RegistryAccess registryAccess = TFMGJeiConstants.registryAccess();
        if (registryAccess == null) {
            return false;
        }
        return ItemHelper.sameItem(recipe1.getResultItem(registryAccess), recipe2.getResultItem(registryAccess));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        TFMGJei.runtime = runtime;
    }

}
