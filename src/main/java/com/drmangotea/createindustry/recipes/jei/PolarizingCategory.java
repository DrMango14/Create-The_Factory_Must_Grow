package com.drmangotea.createindustry.recipes.jei;

import com.drmangotea.createindustry.base.TFMGTools;
import com.drmangotea.createindustry.recipes.jei.machines.Polarizer;
import com.drmangotea.createindustry.recipes.polarizing.PolarizingRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrh0.createaddition.util.Util;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedPress;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.Lang;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.Ingredient;

public class PolarizingCategory extends CreateRecipeCategory<PolarizingRecipe> {
    private Polarizer polarizer = new Polarizer();
    
    public PolarizingCategory(Info<PolarizingRecipe> info) {
        super(info);
    }
    
    public void setRecipe(IRecipeLayoutBuilder builder, PolarizingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 15, 9).setBackground(getRenderedSlot(), -1, -1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 140, 28).setBackground(getRenderedSlot(), -1, -1).addItemStack(recipe.getResultItem());
    }
    
    public void draw(PolarizingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(matrixStack, 85, 32);
        AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 43, 4);
        this.polarizer.draw(matrixStack, 48, 27);
        Minecraft.getInstance().font.draw(matrixStack, TFMGTools.formatEnergy(recipe.getEnergy()) + "fe", 86.0F, 9.0F, 4210752);
    }
    
    public static class AssemblyPolarizing extends SequencedAssemblySubCategory {
        Polarizer polarizer = new Polarizer();
        
        public AssemblyPolarizing() {
            super(25);
        }
        
        public void setRecipe(IRecipeLayoutBuilder builder, SequencedRecipe<?> recipe, IFocusGroup focuses, int x) {
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, x + 4, 15).setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1).addIngredients(recipe.getRecipe().getIngredients().get(0));
            IAssemblyRecipe var7 = recipe.getAsAssemblyRecipe();
            if (var7 instanceof PolarizingRecipe polarizingRecipe) {
                slot.addTooltipCallback((recipeSlotView, tooltip) -> {
                    tooltip.add(1, Lang.text(TFMGTools.formatEnergy(polarizingRecipe.getEnergy()) + "fe").component().withStyle(ChatFormatting.GOLD));
                });
            }
        }
        
        public void draw(SequencedRecipe<?> recipe, PoseStack ms, double mouseX, double mouseY, int index) {
            ms.pushPose();
            ms.translate(0.0, 51.5, 0.0);
            ms.scale(0.6F, 0.6F, 0.6F);
            this.polarizer.draw(ms, this.getWidth() / 2, 30);
            ms.popPose();
        }
    }
}
