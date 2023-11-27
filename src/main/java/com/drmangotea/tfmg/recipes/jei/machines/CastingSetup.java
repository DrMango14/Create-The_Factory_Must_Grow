package com.drmangotea.tfmg.recipes.jei.machines;


import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.block.Blocks;
import org.joml.Vector3f;

public class CastingSetup extends AnimatedKinetics {



    public CastingSetup() {}

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale =  23;



        blockElement(TFMGBlocks.CASTING_SPOUT.getDefaultState())
                .scale(scale)
                .render(graphics);
        blockElement(TFMGBlocks.CASTING_BASIN.getDefaultState())
                .atLocal(0,2,0)
                .scale(scale)
                .render(graphics);
        blockElement(TFMGPartialModels.CASTING_SPOUT_BOTTOM)
                .scale(scale)
                .render(graphics);
        blockElement(TFMGPartialModels.CASTING_SPOUT_CONNECTOR)
                .scale(scale)
                .render(graphics);

        matrixStack.scale(scale, -scale, scale);
        matrixStack.translate(0, -1.8, 0);

        matrixStack.popPose();
    }



}
