package com.drmangotea.tfmg.recipes.jei.machines;


import com.drmangotea.tfmg.content.electricity.utilities.polarizer.PolarizerBlock;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;

public class WindingMachine extends AnimatedKinetics {
    public WindingMachine() {
    }

    public void draw(GuiGraphics graphics, int xOffset, int yOffset, PartialModel coil, boolean shadow) {

        PoseStack matrixStack = graphics.pose();

        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 0.0);
        if (shadow)
            AllGuiTextures.JEI_SHADOW.render(graphics, -16, 13);
        matrixStack.translate(-2.0, 18.0, 0.0);
        int scale = 22;
        GuiGameElement.of(TFMGBlocks.WINDING_MACHINE.getDefaultState().setValue(PolarizerBlock.FACING, Direction.NORTH))
                .rotateBlock(22.5, 22.5, 0.0)
                .scale(scale)
                .render(graphics);
        blockElement(AllPartialModels.SHAFT_HALF)
                .rotateBlock(22.5, 22.5 + 270, 0.0)
                .scale(scale)
                .render(graphics);


        blockElement(TFMGPartialModels.SPOOL)
                .rotateBlock(22.5, 22.5, 0)
                .atLocal(-0.15, -0.4, -0.23)
                .scale(scale)
                .render(graphics);
        if (coil != null)
            blockElement(coil)
                    .rotateBlock(22.5, 22.5, 0)
                    .atLocal(-0.15, -0.4, -0.23)
                    .scale(scale)
                    .render(graphics);

        matrixStack.popPose();
    }


    @Override
    public void draw(GuiGraphics guiGraphics, int i, int i1) {

    }
}