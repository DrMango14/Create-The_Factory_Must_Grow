package com.drmangotea.tfmg.content.machinery.misc.firebox;

import com.drmangotea.tfmg.base.fluid.ForceableFluidTank;
import com.drmangotea.tfmg.base.fluid.InputOutputTankWrapper;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class FireboxBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IMultiBlockEntityContainer.Fluid {

    private static final int MAX_SIZE = 3;

    protected IFluidHandler fluidCapability;
    protected ForceableFluidTank tankInventory;
    protected ForceableFluidTank exhuastTank;
    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    protected boolean updateConnectivity;
    protected boolean updateCapability;
    protected int width;
    protected int height;

    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;

    boolean running = false;


    public FireboxBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(60);
        tankInventory = new ForceableFluidTank(getCapacityMultiplier(), this::onFluidStackChanged).withValidator(fs -> fs.is(TFMGTags.Fluids.FIREBOX_FUEL.tag));
        exhuastTank = new ForceableFluidTank(getCapacityMultiplier(), this::onFluidStackChanged).blockInsertion();
        fluidCapability = new InputOutputTankWrapper(exhuastTank, tankInventory);
        updateConnectivity = false;
        updateCapability = false;
        height = 1;
        width = 1;
        refreshCapability();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.FIREBOX.get(),
                (be, context) -> be.fluidCapability
        );
    }

    protected void updateConnectivity() {
        updateConnectivity = false;
        if (level == null || level.isClientSide)
            return;
        if (!isController())
            return;
        ConnectivityHandler.formMulti(this);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level == null || level.isClientSide) return;
        FireboxBlockEntity controller = isController() ? this : getControllerBE();

        if (controller == null)
            return;

        boolean wasRunning = running;

        FluidStack toDrain = controller.tankInventory.getFluid().copyWithAmount(TFMGConfigs.common().machines.fireboxFuelConsumption.get());
        FluidStack drainable = controller.tankInventory.forceDrain(toDrain.getAmount(), IFluidHandler.FluidAction.SIMULATE);
        if (canBurn(controller) && !drainable.isEmpty() && FluidStack.isSameFluidSameComponents(toDrain, drainable) && drainable.getAmount() >= TFMGConfigs.common().machines.fireboxFuelConsumption.get()) {
            if (!wasRunning)
                level.setBlock(getBlockPos(), getBlockState().setValue(FireboxBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.FADING), 2);
            running = true;
            controller.tankInventory.forceDrain(drainable.getAmount(), IFluidHandler.FluidAction.EXECUTE);
            if (TFMGConfigs.common().machines.fireboxExhaustRequirement.get()) {
                int toFill = controller.exhuastTank.forceFill(new FluidStack(TFMGFluids.CARBON_DIOXIDE.get().getSource(), 500), IFluidHandler.FluidAction.SIMULATE);
                if (toFill > 0) {
                    controller.exhuastTank.forceFill(new FluidStack(TFMGFluids.CARBON_DIOXIDE.get().getSource(), toFill), IFluidHandler.FluidAction.EXECUTE);
                }
            }
            sendData();
            setChanged();
        } else {
            if (wasRunning)
                level.setBlock(getBlockPos(), getBlockState().setValue(FireboxBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.NONE), 2);
            running = false;
        }
    }

    public boolean canBurn(FireboxBlockEntity controller) {
        if(exhuastTank == null)
            return false;
        if(controller.exhuastTank.getCapacity() - controller.exhuastTank.getFluidAmount() <= 0)
            return false;
        return controller.tankInventory.getFluid().is(TFMGTags.Fluids.FIREBOX_FUEL.tag);
    }

    @Override
    public void tick() {
        super.tick();
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }


        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition)) {
            onPositionChanged();
            return;
        }

        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
        if (updateConnectivity)
            updateConnectivity();

    }


    @Override
    public BlockPos getLastKnownPos() {
        return lastKnownPos;
    }

    @Override
    public boolean isController() {
        return controller == null || worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY() && worldPosition.getZ() == controller.getZ();
    }

    @Override
    public void initialize() {
        super.initialize();
        sendData();
        if (level != null && level.isClientSide)
            invalidateRenderBoundingBox();
    }

    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;
        setChanged();
        sendData();

    }


    @SuppressWarnings("unchecked")
    @Override
    public FireboxBlockEntity getControllerBE() {
        if (isController())
            return this;
        if (level != null && level.getBlockEntity(controller) instanceof FireboxBlockEntity be)
            return be;
        return null;
    }

    public void applyFluidTankSize(int blocks) {
        tankInventory.setCapacity(blocks * getCapacityMultiplier());
        exhuastTank.setCapacity(blocks * getCapacityMultiplier());
        int overflow = tankInventory.getFluidAmount() - tankInventory.getCapacity();
        if (overflow > 0)
            tankInventory.drain(overflow, IFluidHandler.FluidAction.EXECUTE);
        int exhaustOverflow = exhuastTank.getFluidAmount() - exhuastTank.getCapacity();
        if (exhaustOverflow > 0)
            exhuastTank.drain(exhaustOverflow, IFluidHandler.FluidAction.EXECUTE);
    }

    public void removeController(boolean keepFluids) {
        if (level == null || level.isClientSide)
            return;
        updateConnectivity = true;
        if (!keepFluids)
            applyFluidTankSize(1);
        controller = null;
        width = 1;
        height = 1;
        onFluidStackChanged(tankInventory.getFluid());

        refreshCapability();
        setChanged();
        sendData();
    }


    public void sendDataImmediately() {
        syncCooldown = 0;
        queuedSync = false;
        sendData();
    }

    @Override
    public void sendData() {
        if (syncCooldown > 0) {
            queuedSync = true;
            return;
        }
        super.sendData();
        queuedSync = false;
        syncCooldown = SYNC_RATE;
    }

    @Override
    public void setController(BlockPos controller) {
        if (level == null || level.isClientSide && !isVirtual())
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;
        refreshCapability();
        setChanged();
        sendData();
    }

    private void refreshCapability() {
        //LazyOptional<IFluidHandler> oldCap = fluidCapability;
        fluidCapability = handlerForCapability();
        invalidateCapabilities();
    }

    private IFluidHandler handlerForCapability() {
        return isController() ? new InputOutputTankWrapper(exhuastTank, tankInventory)
                : getControllerBE() != null ? getControllerBE().handlerForCapability() : new FluidTank(0);
    }

    @Override
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        if (isController())
            return super.createRenderBoundingBox().expandTowards(width - 1, height - 1, width - 1);
        else
            return super.createRenderBoundingBox();
    }

    @Nullable
    public FireboxBlockEntity getOtherFireboxBlockEntity(Direction direction) {
        if (level != null && level.getBlockEntity(worldPosition.relative(direction)) instanceof FireboxBlockEntity otherBE)
            return otherBE;
        return null;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        FireboxBlockEntity controllerBE = getControllerBE();
        if (controllerBE == null)
            return false;
        return containedFluidTooltip(tooltip, isPlayerSneaking, fluidCapability);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);

        BlockPos controllerBefore = controller;
        int prevSize = width;
        int prevHeight = height;

        updateConnectivity = compound.contains("Uninitialized");

        running = compound.getBoolean("IsRunning");

        lastKnownPos = NbtUtils.readBlockPos(compound,"LastKnownPos").orElse(null);
        controller = NbtUtils.readBlockPos(compound,"Controller").orElse(null);

        if (isController()) {
            width = compound.getInt("Size");
            height = compound.getInt("Height");
            tankInventory.setCapacity(getTotalTankSize() * getCapacityMultiplier());
            tankInventory.readFromNBT(registries,compound.getCompound("TankContent"));
            exhuastTank.setCapacity(getTotalTankSize() * getCapacityMultiplier());
            exhuastTank.readFromNBT(registries,compound.getCompound("Exhaust"));
            if (tankInventory.getSpace() < 0)
                tankInventory.forceDrain(-tankInventory.getSpace(), IFluidHandler.FluidAction.EXECUTE);
            if (exhuastTank.getSpace() < 0)
                exhuastTank.forceDrain(-exhuastTank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }

        updateCapability = true;

        if (!clientPacket)
            return;

        boolean changeOfController = !Objects.equals(controllerBefore, controller);
        if (changeOfController || prevSize != width || prevHeight != height) {
            if (level != null)
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            if (isController()) {
                tankInventory.setCapacity(getCapacityMultiplier() * getTotalTankSize());
                exhuastTank.setCapacity(getCapacityMultiplier() * getTotalTankSize());
            }
            invalidateRenderBoundingBox();
        }


    }

    public float getFillState() {
        return (float) tankInventory.getFluidAmount() / tankInventory.getCapacity();
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putBoolean("IsRunning", running);
        if (updateConnectivity)
            compound.putBoolean("Uninitialized", true);
        if (lastKnownPos != null)
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
        if (!isController())
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        if (isController()) {
            compound.put("TankContent", tankInventory.writeToNBT(registries,new CompoundTag()));
            compound.put("Exhaust", exhuastTank.writeToNBT(registries,new CompoundTag()));
            compound.putInt("Size", width);
            compound.putInt("Height", height);
        }
        super.write(compound,registries , clientPacket);


    }

   // @Nonnull
   // @Override
   // public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
   //     if (!fluidCapability.isPresent())
   //         refreshCapability();
   //     if (cap == ForgeCapabilities.FLUID_HANDLER)
   //         return fluidCapability.cast();
   //     return super.getCapability(cap, side);
   // }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        registerAwardables(behaviours, AllAdvancements.STEAM_ENGINE_MAXED, AllAdvancements.PIPE_ORGAN);
    }

    public IFluidTank getTankInventory() {
        return tankInventory;
    }

    public int getTotalTankSize() {
        return width * width * height;
    }

    public static int getMaxSize() {
        return MAX_SIZE;
    }

    public static int getCapacityMultiplier() {
        return AllConfigs.server().fluids.fluidTankCapacity.get() * 1000;
    }

    public static int getMaxHeight() {
        return 1;
    }

    @Override
    public void preventConnectivityUpdate() {
        updateConnectivity = false;
    }

    @Override
    public void notifyMultiUpdated() {
        onFluidStackChanged(tankInventory.getFluid());
        setChanged();
    }

    @Override
    public Direction.Axis getMainConnectionAxis() {
        return Direction.Axis.Y;
    }

    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        if (longAxis == Direction.Axis.Y)
            return getMaxHeight();
        return getMaxWidth();
    }

    @Override
    public int getMaxWidth() {
        return MAX_SIZE;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public boolean hasTank() {
        return true;
    }

    @Override
    public int getTankSize(int tank) {
        return getCapacityMultiplier();
    }

    @Override
    public void setTankSize(int tank, int blocks) {
        applyFluidTankSize(blocks);
    }

    @Override
    public IFluidTank getTank(int tank) {
        return tankInventory;
    }

    @Override
    public FluidStack getFluid(int tank) {
        return tankInventory.getFluid()
                .copy();
    }
}
