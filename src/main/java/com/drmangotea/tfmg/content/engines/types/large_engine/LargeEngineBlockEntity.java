package com.drmangotea.tfmg.content.engines.types.large_engine;


import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.engines.base.AbstractEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.base.EngineFluidTank;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGSoundEvents;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;

public class LargeEngineBlockEntity extends AbstractEngineBlockEntity {


    public WeakReference<PoweredShaftBlockEntity> target;


    public EngineFluidTank airTank;

    public IFluidHandler fluidCapabilityy;


    public LargeEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        target = new WeakReference<>(null);
        exhaustTank = new EngineFluidTank(2000, true, false, f->tankUpdated(f,false));
        fuelTank = new EngineFluidTank(2000, false, true, f->tankUpdated(f,true), TFMGTags.TFMGFluidTags.AIR.tag);
        airTank = new EngineFluidTank(1000, false, true, TFMGTags.TFMGFluidTags.AIR.tag, f->tankUpdated(f,true));
        fluidCapabilityy = new CombinedTankWrapper(exhaustTank,fuelTank,airTank);
    }

    @Override
    public void tankUpdated(FluidStack stack, boolean fuelTank) {
        super.tankUpdated(stack, fuelTank);
        sendStuff();
    }

    @Override
    public void refreshCapability() {}

    @Override
    public List<TagKey<Fluid>> getSupportedFuels() {
        return List.of(TFMGTags.TFMGFluidTags.DIESEL.tag, TFMGTags.TFMGFluidTags.KEROSENE.tag, TFMGTags.TFMGFluidTags.NAPHTHA.tag, TFMGTags.TFMGFluidTags.FURNACE_GAS.tag);
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

        if (fuelConsumptionTimer > 2) {
            airTank.forceDrain(150, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    @Override
    public void tick() {
        super.tick();

        PoweredShaftBlockEntity shaft = getShaft();

        if (shaft == null) {
            if (!level.isClientSide()) {

                if (shaft == null)
                    return;
                if (!shaft.getBlockPos()
                        .subtract(worldPosition)
                        .equals(shaft.enginePos))
                    return;
                if (shaft.engineEfficiency == 0)
                    return;
                Direction facing = LargeEngineBlock.getFacing(getBlockState());
                if (level.isLoaded(worldPosition.relative(facing.getOpposite())))
                    shaft.update(worldPosition, 0, 0);
                return;
            }
        }

        BlockState blockState = getBlockState();
        if (!TFMGBlocks.LARGE_ENGINE.has(blockState) && !TFMGBlocks.SIMPLE_LARGE_ENGINE.has(blockState))
            return;

        if(level.isClientSide)
            makeSound();


        if (!level.isClientSide)
            if (getShaft() != null)
                engineProcess();

    }

    @Override
    public float efficiencyModifier() {
        return 0.5f;
    }

    @Override
    public float speedModifier() {
        return 1;
    }

    @Override
    public float torqueModifier() {
        return 1;
    }

    @Override
    public String engineId() {
        return "Large";
    }


    @OnlyIn(Dist.CLIENT)
    private void makeSound() {
        Float targetAngle = getTargetAngle();
        PoweredShaftBlockEntity ste = target.get();
        if (ste == null)
            return;
        if(getShaft().getSpeed()==0)
            return;
        if(fuelTank.isEmpty()||airTank.isEmpty()||exhaustTank.getSpace() == 0)
            return;
        //if (engineStrength == 0)
        //	return;
        PoweredShaftBlockEntity shaft = getShaft();


        if (targetAngle == null)
            return;

        float angle = AngleHelper.deg(targetAngle);
        angle += (angle < 0) ? -180 + 75 : 360 - 75;
        angle %= 360;


        if (shaft == null || shaft.getSpeed() == 0)
            return;

        if (angle >= 0 && !(prevAngle > 180 && angle < 180)) {
            prevAngle = angle;
            return;
        }

        if (angle < 0 && !(prevAngle < -180 && angle > -180)) {
            prevAngle = angle;
            return;
        }

        TFMGSoundEvents.DIESEL_ENGINE.playAt(level, worldPosition, 0.4f * TFMGConfigs.common().machines.engineLoudness.getF(), 1f, false);

        prevAngle = angle;
    }



    @Override
    public boolean canWork() {

        if (airTank.isEmpty())
            return false;


        return super.canWork();
    }

    private void engineProcess() {
        PoweredShaftBlockEntity shaft = getShaft();


        if (!canWork()) {
            shaft.update(worldPosition, 0, 0);
            return;
        }

        boolean isFuelValid = false;
        for(TagKey<Fluid> tag : getSupportedFuels()){
            if(fuelTank.getFluid().getFluid().is(tag))
                isFuelValid = true;
        }


        shaft.update(worldPosition, 2, 15 * getFuelType().getStress()*(isFuelValid?1:0));
        sendData();
        setChanged();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        if(getShaft() == null)
            return false;

        TFMGLang.text("").style(ChatFormatting.GRAY).forGoggles(tooltip);

        TFMGUtils.createFluidTooltip(this,tooltip);

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
        PoweredShaftBlockEntity shaft = target.get();
        if (shaft == null || shaft.isRemoved() || !shaft.canBePoweredBy(worldPosition)) {
            if (shaft != null)
                target = new WeakReference<>(null);
            Direction facing = LargeEngineBlock.getFacing(getBlockState());
            BlockEntity anyShaftAt = level.getBlockEntity(worldPosition.relative(facing, 2));
            if (anyShaftAt instanceof PoweredShaftBlockEntity ps && ps.canBePoweredBy(worldPosition))
                target = new WeakReference<>(shaft = ps);
        }
        return shaft;
    }


    float prevAngle = 0;


    @Nullable
    @OnlyIn(Dist.CLIENT)
    public Float getTargetAngle() {
        float angle = 0;
        BlockState blockState = getBlockState();
        if (!TFMGBlocks.LARGE_ENGINE.has(blockState)&&!TFMGBlocks.SIMPLE_LARGE_ENGINE.has(blockState))
            return null;

        Direction facing = SteamEngineBlock.getFacing(blockState);
        PoweredShaftBlockEntity shaft = getShaft();
        Axis facingAxis = facing.getAxis();
        Axis axis = Axis.Y;

        if (shaft == null)
            return null;

        axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);
        angle = KineticBlockEntityRenderer.getAngleForBe(shaft, shaft.getBlockPos(), axis);

        if (axis == facingAxis)
            return null;
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
                (be, context) -> be.fluidCapabilityy
        );
    }
    @Override
    public int getFuelConsumption() {
        if(getShaft()==null)
            return 0;

        if(isSimpleEngine()&&getShaft()!=null)
            return (int) getShaft().getGeneratedSpeed()/10;

        if(getShaft()==null)
            return 0;
        return (int) getShaft().getGeneratedSpeed()/40;
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