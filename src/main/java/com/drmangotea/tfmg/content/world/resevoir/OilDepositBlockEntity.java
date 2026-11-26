package com.drmangotea.tfmg.content.world.resevoir;

import com.drmangotea.tfmg.registry.TFMGDataAttachments;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.List;

public class OilDepositBlockEntity extends SmartBlockEntity {

    public OilDepositBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public void tick() {
        super.tick();
        if (level == null) return;
        ChunkAccess chunk = level.getChunk(worldPosition);
        if (!getOrCreateReservoir(chunk).contains(worldPosition)) {
            FluidReservoir.addToReservoir(chunk, worldPosition);
        }
    }

    public FluidReservoir getOrCreateReservoir(ChunkAccess chunk) {
        if (!chunk.hasData(TFMGDataAttachments.FLUID_RESERVOIR)) {
            FluidReservoir.createReservoir(chunk, worldPosition, level.getRandom());
        }
        return chunk.getData(TFMGDataAttachments.FLUID_RESERVOIR);
    }
}
