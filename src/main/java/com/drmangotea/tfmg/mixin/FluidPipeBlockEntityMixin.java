package com.drmangotea.tfmg.mixin;

import com.drmangotea.tfmg.registry.TFMGDataAttachments;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FluidPipeBlockEntity.class)
public abstract class FluidPipeBlockEntityMixin extends SmartBlockEntity {

    public FluidPipeBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //Remap legacy pipes
    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries , clientPacket);
        if (compound.getBoolean("Locked")) {
            setData(TFMGDataAttachments.LOCKED_PIPE, true);
            compound.remove("Locked");
        }
    }
}
