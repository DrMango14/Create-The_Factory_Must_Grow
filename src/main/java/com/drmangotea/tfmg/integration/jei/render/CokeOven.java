package com.drmangotea.tfmg.integration.jei.render;


import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CokeOven extends AnimatedKinetics {
    public CokeOven() {}

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale =  23;
		
		BlockState defaultSouth = TFMGBlocks.COKE_OVEN.getDefaultState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);

        blockElement(defaultSouth)
                .scale(scale)
                .render(graphics);
        blockElement(defaultSouth)
                .atLocal(0,0,1)
                .scale(scale)
                .render(graphics);
        blockElement(defaultSouth)
                .atLocal(0,0,2)
                .scale(scale)
                .render(graphics);
        blockElement(defaultSouth)
                .atLocal(0,1,0)
                .scale(scale)
                .render(graphics);
        blockElement(defaultSouth)
                .atLocal(0,1,1)
                .scale(scale)
                .render(graphics);
        blockElement(defaultSouth)
                .atLocal(0,1,2)
                .scale(scale)
                .render(graphics);
        blockElement(defaultSouth)
                .atLocal(0,-1,0)
                .scale(scale)
                .render(graphics);
        blockElement(defaultSouth)
                .atLocal(0,-1,1)
                .scale(scale)
                .render(graphics);
        blockElement(defaultSouth)
                .atLocal(0,-1,2)
                .scale(scale)
                .render(graphics);

        matrixStack.scale(scale, -scale, scale);
        matrixStack.translate(0, -1.8, 0);
        matrixStack.popPose();
    }
}
