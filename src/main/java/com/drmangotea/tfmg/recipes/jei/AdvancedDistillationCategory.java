package com.drmangotea.tfmg.recipes.jei;

import com.drmangotea.tfmg.recipes.distillation.DistillationRecipe;
import com.drmangotea.tfmg.registry.TFMGGuiTextures;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AdvancedDistillationCategory extends CreateRecipeCategory<DistillationRecipe> {



	public AdvancedDistillationCategory(Info<DistillationRecipe> info) {
		super(info);
	}




	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DistillationRecipe recipe, IFocusGroup focuses) {
		FluidIngredient fluidIngredient=recipe.getInputFluid();

		int outputCount = recipe.getOutputCount(recipe);
		int yModifier = 60 -(outputCount*10);
		int y = 147-yModifier;


		builder
				.addSlot(RecipeIngredientRole.INPUT, 18, 130-yModifier)
				.setBackground(getRenderedSlot(), -1, -1)
				.addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getInputFluid().getMatchingFluidStacks()))
				.addTooltipCallback(addFluidTooltip(recipe.getInputFluid().getRequiredAmount()));


		for(int i = 0; i<outputCount;i++) {
			y -= 24;
			builder
					.addSlot(RecipeIngredientRole.OUTPUT, 105, y)
					.setBackground(getRenderedSlot(), -1, -1)
					.addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getFluidResults().get(i)))
					.addTooltipCallback(addFluidTooltip(recipe.getFirstFluidResult().getAmount()));
		}

	}

	@Override
	public void draw(DistillationRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		int outputCount = recipe.getOutputCount(recipe);
		int yModifier = 60 -(outputCount*10);
		int y = 126-yModifier;


		TFMGGuiTextures.DISTILLATION_TOWER_BOTTOM.render(graphics,10,y);
	//	TFMGGuiTextures.DISTILLATION_TOWER_FIRE.render(matrixStack,10,y+24);
		AllGuiTextures.JEI_ARROW.render(graphics, 56, y);

		for(int i = 0; i<(outputCount-1);i++){
			y -= 24;
			TFMGGuiTextures.DISTILLATION_TOWER_MIDDLE.render(graphics,10,y);
			AllGuiTextures.JEI_ARROW.render(graphics, 56, y);
		}
		y -= 12;
		TFMGGuiTextures.DISTILLATION_TOWER_TOP.render(graphics,10,y);

	}

}
