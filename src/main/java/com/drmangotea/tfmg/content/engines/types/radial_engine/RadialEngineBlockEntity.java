package com.drmangotea.tfmg.content.engines.types.radial_engine;

import com.drmangotea.tfmg.content.engines.base.AbstractEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import static com.drmangotea.tfmg.content.engines.base.EngineBlock.ENGINE_STATE;
import static com.drmangotea.tfmg.content.engines.base.EngineBlock.EngineState.*;
import static com.drmangotea.tfmg.content.engines.base.EngineBlock.SHAFT_FACING;
import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class RadialEngineBlockEntity extends RegularEngineBlockEntity {

    public RadialEngineBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);

    }
    @Override
    public boolean canGenerateSpeed() {
        return true;
    }
    //@Override
    //public float getGeneratedSpeed() {
//
    //    float speed;
//
    //    if (hasLevel())
//
    //        if (level.getBlockEntity(controller) instanceof AbstractEngineBlockEntity controller) {
    //            if (controller.fuelTank.isEmpty())
    //                return 0;
    //            if (!controller.canWork())
    //                return 0;
    //            speed = rpm / 40;
    //            if (reverse)
    //                speed = speed * -1;
//
    //            return convertToDirection(Math.min((int) speed, 256), getBlockState().getValue(HORIZONTAL_FACING));
    //        }
    //    return 0;
    //}

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
        return EngineType.RADIAL;
    }



    public void setBlockStates(AbstractSmallEngineBlockEntity be, BlockPos last) {

        Direction facing = getBlockState().getValue(SHAFT_FACING).getOpposite();

        if(level.getBlockState(getBlockPos().relative(facing)).getBlock()!=this.getBlockState().getBlock()&&level.getBlockState(getBlockPos().relative(facing.getOpposite())).getBlock()!=this.getBlockState().getBlock()){
            level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(ENGINE_STATE, SINGLE), 2);
            return;
        }

        //if(level.getBlockState(getBlockPos().relative(facing.getOpposite())).getBlock()!=this.getBlockState().getBlock()&&isController()){
        //    level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(ENGINE_STATE, SINGLE), 2);
        //    return;
        //}

        if(last!=null){
            level.setBlock(last, level.getBlockState(last).setValue(ENGINE_STATE, BACK), 2);
            return;
        }

        if (be.isController()) {
            level.setBlock(be.getBlockPos(), be.getBlockState().setValue(ENGINE_STATE, SHAFT), 2);
        } else {
            level.setBlock(be.getBlockPos(), be.getBlockState().setValue(SHAFT_FACING, getBlockState().getValue(SHAFT_FACING).getOpposite()).setValue(ENGINE_STATE, NORMAL), 2);
        }

    }
}
