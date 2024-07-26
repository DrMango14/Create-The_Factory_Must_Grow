package com.drmangotea.createindustry.recipes.jei;

import com.drmangotea.createindustry.recipes.gas_blasting.GasBlastingRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;

public class GasBlastingCategory extends CreateRecipeCategory<GasBlastingRecipe> {
    public GasBlastingCategory(Info<GasBlastingRecipe> info) {
        super(info);
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GasBlastingRecipe recipe, IFocusGroup focuses) {
        builder
                .addSlot(RecipeIngredientRole.INPUT, 25, 13)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getInputFluid().getMatchingFluidStacks().get(0)))
                .addTooltipCallback(addFluidTooltip(recipe.getInputFluid().getRequiredAmount()));
        builder
                .addSlot(RecipeIngredientRole.INPUT, 5, 13)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getSecondInputFluid().getMatchingFluidStacks().get(0)))
                .addTooltipCallback(addFluidTooltip(recipe.getSecondInputFluid().getRequiredAmount()));
        
        builder
                .addSlot(RecipeIngredientRole.OUTPUT,140, 117)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getFluidResults().get(0)))
                .addTooltipCallback(addFluidTooltip(recipe.getFluidResults().get(0).getAmount()));
        
        builder
                .addSlot(RecipeIngredientRole.OUTPUT,160, 117)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getFluidResults().get(1)))
                .addTooltipCallback(addFluidTooltip(recipe.getFluidResults().get(1).getAmount()));
    }
    
    @Override
    public void draw(GasBlastingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(matrixStack, 96, 121);
        
        AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 45, 15);
        
    }
}
