package com.drmangotea.tfmg.recipes.jei;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.recipes.*;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.AllFluids;
import com.simibubi.create.Create;
import com.simibubi.create.compat.jei.*;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.equipment.blueprint.BlueprintScreen;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import com.simibubi.create.content.logistics.factoryBoard.FactoryPanelSetItemScreen;
import com.simibubi.create.content.logistics.filter.AbstractFilterScreen;
import com.simibubi.create.content.logistics.redstoneRequester.RedstoneRequesterScreen;
import com.simibubi.create.content.redstone.link.controller.LinkedControllerScreen;
import com.simibubi.create.content.trains.schedule.ScheduleScreen;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@JeiPlugin
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class TFMGJei implements IModPlugin {

    private static final ResourceLocation ID = TFMG.asResource("jei_plugin");

    private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();
    private IIngredientManager ingredientManager;
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
                        .addTypedRecipes(TFMGRecipeTypes.VAT_MACHINE_RECIPE)
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
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ingredientManager = registration.getIngredientManager();

        allCategories.forEach(c -> c.registerRecipes(registration));

        registration.addRecipes(RecipeTypes.CRAFTING, ToolboxColoringRecipeMaker.createRecipes().toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new BlueprintTransferHandler(), RecipeTypes.CRAFTING);
        registration.addUniversalRecipeTransferHandler(new StockKeeperTransferHandler(registration.getJeiHelpers()));
    }

    @Override
    public <T> void registerFluidSubtypes(ISubtypeRegistration registration, IPlatformFluidHelper<T> platformFluidHelper) {
        PotionFluidSubtypeInterpreter interpreter = new PotionFluidSubtypeInterpreter();
        PotionFluid potionFluid = AllFluids.POTION.get();
        registration.registerSubtypeInterpreter(NeoForgeTypes.FLUID_STACK, potionFluid.getSource(), interpreter);
        registration.registerSubtypeInterpreter(NeoForgeTypes.FLUID_STACK, potionFluid.getFlowing(), interpreter);
    }

    @Override
    public void registerExtraIngredients(IExtraIngredientRegistration registration) {
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
        List<Holder.Reference<Potion>> potions = registryAccess.lookupOrThrow(Registries.POTION)
                .listElements()
                .toList();
        Collection<FluidStack> potionFluids = new ArrayList<>(potions.size() * 3);
        Set<Set<Holder<MobEffect>>> visitedEffects = new HashSet<>();
        for (Holder.Reference<Potion> potion : potions) {

            PotionContents potionContents = new PotionContents(potion);

            if (potionContents.hasEffects()) {
                Set<Holder<MobEffect>> effectSet = new HashSet<>();
                potionContents.forEachEffect(mei -> effectSet.add(mei.getEffect()));
                if (!visitedEffects.add(effectSet))
                    continue;
            }

            potionFluids.add(PotionFluid.of(1000, potionContents, PotionFluid.BottleType.REGULAR));
        }
        registration.addExtraIngredients(NeoForgeTypes.FLUID_STACK, potionFluids);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(AbstractSimiContainerScreen.class, new SlotMover());

        registration.addGhostIngredientHandler(AbstractFilterScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(BlueprintScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(LinkedControllerScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(ScheduleScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(RedstoneRequesterScreen.class, new GhostIngredientHandler());
        registration.addGhostIngredientHandler(FactoryPanelSetItemScreen.class, new GhostIngredientHandler());
    }

    private class CategoryBuilder<T extends Recipe<?>> extends CreateRecipeCategory.Builder<T> {
        public CategoryBuilder(Class<? extends T> recipeClass) {
            super(recipeClass);
        }

        @Override
        public CreateRecipeCategory<T> build(ResourceLocation id, CreateRecipeCategory.Factory<T> factory) {
            CreateRecipeCategory<T> category = super.build(id, factory);
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
        RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
        return ItemHelper.sameItem(recipe1.getResultItem(registryAccess), recipe2.getResultItem(registryAccess));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        TFMGJei.runtime = runtime;
    }

}

