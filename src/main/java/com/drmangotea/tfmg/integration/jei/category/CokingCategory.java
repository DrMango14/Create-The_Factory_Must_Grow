package com.drmangotea.tfmg.integration.jei.category;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.integration.jei.render.CokeOven;
import com.drmangotea.tfmg.recipes.CokingRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.StringUtil;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CokingCategory extends CreateRecipeCategory<CokingRecipe> {
    private final CokeOven cokeOven = new CokeOven();

    public CokingCategory(Info<CokingRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CokingRecipe recipe, IFocusGroup focuses) {


        builder.addSlot(RecipeIngredientRole.INPUT, 1, 13)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().getFirst());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 121, 90)
                .setBackground(getRenderedSlot(), -1, -1)
                .addItemStack(recipe.getRollableResults().getFirst().getStack());

        //fluid
        //TODO: Add a way to select Coke Oven size through the category as it influences recipe duration so the Over Time tooltip could be inaccurate.
        FluidStack primaryFluid = recipe.getFluidResults().get(0);
        FluidStack secondaryFluid = recipe.getFluidResults().get(1);
        if (recipe.getFluidResults().size() >= 2) {
            addFluidSlot(builder, 160, 46, secondaryFluid.copyWithAmount(secondaryFluid.getAmount() * recipe.getProcessingDuration())).addRichTooltipCallback((slotView, tooltip) -> tooltip.add(TFMGLang.translate("recipe.over_time", StringUtil.formatTickDuration(recipe.getProcessingDuration(), Minecraft.getInstance().level.tickRateManager().tickrate())).component()));
        }
        if (!recipe.getFluidResults().isEmpty()) {
            addFluidSlot(builder, 160, 22, primaryFluid.copyWithAmount(primaryFluid.getAmount() * recipe.getProcessingDuration())).addRichTooltipCallback((slotView, tooltip) -> tooltip.add(TFMGLang.translate("recipe.over_time", StringUtil.formatTickDuration(recipe.getProcessingDuration(), Minecraft.getInstance().level.tickRateManager().tickrate())).component()));
        }
    }

    @Override
    public void draw(CokingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        cokeOven.draw(graphics, 65, 50);

        AllGuiTextures.JEI_ARROW.render(graphics, 20, 15);
        AllGuiTextures.JEI_ARROW.render(graphics, 115, 25);
        AllGuiTextures.JEI_ARROW.render(graphics, 115, 50);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 115, 73);
    }

}
