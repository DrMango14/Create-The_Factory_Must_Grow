package com.drmangotea.tfmg.content.electricity.experimental.content.devices.electric_motor;

import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.KineticElectricBlockEntity;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.content.ThreePhaseGeneratorProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
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

public class ElectricMotorBlockEntity extends GeneratingKineticBlockEntity implements IRealisticElectric, IHaveGoggleInformation {




    ElectricalProperties properties;

    public float current = 0;


    protected ScrollOptionBehaviour<WindmillBearingBlockEntity.RotationDirection> movementDirection;

    public ElectricMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        properties = new ElectricMotorProperties(getPos(), state.getValue(DirectionalKineticBlock.FACING));

    }

    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }


    @Override
    public void onUpdated() {

        this.current = 0;

        float current = 0;
        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);
        for (int i = 0; i < 3; i++) {
            Resistance resistor = network.getResistance(getPos(),i);

            if(resistor != null){

                double resistance = resistor.resistance;
                double voltage = resistor.getVoltage(level);
                current += (float) (voltage / resistance);

            }
        }

        this.current = current;

        updateGeneratedRotation();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {


        TFMGLang.text("Current "+current).forGoggles(tooltip);

        return true ;
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

    }









    @Override
    public void initialize() {
        super.initialize();

    }

    @Override
    public float getGeneratedSpeed() {
        if(current == 0)
            return 0;


        int rotation = movementDirection.get() == WindmillBearingBlockEntity.RotationDirection.CLOCKWISE ? 1 : -1;

        float speed = Math.min(255,current*20)*rotation;

        return speed;
    }

    @Override
    public float calculateAddedStressCapacity() {
        float speedModifier = Math.abs(getSpeed()/256);

        if(getBlockState().is(TFMGBlocks.HEAVY_ELECTRIC_MOTOR)){
            return super.calculateAddedStressCapacity();
        }

        return (int)(super.calculateAddedStressCapacity()*speedModifier);
    }

    //@Override
    //public boolean canBeInGroups() {
    //    return true;
    //}

    public float resistance() {

        if(getBlockState().is(TFMGBlocks.HEAVY_ELECTRIC_MOTOR)){
            return TFMGConfigs.common().machines.electricMotorInternalResistance.getF()/9;
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
            if(getBlockState().is(TFMGBlocks.HEAVY_ELECTRIC_MOTOR)){
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
