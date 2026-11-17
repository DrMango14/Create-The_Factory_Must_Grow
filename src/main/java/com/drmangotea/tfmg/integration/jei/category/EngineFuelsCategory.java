package com.drmangotea.tfmg.integration.jei.category;

import com.drmangotea.tfmg.integration.jei.util.EngineFuelsRecipe;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class EngineFuelsCategory implements IRecipeCategory<EngineFuelsRecipe> {
    public static final RecipeType<EngineFuelsRecipe> RECIPE_TYPE = RecipeType.create("tfmg", "engine_fuels", EngineFuelsRecipe.class);

    private final IDrawable icon;

    public EngineFuelsCategory(IGuiHelper helper) {
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, TFMGBlocks.REGULAR_ENGINE.asStack());
    }

    @Override
    public RecipeType<EngineFuelsRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("tfmg.jei.engine_fuels.title");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EngineFuelsRecipe recipe, IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }

    @Override
    public void draw(EngineFuelsRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        recipe.draw(graphics);
    }

    @Override
    @SuppressWarnings("removal")
    public IDrawable getBackground() {
        return new EmptyBackground();
    }

    static class EmptyBackground implements IDrawable {
        @Override
        public int getWidth() {
            return 170;
        }

        @Override
        public int getHeight() {
            return 50;
        }

        @Override @ParametersAreNonnullByDefault
        public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {}
    }
}
