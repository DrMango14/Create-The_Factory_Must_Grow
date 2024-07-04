package com.drmangotea.createindustry.blocks.electricity.base;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.blocks.electricity.capacitor.CapacitorBlockEntity;
import com.drmangotea.createindustry.blocks.electricity.resistors.ResistorBlockEntity;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.EnergyStorage;

public interface IElectricBlock {



    float internalResistance();

    int getVoltage();

    boolean gotFElastTick(int value);

    int getCurrent();


    int feGeneration();

    int voltageGeneration();

    int transferSpeed();


    void addVoltage(float amount);

    default float getCharge(){
        return getCurrent()*getVoltage();
    }

    default boolean isStorage(){
        return false;
    }

    TFMGForgeEnergyStorage getForgeEnergy();

    boolean hasElectricitySlot(Direction direction);


    float maxVoltage();

    void explode();

    int FECapacity();


    int getDistanceFromSource();



    void setDistanceFromSource(int value);

    void sendStuff();

    default boolean canBeDisabled(){
        return false;
    }

    default boolean hasSignal() {
        return false;
    }

    default void sendCharge(Level level, BlockPos pos){

        int lowestDistance = Integer.MAX_VALUE;

        boolean getsVoltageFromNonTFMGBlock = false;



        if(canBeDisabled()){


            if(hasSignal()) {
                setDistanceFromSource(Integer.MAX_VALUE);
                return;
            }
        }

        for(Direction direction : Direction.values()){
            if(hasElectricitySlot(direction)){
                BlockEntity be1 =level.getBlockEntity(pos.relative(direction));

                if(be1 instanceof IElectricBlock be2){



                    if(be2.hasElectricitySlot(direction.getOpposite())) {



                        int distance = be2.getDistanceFromSource();


                        if(!isStorage()&&be2.isStorage()&&direction == Direction.UP)
                            distance = Integer.MAX_VALUE;


                       if(getVoltage()==0) {
                           if(distance>getDistanceFromSource())
                               be2.addVoltage(getVoltage());
                       }

                       if(!(be2 instanceof ConverterBlockEntity))
                        if(!(isStorage()&&direction == Direction.DOWN))
                            if(!(!isStorage()&&be2.isStorage()&&direction == Direction.DOWN))
                                if(getVoltage()!=0) {
                                   // if(Create.RANDOM.nextInt(3) ==1)
                                        transferCharge(be2);
                                    if(distance>getDistanceFromSource()){
                                                be2.addVoltage(getVoltage());
                                    }
                                }

                    lowestDistance = Math.min(lowestDistance,distance);

                    }
                    if(direction.getAxis().isHorizontal()) {
                        if (be2 instanceof ResistorBlockEntity resistorBE)
                            if (resistorBE.hasElectricitySlot(direction) && resistorBE.getVoltageOutput() > 0) {
                                lowestDistance = 0;
                                addVoltage(resistorBE.getVoltageOutput());
                            }

                    }



                }else

                if(be1!=null) {

                    if (be1.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).isPresent()) {

                        if (!(be1.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).orElse(new EnergyStorage(0)) instanceof TFMGForgeEnergyStorage)) {




                                    int i = be1.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).orElse(new EnergyStorage(0)).receiveEnergy(1000, true);
                                    int y = getForgeEnergy().extractEnergy(1000, true);

                                    int j = be1.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).orElse(new EnergyStorage(0)).receiveEnergy(Math.min(y,i), false);
                                    getForgeEnergy().extractEnergy(j, false);

                                }
                            }

                }

            }

        }




        if (lowestDistance != Integer.MAX_VALUE&&voltageGeneration()==0) {

            if( lowestDistance >= getDistanceFromSource()){


            }else {
                if(Create.RANDOM.nextInt(2)==0)
                    setDistanceFromSource(lowestDistance + 1);


            }
          //  sendStuff();
        }



        //else   distanceFromSource = Integer.MAX_VALUE;
        if(voltageGeneration()>0){
            setDistanceFromSource(0);
            // sendStuff();
        }

        if(!level.isClientSide)
          if(lowestDistance == getDistanceFromSource()+1&&this.voltageGeneration()==0&&!getsVoltageFromNonTFMGBlock){
              setDistanceFromSource(Integer.MAX_VALUE);
              // sendStuff();
          }



        if(!level.isClientSide)
            if(getDistanceFromSource()<lowestDistance&&getDistanceFromSource() >0&&this.voltageGeneration()==0&&!getsVoltageFromNonTFMGBlock){
                setDistanceFromSource(Integer.MAX_VALUE);
                // sendStuff();
//
            }
        if(!level.isClientSide)
            if(lowestDistance == Integer.MAX_VALUE && voltageGeneration() == 0&&!getsVoltageFromNonTFMGBlock){
                setDistanceFromSource(Integer.MAX_VALUE);
                //  sendStuff();
            }


      //if(voltageGeneration()>0)
      //    setDistanceFromSource(0);

        if(lowestDistance>=getDistanceFromSource()&&getDistanceFromSource()!=0)
            setDistanceFromSource(Integer.MAX_VALUE);

        if(getDistanceFromSource()==Integer.MAX_VALUE)
            addVoltage(0);
    }



    default void transferCharge(IElectricBlock be) {



        if(!isStorage()) {
            if (be.getDistanceFromSource() > getDistanceFromSource()||be.isStorage()) {

                //int amount = getForgeEnergy().extractEnergy(transferSpeed(), true);
                //int amount2 =  be.getForgeEnergy().receiveEnergy( transferSpeed(), true);





                    int amount = getForgeEnergy().extractEnergy(transferSpeed()*100, true);
                    int amount2 = be.getForgeEnergy().receiveEnergy(transferSpeed()*100, true);


                    getForgeEnergy().extractEnergy(Math.min(amount, amount2), false);
                    be.getForgeEnergy().receiveEnergy(Math.min(amount, amount2), false);
                }
            if(be.getDistanceFromSource()==getDistanceFromSource()&&getForgeEnergy().getEnergyStored()>be.getForgeEnergy().getEnergyStored()){

                int diff = Math.abs(getForgeEnergy().getEnergyStored()-be.getForgeEnergy().getEnergyStored());

                int amount = getForgeEnergy().extractEnergy(diff / 2, true);
                int amount2 =  be.getForgeEnergy().receiveEnergy( diff / 2, true);
                getForgeEnergy().extractEnergy( Math.min(amount,amount2), false);
                be.getForgeEnergy().receiveEnergy( Math.min(amount,amount2), false);
            }


        }

        if(isStorage()){
                if(be.isStorage()){

                    if(!be.isStorage()||(be.isStorage()&&be.getForgeEnergy().getEnergyStored()<getForgeEnergy().getEnergyStored())) {
                  //  if(be.getDistanceFromSource()>=getDistanceFromSource()) {
                        int amount = getForgeEnergy().extractEnergy(transferSpeed()*10, true);
                        int amount2 = be.getForgeEnergy().receiveEnergy(transferSpeed()*10, true);


                        getForgeEnergy().extractEnergy(Math.min(amount, amount2), false);
                        be.getForgeEnergy().receiveEnergy(Math.min(amount, amount2), false);
                    }

                }else {
                    int amount = getForgeEnergy().extractEnergy(transferSpeed(), true);
                    int amount2 =  be.getForgeEnergy().receiveEnergy(transferSpeed(), true);


                    //if(be.getForgeEnergy().getEnergyStored()<be.getForgeEnergy().getMaxEnergyStored()/2) {
                        getForgeEnergy().extractEnergy(Math.min(amount, amount2), false);
                        be.getForgeEnergy().receiveEnergy(Math.min(amount, amount2), false);
                    //}
                }

        }


    }
    default void useEnergy(int baseValue){

        getForgeEnergy().extractEnergy(baseValue/(voltageGeneration()+1),false);

    }


    default TFMGForgeEnergyStorage createEnergyStorage(){
        return new TFMGForgeEnergyStorage(FECapacity(), 99999) {
            @Override
            public void onEnergyChanged(int energyAmount,boolean setEnergy) {

                if(setEnergy)
                    return;







                gotFElastTick(1);


                sendStuff();


            }
        };

    }


    default void writeElectrity(CompoundTag compound){

        compound.putInt("Voltage",getVoltage());
        compound.putInt("Current",getCurrent());

        compound.putInt("DistanceFromSource",getDistanceFromSource());

        compound.putInt("ForgeEnergy", getForgeEnergy().getEnergyStored());
    }




}
