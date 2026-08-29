package com.drmangotea.tfmg.content.electricity.experimental.blocks;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.packets.RealNetworkUpdatePacket;
import com.drmangotea.tfmg.content.electricity.experimental.packets.UpdateVoltagePacket;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.logistics.funnel.FunnelFlapPacket;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ThreePhaseGeneratorBlockEntity extends KineticBlockEntity implements IRealisticElectric, IHaveGoggleInformation {

    ElectricalProperties properties;


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
        TFMG.LOGGER.debug("speed changed");
        RealElectricalNetwork electricalNetwork = RealElectricNetworkManager.getNetwork(level);



        electricalNetwork.setVoltageGen(this, (int) Math.abs(getSpeed()));
        sendData();
        if (level instanceof ServerLevel serverLevel) {
           // CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(getPos()), new UpdateVoltagePacket(BlockPos.of(getBlockPos().asLong()), (int) Math.abs(getSpeed())));
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(worldPosition), new UpdateVoltagePacket(this, (int)Math.abs(getSpeed())));
        }

    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);

        Resistance resistor1 = null;

        if (network == null)
            return false;

        if (!network.resistors.isEmpty()) {
            resistor1 = network.resistors.get(0);
        }
        TFMGLang.text("Speed: " + speed).forGoggles(tooltip);

        if (resistor1 != null && resistor1.getVoltagePhasor(level) != null) {
            TFMGLang.text("Resistance Voltage " + Math.pow(resistor1.getVoltage(level), 2) / resistor1.resistance).forGoggles(tooltip);
//
        }
        TFMGLang.text("Network count " + RealElectricNetworkManager.networks.size()).forGoggles(tooltip);
        TFMGLang.text("member count " + network.members.size()).forGoggles(tooltip);
        TFMGLang.text("connection count " + network.connections.size()).forGoggles(tooltip);
        TFMGLang.text("Resistor count " + network.resistors.size()).forGoggles(tooltip);
        //TFMGLang.text("node count " + network.nodes.size()).forGoggles(tooltip);


        return true;
    }

    @Override
    public ElectricalProperties getProperties() {
        return properties;
    }

    // @Override
    // public ElectricalProperties getProperties() {
    //     IRealisticElectric thisBE = this;
    //     return new ElectricalProperties(this){
    //         @Override
    //         public List<ElectricalComponent> createProperties(IRealisticElectric be) {
    //             List<ElectricalComponent> components = new ArrayList<>();
    //             //nodes = new ArrayList<>();
    //             ElectricalNode gnd = new ConnectingElectricalNode(thisBE,getFreeId(level,0),0,new CablePos(getBlockPos().getX(),getBlockPos().getY()+1,getBlockPos().getZ()));
    //             ElectricalNode phase = new ConnectingElectricalNode(thisBE,getFreeId(level,1),1,new CablePos(getBlockPos().getX(),getBlockPos().getY()+1,getBlockPos().getZ()+1));
//
    //             nodes.add(gnd);
    //             nodes.add(phase);
    //             components.add(new IdealVoltageSource(phase,gnd,10,0));
    //             components.add(new Resistance(phase,gnd,10));
    //             components.add(new Resistance(phase,gnd,10));
//
    //             return components;
    //         }
    //     };
//
    // }

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
