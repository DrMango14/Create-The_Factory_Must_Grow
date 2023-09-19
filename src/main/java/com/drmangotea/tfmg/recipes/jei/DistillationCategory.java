package com.drmangotea.tfmg.recipes.jei;



import com.drmangotea.tfmg.recipes.distillation.DistillationRecipe;
import com.drmangotea.tfmg.recipes.jei.machines.Distillery;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DistillationCategory extends CreateRecipeCategory<DistillationRecipe> {

	private final Distillery distiller = new Distillery();

	public DistillationCategory(Info<DistillationRecipe> info) {
		super(info);
	}




	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DistillationRecipe recipe, IFocusGroup focuses) {
		ItemStack result1 = recipe.getFirstItemResult();
		ItemStack result2 = recipe.getSecondItemResult();
		FluidIngredient fluidIngredient=recipe.getInputFluid();


		builder
				.addSlot(RecipeIngredientRole.INPUT, 2, 75)
				.setBackground(getRenderedSlot(), -1, -1)
				.addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getInputFluid().getMatchingFluidStacks()))
				.addTooltipCallback(addFluidTooltip(recipe.getInputFluid().getRequiredAmount()));



		builder
				.addSlot(RecipeIngredientRole.OUTPUT,150, 55)
				.setBackground(getRenderedSlot(), -1, -1)
				.addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getFirstFluidResult()))
				.addTooltipCallback(addFluidTooltip(recipe.getFirstFluidResult().getAmount()));
		builder
				.addSlot(RecipeIngredientRole.OUTPUT,150, 33)
				.setBackground(getRenderedSlot(), -1, -1)
				.addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getSecondFluidResult()))
				.addTooltipCallback(addFluidTooltip(recipe.getSecondFluidResult().getAmount()));

		builder
				.addSlot(RecipeIngredientRole.OUTPUT,150, 12)
				.setBackground(getRenderedSlot(), -1, -1)
				.addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getThirdFluidResult()))
				.addTooltipCallback(addFluidTooltip(recipe.getThirdFluidResult().getAmount()));



		builder
				.addSlot(RecipeIngredientRole.OUTPUT, 105, 100)
				.setBackground(getRenderedSlot(), -1, -1)
				.addItemStack(recipe.getFirstItemResult());
		builder
				.addSlot(RecipeIngredientRole.OUTPUT, 127, 100)
				.setBackground(getRenderedSlot(), -1, -1)
				.addItemStack(recipe.getSecondItemResult());
	}

	@Override
	public void draw(DistillationRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
		distiller
				.draw(matrixStack, 65, 27);
		AllGuiTextures.JEI_ARROW.render(matrixStack, 20, 80);
		AllGuiTextures.JEI_ARROW.render(matrixStack, 100, 14);
		AllGuiTextures.JEI_ARROW.render(matrixStack, 100, 35);
		AllGuiTextures.JEI_ARROW.render(matrixStack, 100, 57);
		AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 100, 79);
	}

}
