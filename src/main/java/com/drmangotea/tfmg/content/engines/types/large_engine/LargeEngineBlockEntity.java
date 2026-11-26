package com.drmangotea.tfmg.content.engines.types.large_engine;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.engines.base.AbstractEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.base.EngineFluidTank;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelType;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGSoundEvents;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class LargeEngineBlockEntity extends AbstractEngineBlockEntity {
    public WeakReference<PoweredShaftBlockEntity> target;
    public EngineFluidTank airTank;
    public IFluidHandler fluidCapability;
	float prevAngle = 0;

    public LargeEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        target = new WeakReference<>(null);
        exhaustTank = EngineFluidTank.exhaustTank(2000, f->tankUpdated(f,false));
        fuelTank = EngineFluidTank.fuelTank(2000, f->tankUpdated(f,true));
        airTank = EngineFluidTank.airTank(1000, f->tankUpdated(f,true));
        fluidCapability = new CombinedTankWrapper(exhaustTank, fuelTank, airTank);
    }

    @Override
    public CombinedInvWrapper itemHandler() {
        return new CombinedInvWrapper();
    }

    @Override
    public void tankUpdated(FluidStack stack, boolean fuelTank) {
        super.tankUpdated(stack, fuelTank);
        sendData();
    }

    @Override
    public void refreshCapability() {}

    @Override
    public Predicate<FluidStack> validFuels() {
		return fs -> level != null && EngineFuelType.test(fs, getFuelTypeTag(), level.registryAccess());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));
    }

    public boolean isSimpleEngine(){
        return TFMGBlocks.SIMPLE_LARGE_ENGINE.has(getBlockState());
    }

    @Override
    public IFluidHandler handlerForCapability() {
        return new CombinedTankWrapper(fuelTank, exhaustTank, airTank);
    }

    @Override
    public void manageFuelAndExhaust() {
        super.manageFuelAndExhaust();
        airTank.forceDrain(50, IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public void tick() {
        super.tick();

        PoweredShaftBlockEntity shaft = getShaft();
        if (level == null || shaft == null) return;

        if (!TFMGBlocks.LARGE_ENGINE.has(getBlockState()) && !TFMGBlocks.SIMPLE_LARGE_ENGINE.has(getBlockState()))
            return;

        if (level.isClientSide && shaft.getSpeed() != 0 && canWork()) {
			Float targetAngle = getTargetAngle();
			if (targetAngle != null) {
				float angle = ((targetAngle * Mth.RAD_TO_DEG) + ((targetAngle < 0) ? -180 + 75 : 360 - 75)) % 360;
				
				if (!(angle >= 0 && !(prevAngle > 180 && angle < 180)) && !(angle < 0 && !(prevAngle < -180 && angle > -180))) {
					makeSound();
				}
				
				prevAngle = angle;
			}
        }

        if (!level.isClientSide) {
			if (!canWork()) {
				shaft.update(worldPosition, 0, 0);
				return;
			}
			
			boolean isFuelValid = validFuels().test(fuelTank.getFluid());
			
			shaft.update(worldPosition, 2, 15 * torqueModifier() * (isFuelValid ? 1 : 0));
			sendData();
			setChanged();
		}
    }

    @Override
    public float efficiencyModifier() {
        return super.efficiencyModifier() * 0.5f;
    }
	
	@Override
	public Optional<EngineFuelType> getFuelType () {
		if (level == null) return Optional.empty();
        return EngineFuelType.find(fuelTank.getFluid(), level.registryAccess(), getFuelTypeTag());
	}
	
	public TagKey<EngineFuelType> getFuelTypeTag () {
		return getBlockState().getBlock() instanceof LargeEngineBlock le ? le.getFuelTypeTag() : null;
	}
    
    @OnlyIn(Dist.CLIENT)
    private void makeSound() {
		TFMGSoundEvents.DIESEL_ENGINE.playAt(level, worldPosition, 0.4f * TFMGConfigs.common().machines.engineLoudness.getF(), 1f, false);
    }

    @Override
    public boolean canWork() {
        if (airTank.isEmpty())
            return false;

        return super.canWork();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean tanksEmpty = fuelTank.isEmpty() && airTank.isEmpty() && exhaustTank.isEmpty();
        if(getShaft() == null || tanksEmpty)
            return false;
        TFMGTexts.header("large_engine").forGoggles(tooltip);
		TFMGUtils.createFluidTooltip(tooltip, true, fuelTank, airTank, exhaustTank);

        return true;
    }

    @Override
    public void remove() {
        PoweredShaftBlockEntity shaft = getShaft();
        if (shaft != null)
            shaft.remove(worldPosition);
        super.remove();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(2);
    }

    public PoweredShaftBlockEntity getShaft() {
        if (level == null) return null;
        PoweredShaftBlockEntity shaft = target.get();
        if (shaft == null || shaft.isRemoved() || !shaft.canBePoweredBy(worldPosition)) {
            if (shaft != null)
                target = new WeakReference<>(null);
            Direction facing = LargeEngineBlock.getFacing(getBlockState());
            BlockEntity anyShaftAt = level.getBlockEntity(worldPosition.relative(facing, 2));
            if (anyShaftAt instanceof PoweredShaftBlockEntity ps && ps.canBePoweredBy(worldPosition)) {
				shaft = ps;
				target = new WeakReference<>(shaft);
            }
        }
        return shaft;
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public Float getTargetAngle() {
        float angle;
        BlockState blockState = getBlockState();
        if (!TFMGBlocks.LARGE_ENGINE.has(blockState)&&!TFMGBlocks.SIMPLE_LARGE_ENGINE.has(blockState))
            return null;
		
		PoweredShaftBlockEntity shaft = getShaft();
		if (shaft == null)
			return null;
		
		Direction facing = LargeEngineBlock.getFacing(blockState);
        Axis facingAxis = facing.getAxis();
        Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);
		if (axis == facingAxis)
			return null;
		
        angle = KineticBlockEntityRenderer.getAngleForBe(shaft, shaft.getBlockPos(), axis);
        if (axis.isHorizontal() && (facingAxis == Axis.X ^ facing.getAxisDirection() == AxisDirection.POSITIVE))
            angle *= -1;
        if (axis == Axis.X && facing == Direction.DOWN)
            angle *= -1;
        return angle;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.put("Air", airTank.writeToNBT(registries,new CompoundTag()));
        super.write(compound,registries , clientPacket);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.LARGE_ENGINE.get(),
                (be, context) -> be.fluidCapability
        );
    }
	
    @Override
    public int getFuelConsumption() {
		PoweredShaftBlockEntity shaft = getShaft();
        if (shaft == null) return 0;

        if (isSimpleEngine())
            return (int) shaft.getGeneratedSpeed()/10;
		
		return (int) shaft.getGeneratedSpeed()/40;
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        airTank.readFromNBT(registries,compound.getCompound("Air"));
        super.read(compound,registries , clientPacket);
    }

    @Override
    public void invalidate() {
        super.invalidate();

        invalidateCapabilities();
    }

    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }
}
