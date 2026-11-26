package com.drmangotea.tfmg.integration.jei.category;


import com.drmangotea.tfmg.integration.jei.TFMGJeiConstants;
import com.drmangotea.tfmg.integration.jei.render.CastingSetup;
import com.drmangotea.tfmg.recipes.CastingRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CastingCategory extends CreateRecipeCategory<CastingRecipe> {
    private final CastingSetup castingSetup = new CastingSetup();

    public CastingCategory(Info<CastingRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CastingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 130, 20)
                .setBackground(getRenderedSlot(), -1, -1)
                .addItemStack(TFMGJeiConstants.getSingleResult(recipe));

        addFluidSlot(builder,15,20,recipe.getFluidIngredients().getFirst());
    }

    @Override
    public void draw(CastingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        castingSetup.draw(graphics, 72, 40);

        AllGuiTextures.JEI_ARROW.render(graphics, 78, 23);
    }
}
