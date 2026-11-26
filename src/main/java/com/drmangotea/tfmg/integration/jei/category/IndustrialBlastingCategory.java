package com.drmangotea.tfmg.integration.jei.category;


import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.integration.jei.render.BlastFurnace;
import com.drmangotea.tfmg.recipes.IndustrialBlastingRecipe;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGGuiTextures;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class IndustrialBlastingCategory extends CreateRecipeCategory<IndustrialBlastingRecipe> {
    private final BlastFurnace blastFurnace = new BlastFurnace();

    public IndustrialBlastingCategory(Info<IndustrialBlastingRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IndustrialBlastingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 3)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().get(0));
        if (recipe.getIngredients().size() > 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, 8, 3)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addIngredients(recipe.getIngredients().get(1));
        }
        int coalCokeAmount = Math.max(1, recipe.getProcessingDuration() / TFMGConfigs.common().machines.blastFurnaceFuelConsumption.get());
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 23)
                .setBackground(getRenderedSlot(), -1, -1)
                .addItemStack(new ItemStack(TFMGItems.COAL_COKE_DUST.get(), coalCokeAmount));

        //fluid
        addFluidSlot(builder, 145, 117, recipe.getFluidResults().get(0));
        if (recipe.getFluidResults().size() > 2)
            addFluidSlot(builder, 145, 97, recipe.getFluidResults().get(1));
        if (!recipe.getGasByproduct().isEmpty())
            addFluidSlot(builder, 130, 20, recipe.getGasByproduct().copyWithAmount(recipe.getProcessingDuration() * recipe.getGasByproduct().getAmount()))
                    .addRichTooltipCallback((slotView, tooltip) -> tooltip.add(TFMGLang.translate("recipe.over_time", StringUtil.formatTickDuration(recipe.getProcessingDuration(), Minecraft.getInstance().level.tickRateManager().tickrate())).component()));

        if (recipe.hotAirUsage > 0) {
            addFluidSlot(builder, 5, 70, SizedFluidIngredient.of(new FluidStack(FluidHelper.convertToStill(TFMGFluids.HOT_AIR.get()), recipe.hotAirUsage * recipe.getProcessingDuration() * 20)))
                    .addRichTooltipCallback((slotView, tooltip) -> tooltip.add(TFMGLang.translate("recipe.over_time", StringUtil.formatTickDuration(recipe.getProcessingDuration(), Minecraft.getInstance().level.tickRateManager().tickrate())).component()));
        }
    }

    @Override
    public void draw(IndustrialBlastingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        blastFurnace.topHatch = !recipe.getGasByproduct().isEmpty();
        blastFurnace.tuyere = recipe.hotAirUsage > 0;
        blastFurnace.draw(graphics, 60, 135);

        TFMGGuiTextures.SHORTISH_ARROW.render(graphics, 107, 121);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 47, 3);
        if (!recipe.getGasByproduct().isEmpty()) {
            AllGuiTextures.JEI_ARROW.render(graphics, 85, 24);
        }
        if (recipe.hotAirUsage > 0) {
            TFMGGuiTextures.DOWN_RIGHT_ARROW.render(graphics, 10, 90);
        }
    }
}
