package com.drmangotea.createindustry.recipes.jei.machines;

import com.drmangotea.createindustry.blocks.electricity.polarizer.PolarizerBlock;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import net.minecraft.core.Direction;

public class Polarizer extends AnimatedKinetics {
    public Polarizer() {
    }
    
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 0.0);
        AllGuiTextures.JEI_SHADOW.render(matrixStack, -16, 13);
        matrixStack.translate(-2.0, 18.0, 0.0);
        int scale = 22;
        GuiGameElement.of(TFMGBlocks.POLARIZER.getDefaultState().setValue(PolarizerBlock.FACING, Direction.NORTH)).rotateBlock(22.5, 22.5, 0.0).scale(scale).render(matrixStack);
        matrixStack.popPose();
    }
}
