package com.drmangotea.tfmg.content.engines.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.KineticElectricBlockEntity;
import com.drmangotea.tfmg.content.engines.fuels.BaseFuelTypes;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelTypeManager;
import com.drmangotea.tfmg.content.engines.fuels.FuelType;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.drmangotea.tfmg.content.engines.base.EngineBlock.ENGINE_STATE;
import static com.drmangotea.tfmg.content.engines.base.EngineBlock.EngineState.SHAFT;

public abstract class AbstractEngineBlockEntity extends KineticElectricBlockEntity {

    //

    public EngineFluidTank fuelTank;
    public EngineFluidTank exhaustTank;
    public IFluidHandler fluidCapability;
    //

    //
    public float rpm = 0;
    public float fuelInjectionRate = 0;
    //
    public boolean reverse = false;
    //
    public int highestSignal;
    public int signal;
    //
    public BlockPos engineController;
    //

    //

    public float torque = 0;
    public boolean signalChanged;
    //
    public int fuelConsumptionTimer = 0;
    //


    public AbstractEngineBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        setLazyTickRate(10);
        fuelTank = new EngineFluidTank(4000, false, true, f->tankUpdated(f,true), TFMGTags.TFMGFluidTags.AIR.tag);
        exhaustTank = new EngineFluidTank(8000, true, false, f->tankUpdated(f,false));
        fluidCapability = new CombinedTankWrapper(fuelTank, exhaustTank);

        refreshCapability();
    }

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

    }


    @Override
    public void tick() {
        if (signalChanged) {
            signalChanged = false;
            analogSignalChanged();
        }
        super.tick();
    }

    public void tankUpdated(FluidStack stack, boolean fuelTank ) {
        if(fuelTank && stack.isEmpty()) {
            fuelInjectionRate = 0;
            updateRotation();
        }
        sendData();
        setChanged();
    }

    public boolean hasEngineController() {
        return engineController != null;
    }


    @Override
    public void updateNetwork() {
        super.updateNetwork();
    }

    protected void analogSignalChanged() {


        if (hasEngineController()) {
            fuelInjectionRate = highestSignal / 15f;
            return;
        }

        int newSignal = level.getBestNeighborSignal(getBlockPos());

        signal = newSignal;


        newSignal = Math.max(level.getBestNeighborSignal(getBlockPos()), newSignal);
        highestSignal = newSignal;
        fuelInjectionRate = highestSignal / 15f;
        updateRotation();

    }

    //@Override
    //public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
//
    //    CreateLang.text("SIGNAL " + highestSignal).forGoggles(tooltip);
//
//
    //    return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    //}


    @Override
    public void lazyTick() {
        super.lazyTick();

        neighbourChanged();



        manageFuelAndExhaust();
    }

    public void manageFuelAndExhaust() {
        exhaustTank.forceFill(new FluidStack(TFMGFluids.CARBON_DIOXIDE.get(), Math.min(300, getFuelConsumption())), IFluidHandler.FluidAction.EXECUTE);

        if (fuelConsumptionTimer <= 2) {
            fuelConsumptionTimer++;
        } else {
            fuelConsumptionTimer = 0;
            fuelTank.forceDrain(getFuelConsumption(), IFluidHandler.FluidAction.EXECUTE);

            if (fuelTank.isEmpty())
                updateRotation();

        }
    }


    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return false;
    }


    public float getSpeedEfficiency() {
        if (rpm >= 6000)
            return 1;

        return 1 / (0.08f * (rpm / 1000) + 0.5f);
    }


    public abstract List<TagKey<Fluid>> getSupportedFuels();

    public void onUpdated() {
    }


    public boolean canWork() {

        if (fuelTank.isEmpty())
            return false;

        if (exhaustTank.getSpace() == 0)
            return false;


        return true;
    }

    public void updateRotation() {
    }

    public abstract float efficiencyModifier();

    public abstract float speedModifier();

    public abstract float torqueModifier();

    public abstract String engineId();


    public FuelType getFuelType() {

        AtomicReference<FuelType> matchingType = new AtomicReference<>(BaseFuelTypes.FALLBACK);

        EngineFuelTypeManager.GLOBAL_TYPE_MAP.forEach((r, t) -> {
            TagKey<Fluid> fluidTag = t.getFluid();
            FluidStack fluid = fuelTank.getFluid();
            if (fluid.getFluid().is(fluidTag)) {
                matchingType.set(t);
            }

        });
        return matchingType.get();
    }

    public void refreshCapability() {
        IFluidHandler oldCap = fluidCapability;
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
        Vec3 dropVec = VecHelper.getCenterOf(worldPosition).add(0, 0.3f, 0);
        ItemEntity dropped = new ItemEntity(level, dropVec.x, dropVec.y, dropVec.z, stack);
        dropped.setDefaultPickUpDelay();
        dropped.setDeltaMovement(0, 0.15f, 0);
        level.addFreshEntity(dropped);
    }


    public void playInsertionSound() {
        level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4f, 0.5f);
    }

    public void playRemovalSound() {
        level.playSound(null, getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4f, 0.5f);
    }


    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);

        reverse = compound.getBoolean("Reverse");
        signal = compound.getInt("Signal")+1;
        if (hasEngineController())
            engineController = BlockPos.of(compound.getLong("EngineController"));
        fuelInjectionRate = compound.getFloat("RPM");

        // if (isController()) {
        // if (!BlockPos.of(compound.getLong("ControllerPos")).equals(new BlockPos(0, 0, 0)))
        //     controller = BlockPos.of(compound.getLong("ControllerPos"));

        fuelTank.readFromNBT(registries,compound.getCompound("FuelTank"));
        exhaustTank.readFromNBT(registries,compound.getCompound("ExhaustTank"));

        highestSignal = compound.getInt("HighestSignal");

        //signalChanged = true;
        updateRotation();
        updateGeneratedRotation();

        //}

    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);

        compound.putBoolean("Reverse", reverse);
        compound.putInt("Signal", signal);
        if (hasEngineController())
            compound.putLong("EngineController", engineController.asLong());
        compound.putFloat("RPM", fuelInjectionRate);


        compound.put("FuelTank", fuelTank.writeToNBT(registries,new CompoundTag()));
        compound.put("ExhaustTank", exhaustTank.writeToNBT(registries,new CompoundTag()));

        compound.putInt("HighestSignal", highestSignal);


    }


    public abstract int getFuelConsumption();


    @Override
    public void onPlaced() {
        super.onPlaced();
    }


    public void neighbourChanged() {


        if (!hasLevel())
            return;


        int power = level.getBestNeighborSignal(getBlockPos());


        if (power != this.signal)
            this.signalChanged = true;

    }


}
