package com.drmangotea.createindustry.recipes.jei.machines;


import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.world.level.block.state.properties.WallSide;

import static net.minecraft.world.level.block.WallBlock.*;

public class BlastFurnace extends AnimatedKinetics {



    public BlastFurnace() {}

    @Override
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
        int scale =  23;
        BlazeBurnerBlock.HeatLevel heatLevel = BlazeBurnerBlock.HeatLevel.SMOULDERING;


        blockElement(TFMGBlocks.BLAST_FURNACE_OUTPUT.getDefaultState())
                .scale(scale)
                .render(matrixStack);
        blockElement(TFMGBlocks.FIREPROOF_BRICKS.getDefaultState())
                .atLocal(0,0,-1)
                .scale(scale)
                .render(matrixStack);

        for(int i = 0; i<4;i++) {
            if(i !=0)
                blockElement(TFMGBlocks.FIREPROOF_BRICKS.getDefaultState())
                        .atLocal(0, -i, 0)
                        .scale(scale)
                        .render(matrixStack);
            blockElement(TFMGBlocks.FIREPROOF_BRICKS.getDefaultState())
                    .atLocal(0, -i, -2)
                    .scale(scale)
                    .render(matrixStack);
            blockElement(TFMGBlocks.FIREPROOF_BRICKS.getDefaultState())
                    .atLocal(-1, -i, -1)
                    .scale(scale)
                    .render(matrixStack);
            blockElement(TFMGBlocks.FIREPROOF_BRICKS.getDefaultState())
                    .atLocal(1, -i, -1)
                    .scale(scale)
                    .render(matrixStack);

            if(i!=3){

                blockElement(TFMGBlocks.FIREPROOF_BRICK_REINFORCEMENT.getDefaultState().setValue(NORTH_WALL, WallSide.TALL).setValue(WEST_WALL, WallSide.TALL))
                        .atLocal(1, -i, 0)
                        .scale(scale)
                        .render(matrixStack);
                blockElement(TFMGBlocks.FIREPROOF_BRICK_REINFORCEMENT.getDefaultState().setValue(SOUTH_WALL, WallSide.TALL).setValue(WEST_WALL, WallSide.TALL))
                        .atLocal(1, -i, -2)
                        .scale(scale)
                        .render(matrixStack);
                blockElement(TFMGBlocks.FIREPROOF_BRICK_REINFORCEMENT.getDefaultState().setValue(NORTH_WALL, WallSide.TALL).setValue(EAST_WALL, WallSide.TALL))
                        .atLocal(-1, -i, 0)
                        .scale(scale)
                        .render(matrixStack);
                blockElement(TFMGBlocks.FIREPROOF_BRICK_REINFORCEMENT.getDefaultState().setValue(SOUTH_WALL, WallSide.TALL).setValue(EAST_WALL, WallSide.TALL))
                        .atLocal(-1, -i, -2)
                        .scale(scale)
                        .render(matrixStack);

            }

        }


        matrixStack.scale(scale, -scale, scale);
        matrixStack.translate(0, -1.8, 0);
        matrixStack.popPose();
    }



}
