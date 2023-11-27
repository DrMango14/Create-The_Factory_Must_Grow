package com.drmangotea.tfmg.recipes.jei;




import com.drmangotea.tfmg.recipes.casting.CastingRecipe;
import com.drmangotea.tfmg.recipes.jei.machines.CastingSetup;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CastingCategory extends CreateRecipeCategory<CastingRecipe> {

	private final CastingSetup castingSetup = new CastingSetup();

	public CastingCategory(Info<CastingRecipe> info) {
		super(info);
	}




	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CastingRecipe recipe, IFocusGroup focuses) {


		int y = 77;
		for(int i = 0;i<CastingRecipe.getOutputCount(recipe);i++) {
			RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();
			builder
					.addSlot(RecipeIngredientRole.OUTPUT, 130, y)
					.setBackground(getRenderedSlot(), -1, -1)
					.addItemStack(recipe.getResultItem(registryAccess));


			ItemStack mold= TFMGItems.INGOT_MOLD.asStack();
			if(i==0)
				mold = TFMGItems.INGOT_MOLD.asStack();
			if(i==1)
				mold = TFMGItems.BLOCK_MOLD.asStack();


			builder
					.addSlot(RecipeIngredientRole.OUTPUT, 111, y)
					.setBackground(getRenderedSlot(), -1, -1)
					.addItemStack(mold);
			y -= 24;

		}

		//fluid

		builder
				.addSlot(RecipeIngredientRole.INPUT, 2, 33)
				.setBackground(getRenderedSlot(), -1, -1)
				.addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(recipe.getInputFluid().getMatchingFluidStacks()))
				.addTooltipCallback(addFluidTooltip(recipe.getInputFluid().getRequiredAmount()));


	}

	@Override
	public void draw(CastingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
		castingSetup
				.draw(graphics, 65, 50);



		AllGuiTextures.JEI_ARROW.render(graphics, 20, 36);







	}

}
