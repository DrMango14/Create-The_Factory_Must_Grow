package com.drmangotea.tfmg.blocks.electricity.generation.large_generator;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.base.cables.VoltagePacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;


import java.util.List;

import static com.drmangotea.tfmg.blocks.electricity.generation.large_generator.StatorBlock.STATOR_STATE;
import static net.minecraft.world.level.block.DirectionalBlock.FACING;


public class StatorBlockEntity extends ElectricBlockEntity implements IHaveGoggleInformation {


    public BlockPos rotor=null;






    public StatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }



    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.STATOR.get());
    }





    public void setRotor(RotorBlockEntity be){
        rotor = be.getBlockPos();

    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if(rotor!=null)
            if(!(level.getBlockEntity(rotor) instanceof RotorBlockEntity))
                rotor = null;


    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return true;
    }


    @Override
    public int FECapacity() {
        return 10000;
    }







}
