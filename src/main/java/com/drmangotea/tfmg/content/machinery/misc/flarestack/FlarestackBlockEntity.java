package com.drmangotea.tfmg.content.machinery.misc.flarestack;


import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.Create;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class FlarestackBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    protected LazyOptional<IFluidHandler> fluidCapability;
    public FluidTank tankInventory;

    public int smokeTimer = 0;


    public FlarestackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();
        fluidCapability = LazyOptional.of(() -> tankInventory);
    }

    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(2500, this::onFluidStackChanged) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().is(TFMGTags.TFMGFluidTags.FLAMMABLE.tag)||
                        stack.getFluid().is(TFMGTags.TFMGFluidTags.FUEL.tag);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

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
        if (!hasLevel()) return;
        sendData();
        setChanged();
    }

    @Override
    @SuppressWarnings("removal")
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return TFMGUtils.createFluidTooltip(this, tooltip);
    }

    @Override
    public void tick() {
        super.tick();

        if (tankInventory.isEmpty() || !tankInventory.isFluidValid(tankInventory.getFluid())) {
            level.setBlock(getBlockPos(), this.getBlockState()
                    .setValue(FlarestackBlock.LIT, false), 2);
            return;
        }

        float fillRatio = (float) tankInventory.getFluidAmount() / tankInventory.getCapacity();

        // Dynamic drain rate: 30mB/t (empty) → 100mB/t (full)
        int drainRate = 30 + (int) (70 * fillRatio);
        if (tankInventory.getFluidAmount() > 0) {
            tankInventory.drain(drainRate, IFluidHandler.FluidAction.EXECUTE);
            smokeTimer = 100;
        }

        if (smokeTimer > 0) {
            smokeTimer--;

            // Light flarestack
            level.setBlock(getBlockPos(), this.getBlockState()
                    .setValue(FlarestackBlock.LIT, true), 2);

            // Spawn particles every 1-10 ticks
            if ((level.getGameTime() + getBlockPos().hashCode()) % Math.max(1, 10 - (int) (9 * fillRatio)) == 0) {
                makeParticles(level, getBlockPos());
            }
        }
    }

    public static void makeParticles(Level level, BlockPos pos) {
        Random random = Create.RANDOM;

        level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX()  +random.nextFloat(1), pos.getY() + 1, pos.getZ()  +random.nextFloat(1), 0.0D, 0.08D, 0.0D);
        level.addParticle(ParticleTypes.FLAME, pos.getX()  +random.nextFloat(1), pos.getY() + 1, pos.getZ()  +random.nextFloat(1), Create.RANDOM.nextDouble(0.28)-0.14D, 0.14D, Create.RANDOM.nextDouble(0.28)-0.14D);
        level.addParticle(ParticleTypes.FLAME, pos.getX()  +random.nextFloat(1), pos.getY() + 1, pos.getZ()  +random.nextFloat(1), Create.RANDOM.nextDouble(0.28)-0.14D, 0.14D, Create.RANDOM.nextDouble(0.28)-0.14D);
        level.addParticle(ParticleTypes.FLAME, pos.getX()  +random.nextFloat(1), pos.getY() + 1, pos.getZ()  +random.nextFloat(1), Create.RANDOM.nextDouble(0.28)-0.14D, 0.14D, Create.RANDOM.nextDouble(0.28)-0.14D);

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
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

}
