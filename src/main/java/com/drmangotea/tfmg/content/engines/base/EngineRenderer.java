package com.drmangotea.tfmg.content.engines.base;

import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.tfmg.content.engines.base.EngineBlock.ENGINE_STATE;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class EngineRenderer extends KineticBlockEntityRenderer<AbstractSmallEngineBlockEntity> {
    public EngineRenderer(BlockEntityRendererProvider.Context context) {

        super(context);
    }

    @Override
    protected void renderSafe(AbstractSmallEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {


        if(be.hasUpgrade()){
            be.upgrade.get().render(be, partialTicks, ms, buffer,light);
        }


        if(be.getBlockState().getValue(ENGINE_STATE) == EngineBlock.EngineState.SHAFT)
            super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(AbstractSmallEngineBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, state,state.getValue(HORIZONTAL_FACING));
    }
}
