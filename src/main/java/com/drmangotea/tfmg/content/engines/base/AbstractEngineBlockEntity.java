package com.drmangotea.tfmg.content.engines.base;

import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelType;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractEngineBlockEntity extends GeneratingKineticBlockEntity {

    //

    public EngineFluidTank fuelTank;
    public EngineFluidTank exhaustTank;
    public IFluidHandler fluidCapability;
    //

    //
    public float rpm = 0;
    //
    public boolean reverse = false;
    //
    public float highestSignal;
    public int signal;
    //

    public float torque = 0;
    public boolean signalChanged;
    //
    public boolean drainFuel = true;
    protected boolean updateCapability;


    public AbstractEngineBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        setLazyTickRate(10);
        fuelTank = EngineFluidTank.fuelTank(8000, f -> tankUpdated(f, true));
        exhaustTank = EngineFluidTank.exhaustTank(8000, f -> tankUpdated(f, false));
        fluidCapability = new CombinedTankWrapper(fuelTank, exhaustTank);
        updateCapability = false;
        refreshCapability();
    }

    public abstract CombinedInvWrapper itemHandler();

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.REGULAR_ENGINE.get(),
                (be, context) -> be.fluidCapability
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.TURBINE_ENGINE.get(),
                (be, context) -> be.fluidCapability
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.RADIAL_ENGINE.get(),
                (be, context) -> be.fluidCapability
        );

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TFMGBlockEntities.REGULAR_ENGINE.get(),
                (be, context) -> be.itemHandler()
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TFMGBlockEntities.TURBINE_ENGINE.get(),
                (be, context) -> be.itemHandler()
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TFMGBlockEntities.RADIAL_ENGINE.get(),
                (be, context) -> be.itemHandler()
        );
    }


    @Override
    public void tick() {
        if (level == null) return;
        if (signalChanged) {
            signalChanged = false;
            analogSignalChanged();
        }
        super.tick();
        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
    }

    public void tankUpdated(FluidStack stack, boolean fuelTank) {
        if (fuelTank && stack.isEmpty()) {
            rpm = 0;
            updateRotation();
            analogSignalChanged();
        }
        sendData();
        setChanged();
    }
	
    protected void analogSignalChanged() {
        if (level == null) return;
        int newSignal = level.getBestNeighborSignal(getBlockPos());
        signal = newSignal;
        newSignal = Math.max(level.getBestNeighborSignal(getBlockPos()), newSignal);
        highestSignal = newSignal / 15f;
        updateRotation();
    }


    @Override
    public void lazyTick() {
        super.lazyTick();

        neighbourChanged();
        if(drainFuel) {
            manageFuelAndExhaust();
        } else drainFuel = true;
    }

	//TODO: Make this more configurable
    public void manageFuelAndExhaust() {
        exhaustTank.forceFill(new FluidStack(exhaustFluid(), Math.min(300, getFuelConsumption())), IFluidHandler.FluidAction.EXECUTE);
        fuelTank.forceDrain(getFuelConsumption(), IFluidHandler.FluidAction.EXECUTE);
        if (fuelTank.isEmpty())
            updateRotation();
        drainFuel = false;
    }

    public float getSpeedEfficiency() {
        if (rpm >= 6000)
            return 1;
		return 2f / (1f + (rpm / 6250));
    }

    public abstract Predicate<FluidStack> validFuels();

    public void onUpdated() {
    }

    public boolean canWork() {
        return !fuelTank.isEmpty() && exhaustTank.getSpace() != 0;
    }

    public void updateRotation() {
    }

	public float efficiencyModifier() {
		return getFuelType().map(EngineFuelType::efficiency).orElse(1.0f);
	}
	
    public float speedModifier() {
		return getFuelType().map(EngineFuelType::speed).orElse(1.0f);
	}
	
	public float torqueModifier() {
		return getFuelType().map(EngineFuelType::torque).orElse(1.0f);
	}
	
	public Fluid exhaustFluid() {
		//fallback to Co2 if no exhaust
		return getFuelType().map(EngineFuelType::exhaust)
			.orElseGet(TFMGFluids.CARBON_DIOXIDE::getSource);
	}
	
    public Optional<EngineFuelType> getFuelType() {
        if (this.level == null) return Optional.empty();
        return EngineFuelType.getTypeForFluid(this.level.registryAccess(), fuelTank.getFluid().getFluid());
    }

    public void refreshCapability() {
        fluidCapability = this.handlerForCapability();
        invalidateCapabilities();
    }

    public IFluidHandler handlerForCapability() {
        return new CombinedTankWrapper(fuelTank, exhaustTank);
    }


    public int getMaxLength() {
        return TFMGConfigs.common().machines.engineMaxLength.get();
    }

    public void changeDirection() {
        playInsertionSound();
        reverse = !reverse;
        updateRotation();
    }

    public void dropItem(ItemStack stack) {
        if (level == null) return;
        Vec3 dropVec = VecHelper.getCenterOf(worldPosition).add(0, 0.3f, 0);
        ItemEntity dropped = new ItemEntity(level, dropVec.x, dropVec.y, dropVec.z, stack);
        dropped.setDefaultPickUpDelay();
        dropped.setDeltaMovement(0, 0.15f, 0);
        level.addFreshEntity(dropped);
    }


    public void playInsertionSound() {
        if (level == null) return;
        level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4f, 0.5f);
    }

    public void playRemovalSound() {
        if (level == null) return;
        level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4f, 0.5f);
    }


    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        reverse = compound.getBoolean("Reverse");
        signal = compound.getInt("Signal") + 1;
        fuelTank.readFromNBT(registries, compound.getCompound("FuelTank"));
        exhaustTank.readFromNBT(registries, compound.getCompound("ExhaustTank"));

        updateCapability = true;

        updateRotation();
        updateGeneratedRotation();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);

        compound.putBoolean("Reverse", reverse);
        compound.putInt("Signal", signal);
        compound.put("FuelTank", fuelTank.writeToNBT(registries, new CompoundTag()));
        compound.put("ExhaustTank", exhaustTank.writeToNBT(registries, new CompoundTag()));

    }

    public abstract int getFuelConsumption();

    public void neighbourChanged() {
        if (level == null)
            return;
        int power = level.getBestNeighborSignal(getBlockPos());
        if (power != this.signal)
            this.signalChanged = true;
    }
}
