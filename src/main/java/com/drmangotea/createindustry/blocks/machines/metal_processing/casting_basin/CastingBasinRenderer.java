package com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin;


import com.drmangotea.createindustry.registry.TFMGPartialModels;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin.CastingBasinBlock.MOLD_TYPE;

public class CastingBasinRenderer extends SafeBlockEntityRenderer<CastingBasinBlockEntity> {

    public CastingBasinRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(CastingBasinBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {








        ms.pushPose();
        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());



        if(be.getBlockState().getValue(MOLD_TYPE)== CastingBasinBlockEntity.MoldType.INGOT)
            CachedBufferer.partial(TFMGPartialModels.INGOT_MOLD, blockState)
                    .light(light)
                    .renderInto(ms, vb);

        if(be.getBlockState().getValue(MOLD_TYPE)== CastingBasinBlockEntity.MoldType.BLOCK)
            CachedBufferer.partial(TFMGPartialModels.BlOCK_MOLD, blockState)
                    .light(light)
                    .renderInto(ms, vb);

        ms.popPose();

    }

}