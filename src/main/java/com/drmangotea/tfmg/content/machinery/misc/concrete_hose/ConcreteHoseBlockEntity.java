package com.drmangotea.tfmg.content.machinery.misc.concrete_hose;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

public class ConcreteHoseBlockEntity extends KineticBlockEntity {
    LerpedFloat offset;
    boolean isMoving;

    public SmartFluidTank internalTank;
    public IFluidHandler capability;
    public ConcreteFillingBehavior filler;
    public ConcreteHoseFluidHandler handler;
    public boolean infinite;
    protected boolean updateCapability;

    public ConcreteHoseBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        offset = LerpedFloat.linear()
                .startWithValue(0);
        isMoving = true;
        internalTank = new SmartFluidTank(1500, this::onTankContentsChanged);
        handler = new ConcreteHoseFluidHandler(internalTank, filler,
                () -> worldPosition.below((int) Math.ceil(offset.getValue())), () -> !this.isMoving);
        capability = handler;
        updateCapability = false;
        refreshCapability();
    }
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.CONCRETE_HOSE.get(),
                (be, context) -> {
                    if (context == null || HosePulleyBlock.hasPipeTowards(be.level, be.worldPosition, be.getBlockState(), context))
                        return be.handler;
                    return null;
                }
        );
    }
    @Override
    public void sendData() {
        infinite = filler.isInfinite();
        super.sendData();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean addToGoggleTooltip = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if (infinite)
            TooltipHelper.addHint(tooltip, "hint.hose_pulley");
        return addToGoggleTooltip;
    }

    public float getInterpolatedOffset(float pt) {
        return Math.max(offset.getValue(pt), 3 / 16f);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        filler = new ConcreteFillingBehavior(this);
        behaviours.add(filler);
        super.addBehaviours(behaviours);
    }

    protected void onTankContentsChanged(FluidStack contents) {}

    @Override
    public void onSpeedChanged(float previousSpeed) {
        isMoving = true;
        if (getSpeed() == 0) {
            offset.forceNextSync();
            offset.setValue(Math.round(offset.getValue()));
            isMoving = false;
        }

        if (isMoving) {
            float newOffset = offset.getValue() + getMovementSpeed();
            if (newOffset < 0)
                isMoving = false;
            if (!level.getBlockState(worldPosition.below((int) Math.ceil(newOffset)))
                    .canBeReplaced()) {
                isMoving = false;
            }
            if (isMoving) {
                filler.reset();
            }
        }

        super.onSpeedChanged(previousSpeed);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().expandTowards(0, -offset.getValue(), 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null) return;
        float newOffset = offset.getValue() + getMovementSpeed();
        if (newOffset < 0) {
            newOffset = 0;
            isMoving = false;
        }
        if (!level.getBlockState(worldPosition.below((int) Math.ceil(newOffset)))
                .canBeReplaced()) {
            newOffset = (int) newOffset;
            isMoving = false;
        }
        if (getSpeed() == 0)
            isMoving = false;

        offset.setValue(newOffset);
        invalidateRenderBoundingBox();

        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (isMoving || level == null || level.isClientSide)
            return;

        int ceil = (int) Math.ceil(offset.getValue() + getMovementSpeed());
        if (getMovementSpeed() > 0 && level.getBlockState(worldPosition.below(ceil))
                .canBeReplaced()) {
            isMoving = true;
            filler.reset();
            return;
        }

        sendData();
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if (clientPacket)
            offset.forceNextSync();
        compound.put("Offset", offset.writeNBT());
        compound.put("Tank", internalTank.writeToNBT(registries,new CompoundTag()));
        super.write(compound,registries , clientPacket);
        if (clientPacket)
            compound.putBoolean("Infinite", infinite);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        offset.readNBT(compound.getCompound("Offset"), clientPacket);
        internalTank.readFromNBT(registries,compound.getCompound("Tank"));
        super.read(compound,registries , clientPacket);
        if (clientPacket)
            infinite = compound.getBoolean("Infinite");

        updateCapability = true;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    public void refreshCapability() {
        capability = internalTank;
        invalidateCapabilities();
    }

    public float getMovementSpeed() {
        float movementSpeed = convertToLinear(getSpeed());
        if (level != null && level.isClientSide)
            movementSpeed *= ServerSpeedProvider.get();
        return movementSpeed;
    }
}
