package com.drmangotea.tfmg.integration.jei.category;

import com.drmangotea.tfmg.recipes.DistillationRecipe;
import com.drmangotea.tfmg.registry.TFMGGuiTextures;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.gui.GuiGraphics;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DistillationCategory extends CreateRecipeCategory<DistillationRecipe> {
	public DistillationCategory(Info<DistillationRecipe> info) {
		super(info);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, DistillationRecipe recipe, IFocusGroup focuses) {
		int outputCount = recipe.getOutputCount(recipe);
		int yModifier = 60 - (outputCount * 10);
		int y = 147 - yModifier;
		addFluidSlot(builder, 18, 130 - yModifier, recipe.getInputFluid());

		for(int i = 0; i < outputCount; i++) {
			y -= 24;
			addFluidSlot(builder, 105, y, recipe.getFluidResults().get(i));
		}
	}

	@Override
	public void draw(DistillationRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		int outputCount = recipe.getOutputCount(recipe);
		int yModifier = 60 - (outputCount * 10);
		int y = 126 - yModifier;

		TFMGGuiTextures.DISTILLATION_TOWER_BOTTOM.render(graphics, 10, y);
		AllGuiTextures.JEI_ARROW.render(graphics, 56, y);

		for (int i = 0; i < (outputCount - 1); i++){
			y -= 24;
			TFMGGuiTextures.DISTILLATION_TOWER_MIDDLE.render(graphics, 10, y);
			AllGuiTextures.JEI_ARROW.render(graphics, 56, y);
		}

		y -= 12;
		TFMGGuiTextures.DISTILLATION_TOWER_TOP.render(graphics, 10, y);
	}
}
