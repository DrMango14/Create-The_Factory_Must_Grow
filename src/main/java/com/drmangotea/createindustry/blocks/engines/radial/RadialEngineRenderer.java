package com.drmangotea.createindustry.blocks.engines.radial;


import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class RadialEngineRenderer extends KineticBlockEntityRenderer<RadialEngineBlockEntity> {

    public RadialEngineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(RadialEngineBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(AllPartialModels.COGWHEEL_SHAFT, state);
    }

}