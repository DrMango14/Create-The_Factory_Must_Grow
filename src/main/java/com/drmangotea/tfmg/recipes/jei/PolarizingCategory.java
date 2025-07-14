package com.drmangotea.tfmg.recipes.jei;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.recipes.PolarizingRecipe;
import com.drmangotea.tfmg.recipes.jei.machines.Polarizer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class PolarizingCategory extends CreateRecipeCategory<PolarizingRecipe> {
    private Polarizer polarizer = new Polarizer();

    public PolarizingCategory(Info<PolarizingRecipe> info) {
        super(info);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, PolarizingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 15, 9).setBackground(getRenderedSlot(), -1, -1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 140, 28).setBackground(getRenderedSlot(), -1, -1).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }

    public void draw(PolarizingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {

        AllGuiTextures.JEI_ARROW.render(graphics, 85, 32);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 43, 4);
        this.polarizer.draw(graphics, 48, 27);


    }

    public static class AssemblyPolarizing extends SequencedAssemblySubCategory {
        Polarizer polarizer = new Polarizer();

        public AssemblyPolarizing() {
            super(25);
        }

        public void setRecipe(IRecipeLayoutBuilder builder, SequencedRecipe<?> recipe, IFocusGroup focuses, int x) {

        }

        public void draw(SequencedRecipe<?> recipe, GuiGraphics graphics, double mouseX, double mouseY, int index) {

            PoseStack ms = graphics.pose();

            ms.pushPose();
            ms.translate(0.0, 51.5, 0.0);
            ms.scale(0.6F, 0.6F, 0.6F);
            this.polarizer.draw(graphics, this.getWidth() / 2, 30);
            ms.popPose();
        }
    }
}