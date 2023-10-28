package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.machine_input;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;


    public class MachineInputRenderer extends KineticBlockEntityRenderer<MachineInputBlockEntity> {

        public MachineInputRenderer(BlockEntityRendererProvider.Context context) {
            super(context);

        }


        @Override
        protected SuperByteBuffer getRotatedModel(MachineInputBlockEntity be, BlockState state) {
            return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state, state
                    .getValue(MachineInputBlock.FACING));
        }
    }