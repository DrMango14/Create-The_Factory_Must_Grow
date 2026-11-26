package com.drmangotea.tfmg.content.decoration.pipes.rendering;

import com.drmangotea.tfmg.content.decoration.pipes.block.TFMGPumpBlock;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class TFMGMechanicalPumpRenderer extends KineticBlockEntityRenderer<PumpBlockEntity> {

    public TFMGMechanicalPumpRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(PumpBlockEntity be, BlockState state) {
        PartialModel partial = AllPartialModels.MECHANICAL_PUMP_COG;
        if (state.getBlock() instanceof TFMGPumpBlock pumpBlock) {
            partial = TFMGPartialModels.PUMP_COGS.get(pumpBlock.material);
        }
        return CachedBuffers.partialFacing(partial, state);
    }
}
