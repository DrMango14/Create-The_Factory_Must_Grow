package com.drmangotea.tfmg.blocks.electricity.lights;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.tfmg.blocks.electricity.base.WallMountBlock.FACING;
import static com.drmangotea.tfmg.blocks.electricity.lights.LightBulbBlock.LIGHT;


public class LightBulbBlockEntity extends ElectricBlockEntity {

    public LerpedFloat glow = LerpedFloat.linear();

    boolean shouldDestroy = false;
    boolean signalChanged;



    boolean hasSignal;

    public LightBulbBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    public int FECapacity() {
        return 500;
    }

    @Override
    public boolean outputAllowed() {
        return false;
    }



    @Override
    public void tick() {
        super.tick();

        getOrCreateElectricNetwork().requestEnergy(this);

        if(!hasSignal&&energy.getEnergyStored()!=0) {
            glow.chase(getVoltage(), 0.4, LerpedFloat.Chaser.EXP);
            glow.tickChaser();

            if(getVoltage()!=0)
                useEnergy(1);

            if ((int) Math.min(getVoltage() / 10, 15) != getBlockState().getValue(LIGHT))
                level.setBlock(getBlockPos(), getBlockState().setValue(LIGHT, (int) Math.min(getVoltage() / 10, 15)), 2);
        }else {
            if (getBlockState().getValue(LIGHT)!=0)
                level.setBlock(getBlockPos(), getBlockState().setValue(LIGHT, 0), 2);
            glow.chase(0, 0.4, LerpedFloat.Chaser.EXP);
            glow.tickChaser();

        }

        if (signalChanged) {
            signalChanged = false;
            analogSignalChanged(level.getBestNeighborSignal(worldPosition));
        }

    }

    public void neighbourChanged() {
        if (!hasLevel())
            return;
        boolean powered = level.getBestNeighborSignal(worldPosition)>0;
        if (powered != hasSignal)
            signalChanged = true;
    }
    @Override
    public void lazyTick() {
        super.lazyTick();
        neighbourChanged();


    }

    protected void analogSignalChanged(int newSignal) {

            hasSignal = newSignal > 0;

    }

    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.LIGHT_BULB.get());
    }



    @Override
    public void explode() {

        //level.setBlock(getBlockPos(), Blocks.GRANITE.defaultBlockState(),3);
        level.setBlock(getBlockPos(), TFMGBlocks.LIGHT_BULB.getDefaultState().setValue(FACING,getBlockState().getValue(FACING).getOpposite()),3);


        destroy();

        network = Long.MAX_VALUE-88888;

        needsNetworkUpdate();
        needsVoltageUpdate();


        level.playSound(null, getBlockPos(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 2.0F,
                level.random.nextFloat() * 0.4F + 0.8F);

        level.destroyBlock(getBlockPos(),false);


        sendStuff();
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite();
    }



}
