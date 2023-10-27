package com.drmangotea.tfmg.blocks.concrete.formwork.rebar;

import com.drmangotea.tfmg.blocks.concrete.formwork.FormWorkBlockEntity;
import com.drmangotea.tfmg.blocks.machines.TFMGMachineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.jozufozu.flywheel.core.GameStateRegistry;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class RebarFormWorkBlockEntity extends FormWorkBlockEntity {





    public RebarFormWorkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }


    @Override
    public void tick() {



        if(tankInventory.getFluidAmount()==tankInventory.getCapacity()){
            if(timer==-1) {
                timer = 180 * 24;
            }else {

                timer--;

                if(timer==0){
                    level.setBlock(getBlockPos(), TFMGBlocks.REBAR_CONCRETE.getDefaultState() ,3);
                }
            }


        } else
            timer = -1;


        super.tick();
        fluidLevel.chase(tankInventory.getFluidAmount(), 0.3f, LerpedFloat.Chaser.EXP);
        fluidLevel.tickChaser();

        BlockEntity blockEntityBelow = level.getBlockEntity(this.getBlockPos().below());
        BlockEntity blockEntityNorth = level.getBlockEntity(this.getBlockPos().north());
        BlockEntity blockEntityWest = level.getBlockEntity(this.getBlockPos().west());
        BlockEntity blockEntityEast = level.getBlockEntity(this.getBlockPos().east());
        BlockEntity blockEntitySouth = level.getBlockEntity(this.getBlockPos().south());


        bottom = !(blockEntityBelow instanceof RebarFormWorkBlockEntity);
        east = !(blockEntityEast instanceof RebarFormWorkBlockEntity);
        west = !(blockEntityWest instanceof RebarFormWorkBlockEntity);
        north = !(blockEntityNorth instanceof RebarFormWorkBlockEntity);
        south = !(blockEntitySouth instanceof RebarFormWorkBlockEntity);
        for (int x = 0; x < 30; x++) {
            for (int i = 0; i < 5; i++) {
                BlockEntity CheckedBE = null;

                if (i == 0) {
                    CheckedBE = level.getBlockEntity(this.getBlockPos().below());
                }
                if (i == 1) {
                    CheckedBE = level.getBlockEntity(this.getBlockPos().east());
                }
                if (i == 2) {
                    CheckedBE = level.getBlockEntity(this.getBlockPos().west());
                }
                if (i == 3) {
                    CheckedBE = level.getBlockEntity(this.getBlockPos().north());
                }
                if (i == 4) {
                    CheckedBE = level.getBlockEntity(this.getBlockPos().south());
                }

                if (CheckedBE instanceof RebarFormWorkBlockEntity) {
                    if(((RebarFormWorkBlockEntity) CheckedBE).tankInventory.getFluidAmount()>this.tankInventory.getFluidAmount()&&i!=0)
                        continue;

                    FluidTank checkedTank = ((RebarFormWorkBlockEntity) CheckedBE).tankInventory;
                    if (checkedTank.getFluidAmount() < 1000) {
                  if(checkedTank.getFluidAmount()>=995&&tankInventory.getFluidAmount()>0){
                      checkedTank.setFluid(new FluidStack(TFMGFluids.LIQUID_CONCRETE.getSource(), checkedTank.getFluidAmount()+1));
                      //tankInventory.drain(1, IFluidHandler.FluidAction.EXECUTE);
                //      continue;
                  }

                        int reducedAmount = tankInventory.getFluidAmount() / 8;

                        if (tankInventory.getFluidAmount() != 0)
                            reducedAmount = 1;
                        int newFluidAmount = checkedTank.getFluidAmount() + reducedAmount;

                        int toRemove = reducedAmount;
                        //if full
                         if (newFluidAmount > 1000) {
                             continue;
                     //       int amountModifier = newFluidAmount - 1000;
                     //       newFluidAmount -= amountModifier;
                     //       toRemove = reducedAmount - amountModifier;
                        }
                        //
                        checkedTank.setFluid(new FluidStack(TFMGFluids.LIQUID_CONCRETE.getSource(), newFluidAmount));
                        tankInventory.drain(1, IFluidHandler.FluidAction.EXECUTE);

                    }
                }
            }
        }


/*
        BlockPos below = this.getBlockPos().below();

        BlockPos west = this.getBlockPos().west();
        BlockPos east = this.getBlockPos().east();
        BlockPos north = this.getBlockPos().north();
        BlockPos south = this.getBlockPos().south();

        if(level.getBlockEntity(below) instanceof FormWorkBlockEntity){
            bottom = false;
        }else bottom = true;
        if(level.getBlockEntity(west) instanceof FormWorkBlockEntity){
            bottom = false;
        }else bottom = true;
        if(level.getBlockEntity(east) instanceof FormWorkBlockEntity){
            bottom = false;
        }else bottom = true;
        if(level.getBlockEntity(north) instanceof FormWorkBlockEntity){
            bottom = false;
        }else bottom = true;
        if(level.getBlockEntity(south) instanceof FormWorkBlockEntity){
            bottom = false;
        }else bottom = true;


 */
}


    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;



        if (!level.isClientSide) {
            setChanged();
            sendData();
        }


    }
    public float getFillState() {
        return (float) tankInventory.getFluidAmount() / tankInventory.getCapacity();
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        timer = compound.getInt("Timer");

        bottom = compound.getBoolean("Bottom");

        east = compound.getBoolean("East");
        west = compound.getBoolean("West");
        north = compound.getBoolean("North");
        south = compound.getBoolean("South");
        tankInventory.readFromNBT(compound.getCompound("TankContent"));



    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);


        compound.putInt("Timer", timer);

        compound.putBoolean("Bottom", bottom);

        compound.putBoolean("East", east);
        compound.putBoolean("West", west);
        compound.putBoolean("North", north);
        compound.putBoolean("South", south);
        compound.put("TankContent", tankInventory.writeToNBT(new CompoundTag()));



    }


    public LerpedFloat getFluidLevel() {
        return fluidLevel;
    }




}
