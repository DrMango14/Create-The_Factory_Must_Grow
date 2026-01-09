package com.drmangotea.tfmg.content.electricity.connection.copycat_cable;

import com.drmangotea.tfmg.content.electricity.base.*;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatCableBlockEntity extends CopycatBlockEntity implements IElectric, IHaveHoveringInformation {

    public ElectricBlockValues data = new ElectricBlockValues(getPos());

    public CopycatCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        data.connectNextTick = true;

    }

    @Override
    public LevelAccessor getLevelAccessor() {
        return level;
    }




    @Override
    public void lazyTick() {
        super.lazyTick();
        lazyTickElectricity();
    }

    @Override
    public ElectricBlockValues getData() {
        return data;
    }





    @Override
    public void sendStuff() {
        sendData();
    }


    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public void remove() {
        super.remove();
       onRemoved();
    }

    @Override
    public void tick() {
        super.tick();
       tickElectricity();
    }



    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        // Validate Material
        if (getMaterial() != null && !clientPacket) {
            BlockState blockState = getBlockState();
            if (blockState == null)
                return;
            if (!(blockState.getBlock() instanceof CopycatBlock cb))
                return;
            BlockState acceptedBlockState = cb.getAcceptedBlockState(level, worldPosition, ItemStack.EMPTY, null);
            if (acceptedBlockState != null && getMaterial().is(acceptedBlockState.getBlock()))
                return;
            setMaterial(AllBlocks.COPYCAT_BASE.getDefaultState());
        }

        //
        readElectricity(compound,clientPacket);
    }
}
