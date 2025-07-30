package com.drmangotea.tfmg.content.machinery.vat.electrode_holder;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

public class ElectrodeHolderRenderer extends SafeBlockEntityRenderer<ElectrodeHolderBlockEntity> {

    private final ItemRenderer itemRenderer;

    public ElectrodeHolderRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    protected void renderSafe(ElectrodeHolderBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {


        BlockState blockState = be.getBlockState();


        if (be.electrode == TFMGUtils.getElectrode(TFMG.asResource("none")))
            return;
        if (be.electrode.getStack().isEmpty())
            return;
        if (be.getLevel() == null)
            return;
        ms.pushPose();
        ms.mulPose(Axis.XP.rotationDegrees(0));
        ms.translate(0.5, -1.4369, 0.5);
        ms.scale(3.33f, 3.33f, 3.33f);
        itemRenderer.renderStatic(be.electrode.getStack(), ItemDisplayContext.GROUND, LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().below()), overlay, ms, buffer, be.getLevel(), 0);
        ms.popPose();
    }

}



