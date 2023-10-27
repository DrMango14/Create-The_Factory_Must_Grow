package com.drmangotea.tfmg.blocks.engines.diesel.engine_expansion;

import com.drmangotea.tfmg.registry.TFMGFluids;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class DieselEngineExpansionBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    protected LazyOptional<IFluidHandler> fluidCapability;
    protected FluidTank coolantTank;
    protected FluidTank lubricationOilTank;
    protected FluidTank airTank;




    public DieselEngineExpansionBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        airTank = createUpgradeTankInventory(TFMGFluids.AIR.get());
        coolantTank = createUpgradeTankInventory(TFMGFluids.COOLING_FLUID.get());
        lubricationOilTank = createUpgradeTankInventory(TFMGFluids.LUBRICATION_OIL.get());

        fluidCapability = LazyOptional.of(() -> new CombinedTankWrapper(airTank,lubricationOilTank,coolantTank));


    }


    @Nonnull
    @Override
    @SuppressWarnings("removal")
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }
    @Override
    public void invalidate() {
        super.invalidate();
        fluidCapability.invalidate();
    }
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {

        compound.put("LubricationOil", lubricationOilTank.writeToNBT(new CompoundTag()));
        compound.put("Air", airTank.writeToNBT(new CompoundTag()));

        compound.put("Coolant", coolantTank.writeToNBT(new CompoundTag()));


        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        lubricationOilTank.readFromNBT(compound.getCompound("LubricationOil"));

        airTank.readFromNBT(compound.getCompound("Air"));

        coolantTank.readFromNBT(compound.getCompound("Coolant"));



        super.read(compound, clientPacket);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        sendData();
    }
    protected SmartFluidTank createUpgradeTankInventory(Fluid validFluid) {
        return new SmartFluidTank(1000, this::onFluidStackChanged){
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(validFluid);
            }
        };
    }
}
