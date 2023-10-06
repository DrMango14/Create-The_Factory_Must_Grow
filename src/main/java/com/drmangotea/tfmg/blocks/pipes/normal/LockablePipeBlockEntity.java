package com.drmangotea.tfmg.blocks.pipes.normal;


import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LockablePipeBlockEntity extends FluidPipeBlockEntity {

    public boolean locked = false;

    public LockablePipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void toggleLock(Player player){
        level.playSound(player, getBlockPos(), SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.3f,0.5f);
        locked = !locked;

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
