package com.drmangotea.tfmg.integration.jei.category;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.pressure.Pressure;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatCategoryEvent;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.operations.VatOperation;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.types.VatType;
import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import com.drmangotea.tfmg.recipes.VatRecipeParams;
import com.drmangotea.tfmg.registry.TFMGGuiTextures;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.item.ItemHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.createmod.catnip.data.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;

@NothingNullByDefault
public class ChemicalVatCategory extends CreateRecipeCategory<VatMachineRecipe> {
    // Cheap way to do this
    private static final int[] PRESSURE_ANGLES = {
            0,   // 0
            15,  // 1
            25,  // 2
            35,  // 3
            40,  // 4
            48,  // 5
            57,  // 6
            70,  // 7
            78,  // 8
            90   // 9
    };

    private static final float WOBBLE_AMPLITUDE = 2f;
    private static final float WOBBLE_SPEED = 0.02f;

    public ChemicalVatCategory(Info<VatMachineRecipe> info) {
        super(info);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, VatMachineRecipe recipe, IFocusGroup focuses) {
        int fluidCount = recipe.getFluidIngredients().size();
        int pos = 55;
        int width = ((fluidCount) * 20) / 2;
        int movement = fluidCount != 4 ? 1 : 0;
        if (fluidCount == 1)
            movement = 2;
        for (int i = 0; i < fluidCount; i++) {

            addFluidSlot(builder, pos - width + movement, recipe.getIngredients().isEmpty() ? 72 : 85, recipe.getFluidIngredients().get(i));

            pos += 21;
        }
        List<Pair<Ingredient, MutableInt>> condensedIngredients = ItemHelper.condenseIngredients(recipe.getIngredients());

        int itemCount = condensedIngredients.size();
        int itemPos = 55;
        int itemWidth = ((itemCount) * 20) / 2;
        int itemMovement = itemCount != 4 ? 1 : 0;
        if (itemCount == 1)
            itemMovement = 2;
        for (Pair<Ingredient, MutableInt> pair : condensedIngredients) {
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack itemStack : pair.getFirst().getItems()) {
                ItemStack copy = itemStack.copy();
                copy.setCount(pair.getSecond().getValue());
                stacks.add(copy);
            }
            builder.addSlot(RecipeIngredientRole.INPUT, itemPos - itemWidth + itemMovement, recipe.getFluidIngredients().isEmpty() ? 72 : 64).setBackground(getRenderedSlot(), -1, -1).addItemStacks(stacks);

            itemPos += 21;
        }
        /////////////////////////////

        int fluidResultPos = 106;

        for (int i = 0; i < recipe.getFluidResults().size(); i++) {

            addFluidSlot(builder, 150, fluidResultPos, recipe.getFluidResults().get(i));

            fluidResultPos -= 21;
        }

        int itemResultPos = 106;

        for (int i = 0; i < recipe.getRollableResults().size(); i++) {
            ProcessingOutput output = recipe.getRollableResults().get(i);
            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, 128, itemResultPos)
                    .setBackground(getRenderedSlot(output), -1, -1)
                    .addItemStack(output.getStack())
                    .addRichTooltipCallback(addStochasticTooltip(output))
            ;

