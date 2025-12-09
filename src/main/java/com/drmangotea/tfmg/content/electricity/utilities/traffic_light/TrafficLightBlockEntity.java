package com.drmangotea.tfmg.content.electricity.utilities.traffic_light;

import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TrafficLightBlockEntity extends ElectricBlockEntity {

    protected ScrollValueBehaviour timerLength;

    public LerpedFloat glow = LerpedFloat.linear();

    int light = 0;

    public int timer = 180;
    public TrafficLightBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        timerLength = new TimerScrollBehaviour(CreateLang.translateDirect("traffic_light.timer"), this,
                new TrafficLightScrollSlot()).between(180, 60 * 20 * 60);
        timerLength.withFormatter(this::format);
        timerLength.withCallback(value-> timer = value);
        timerLength.setValue(2);
        behaviours.add(timerLength);


    }
    private String format(int value) {
        if (value < 60)
            return value + "t";
        if (value < 20 * 60)
            return (value / 20) + "s";
        return (value / 20 / 60) + "m";
    }


    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction.getAxis().isVertical();
    }

    @Override
    public float resistance() {
        return 175;
    }

    @Override
    public void tick() {
        super.tick();

        if(!level.isClientSide)
            return;
        if(timer>0) {
            timer--;
        }


        glow.chase(200f,0.4, LerpedFloat.Chaser.EXP);


        int halfTimer = timerLength.getValue()/2;


        if(timer<halfTimer-30&&timer>60) {
            light = 0;
        }else

        if(timer>halfTimer+30) {
            light = 2;
        }else{ light = 1;
        }

        if(timer == 0){



            glow.setValue(0);

            timer = timerLength.getValue();
        }


    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        timer = compound.getInt("Timer");

    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.putInt("Timer", timer);
    }
}
