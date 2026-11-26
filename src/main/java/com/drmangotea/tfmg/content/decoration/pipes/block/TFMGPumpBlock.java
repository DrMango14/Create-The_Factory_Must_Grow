package com.drmangotea.tfmg.content.decoration.pipes.block;


import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipes;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TFMGPumpBlock extends PumpBlock {
    public final TFMGPipes.PipeMaterial material;

    public TFMGPumpBlock(Properties properties, TFMGPipes.PipeMaterial material) {
        super(properties);
        this.material = material;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource r) {
        super.tick(state, world, pos, r);
        withBlockEntityDo(world, pos, PumpBlockEntity::updatePressureChange);
    }

    @Override
    public BlockEntityType<? extends PumpBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.TFMG_MECHANICAL_PUMP.get();
    }
}
