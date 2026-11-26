package com.drmangotea.tfmg.content.machinery.vat.base.registry.functions;

import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface DrawableVatOperation {

    void draw(VatMachineRecipe recipe, GuiGraphics graphics, double mouseX, double mouseY);
}
