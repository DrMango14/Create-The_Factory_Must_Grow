package com.drmangotea.tfmg.content.engines.types.radial_engine;

import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.EngineType;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGEngineTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import static com.drmangotea.tfmg.content.engines.base.EngineBlock.ENGINE_STATE;
import static com.drmangotea.tfmg.content.engines.base.EngineBlock.EngineState.*;
import static com.drmangotea.tfmg.content.engines.base.EngineBlock.SHAFT_FACING;

public class RadialEngineBlockEntity extends RegularEngineBlockEntity {
    public RadialEngineBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);

    }
    @Override
    public boolean canGenerateSpeed() {
        return true;
    }

    @Override
    public boolean hasTwoShafts() {
        return engineLength()>1;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos()).inflate(1);
    }

    @Override
    public EngineType getDefaultEngineType() {
        return TFMGEngineTypes.RADIAL.get();
    }

    @Override
    public boolean canConnect(AbstractSmallEngineBlockEntity candidate) {
        if (candidate instanceof RadialEngineBlockEntity radialEngine) {
            return radialEngine.type == this.type;
        }
        return false;
    }

    public void setBlockStates(AbstractSmallEngineBlockEntity be, BlockPos last) {
		if (level == null) return;

        Direction facing = getBlockState().getValue(SHAFT_FACING).getOpposite();
		Block thisBlock = this.getBlockState().getBlock();

        if (level.getBlockState(getBlockPos().relative(facing)).getBlock() != thisBlock &&
			level.getBlockState(getBlockPos().relative(facing.getOpposite())).getBlock() != thisBlock
		) {
            level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(ENGINE_STATE, SINGLE), 2);
            return;
        }

        if (last!=null) {
            level.setBlock(last, level.getBlockState(last).setValue(ENGINE_STATE, BACK), 2);
            return;
        }

        if (be.isController()) {
            level.setBlock(be.getBlockPos(), be.getBlockState().setValue(ENGINE_STATE, SHAFT), 2);
        } else {
            level.setBlock(be.getBlockPos(), be.getBlockState().setValue(SHAFT_FACING, facing).setValue(ENGINE_STATE, NORMAL), 2);
        }

    }
}
