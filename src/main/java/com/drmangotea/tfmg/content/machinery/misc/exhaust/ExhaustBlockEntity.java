package com.drmangotea.tfmg.content.machinery.misc.exhaust;



import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.simibubi.create.Create;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;


import java.util.List;
import java.util.Random;

@SuppressWarnings("removal")
public class ExhaustBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    protected IFluidHandler fluidCapability;
    public FluidTank tankInventory;

    public boolean spawnsSmoke=false;
    public int smokeTimer=0;



    public ExhaustBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();
        fluidCapability = tankInventory;

    }
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.EXHAUST.get(),
                (be, context) -> be.fluidCapability
        );
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        return TFMGUtils.createFluidTooltip(this, tooltip);

    }


    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(1000, this::onFluidStackChanged) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(TFMGFluids.CARBON_DIOXIDE.getSource());
            }
        };
    }


    protected void onFluidStackChanged(FluidStack newFluidStack) {
        sendData();
        setChanged();
    }

    @Override
    public void tick() {
        super.tick();
        Direction direction = this.getBlockState().getValue(ExhaustBlock.FACING);

        if(smokeTimer!=0) {
            spawnsSmoke = true;
            smokeTimer--;
        }else spawnsSmoke = false;
        if (direction == Direction.UP)
            if(spawnsSmoke)
                makeParticles(level, this.getBlockPos(), 0);
        if (direction == Direction.DOWN)
            if(spawnsSmoke)
                makeParticles(level, this.getBlockPos(), 1);
        if (direction == Direction.NORTH)
            if(spawnsSmoke)
                makeParticles(level, this.getBlockPos(), 2);
        if (direction == Direction.SOUTH)
            if(spawnsSmoke)
                makeParticles(level, this.getBlockPos(), 3);
        if (direction == Direction.EAST)
            if(spawnsSmoke)
                makeParticles(level, this.getBlockPos(), 4);
        if (direction == Direction.WEST)
            if(spawnsSmoke)
                makeParticles(level, this.getBlockPos(), 5);
        if(tankInventory.getFluidAmount()>0) {

            smokeTimer = 100;
            spawnsSmoke = true;
        }
            if(tankInventory.getSpace()>700) {
                tankInventory.drain(100, IFluidHandler.FluidAction.EXECUTE);
            }else tankInventory.drain(10, IFluidHandler.FluidAction.EXECUTE);






    }



    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);

        tankInventory.readFromNBT(registries,compound.getCompound("TankContent"));
    }





    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {

        super.write(compound,registries , clientPacket);

        compound.put("TankContent", tankInventory.writeToNBT(registries,new CompoundTag()));

        compound.putBoolean("Active", smokeTimer>0);
    }

    public static void makeParticles(Level level, BlockPos pos, int particleRotation) {
        Random random = Create.RANDOM;
        int shouldSpawnSmoke = random.nextInt(7);
        if(shouldSpawnSmoke==0) {


            if(particleRotation==0)
                level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX()  +random.nextFloat(1), pos.getY() + 1, pos.getZ()  +random.nextFloat(1), 0.0D, 0.08D, 0.0D);
            if(particleRotation==1)
                level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX()  +random.nextFloat(1), pos.getY(), pos.getZ()  +random.nextFloat(1), 0.0D, 0.08D, 0.0D);

            if(particleRotation==2)
                level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX()  +random.nextFloat(1), pos.getY()  +random.nextFloat(1), pos.getZ(), 0.0D, 0.08D, 0.0D);
            if(particleRotation==3)
                level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX()  +random.nextFloat(1), pos.getY()  +random.nextFloat(1), pos.getZ() + 1, 0.0D, 0.08D, 0.0D);
            if(particleRotation==4)
                level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() + 1, pos.getY()  +random.nextFloat(1), pos.getZ()  +random.nextFloat(1), 0.0D, 0.08D, 0.0D);
            if(particleRotation==5)
                level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX(), pos.getY()  +random.nextFloat(1), pos.getZ()  +random.nextFloat(1), 0.0D, 0.08D, 0.0D);



        }

    }

    //@Nonnull
    //@Override
    //public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//
    //    if (cap == ForgeCapabilities.FLUID_HANDLER)
    //        return fluidCapability.cast();
    //    return super.getCapability(cap, side);
    //}



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}



}

