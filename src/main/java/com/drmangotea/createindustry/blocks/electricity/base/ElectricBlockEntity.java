package com.drmangotea.createindustry.blocks.electricity.base;

import com.drmangotea.createindustry.base.util.TFMGUtils;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ElectricBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IElectricBlock {

    public int voltage=0;
    public int current =0;

    private boolean gotFElastTick = false;

    public int distanceFromSource = Integer.MAX_VALUE;




    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    public final TFMGForgeEnergyStorage energy = createEnergyStorage();




    public ElectricBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

    public void tick(){
        super.tick();

        energy.receiveEnergy(feGeneration(),false);


        manageVoltage();






        sendCharge(level,getBlockPos());



    }






    public TFMGForgeEnergyStorage getEnergy() {
        return energy;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyHandler = LazyOptional.of(() -> energy);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {


        if (cap == ForgeCapabilities.ENERGY&&side == null) {
            return lazyEnergyHandler.cast();
        } else

            if (cap == ForgeCapabilities.ENERGY&&hasElectricitySlot(side)) {
                return lazyEnergyHandler.cast();
            }

        return super.getCapability(cap, side);
    }


    public void manageVoltage(){

        if(voltageGeneration()>0) {
            voltage = voltageGeneration();
            distanceFromSource = 0;
        }
        if(distanceFromSource == Integer.MAX_VALUE)
            voltage = 0;


        current = energy.getEnergyStored()/(voltage+1);




        if(voltage>maxVoltage()){
            voltage = 0;
            explode();
            voltage = 0;
        }



    }





    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }


    @Override
    @SuppressWarnings("removal")
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        //if(energy.getEnergyStored()==0&&voltageGeneration()==0)
        //    Lang.translate("goggles.electric_machine.no_power")
        //            .style(ChatFormatting.RED)
        //            .forGoggles(tooltip, 1);
//
//
        //Lang.translate("goggles.blast_furnace.height", voltage)
        //        .style(ChatFormatting.DARK_PURPLE)
        //        .forGoggles(tooltip, 1);
//
        //Lang.translate("goggles.blast_furnace.height", current)
        //        .style(ChatFormatting.LIGHT_PURPLE)
        //        .forGoggles(tooltip, 1);
//
        //Lang.translate("goggles.blast_furnace.height", energy.getEnergyStored())
        //        .style(ChatFormatting.DARK_RED)
        //        .forGoggles(tooltip, 1);
//
        //Lang.translate("goggles.blast_furnace.height", distanceFromSource)
        //        .style(ChatFormatting.DARK_AQUA)
        //        .forGoggles(tooltip, 1);


        return true;
    }





    @Override
    public float internalResistance() {
        return 0;
    }

    @Override
    public int getVoltage() {
        return voltage;
    }

    @Override
    public boolean gotFElastTick(int value) {



        gotFElastTick = value == 3 ? gotFElastTick : value == 1;

        return gotFElastTick;
    }

    @Override
    public int getCurrent() {
        return current;
    }





    @Override
    public void addVoltage(float amount) {



       // if(voltageGeneration()>0)
       //     return;


        voltage = (int) amount;

        //voltage = (int) ((getVoltage()+amount)/2);
    }


    public void addVoltageFromNonTFMGBlock() {

        //if(voltage<250) {
        //    voltage = 250;
//
        //}
    }

    @Override
    public TFMGForgeEnergyStorage getForgeEnergy() {
        return energy;
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return false;
    }






    @Override
    public float maxVoltage() {
        return 500;
    }

    @Override
    public void explode() {
        TFMGUtils.createFireExplosion(level,null, getBlockPos(),10,1.5f);
    }

    @Override
    public int FECapacity() {
        return 10000;
    }

    @Override
    public int getDistanceFromSource() {
        return distanceFromSource;
    }



    @Override
    public void setDistanceFromSource(int value) {
        distanceFromSource = value;
     //   sendStuff();
    }

    @Override
    public void sendStuff() {
        sendData();
        setChanged();
    }


    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        writeElectrity(compound);

   ;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        voltage = compound.getInt("Voltage");
        current = compound.getInt("Current");

        distanceFromSource = compound.getInt("DistanceFromSource");



        energy.setEnergy(compound.getInt("ForgeEnergy"));

    }



    @Override
    public int feGeneration() {
        return 0;
    }

    @Override
    public int voltageGeneration() {
        return 0;
    }

    @Override
    public int transferSpeed() {
        return 3000;
    }
}