            itemResultPos -= 21;
        }
    }

    //TODO: Add better visualisation of vat configuration required for a recipe
    public void draw(VatMachineRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        List<VatType> allowedVatTypes = recipe.allowedVatTypes.isEmpty() ? VatRecipeParams.allVatTypes() : recipe.allowedVatTypes;
        TFMGGuiTextures.VAT.render(graphics, 0, 24);
        VatCategoryEvent.drawVatTypes(allowedVatTypes, graphics, mouseX, mouseY);

        List<VatOperation> alreadyHandled = new ArrayList<>();

        for (VatOperation operation : recipe.machines) {
            if (alreadyHandled.contains(operation)) continue;
            VatCategoryEvent.drawOperation(operation, recipe, graphics, mouseX, mouseY);
            alreadyHandled.add(operation);
        }

        renderPressure(recipe.pressure, graphics);

        renderHeated(recipe.heatLevel, graphics);

        int pos = 55;
        int width = ((recipe.getFluidIngredients().size()) * 21) / 2;
        for (int i = 0; i < recipe.getFluidIngredients().size(); i++) {
            TFMGGuiTextures.SLOT.render(graphics, pos - width, recipe.getIngredients().isEmpty() ? 70 : 83);
            pos += 21;
        }
        int posItem = 55;
        List<Pair<Ingredient, MutableInt>> condensedIngredients = ItemHelper.condenseIngredients(recipe.getIngredients());
        int widthItem = ((condensedIngredients.size()) * 21) / 2;
        for (int i = 0; i < condensedIngredients.size(); i++) {
            TFMGGuiTextures.SLOT.render(graphics, posItem - widthItem, recipe.getFluidIngredients().isEmpty() ? 70 : 62);
            posItem += 21;
        }

        if (recipe.minSize > 1) {
            graphics.drawString(Minecraft.getInstance().font, TFMGLang.translate("recipe.vat.minsize", recipe.minSize).component(), 122, 45, 16579836);
        }
    }

    @Override
    public List<Component> getTooltipStrings(VatMachineRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltip = new ArrayList<>();
        int pressure = recipe.pressure.getValue();

        if (mouseY > -3 && mouseY < 43 && mouseX > 127 && mouseX < 170) {
            if(pressure != 0) {
                int colour = 0x5b6ee1;
                if (pressure > -8 && pressure < -3) colour = 0x5fcde4;
                if (pressure > -4 && pressure < 3) colour = 0x37946e;
                if (pressure > 2 && pressure < 8) colour = 0xd95763;
                if (pressure > 7 && pressure < 10) colour = 0x9b1c1c;

                tooltip.add(TFMGLang.translate("recipe.vat.pressure", pressure).component()
                        .withColor(colour));
            } else {
                tooltip.add(TFMGLang.translate("recipe.vat.pressure.none").component()
                        .withStyle(ChatFormatting.GRAY));
            }
        }

        boolean showHeat;
        if (recipe.heatLevel > 9) {
            showHeat = mouseY > 109 && mouseY < 118 && mouseX > 10 && mouseX < 100;
        } else if (recipe.heatLevel > 0) {
            showHeat = mouseY > 109 && mouseY < 118 && mouseX > 10 && mouseX < (recipe.heatLevel * 10) + 10;
        } else {
            showHeat = mouseY > 109 && mouseY < 118 && mouseX > 10 && mouseX < (Math.abs(recipe.heatLevel) * 10) + 10;
        }

        if (showHeat) {
            int colour = recipe.heatLevel <= 0 ? 0x9eccfb : recipe.heatLevel > 9 ? 0x425bf5 : 0xd17800;
            tooltip.add(TFMGLang.translate("recipe.vat.heat", recipe.heatLevel).component()
                    .withColor(colour));
        }

        List<VatOperation> alreadyHandled = new ArrayList<>();

        for (VatOperation operation : recipe.machines) {
            if (alreadyHandled.contains(operation)) continue;
            VatCategoryEvent.addDescriptor(operation, recipe, tooltip::add, mouseX, mouseY);
            alreadyHandled.add(operation);
        }

        return tooltip;
    }

    private void renderPressure(Pressure pressure, GuiGraphics graphics) {
        TFMGGuiTextures.VAT_BAROMETER.render(graphics, 128, 0);
        renderPressureBar(graphics, pressure.getValue());
    }

    private float getNeedleAngle(int pressure) {
        int abs = Math.abs(pressure);
        // 110 is area for above 9 kPa
        int magnitude = abs >= PRESSURE_ANGLES.length ? 110 : PRESSURE_ANGLES[abs];
        return pressure < 0 ? -magnitude : magnitude;
    }

    private void renderPressureBar(GuiGraphics graphics, int pressure) {
        int minX = 148;
        int minY = 10;
        int width = 2;
        int height = 16;

        float pivotX = minX + width / 2f;
        float pivotY = minY + height - 1f;

        float angleDegrees = getNeedleAngle(pressure);

        if (TFMGConfigs.client().ui.pressureNeedleWobble.get() && Math.abs(pressure) >= PRESSURE_ANGLES.length) {
            angleDegrees += getPeggedWobble();
        }

        PoseStack pose = graphics.pose();
        pose.pushPose();

        pose.translate(pivotX, pivotY, 0);
        pose.mulPose(Axis.ZP.rotationDegrees(angleDegrees));
        pose.translate(-pivotX, -pivotY, 0);

        graphics.fill(minX, minY, minX + width, minY + height, 0xFF000000);

        pose.popPose();
    }

    private float getPeggedWobble() {
        long time = Util.getMillis();
        float t = time * WOBBLE_SPEED;
        return (float) (Math.sin(t) * WOBBLE_AMPLITUDE + Math.sin(t * 2.7f) * (WOBBLE_AMPLITUDE * 0.3f));
    }

    private void renderHeated(int heatLevel, GuiGraphics graphics) {
        if(heatLevel != 0) {
            if (heatLevel > 9) {
                for (int i = 9; i > 0; i--) {
                    if (heatLevel >= 9 + i) {
                        TFMGGuiTextures.VAT_SUPERHEATER.render(graphics, i * 10, 109);
                    } else {
                        TFMGGuiTextures.VAT_HEATER.render(graphics, i * 10, 109);
                    }
                }
            } else if (heatLevel > 0) {
                for (int i = heatLevel; i > 0; i--) {
                    TFMGGuiTextures.VAT_HEATER.render(graphics, i * 10, 109);
                }
            } else {
                for (int i = -1 * heatLevel; i > 0; i--) {
                    TFMGGuiTextures.VAT_FREEZER.render(graphics, i * 10, 109);
                }
            }
        }
    }
}
