package com.drmangotea.tfmg.content.machinery.misc.gas_lamp;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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

public class GasLampBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    protected LazyOptional<IFluidHandler> fluidCapability;
    public FluidTank tankInventory;

    public int lightTimer = 0;


    public GasLampBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();
        fluidCapability = LazyOptional.of(() -> tankInventory);
    }

    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(1000, this::onFluidStackChanged) {
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
                    .setValue(GasLampBlock.LIT, false), 2);
            return;
        }

        // drain rate: 5mB/t
        if (tankInventory.getFluidAmount() > 0) {
            tankInventory.drain(5, IFluidHandler.FluidAction.EXECUTE);
            lightTimer = 100;
        }

        if (lightTimer > 0) {
            lightTimer--;
            level.setBlock(getBlockPos(), this.getBlockState()
                    .setValue(GasLampBlock.LIT, true), 2);
        }
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
