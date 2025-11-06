package com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class BlastFurnaceHatchBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {


    public FluidTank tank;

    public SmartInventory inventory;

    public LazyOptional<IFluidHandler> fluidCapability;

    public LazyOptional<IItemHandlerModifiable> itemCapability;


    public BlastFurnaceHatchBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
        tank = TFMGUtils.createTank(4000, true, this::onFluidChanged);
        inventory = new SmartInventory(1, this).withMaxStackSize(64);
        fluidCapability = LazyOptional.of(() -> tank);
        itemCapability = LazyOptional.of(()-> inventory);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        return TFMGUtils.createFluidTooltip(this,tooltip);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        dropItems();
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inventory);
    }

    public void dropItems(){

        if(level.getBlockState(getBlockPos().below()).isAir()){
            Vec3 dropVec = VecHelper.getCenterOf(worldPosition)
                    .add(0, -12 / 16f, 0);
            ItemEntity dropped = new ItemEntity(level, dropVec.x, dropVec.y, dropVec.z, inventory.getItem(0).copy());
            dropped.setDefaultPickUpDelay();
            dropped.setDeltaMovement(0, -.25f, 0);
            level.addFreshEntity(dropped);
            inventory.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        tank.readFromNBT(compound.getCompound("TankContent"));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("TankContent", tank.writeToNBT(new CompoundTag()));


    }

    private void onFluidChanged(FluidStack stack) {
        if (!hasLevel())
            return;

        if (!level.isClientSide) {
            setChanged();
            sendData();
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {

        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidCapability.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemCapability.cast();
        }
        return super.getCapability(cap, side);
    }



}
