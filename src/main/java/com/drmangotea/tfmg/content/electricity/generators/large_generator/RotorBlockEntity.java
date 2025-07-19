package com.drmangotea.tfmg.content.electricity.generators.large_generator;


import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.base.KineticElectricBlockEntity;
import com.drmangotea.tfmg.content.electricity.generators.large_generator.StatorBlock.StatorState;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGSoundEvents;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.*;

import static com.drmangotea.tfmg.content.electricity.generators.large_generator.StatorBlock.STATOR_STATE;
import static com.drmangotea.tfmg.content.electricity.generators.large_generator.StatorBlock.VALUE;
import static com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock.AXIS;
import static net.minecraft.core.Direction.*;
import static net.minecraft.world.level.block.DirectionalBlock.FACING;


public class RotorBlockEntity extends KineticElectricBlockEntity {


    LerpedFloat visualSpeed = LerpedFloat.linear();
    float angle;
    boolean findNextTick = false;


    List<BlockPos> stators = new ArrayList<>();

    public static final Map<Axis, Map<StatorOffset, BlockState>> statorPosition = setStatorPositons();

    public RotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(2);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        visualSpeed.chase(getGeneratedSpeed(), 1 / 128f, LerpedFloat.Chaser.EXP);
    }

    @Override
    public void tick() {
        super.tick();
        manageRotation();
        if(findNextTick){
            findStators();
            findNextTick = false;
        }

        if (level.isClientSide()) {
            float speed = Math.abs(visualSpeed.getValue());
            float minSpeed = TFMGConfigs.common().machines.largeGeneratorMinSpeed.getF();

            // Only play sound if above minimum speed
            if (speed > minSpeed) {
                float maxSpeed = 255f; // Max expected speed
                // Normalize speed between 0-1 range (clamped)
                float normalizedSpeed = Math.min(1.0f, (speed - minSpeed) / (maxSpeed - minSpeed));

                // Volume scales from 0.1 to 1.0 with speed
                float volume = 0.1f + (0.9f * normalizedSpeed);

                // Pitch scales from 0.5 to 1.0 with speed (Java clamps below 0.5)
                float pitch = 0.5f + (0.5f * normalizedSpeed);

                TFMGSoundEvents.GENERATOR_HUM.playAt(level, worldPosition, volume, pitch, false);
            }
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        findStators();
    }

    @Override
    public void onPlaced() {
        super.onPlaced();
        findStators();
        updateNextTick();
    }

    @Override
    public int voltageGeneration() {
        return (int) Math.min(3000, generation() * 3);
    }

    public int generation() {

        if (stators.size() != 8)
            return 0;

        float modifier = TFMGConfigs.common().machines.largeGeneratorModifier.getF();
        float minSpeed = TFMGConfigs.common().machines.largeGeneratorMinSpeed.getF();

        return (int) Math.max(0, ((Math.abs(getSpeed()) - minSpeed) * modifier));
    }

    @Override
    public int powerGeneration() {
        return (int) (generation() * 40*1.84563);
    }

    public void findStators() {
        Axis axis = getBlockState().getValue(AXIS);

        Map<StatorOffset, BlockState> position = statorPosition.get(axis);
        stators = new ArrayList<>();
        position.forEach(((offset, state) -> {
            BlockPos pos = getBlockPos().relative(offset.direction1);
            if (offset.direction2.isPresent())
                pos = pos.relative(offset.direction2.get());

            if (level.getBlockEntity(pos) instanceof StatorBlockEntity be) {

                if (be.rotor == null || be.rotor == getBlockPos()) {

                    stators.add(pos);
                    level.setBlock(pos, state, 2);
                    be.rotor = getBlockPos();

                } else {
                    stators = new ArrayList<>();
                }
            } else {
                stators = new ArrayList<>();
            }

        }));
    }

    public void manageRotation() {
        float targetSpeed = getSpeed();
        visualSpeed.updateChaseTarget(targetSpeed);
        visualSpeed.tickChaser();
        angle += visualSpeed.getValue() * 3 / 10f;
        angle %= 360;
    }


    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction.getAxis() != getBlockState().getValue(AXIS);
    }

    public static Map<Axis, Map<StatorOffset, BlockState>> setStatorPositons() {
        Map<Axis, Map<StatorOffset, BlockState>> statorPositions = new HashMap<>();

        BlockState defaultState = TFMGBlocks.STATOR.getDefaultState();

        BlockState cornerState = defaultState.setValue(STATOR_STATE, StatorState.CORNER);
        BlockState horizontal = defaultState.setValue(STATOR_STATE, StatorState.CORNER_HORIZONTAL);
        BlockState sideState = defaultState.setValue(STATOR_STATE, StatorState.SIDE);
        BlockState cornerFlipped = cornerState.setValue(VALUE, false);
        BlockState sideFlipped = sideState.setValue(VALUE, false);
        Map<StatorOffset, BlockState> xPos = new HashMap<>();
        xPos.put(pos(UP), sideFlipped.setValue(FACING, DOWN));
        xPos.put(pos(DOWN), sideFlipped.setValue(FACING, UP));
        xPos.put(pos(NORTH), sideFlipped.setValue(FACING, SOUTH));
        xPos.put(pos(SOUTH), sideFlipped.setValue(FACING, NORTH));
        xPos.put(pos(UP, NORTH), cornerState.setValue(FACING, EAST));
        xPos.put(pos(UP, SOUTH), cornerState.setValue(FACING, WEST));
        xPos.put(pos(DOWN, NORTH), cornerFlipped.setValue(FACING, EAST));
        xPos.put(pos(DOWN, SOUTH), cornerFlipped.setValue(FACING, WEST));
        Map<StatorOffset, BlockState> yPos = new HashMap<>();
        yPos.put(pos(EAST), sideState.setValue(FACING, WEST));
        yPos.put(pos(WEST), sideState.setValue(FACING, EAST));
        yPos.put(pos(SOUTH), sideState.setValue(FACING, NORTH));
        yPos.put(pos(NORTH), sideState.setValue(FACING, SOUTH));
        yPos.put(pos(NORTH, WEST), horizontal.setValue(FACING, NORTH));
        yPos.put(pos(SOUTH, EAST), horizontal.setValue(FACING, SOUTH));
        yPos.put(pos(SOUTH, WEST), horizontal.setValue(FACING, WEST));
        yPos.put(pos(NORTH, EAST), horizontal.setValue(FACING, EAST));
        Map<StatorOffset, BlockState> zPos = new HashMap<>();
        zPos.put(pos(UP), sideState.setValue(FACING, DOWN));
        zPos.put(pos(DOWN), sideState.setValue(FACING, UP));
        zPos.put(pos(WEST), sideFlipped.setValue(FACING, EAST));
        zPos.put(pos(EAST), sideFlipped.setValue(FACING, WEST));
        zPos.put(pos(UP, WEST), cornerState.setValue(FACING, UP));
        zPos.put(pos(UP, EAST), cornerState.setValue(FACING, SOUTH));
        zPos.put(pos(DOWN, WEST), cornerFlipped.setValue(FACING, UP));
        zPos.put(pos(DOWN, EAST), cornerFlipped.setValue(FACING, SOUTH));
        statorPositions.put(Axis.X, xPos);
        statorPositions.put(Axis.Y, yPos);
        statorPositions.put(Axis.Z, zPos);
        return statorPositions;
    }

    private static StatorOffset pos(Direction dir1) {
        return new StatorOffset(dir1, Optional.empty());
    }

    private static StatorOffset pos(Direction dir1, Direction dir2) {
        return new StatorOffset(dir1, Optional.of(dir2));
    }


    private static class StatorOffset {
        public final Direction direction1;
        public final Optional<Direction> direction2;

        public StatorOffset(Direction dir1, Optional<Direction> dir2) {
            this.direction1 = dir1;
            this.direction2 = dir2;

        }


    }


}
