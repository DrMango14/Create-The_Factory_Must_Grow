package com.drmangotea.tfmg.content.machines.oil_processing.pumpjack.machine_input;


import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MachineInputBlockEntity extends KineticBlockEntity {
    public int powerLevel=0;
    public MachineInputBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick(){
        super.tick();
        if(stress<80)
            powerLevel=0;
        if(stress>=80)
            powerLevel=1;
        if(stress>=160)
            powerLevel=2;
        if(stress>=320)
            powerLevel=3;
        if(stress>=512)
            powerLevel=4;

    }



    public int getPowerLevel(){
        return powerLevel;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {

        compound.putInt("PowerLevel", powerLevel);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        powerLevel = compound.getInt("PowerLevel");
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        Lang.translate("goggles.machine_input.info")
                .forGoggles(tooltip);
        /*
        if(powerLevel==0){
            Lang.translate("goggles.machine_input.no_rot")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip);
            return true;
        }

         */
        Lang.translate("goggles.machine_input.power_level")
                .style(ChatFormatting.AQUA)
                .add(Lang.translate("goggles.number", this.getPowerLevel()))
                .forGoggles(tooltip,1);


        return true;
    }


}
