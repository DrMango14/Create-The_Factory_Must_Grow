package com.drmangotea.createindustry.recipes.jei;



import com.drmangotea.createindustry.recipes.coking.CokingRecipe;
import com.drmangotea.createindustry.recipes.jei.machines.CokeOven;
import com.drmangotea.createindustry.registry.TFMGFluids;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CokingCategory extends CreateRecipeCategory<CokingRecipe> {

	private final CokeOven cokeOven = new CokeOven();

	public CokingCategory(Info<CokingRecipe> info) {
		super(info);
	}




	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CokingRecipe recipe, IFocusGroup focuses) {


		builder
				.addSlot(RecipeIngredientRole.INPUT, 1, 13)
				.setBackground(getRenderedSlot(), -1, -1)
				.addIngredients(recipe.getIngredients().get(0));

		builder
				.addSlot(RecipeIngredientRole.OUTPUT, 121, 90)
				.setBackground(getRenderedSlot(), -1, -1)
				.addItemStack(recipe.getResultItem());

		//fluid

		builder
				.addSlot(RecipeIngredientRole.OUTPUT,160, 46)
				.setBackground(getRenderedSlot(), -1, -1)
				.addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(new FluidStack(TFMGFluids.CARBON_DIOXIDE.get(),5000)))
				.addTooltipCallback(addFluidTooltip(5000));

       builder
       		.addSlot(RecipeIngredientRole.OUTPUT,160, 22)
       		.setBackground(getRenderedSlot(), -1, -1)
       		.addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getFluidResults().get(0)))
       		.addTooltipCallback(addFluidTooltip(recipe.getFluidResults().get(0).getAmount()));

	}

	@Override
	public void draw(CokingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
		cokeOven
				.draw(matrixStack, 65, 50);
		AllGuiTextures.JEI_ARROW.render(matrixStack, 20, 15);


		AllGuiTextures.JEI_ARROW.render(matrixStack, 115, 25);
		AllGuiTextures.JEI_ARROW.render(matrixStack, 115, 50);

		AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 115, 73);

	}

}
