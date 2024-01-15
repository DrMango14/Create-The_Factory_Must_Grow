package com.drmangotea.createindustry.blocks.engines.compact;


import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class CompactEngineRenderer extends KineticBlockEntityRenderer<CompactEngineBlockEntity> {

    public CompactEngineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(CompactEngineBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state);
    }

}