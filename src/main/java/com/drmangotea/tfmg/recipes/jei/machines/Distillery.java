package com.drmangotea.tfmg.recipes.jei.machines;


import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;

public class Distillery extends AnimatedKinetics {



    public Distillery() {}

    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale =  23;
        BlazeBurnerBlock.HeatLevel heatLevel = BlazeBurnerBlock.HeatLevel.SMOULDERING;


        blockElement(TFMGBlocks.CAST_IRON_DISTILLATION_OUTPUT.getDefaultState())
                .scale(scale)
                .render(graphics);
        blockElement(TFMGBlocks.CAST_IRON_DISTILLATION_OUTPUT.getDefaultState())
                .atLocal(0,1,0)
                .scale(scale)
                .render(graphics);
        blockElement(TFMGBlocks.CAST_IRON_DISTILLATION_OUTPUT.getDefaultState())
                .atLocal(0,2,0)
                .scale(scale)
                .render(graphics);
        blockElement(TFMGBlocks.CAST_IRON_DISTILLATION_CONTROLLER.getDefaultState())
                .atLocal(0,3,0)
                .scale(scale)
                .render(graphics);

        float offset = (Mth.sin(AnimationTickHolder.getRenderTime() / 16f) + 0.5f) / 16f;

        blockElement(AllBlocks.BLAZE_BURNER.getDefaultState())
                .atLocal(0, 4.1, 0)
                .scale(scale)
                .render(graphics);

        PartialModel blaze =
                AllPartialModels.BLAZE_ACTIVE;
        PartialModel rods2 = AllPartialModels.BLAZE_BURNER_RODS_2;



        blockElement(blaze).atLocal(1, 4.1, 1)
                .rotate(0, 180, 0)
                .scale(scale)
                .render(graphics);
        blockElement(rods2).atLocal(1, 4.1 + offset, 1)
                .rotate(0, 180, 0)
                .scale(scale)
                .render(graphics);




        matrixStack.scale(scale, -scale, scale);
        matrixStack.translate(0, -1.8, 0);

        SpriteShiftEntry spriteShift =
                AllSpriteShifts.BURNER_FLAME;

        float spriteWidth = spriteShift.getTarget()
                .getU1()
                - spriteShift.getTarget()
                .getU0();

        float spriteHeight = spriteShift.getTarget()
                .getV1()
                - spriteShift.getTarget()
                .getV0();

        float time = AnimationTickHolder.getRenderTime(Minecraft.getInstance().level);
        float speed = 1 / 32f + 1 / 64f * BlazeBurnerBlock.HeatLevel.KINDLED.ordinal();

        double vScroll = speed * time;
        vScroll = vScroll - Math.floor(vScroll);
        vScroll = vScroll * spriteHeight / 2;

        double uScroll = speed * time / 2;
        uScroll = uScroll - Math.floor(uScroll);
        uScroll = uScroll * spriteWidth / 2;

        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers()
                .bufferSource();
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
        CachedBufferer.partial(AllPartialModels.BLAZE_BURNER_FLAME, Blocks.AIR.defaultBlockState())
                .shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll)
                .light(LightTexture.FULL_BRIGHT)
                .renderInto(matrixStack, vb);
        matrixStack.popPose();
    }



}
