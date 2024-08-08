package com.drmangotea.createindustry.blocks.electricity.base;

import com.drmangotea.createindustry.blocks.electricity.api.Energy;
import com.drmangotea.createindustry.blocks.electricity.resistors.ResistorBlockEntity;
import com.simibubi.create.Create;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IElectricBlock {

    float internalResistance();

    Energy getStorage();

    int getVoltage();

    int getCurrent();

    int voltageGeneration();

    int transferSpeed();

    void addVoltage(float amount);

    default float getCharge(){
        return getCurrent()*getVoltage();
    }

    default boolean isStorage(){
        return false;
    }

    boolean hasElectricitySlot(Direction direction);

    float maxVoltage();

    void explode();

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
                                       if(distance>getDistanceFromSource())
                                           be2.addVoltage(getVoltage());
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

                } else if(be1!=null) {
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

//PLUH

    default void transferCharge(IElectricBlock be) {
        boolean hasHigherVoltage = getStorage().getVoltage() > be.getStorage().getVoltage();
        if(isStorage()) {
            if(be.isStorage() && !hasHigherVoltage) {
                long amount = getStorage().transfer(be, transferSpeed() * 10, true);
                getStorage().transfer(be, amount, false);
            } else {
                long amount = be.getStorage().transfer(be, transferSpeed(), true);
                getStorage().transfer(be, amount, false);
            }
        } else {
            if (be.getDistanceFromSource() > getDistanceFromSource() || be.isStorage()) {
                long amount = getStorage().transfer(be, transferSpeed()*100, true);
                getStorage().transfer(be, amount, false);
            }
            if(be.getDistanceFromSource() == getDistanceFromSource() && hasHigherVoltage){

                long diff = Math.abs(getStorage().getVoltage() - be.getStorage().getVoltage());

                long amount = getStorage().transfer(be, diff / 2, true);
                getStorage().transfer(be, amount, false);
            }
        }
    }

    //PLUH

    default void useEnergy(int baseValue){
        getStorage().discharge(baseValue/(voltageGeneration()+1),false);
    }


    default void writeElectrity(CompoundTag compound){

        compound.putDouble("Voltage",getVoltage());
        compound.putDouble("Current",getCurrent());

        compound.putInt("DistanceFromSource",getDistanceFromSource());

        //compound.putInt("ForgeEnergy", getForgeEnergy().getEnergyStored());
    }
}
