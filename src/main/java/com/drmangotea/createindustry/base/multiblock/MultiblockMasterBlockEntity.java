package com.drmangotea.createindustry.base.multiblock;

import com.drmangotea.createindustry.blocks.machines.metal_processing.blast_stove.BlastStoveBlock;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class MultiblockMasterBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    protected LazyOptional<IFluidHandler> fluidCapability = LazyOptional.of(() -> {
        return this.tankInventory;
    });
    public FluidTank tankInventory = this.createInventory();
    public boolean isValid = false;
    public int timer;
    public final String multiblockIdentifier;
    public final int masterTankCapacity;
    public MultiblockStructure multiblockStructure;
    public Direction getMasterDirection() {
        return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }
    
    public MultiblockMasterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int masterTankCapacity, String multiblockIdentifier) {
        super(type, pos, state);
        this.timer = -1;
        this.masterTankCapacity = masterTankCapacity;
        this.multiblockIdentifier = multiblockIdentifier;
    }
    
    public String getMultiblockIdentifier() {
        return this.multiblockIdentifier;
    }
    
    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(1000, this::onFluidStackChanged);
    }
    
    protected void onFluidStackChanged(FluidStack newFluidStack) {
        this.sendData();
    }
    
    public void tick() {
        super.tick();
        if (this.level == null) {
            return;
        }
        if (multiblockStructure != null) {
            multiblockStructure.renderGhostBlocks();
            multiblockStructure.setFluidOutputCapacities();
            isValid = multiblockStructure.isStructureCorrect();
        }
    }
    
    public void invalidate() {
        super.invalidate();
        this.fluidCapability.invalidate();
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.fluidCapability.isPresent()) {
            this.refreshCapability();
        }
        
        return cap == ForgeCapabilities.FLUID_HANDLER ? this.fluidCapability.cast() : super.getCapability(cap, side);
    }
    
    private IFluidHandler handlerForCapability() {
        return this.tankInventory;
    }
    
    public IFluidTank getTankInventory() {
        return this.tankInventory;
    }
    
    private void refreshCapability() {
        LazyOptional<IFluidHandler> oldCap = this.fluidCapability;
        this.fluidCapability = LazyOptional.of(this::handlerForCapability);
        oldCap.invalidate();
    }
    
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.tankInventory.readFromNBT(compound.getCompound("TankContent"));
    }
    
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("TankContent", this.tankInventory.writeToNBT(new CompoundTag()));
    }
    
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
    
    }
    
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!isValid) {
            Lang.translate("goggles.reverbaratory.invalid").style(ChatFormatting.RED).forGoggles(tooltip, 1);
        }   else {
            if (!isPlayerSneaking) {
                Lang.translate("goggles.reverbaratory.stats").style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                if (this.timer > 0) {
                    Lang.translate("goggles.blast_furnace.status.running").style(ChatFormatting.YELLOW).forGoggles(tooltip, 1);
                } else {
                    Lang.translate("goggles.blast_furnace.status.off").style(ChatFormatting.YELLOW).forGoggles(tooltip, 1);
                }
            }
        }
        LazyOptional<IFluidHandler> handler = this.getCapability(ForgeCapabilities.FLUID_HANDLER);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent()) {
            return false;
        } else {
            if (isPlayerSneaking) {
                multiblockStructure.addToGoggleTooltip(tooltip);
                return false;
            }
            IFluidHandler tank = resolve.get();
            if (tank.getTanks() == 0) {
                return false;
            } else {
                LangBuilder mb = Lang.translate("generic.unit.millibuckets");
                boolean isEmpty = true;
                
                for(int i = 0; i < tank.getTanks(); ++i) {
                    FluidStack fluidStack = tank.getFluidInTank(i);
                    if (!fluidStack.isEmpty()) {
                        Lang.fluidName(fluidStack).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                        Lang.builder().add(Lang.number((double)fluidStack.getAmount()).add(mb).style(ChatFormatting.GOLD)).text(ChatFormatting.GRAY, " / ").add(Lang.number((double)tank.getTankCapacity(i)).add(mb).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
                        isEmpty = false;
                    }
                }
                
                if (tank.getTanks() > 1) {
                    if (isEmpty) {
                        tooltip.remove(tooltip.size() - 1);
                    }
                    
                    return true;
                } else if (!isEmpty) {
                    return true;
                } else {
                    Lang.translate("gui.goggles.fluid_container.capacity", new Object[0]).add(Lang.number(tank.getTankCapacity(0)).add(mb).style(ChatFormatting.DARK_GREEN)).style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);
                    return true;
                }
            }
        }
    }
}
