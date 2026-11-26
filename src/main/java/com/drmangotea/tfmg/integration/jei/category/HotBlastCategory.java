package com.drmangotea.tfmg.integration.jei.category;

import com.drmangotea.tfmg.recipes.HotBlastRecipe;
import com.drmangotea.tfmg.registry.TFMGGuiTextures;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.gui.GuiGraphics;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class HotBlastCategory extends CreateRecipeCategory<HotBlastRecipe> {
    public HotBlastCategory(Info<HotBlastRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, HotBlastRecipe recipe, IFocusGroup focuses) {
        addFluidSlot(builder,18,52,recipe.getFluidIngredients().get(0));
        addFluidSlot(builder,18,74,recipe.getFluidIngredients().get(1));

        addFluidSlot(builder,105,51,recipe.getFluidResults().get(0));
        addFluidSlot(builder,105,75,recipe.getFluidResults().get(1));
    }

    @Override
    public void draw(HotBlastRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        TFMGGuiTextures.BLAST_STOVE.render(graphics, 10, 0);
        AllGuiTextures.JEI_ARROW.render(graphics, 56, 55);
        AllGuiTextures.JEI_ARROW.render(graphics, 56, 78);
    }
}
