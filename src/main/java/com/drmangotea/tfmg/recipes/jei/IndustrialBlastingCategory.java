package com.drmangotea.tfmg.recipes.jei;


import com.drmangotea.tfmg.recipes.IndustrialBlastingRecipe;
import com.drmangotea.tfmg.recipes.jei.machines.BlastFurnace;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class IndustrialBlastingCategory extends CreateRecipeCategory<IndustrialBlastingRecipe> {

    private final BlastFurnace blastFurnace = new BlastFurnace();

    public IndustrialBlastingCategory(Info<IndustrialBlastingRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IndustrialBlastingRecipe recipe, IFocusGroup focuses) {


        builder
                .addSlot(RecipeIngredientRole.INPUT, 25, 13)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().get(0));
        if (recipe.getIngredients().size() > 1) {
            builder
                    .addSlot(RecipeIngredientRole.INPUT, 5, 13)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addIngredients(recipe.getIngredients().get(1));
        }
        builder
                .addSlot(RecipeIngredientRole.INPUT, 70, 13)
                .setBackground(getRenderedSlot(), -1, -1)
                .addItemStack(new ItemStack(TFMGItems.COAL_COKE_DUST.get()));

        //fluid
        addFluidSlot(builder, 140, 117, recipe.getFluidResults().get(0));
        if (recipe.getFluidResults().size() > 2)
            addFluidSlot(builder, 160, 117, recipe.getFluidResults().get(1));


    }

    @Override
    public void draw(IndustrialBlastingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        blastFurnace
                .draw(graphics, 50, 135);


        AllGuiTextures.JEI_ARROW.render(graphics, 96, 121);

        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 45, 15);

    }

}
