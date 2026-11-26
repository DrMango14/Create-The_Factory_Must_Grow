package com.drmangotea.tfmg.content.machinery.vat.base.registry.functions;

import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public interface VatOperationDescriptor {

    void draw(VatMachineRecipe recipe, Consumer<Component> tooltip, double mouseX, double mouseY);
}
