package com.drmangotea.tfmg.content.electricity.content.devices.generators.three_phase_generator;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.packets.UpdateVoltagePacket;
import com.drmangotea.tfmg.content.electricity.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ThreePhaseGeneratorBlockEntity extends KineticBlockEntity implements IElectric, IHaveGoggleInformation {

    ElectricalProperties properties;

    double[] voltage = new double[3];
    double[] internalVoltage = new double[3];
    double[] power = new double[3];
    double[] maxPower = new double[3];

    public ThreePhaseGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        properties = new ThreePhaseGeneratorProperties(getPos(), state.getValue(TFMGDirectionalBlock.FACING));
    }

    @Override
    public Level getWorld() {
        return getLevel();
    }

    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        updateVoltage();

    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateVoltage();
    }

    public void updateVoltage() {

        ElectricalNetwork electricalNetwork = ElectricNetworkManager.getNetwork(level);


        electricalNetwork.setVoltageGen(this, (int) Math.abs(getSpeed()) * 10);
        sendData();
        if (level instanceof ServerLevel serverLevel) {
            // CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(getPos()), new UpdateVoltagePacket(BlockPos.of(getBlockPos().asLong()), (int) Math.abs(getSpeed())));
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(worldPosition), new UpdateVoltagePacket(this, (int) Math.abs(getSpeed()) * 10));
        }
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {


        for (int i = 0; i < 3; i++) {
            TFMGTexts.Multimeter.voltageGenerated(voltage[i]).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.phaseOffset(120 * i).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.maxPower(maxPower[i]).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.powerGenerated(power[i]).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.current(power[i]/voltage[i]).forGoggles(tooltip,1);
            TFMGTexts.Multimeter.voltageLoss(internalVoltage[i]).forGoggles(tooltip,1);

        }


        return true;
    }

    @Override
    public void onUpdated() {


        ElectricalNetwork network = ElectricNetworkManager.getNetwork(level);

        for (int i = 0; i < 3; i++) {
            Resistance r = network.getResistance(getPos(), i);
            IdealVoltageSource s = network.getVoltageSource(getPos(), i);
            if (!(r == null || s == null)) {

                double offset = r.getVoltageComplex(level).phaseDegrees()-s.phaseOffset;

                double voltage = r.getVoltage(level);
                double current = voltage / r.resistance;
                double power = s.amplitude * current;
                double maxPower = s.power;



                this.voltage[i] = s.amplitude;
                this.internalVoltage[i] = voltage;
                this.power[i] = power;
                this.maxPower[i] = maxPower;


                double overloadIntensity = 0;

                if (maxPower < power)
                    overloadIntensity = power / maxPower;

                if (overloadIntensity > 2) {
                    getLevel().destroyBlock(getPos(), false);
                    level.setBlock(getBlockPos(), Blocks.FIRE.defaultBlockState(), 3);
                    return;

                }
                if (overloadIntensity > 1.5) {
                    getLevel().destroyBlock(getPos(), false);
                    return;
                }
                if (overloadIntensity > 1.1) {
                    getLevel().destroyBlock(getPos(), true);

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
    public ElectricalProperties getProperties() {
        return properties;
    }


    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
