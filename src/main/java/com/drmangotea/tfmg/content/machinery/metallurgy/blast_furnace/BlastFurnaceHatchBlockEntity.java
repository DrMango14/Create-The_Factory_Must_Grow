package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.fluid.ForceableFluidTank;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public class BlastFurnaceHatchBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, Clearable {
    public ForceableFluidTank tank;

    public SmartInventory inputInventory;
    public SmartInventory fluxInventory;
    public SmartInventory fuelInventory;
    public IFluidHandler fluidCapability;
    public CombinedInvWrapper itemCapability;


    public BlastFurnaceHatchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
        tank = new ForceableFluidTank(4000, this::onFluidChanged);
        inputInventory = new SmartInventory(1, this, (i, stack) -> !stack.is(TFMGTags.Items.FLUX.tag) && !stack.is(TFMGTags.Items.BLAST_FURNACE_FUEL.tag)).withMaxStackSize(64);
        fluxInventory = new SmartInventory(1, this, (i, stack) -> stack.is(TFMGTags.Items.FLUX.tag)).withMaxStackSize(64);
        fuelInventory = new SmartInventory(1, this, (i, stack) -> stack.is(TFMGTags.Items.BLAST_FURNACE_FUEL.tag)).withMaxStackSize(64);
        fluidCapability = tank;
        itemCapability = new CombinedInvWrapper(inputInventory, fluxInventory, fuelInventory);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.BLAST_FURNACE_HATCH.get(),
                (be, context) -> be.fluidCapability
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TFMGBlockEntities.BLAST_FURNACE_HATCH.get(),
                (be, context) -> be.itemCapability
        );
    }

    public void setTuyere() {
        this.tank.allowInsertion().withValidator((stack) -> stack.is(TFMGTags.Fluids.HOT_AIR.tag));
        this.inputInventory.forbidExtraction().forbidInsertion();
        this.fluxInventory.forbidExtraction().forbidInsertion();
        this.fuelInventory.forbidExtraction().forbidInsertion();
    }

    public void setTopHatch() {
        this.tank.blockInsertion().withValidator((stack) -> true);
        this.inputInventory.allowExtraction().allowInsertion();
        this.fluxInventory.allowExtraction().allowInsertion();
        this.fuelInventory.allowExtraction().allowInsertion();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		TFMGUtils.createFluidTooltip(tooltip, fluidCapability);
		TFMGUtils.createItemTooltip(tooltip, itemCapability);
        return true;
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInventory);
        ItemHelper.dropContents(level, worldPosition, fluxInventory);
        ItemHelper.dropContents(level, worldPosition, fuelInventory);
    }

    public void fillFurnace(BlastFurnaceOutputBlockEntity blastFurnaceOutput) {
        if (blastFurnaceOutput.multiblock.getTopHatch() != null && blastFurnaceOutput.multiblock.getTopHatch().equals(getBlockPos())) {
            if (!inputInventory.isEmpty()
                    && (blastFurnaceOutput.inputInventory.getStackInSlot(0).isEmpty() || ItemStack.isSameItemSameComponents(inputInventory.getStackInSlot(0), blastFurnaceOutput.inputInventory.getStackInSlot(0)))
                    && blastFurnaceOutput.inputInventory.getStackInSlot(0).getCount() < blastFurnaceOutput.inputInventory.getSlotLimit(0)) {
                int toExtract = Math.min(blastFurnaceOutput.inputInventory.getSlotLimit(0) - blastFurnaceOutput.inputInventory.getStackInSlot(0).getCount(), inputInventory.getStackInSlot(0).getCount());
                ItemStack toPlace = inputInventory.getStackInSlot(0).copyWithCount(blastFurnaceOutput.inputInventory.getStackInSlot(0).getCount() + toExtract);
                inputInventory.extractItem(0, toExtract, false);
                blastFurnaceOutput.inputInventory.setItem(0, toPlace);
            }
            if (!fluxInventory.isEmpty()
                    && (blastFurnaceOutput.fluxInventory.getStackInSlot(0).isEmpty() || ItemStack.isSameItemSameComponents(fluxInventory.getStackInSlot(0), blastFurnaceOutput.fluxInventory.getStackInSlot(0)))
                    && blastFurnaceOutput.fluxInventory.getStackInSlot(0).getCount() < blastFurnaceOutput.fluxInventory.getSlotLimit(0)) {
                int toExtract = Math.min(blastFurnaceOutput.fluxInventory.getSlotLimit(0) - blastFurnaceOutput.fluxInventory.getStackInSlot(0).getCount(), fluxInventory.getStackInSlot(0).getCount());
                ItemStack toPlace = fluxInventory.getStackInSlot(0).copyWithCount(blastFurnaceOutput.fluxInventory.getStackInSlot(0).getCount() + toExtract);
                fluxInventory.extractItem(0, toExtract, false);
                blastFurnaceOutput.fluxInventory.setItem(0, toPlace);
            }
            if (!fuelInventory.isEmpty()) {
                int toExtract = Math.min(BlastFurnaceOutputBlockEntity.STORAGE_SPACE - blastFurnaceOutput.fuel, fuelInventory.getStackInSlot(0).getCount());
                fuelInventory.extractItem(0, toExtract, false);
                blastFurnaceOutput.addFuel(toExtract);
            }
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        tank.readFromNBT(registries,compound.getCompound("TankContent"));
        inputInventory.deserializeNBT(registries,compound.getCompound("Input"));
        fluxInventory.deserializeNBT(registries,compound.getCompound("Flux"));
        fuelInventory.deserializeNBT(registries,compound.getCompound("Fuel"));
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.put("TankContent", tank.writeToNBT(registries,new CompoundTag()));
        compound.put("Input", inputInventory.serializeNBT(registries));
        compound.put("Flux", fluxInventory.serializeNBT(registries));
        compound.put("Fuel", fuelInventory.serializeNBT(registries));
    }

    private void onFluidChanged(FluidStack stack) {
        if (!hasLevel())
            return;
        setChanged();
        sendData();
    }


    @Override
    public void clearContent() {
        this.inputInventory.clearContent();
        this.fluxInventory.clearContent();
        this.fuelInventory.clearContent();
    }
}
