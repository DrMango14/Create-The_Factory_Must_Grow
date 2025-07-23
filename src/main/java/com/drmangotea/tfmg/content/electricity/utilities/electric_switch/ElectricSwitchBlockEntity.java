package com.drmangotea.tfmg.content.electricity.utilities.electric_switch;

import com.drmangotea.tfmg.content.electricity.utilities.diode.ElectricDiodeBlockEntity;
import com.drmangotea.tfmg.registry.TFMGSoundEvents;
import com.simibubi.create.Create;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.DirectionalBlock.FACING;

public class ElectricSwitchBlockEntity extends ElectricDiodeBlockEntity {

    int signal;
    boolean signalChanged;

    public ElectricSwitchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        neighbourChanged();


    }

    @Override
    public void tick() {
        super.tick();

        if (signalChanged) {
            signalChanged = false;
            analogSignalChanged(level.getBestNeighborSignal(worldPosition));
        }
    }
    protected void analogSignalChanged(int newSignal) {
        signal = newSignal;


        updateInFrontNextTick();
        updateNextTick();

        if (level.isClientSide()) {

            if (signal == 0)
                TFMGSoundEvents.SWITCH_OFF.playAt(level, worldPosition, 0.8f + Create.RANDOM.nextFloat() * 0.4f, 0.8f + Create.RANDOM.nextFloat() * 0.4f, false);
            else
                TFMGSoundEvents.SWITCH_ON.playAt(level, worldPosition, 0.8f + Create.RANDOM.nextFloat() * 0.4f, 0.8f + Create.RANDOM.nextFloat() * 0.4f, false);
        }

    }

    public void neighbourChanged() {
        if (!hasLevel())
            return;
        int power = level.getBestNeighborSignal(worldPosition);
        if (power != signal)
            signalChanged = true;
    }

    @Override
    public int getOutputVoltage() {
        return (int) (data.getVoltage()*(signal/15f));
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite();
    }
}
