package com.drmangotea.tfmg.content.electricity.base;

import com.drmangotea.tfmg.content.engines.types.regular_engine.RegularEngineBlockEntity;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class KineticElectricBlockEntity extends GeneratingKineticBlockEntity implements IElectric, IHaveGoggleInformation, IHaveHoveringInformation {

    public ElectricBlockValues data = new ElectricBlockValues(getPos());

    public KineticElectricBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
        readElectricity(compound,clientPacket);
    }


    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);

        if (this instanceof RegularEngineBlockEntity)
            notifyNetworkAboutSpeedChange();


    }

    public void notifyNetworkAboutSpeedChange() {
        updateNextTick();
    }
}
