package com.drmangotea.createindustry.blocks.electricity.voltmeter.energy_meter;

import com.drmangotea.createindustry.blocks.electricity.base.IElectricBlock;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.EnergyStorage;

import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;


public class EnergyMeterBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {


    public LerpedFloat angle = LerpedFloat.angular();


    public int energy=0;

    public int range = 1;

    public EnergyMeterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }


    @Override
    public void tick() {
        super.tick();
        //if(!level.isClientSide)
        //    return;
        Direction direction = getBlockState().getValue(FACING).getOpposite();
        BlockEntity beBehind = level.getBlockEntity(getBlockPos().relative(direction));


        if(beBehind!=null)
            if(beBehind.getCapability(ForgeCapabilities.ENERGY).isPresent()){
                energy = beBehind.getCapability(ForgeCapabilities.ENERGY).orElse(new EnergyStorage(0)).getEnergyStored();
                range = beBehind.getCapability(ForgeCapabilities.ENERGY).orElse(new EnergyStorage(0)).getMaxEnergyStored();

            } else energy = 0;

        float value = (float) Math.abs(energy) / getRange();

        if(value>1)
            value = 1;

        float targetAngle = value*180;



        angle.chase(targetAngle,0.05f, LerpedFloat.Chaser.EXP);
        angle.tickChaser();



    }


    public int getRange(){
        return range;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);


        compound.putInt("range",range);
        compound.putFloat("angle",(float) Math.abs(energy) / getRange());

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

        Lang.translate("goggles.energy_meter")
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip, 1);

        Lang.translate("goggles.energy_meter.energy", energy)
                .style(ChatFormatting.AQUA)
                .forGoggles(tooltip, 1);

        Lang.translate("goggles.energy_meter.energy", angle.getValue())
                .style(ChatFormatting.AQUA);





        return true;
    }




}
