package com.drmangotea.tfmg.content.machinery.misc.smokestack;

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

import static com.drmangotea.tfmg.content.machinery.misc.smokestack.SmokestackBlock.TOP;


public class SmokestackBlockEntity extends SmartBlockEntity {

    protected LazyOptional<IFluidHandler> fluidCapability;
    public FluidTank tankInventory;

    int smokeTimer = 0;


    public SmokestackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();
        fluidCapability = LazyOptional.of(() -> tankInventory);
    }

    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(1000, this::onFluidStackChanged) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(TFMGFluids.CARBON_DIOXIDE.getSource());
            }
        };
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

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;
        setChanged();
        sendData();
    }

    @Override
    public void tick() {
        super.tick();

        if (tankInventory.isEmpty()) return;

        float fillRatio = (float) tankInventory.getFluidAmount() / tankInventory.getCapacity();

        // Dynamic drain rate: 10mB/t (empty) → 100mB/t (full)
        int drainRate = 10 + (int) (90 * fillRatio);

        if (getBlockState().getValue(TOP)) {
            tankInventory.drain(drainRate, IFluidHandler.FluidAction.EXECUTE);
            smokeTimer = 40;
        }

        if (smokeTimer > 0) {
            smokeTimer--;
            // Spawn particles every 1-10 ticks
            if ((level.getGameTime() + getBlockPos().hashCode()) % Math.max(1, 10 - (int) (9 * fillRatio)) == 0) {
                makeParticles(level, getBlockPos());
            }
        }

        //Transfer smoke upwards
        if (level.getBlockEntity(getBlockPos().above()) instanceof SmokestackBlockEntity be) {
            int transferAmount = Math.min(tankInventory.getFluidAmount(), be.tankInventory.getCapacity() - be.tankInventory.getFluidAmount());

            tankInventory.drain(transferAmount, IFluidHandler.FluidAction.EXECUTE);
            be.tankInventory.fill(new FluidStack(TFMGFluids.CARBON_DIOXIDE.getSource(), transferAmount), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public static void makeParticles(Level level, BlockPos pos) {
        Random random = Create.RANDOM;

        level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() + random.nextFloat(1), pos.getY() + 1, pos.getZ() + random.nextFloat(1), 0.0D, 0.08D, 0.0D);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        tankInventory.readFromNBT(compound.getCompound("TankContent"));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("TankContent", tankInventory.writeToNBT(new CompoundTag()));
        compound.putBoolean("Active", smokeTimer > 0);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}
}
