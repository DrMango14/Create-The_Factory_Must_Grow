package com.drmangotea.tfmg.content.machinery.vat.base.registry.functions;

import com.drmangotea.tfmg.content.machinery.vat.base.registry.types.VatType;
import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface DrawableVatType {
    void draw(VatType vatType, GuiGraphics graphics, double mouseX, double mouseY);
}
