package com.drmangotea.createindustry.blocks.electricity.lights;

import com.drmangotea.createindustry.blocks.electricity.base.ElectricBlockEntity;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.createindustry.blocks.electricity.base.WallMountBlock.FACING;
import static com.drmangotea.createindustry.blocks.electricity.lights.LightBulbBlock.LIGHT;

public class LightBulbBlockEntity extends ElectricBlockEntity {

    public LerpedFloat glow = LerpedFloat.linear();


    boolean signalChanged;

    boolean hasSignal;

    public LightBulbBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }



    @Override
    public void tick() {
        super.tick();



        if(!hasSignal&&energy.getEnergyStored()!=0) {
            glow.chase(voltage, 0.4, LerpedFloat.Chaser.EXP);
            glow.tickChaser();

            if(voltage!=0)
                useEnergy(1);

            if ((int) Math.min(voltage / 10, 15) != getBlockState().getValue(LIGHT))
                level.setBlock(getBlockPos(), getBlockState().setValue(LIGHT, (int) Math.min(voltage / 10, 15)), 2);
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
    public void explode() {



        level.playSound(null, getBlockPos(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 2.0F,
                level.random.nextFloat() * 0.4F + 0.8F);

        level.destroyBlock(getBlockPos(),false);




        sendStuff();


     //   level.explode(null, getBlockPos().getX()+0.5, getBlockPos().getY()+0.5, getBlockPos().getZ()+0.5, 0.1f, Explosion.BlockInteraction.BREAK);

       // TFMGUtils.createFireExplosion(level, getBlockPos(),5,.1f);
 }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite();
    }



}
