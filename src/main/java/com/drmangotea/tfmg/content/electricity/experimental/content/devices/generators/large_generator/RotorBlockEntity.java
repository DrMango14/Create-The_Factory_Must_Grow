package com.drmangotea.tfmg.content.electricity.experimental.content.devices.generators.large_generator;


import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.content.devices.generators.large_generator.StatorBlock.StatorState;
import com.drmangotea.tfmg.content.electricity.experimental.content.devices.generators.three_phase_generator.ThreePhaseGeneratorProperties;
import com.drmangotea.tfmg.content.electricity.experimental.packets.UpdateVoltagePacket;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.*;

import static com.drmangotea.tfmg.content.electricity.experimental.content.devices.generators.large_generator.StatorBlock.STATOR_STATE;
import static com.drmangotea.tfmg.content.electricity.experimental.content.devices.generators.large_generator.StatorBlock.VALUE;
import static com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock.AXIS;
import static net.minecraft.core.Direction.*;
import static net.minecraft.world.level.block.DirectionalBlock.FACING;


public class RotorBlockEntity extends KineticBlockEntity implements IRealisticElectric, IHaveGoggleInformation {

    ElectricalProperties properties;

    LerpedFloat visualSpeed = LerpedFloat.linear();
    float angle;
    boolean findNextTick = false;

    double[] voltage = new double[3];
    double[] internalVoltage = new double[3];
    double[] power = new double[3];
    double[] maxPower = new double[3];

    List<BlockPos> stators = new ArrayList<>();

    public static final Map<Axis, Map<StatorOffset, BlockState>> statorPosition = setStatorPositons();

    public RotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        properties = new ThreePhaseGeneratorProperties(getPos(), Direction.NORTH);
    }

    @Override
    public ElectricalProperties getProperties() {
        return properties;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(2);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        visualSpeed.chase(getGeneratedSpeed(), 1 / 128f, LerpedFloat.Chaser.EXP);
    }

    @Override
    public void onUpdated() {


        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);

        for (int i = 0; i < 3; i++) {
            Resistance r = network.getResistance(getPos(), i);
            IdealVoltageSource s = network.getVoltageSource(getPos(), i);
            if (!(r == null || s == null)) {

                double offset = r.getVoltageComplex(level).phaseDegrees() - s.phaseOffset;

                double voltage = r.getVoltage(level);
                double current = voltage / r.resistance;
                double power = s.amplitude * current;
                double maxPower = s.power;


                this.voltage[i] = s.amplitude;
                this.internalVoltage[i] = voltage;
                this.power[i] = power;
                this.maxPower[i] = maxPower;


                double overloadIntensity = 0;

                //if (maxPower < power)
                //    overloadIntensity = power / maxPower;

                if (overloadIntensity > 2) {
                    getLevel().destroyBlock(BlockPos.of(getPos()), false);
                    level.setBlock(getBlockPos(), Blocks.FIRE.defaultBlockState(), 3);
                    return;

                }
                if (overloadIntensity > 1.5) {
                    getLevel().destroyBlock(BlockPos.of(getPos()), false);
                    return;
                }
                if (overloadIntensity > 1.1) {
                    getLevel().destroyBlock(BlockPos.of(getPos()), true);

                }
            }


        }


        //float overloadIntensity = 0;
//
        //for (Pair<Pair<Integer, Integer>, Pair<Float, Float>> generationData : getVoltageGeneration()) {
//
        //    float maxPower = generationData.getSecond().getFirst();
        //    float voltage = generationData.getFirst().getFirst();
        //    float current = generationData.getSecond().getSecond();
        //    float power = current * voltage;
//
        //    if (maxPower < current * voltage)
        //        overloadIntensity = power / maxPower;
        //}
        //if (overloadIntensity > 2) {
        ////    getLevel().destroyBlock(BlockPos.of(getPos()), false);
        // //   level.setBlock(getBlockPos(), Blocks.FIRE.defaultBlockState(), 3);
        //    return;
//
        //}
        //if (overloadIntensity > 1.5) {
        //  //  getLevel().destroyBlock(BlockPos.of(getPos()), false);
        //    return;
        //}
        //if (overloadIntensity > 1.1) {
        //    //getLevel().destroyBlock(BlockPos.of(getPos()), true);
//
        //}
    }

    @Override
    public void tick() {
        super.tick();
        manageRotation();
        if (findNextTick) {
            findStators();
            findNextTick = false;
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        int oldStatorCount = stators.size();
        findStators();
        int statorCount = stators.size();


        if (statorCount != oldStatorCount) {
    updateVoltage();

        }
    }

    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }

    public void updateVoltage() {

        RealElectricalNetwork electricalNetwork = RealElectricNetworkManager.getNetwork(level);

        int voltage = (int) Math.abs(getSpeed()) * 10;

        if(stators.size() !=8){
            voltage = 0;
        }

        electricalNetwork.setVoltageGen(this, voltage);
        sendData();
        if (level instanceof ServerLevel serverLevel) {
            // CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(getPos()), new UpdateVoltagePacket(BlockPos.of(getBlockPos().asLong()), (int) Math.abs(getSpeed())));
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(worldPosition), new UpdateVoltagePacket(this, (int) Math.abs(getSpeed()) * 10));
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateVoltage();
    }

    public int generation() {

        if (stators.size() != 8)
            return 0;

        float modifier = TFMGConfigs.common().machines.largeGeneratorModifier.getF();
        float maxSpeed = TFMGConfigs.common().machines.largeGeneratorMinSpeed.getF();

        return (int) Math.max(0, ((Math.abs(getSpeed()) - maxSpeed) * modifier));
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        updateVoltage();

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

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {


        for (int i = 0; i < 3; i++) {
            TFMGTexts.Multimeter.voltageGenerated(voltage[i]).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.phaseOffset(120 * i).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.maxPower(maxPower[i]).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.powerGenerated(power[i]).forGoggles(tooltip, 1);
            TFMGLang.text("Voltage loss: " + internalVoltage[i]).forGoggles(tooltip);
            TFMGLang.text("Current: " + power[i] / voltage[i]).forGoggles(tooltip);
        }


        return true;
    }

    public void manageRotation() {
        float targetSpeed = getSpeed();
        visualSpeed.updateChaseTarget(targetSpeed);
        visualSpeed.tickChaser();
        angle += visualSpeed.getValue() * 3 / 10f;
        angle %= 360;
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

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public Level getWorld() {
        return level;
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