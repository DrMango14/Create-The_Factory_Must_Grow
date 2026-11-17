package com.drmangotea.tfmg.content.electricity.content.devices.electric_motor;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class ElectricMotorBlockEntity extends GeneratingKineticBlockEntity implements IElectric, IHaveGoggleInformation {


    ElectricalProperties properties;

    public double[] voltage = new double[3];
    public double[] power = new double[3];


    public boolean supplyTooUnbalanced = false;




    protected ScrollOptionBehaviour<WindmillBearingBlockEntity.RotationDirection> movementDirection;

    public ElectricMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        properties = new ElectricMotorProperties(getPos(), state.getValue(DirectionalKineticBlock.FACING));

    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateGeneratedRotation();
    }

    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }

    public double getPower() {
        double power = 0;

        for (int i = 0; i < 3; i++) {

            double powerAddition = this.power[i];

            power += powerAddition;
        }

        return power;
    }

    public double getVoltage() {

        return (voltage[0] + voltage[1] + voltage[2]) / 3;
    }


    @Override
    public void onUpdated() {
        ElectricalNetwork network = ElectricNetworkManager.getNetwork(level);
        for (int i = 0; i < 3; i++) {
            Resistance r = network.getResistance(getPos(), i);
            if (!(r == null)) {

                double resistance = r.resistance;
                double voltage = r.getVoltage(level);
                double current = voltage / resistance;
                double power = voltage * current;


                this.voltage[i] = voltage;
                this.power[i] = power;
            }
        }
        for (int i = 0; i < 3; i++) {

            if (i != 0) {
                double avgPower = (power[0] + power[1] + power[2]) / 3;
                double phasePower = power[i];

                double mismatchPercentage = (avgPower / phasePower);
                if (mismatchPercentage > 1)
                    mismatchPercentage--;
                mismatchPercentage *= 100;
                this.supplyTooUnbalanced = mismatchPercentage > 10;

            }


        }

        TFMG.LOGGER.debug("Power " + getPower());
        updateGeneratedRotation();
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        TFMGTexts.Multimeter.power(getPower()).forGoggles(tooltip);
        TFMGTexts.Multimeter.voltage(getVoltage()).forGoggles(tooltip);

        return true;
    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        movementDirection = new ScrollOptionBehaviour<>(WindmillBearingBlockEntity.RotationDirection.class,
                CreateLang.translateDirect("contraptions.windmill.rotation_direction"), this, new MotorValueBox());

        movementDirection.withCallback($ -> onDirectionChanged());
        behaviours.add(movementDirection);
    }

    private void onDirectionChanged() {
        updateGeneratedRotation();
    }


    @Override
    public void initialize() {
        super.initialize();

    }

    @Override
    public void onPlace() {
        IElectric.super.onPlace();
        updateGeneratedRotation();
    }

    @Override
    public float getGeneratedSpeed() {


        if (getPower() == 0)
            return 0;

        double avgPower = (power[0] + power[1] + power[2]) / 3;
        for (int i = 0; i < 3; i++) {

            double phasePower = power[i];
            if (phasePower == 0) {
                return 0;
            }
            double ratio = Math.abs((avgPower / phasePower) - 1) * 100;

           // if (ratio > 50) {
           //     return 0;
           // }


        }

        int rotation = movementDirection.get() == WindmillBearingBlockEntity.RotationDirection.CLOCKWISE ? 1 : -1;

        double speed = Math.min(255, (getPower()) );
        if (Double.isNaN(speed))
            return 0;
        return (int) speed* rotation;
    }

    @Override
    public float calculateAddedStressCapacity() {
        float speedModifier = Math.abs(getSpeed() / 256);
        if (getPower() == 0 || supplyTooUnbalanced)
            return 0;
        if (getBlockState().is(TFMGBlocks.HEAVY_ELECTRIC_MOTOR)) {
            return super.calculateAddedStressCapacity();
        }

        return (int) (super.calculateAddedStressCapacity() * speedModifier);
    }

    //@Override
    //public boolean canBeInGroups() {
    //    return true;
    //}

    public float resistance() {

        if (getBlockState().is(TFMGBlocks.HEAVY_ELECTRIC_MOTOR)) {
            return TFMGConfigs.common().machines.electricMotorInternalResistance.getF() / 9;
        }

        return TFMGConfigs.common().machines.electricMotorInternalResistance.getF();
    }

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public Level getWorld() {
        return level;
    }

    @Override
    public ElectricalProperties getProperties() {
        return properties;
    }

    class MotorValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            if (getBlockState().is(TFMGBlocks.HEAVY_ELECTRIC_MOTOR)) {
                return VecHelper.voxelSpace(8, 8, 14.5);
            }
            return VecHelper.voxelSpace(8, 8, 12.5);
        }

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Direction facing = state.getValue(FACING);
            return super.getLocalOffset(level, pos, state).add(Vec3.atLowerCornerOf(facing.getNormal())
                    .scale(-1 / 16f));
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            super.rotate(level, pos, state, ms);
            Direction facing = state.getValue(FACING);
            if (facing.getAxis() == Direction.Axis.Y)
                return;
            if (getSide() != Direction.UP)
                return;
            TransformStack.of(ms)
                    .rotateZ(-AngleHelper.horizontalAngle(facing) + 180);
        }


        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            Direction facing = state.getValue(FACING);
            if (facing.getAxis() != Direction.Axis.Y && direction == Direction.DOWN || direction == Direction.UP)
                return false;
            return direction.getAxis() != facing.getAxis();
        }

    }
}
