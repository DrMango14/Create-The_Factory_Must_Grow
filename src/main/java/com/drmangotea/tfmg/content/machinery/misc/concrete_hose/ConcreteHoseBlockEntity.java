package com.drmangotea.tfmg.content.machinery.misc.concrete_hose;

import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public class ConcreteHoseBlockEntity extends KineticBlockEntity {

    LerpedFloat offset;
    boolean isMoving;

    public SmartFluidTank internalTank;
    public LazyOptional<IFluidHandler> capability;
    public ConcreteFillingBehavior filler;
    public ConcreteHoseFluidHandler handler;
    public boolean infinite;

    public ConcreteHoseBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        offset = LerpedFloat.linear()
                .startWithValue(0);
        isMoving = true;
        internalTank = new SmartFluidTank(1500, this::onTankContentsChanged);
        handler = new ConcreteHoseFluidHandler(internalTank, filler,
                () -> worldPosition.below((int) Math.ceil(offset.getValue())), () -> !this.isMoving);
        capability = LazyOptional.of(() -> handler);
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
        return offset.getValue(pt);
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
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level.isClientSide)
            return;
        if (isMoving)
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
    protected void write(CompoundTag compound, boolean clientPacket) {
        if (clientPacket)
            offset.forceNextSync();
        compound.put("Offset", offset.writeNBT());
        compound.put("Tank", internalTank.writeToNBT(new CompoundTag()));
        super.write(compound, clientPacket);
        if (clientPacket)
            compound.putBoolean("Infinite", infinite);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        offset.readNBT(compound.getCompound("Offset"), clientPacket);
        internalTank.readFromNBT(compound.getCompound("Tank"));
        super.read(compound, clientPacket);
        if (clientPacket)
            infinite = compound.getBoolean("Infinite");
    }

    @Override
    public void invalidate() {
        super.invalidate();
        capability.invalidate();
    }

    public float getMovementSpeed() {
        float movementSpeed = convertToLinear(getSpeed());
        if (level.isClientSide)
            movementSpeed *= ServerSpeedProvider.get();
        return movementSpeed;
    }
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (isFluidHandlerCap(cap)
                && (side == null || HosePulleyBlock.hasPipeTowards(level, worldPosition, getBlockState(), side)))
            return this.capability.cast();
        return super.getCapability(cap, side);
    }
}
