package com.drmangotea.tfmg.recipes.jei.machines;


import com.drmangotea.tfmg.content.electricity.utilities.polarizer.PolarizerBlock;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;

public class Polarizer extends AnimatedKinetics {
    public Polarizer() {
    }
    
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {

        PoseStack matrixStack = graphics.pose();

        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 0.0);
        AllGuiTextures.JEI_SHADOW.render(graphics, -16, 13);
        matrixStack.translate(-2.0, 18.0, 0.0);
        int scale = 22;

        GuiGameElement.of(TFMGBlocks.POLARIZER.getDefaultState().setValue(PolarizerBlock.FACING, Direction.SOUTH)).rotateBlock(22.5, 22.5, 0.0).scale(scale).render(graphics);
        matrixStack.popPose();
    }
}