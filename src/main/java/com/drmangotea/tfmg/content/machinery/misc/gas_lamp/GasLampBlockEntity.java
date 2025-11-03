package com.drmangotea.tfmg.content.machinery.misc.gas_lamp;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;


import java.util.List;

public class GasLampBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {


    public FluidTank tankInventory;

    public int lightTimer = 0;


    public GasLampBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();

    }

    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(4000, this::onFluidStackChanged) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().is(TFMGTags.TFMGFluidTags.FLAMMABLE.tag)||
                        stack.getFluid().is(TFMGTags.TFMGFluidTags.FUEL.tag);
            }
        };
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.GAS_LAMP.get(),
                (be, context) -> be.tankInventory
        );
    }
    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
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


        if (tankInventory.getFluidAmount() > 0) {
            if (level.random.nextInt(20) == 0)
                tankInventory.drain(1, IFluidHandler.FluidAction.EXECUTE);
            lightTimer = 100;
        }

        if (lightTimer > 0) {
            lightTimer--;
            level.setBlock(getBlockPos(), this.getBlockState()
                    .setValue(GasLampBlock.LIT, true), 2);
        }
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



    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

}
