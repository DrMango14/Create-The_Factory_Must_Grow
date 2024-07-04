package com.drmangotea.createindustry.blocks;


import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class HalfShaftRenderer<T extends KineticBlockEntity> extends KineticBlockEntityRenderer<T> {

public HalfShaftRenderer(BlockEntityRendererProvider.Context context) {
        super(context);

        }


@Override
protected SuperByteBuffer getRotatedModel(T be, BlockState state) {
        return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state, state
        .getValue(DirectionalKineticBlock.FACING));}



        }