package com.drmangotea.tfmg.blocks.machines.smokestack;

import com.drmangotea.tfmg.registry.TFMGFluids;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

import static com.drmangotea.tfmg.blocks.machines.smokestack.SmokestackBlock.TOP;


public class SmokestackBlockEntity extends SmartBlockEntity {


    int smokeTimer = 0;


    public FluidTank tankInventory;

    protected LazyOptional<IFluidHandler> fluidCapability;

    public SmokestackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        tankInventory = new SmartFluidTank(8000,this::onFluidStackChanged){
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(TFMGFluids.CARBON_DIOXIDE.getSource());
            }
        };

        fluidCapability = LazyOptional.of(() -> tankInventory);
    }

    @Nonnull
    @Override
    @SuppressWarnings("removal")
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {

        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }
    @Override
    public void invalidate() {
        super.invalidate();

        fluidCapability.invalidate();
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        tankInventory.readFromNBT(compound.getCompound("TankContent"));



    }
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;






        if (!level.isClientSide) {
            setChanged();
            sendData();
        }




    }

    public static void makeParticles(Level level, BlockPos pos) {
        Random random = Create.RANDOM;
        int shouldSpawnSmoke = random.nextInt(7);
        if(shouldSpawnSmoke==0) {



            level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() +random.nextFloat(1), pos.getY() + 1, pos.getZ() +random.nextFloat(1), 0.0D, 0.08D, 0.0D);




        }

    }

    @Override
    public void tick() {
        super.tick();

        if(smokeTimer>0){

            makeParticles(level,getBlockPos());

            smokeTimer--;
        }


        if(tankInventory.isEmpty())
            return;


        if(getBlockState().getValue(TOP)){
            tankInventory.drain(tankInventory.getSpace()<1000 ? 50 : 10, IFluidHandler.FluidAction.EXECUTE);

            smokeTimer = 40;

        }


        if(level.getBlockEntity(getBlockPos().above()) instanceof SmokestackBlockEntity be){

            int transferAmount = Math.min(tankInventory.getFluidAmount(),be.tankInventory.getCapacity()-be.tankInventory.getFluidAmount());

            tankInventory.drain(transferAmount, IFluidHandler.FluidAction.EXECUTE);
            be.tankInventory.fill(new FluidStack(TFMGFluids.CARBON_DIOXIDE.getSource(),transferAmount), IFluidHandler.FluidAction.EXECUTE);

        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);


        compound.put("TankContent", tankInventory.writeToNBT(new CompoundTag()));


        compound.putBoolean("Active", smokeTimer>0);


    }



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}
}
