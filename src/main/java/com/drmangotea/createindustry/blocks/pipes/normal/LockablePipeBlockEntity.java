package com.drmangotea.createindustry.blocks.pipes.normal;


import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Map;

public class LockablePipeBlockEntity extends FluidPipeBlockEntity {

    public boolean locked = false;

    public LockablePipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void toggleLock(Player player){
        level.playSound(player, getBlockPos(), SoundEvents.COPPER_STEP, SoundSource.BLOCKS, 0.3f,0.5f);
        locked = !locked;
        if(locked)
            return;

        BlockState newState;
        Level world = level;
        BlockPos pos = getBlockPos();
        FluidTransportBehaviour.cacheFlows(world, pos);
        newState = updatePipe(world, pos, getBlockState()).setValue(BlockStateProperties.WATERLOGGED, getBlockState().getValue(BlockStateProperties.WATERLOGGED));
        world.setBlock(pos, newState, 3);
        FluidTransportBehaviour.loadFlows(world, pos);
    }
    public BlockState updatePipe(LevelAccessor world, BlockPos pos, BlockState state) {
        Direction side = Direction.UP;
        Map<Direction, BooleanProperty> facingToPropertyMap = FluidPipeBlock.PROPERTY_BY_DIRECTION;
        return AllBlocks.FLUID_PIPE.get()
                .updateBlockState(state.getBlock().defaultBlockState()
                        .setValue(facingToPropertyMap.get(side), true)
                        .setValue(facingToPropertyMap.get(side.getOpposite()), true), side, null, world, pos);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {

        compound.putBoolean("Locked", locked);

        super.write(compound, clientPacket);

    }
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);


        locked = compound.getBoolean("Locked");

    }
}
