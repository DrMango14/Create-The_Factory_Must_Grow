package com.drmangotea.tfmg.content.deposits;



import com.drmangotea.tfmg.CreateTFMG;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public class FluidDepositTileEntity extends SmartBlockEntity {

    public final int baseFluidAmount= Create.RANDOM.nextInt(300000000);
    public final int fluidAmountToBuckets =baseFluidAmount/1000;
    public int fluidAmount= fluidAmountToBuckets;

    public FluidDepositTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        CreateTFMG.LOGGER.debug("Created Oil Deposit with "+baseFluidAmount+" buckets of Crude Oil");
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {

        compound.putInt("FluidAmount", baseFluidAmount);

        super.write(compound, clientPacket);

    }
    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);


       fluidAmount = compound.getInt("FluidAmount");

    }
    public Fluid getDepositFluid(){
        return TFMGFluids.CRUDE_OIL.getSource();
    }

}
