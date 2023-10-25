package com.drmangotea.tfmg.recipes.jei.machines;


import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;

public class CastingSetup extends AnimatedKinetics {



    public CastingSetup() {}

    @Override
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
        int scale =  23;



        blockElement(TFMGBlocks.CASTING_SPOUT.getDefaultState())
                .scale(scale)
                .render(matrixStack);
        blockElement(TFMGBlocks.CASTING_BASIN.getDefaultState())
                .atLocal(0,2,0)
                .scale(scale)
                .render(matrixStack);
        blockElement(TFMGPartialModels.CASTING_SPOUT_BOTTOM)
                .scale(scale)
                .render(matrixStack);
        blockElement(TFMGPartialModels.CASTING_SPOUT_CONNECTOR)
                .scale(scale)
                .render(matrixStack);

        matrixStack.scale(scale, -scale, scale);
        matrixStack.translate(0, -1.8, 0);

        matrixStack.popPose();
    }



}
