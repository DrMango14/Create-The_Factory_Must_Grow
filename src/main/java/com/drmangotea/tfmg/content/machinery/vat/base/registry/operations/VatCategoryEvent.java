package com.drmangotea.tfmg.content.machinery.vat.base.registry.operations;

import com.drmangotea.tfmg.content.machinery.vat.base.registry.functions.DrawableVatOperation;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.functions.DrawableVatType;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.functions.VatOperationDescriptor;
import com.drmangotea.tfmg.content.machinery.vat.base.registry.types.VatType;
import com.drmangotea.tfmg.recipes.VatMachineRecipe;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class VatCategoryEvent extends Event implements IModBusEvent {
    private static final Map<VatOperation, DrawableVatOperation> RENDERED_OPERATIONS = new HashMap<>();
    private static final Map<VatType, DrawableVatType> RENDERED_VAT_TYPES = new HashMap<>();
    private static final Map<VatOperation, VatOperationDescriptor> OPERATION_TOOLTIPS = new HashMap<>();

    public VatCategoryEvent() {}

    public void addDrawableOperation(VatOperation operation, DrawableVatOperation drawable) {
        RENDERED_OPERATIONS.put(operation, drawable);
    }

    public void addDrawableVatType(VatType type, DrawableVatType drawable) {
        RENDERED_VAT_TYPES.put(type, drawable);
    }

    public void addOperationTooltip(VatOperation operation, VatOperationDescriptor tooltip) {
        OPERATION_TOOLTIPS.put(operation, tooltip);
    }

    public static void drawOperation(VatOperation operation, VatMachineRecipe recipe, GuiGraphics graphics, double mouseX, double mouseY) {
        DrawableVatOperation drawable = RENDERED_OPERATIONS.get(operation);
        if (drawable != null) {
            drawable.draw(recipe, graphics, mouseX, mouseY);
        }
    }

    public static void drawVatTypes(List<VatType> vatTypes, GuiGraphics graphics, double mouseX, double mouseY) {
        if (vatTypes.isEmpty()) return;
        // Filter by the rendered vat types to prevent crashes
        // When a type is not registered
        List<VatType> drawableTypes = vatTypes.stream()
                .filter(RENDERED_VAT_TYPES::containsKey)
                .toList();
        if (drawableTypes.isEmpty()) return;
        // Cycle every 2 seconds
        int index = (int) ((Util.getMillis() / 2000) % drawableTypes.size());
        VatType vatType = drawableTypes.get(index);
        RENDERED_VAT_TYPES.get(vatType).draw(vatType, graphics, mouseX, mouseY);
    }

    public static void addDescriptor(VatOperation operation, VatMachineRecipe recipe, Consumer<Component> tooltip, double mouseX, double mouseY) {
        VatOperationDescriptor descriptor = OPERATION_TOOLTIPS.get(operation);
        if (descriptor != null) {
            descriptor.draw(recipe, tooltip, mouseX, mouseY);
        }
    }
}
