package com.drmangotea.createindustry.blocks.electricity.voltmeter;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.blocks.electricity.base.IElectricBlock;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;


public class VoltMeterBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {


    public LerpedFloat angle = LerpedFloat.angular();


    public int voltage=0;

    public int range = 500;

    public VoltMeterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }


    @Override
    public void tick() {
        super.tick();
        if(!level.isClientSide)
            return;

        BlockEntity beBehind = level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(FACING).getOpposite()));

        if(beBehind instanceof IElectricBlock be){
            voltage = Math.min(be.getVoltage(), range );

        } else voltage = 0;

        float value = (float) Math.abs(voltage) / getRange();

        if(value>1)
            value = 1;

        float targetAngle = Math.abs(value*180);



        angle.chase(Math.abs(targetAngle),0.05f, LerpedFloat.Chaser.EXP);
        angle.tickChaser();



    }


    public int getRange(){
        return range;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);


        compound.putInt("range",range);
        compound.putFloat("angle", (float) Math.abs(voltage) / getRange());

    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        range = compound.getInt("range");

        angle.setValue(compound.getFloat("angle"));




    }

    @Override
    @SuppressWarnings("removal")
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        Lang.translate("goggles.voltmeter")
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip, 1);

        Lang.translate("goggles.voltmeter.voltage", Math.min(voltage,range))
                .style(ChatFormatting.AQUA)
                .forGoggles(tooltip, 1);
        Lang.translate("goggles.voltmeter.range", range)
                .style(ChatFormatting.DARK_AQUA)
                .forGoggles(tooltip, 1);





        return true;
    }
}
