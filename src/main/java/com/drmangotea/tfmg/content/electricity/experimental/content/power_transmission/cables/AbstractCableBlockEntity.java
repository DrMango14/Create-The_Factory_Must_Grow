package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables;

import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.WireConnection;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.switches.SwitchProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.*;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class AbstractCableBlockEntity extends SmartBlockEntity implements IRealisticElectric {

    public CableProperties p;

    public boolean connectNextTick = false;

    public AbstractCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);


    }

    @Override
    public void tick() {
        super.tick();
        if (connectNextTick) {

            connectNextTick = connectToNeighbors(level, this);
        }
    }

    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }

    @Override
    public CableProperties getProperties() {
        return p;
    }

    public static boolean connectToNeighbors(Level level, IRealisticElectric be) {
        RealElectricalNetwork network = be.getNetwork(level);
        ElectricalProperties properties = network.members.get(be.getPos());
        network.removeConnections(be);

        for (Direction facing : properties.getConnectableDirections()) {
            BlockPos pos = BlockPos.of(be.getPos()).relative(facing);

            if (level.getBlockEntity(pos) instanceof IRealisticElectric) {
                ElectricalProperties neighborProperties = network.members.get(pos.asLong());
                if (neighborProperties == null) {
                    return true;
                }
                if(neighborProperties.getConnectableDirections().contains(facing.getOpposite())){

                    for (int i = 0; i < Math.min(4,Math.min(properties.nodes.size(),neighborProperties.nodes.size())); i++) {
                        if (properties.nodes.get(i) instanceof ConnectableElectricalNode n1 && neighborProperties.nodes.get(i) instanceof ConnectableElectricalNode n2) {

                            WireConnection connection = new WireConnection(n1, n2, 10, false);
                            if (!network.connections.contains(connection))
                                network.connections.add(connection);
                        }
                    }

                }
                if (neighborProperties instanceof SwitchProperties switchProperties) {
                    if (switchProperties.direction.getAxis() == facing.getAxis()) {

                        for (ElectricalNode node : switchProperties.nodes) {
                            int id = node.localId <= 3 ? node.localId : node.localId - 3;

                            if (node instanceof DirectionalElectricalNode d && properties.nodes.get(id) instanceof ConnectableElectricalNode ld) {
                                if (d.facing.contains(facing.getOpposite())) {

                                    network.connections.add(new WireConnection(d, ld, 10, false));

                                }
                            }
                        }
                    }
                }


            }

        }
        if(true)
            return false;
        for (Direction facing : properties.getConnectableDirections()) {
            BlockPos pos = BlockPos.of(be.getPos()).relative(facing);
            if (level.getBlockEntity(pos) instanceof IRealisticElectric) {
                ElectricalProperties neighborProperties = network.members.get(pos.asLong());
                if (neighborProperties == null) {
                    return true;
                    // continue;
                }

                if (neighborProperties instanceof CableProperties cableProperties) {
                    if (cableProperties.directions.contains(facing)) {

                        for (int i = 0; i < 4; i++) {
                            if (properties.nodes.get(i) instanceof DirectionalElectricalNode n1 && cableProperties.nodes.get(i) instanceof DirectionalElectricalNode n2) {

                                WireConnection connection = new WireConnection(n1, n2, 10, false);
                                if (!network.connections.contains(connection))
                                    network.connections.add(connection);
                            }
                        }
                    }
                }
                if (neighborProperties instanceof SwitchProperties switchProperties) {
                    if (switchProperties.direction.getAxis() == facing.getAxis()) {

                        for (ElectricalNode node : switchProperties.nodes) {
                            int id = node.localId <= 3 ? node.localId : node.localId - 3;

                            if (node instanceof DirectionalElectricalNode d && properties.nodes.get(id) instanceof DirectionalElectricalNode ld) {
                                if (d.facing.contains(facing)) {

                                    network.connections.add(new WireConnection(d, ld, 10, false));

                                }
                            }
                        }
                    }
                }
                if (neighborProperties.cableConnectable() && neighborProperties.hasElectricityPort(facing.getOpposite())) {
                    for (int i = 0; i < 4; i++) {
                        if (i < properties.nodes.size() && i < neighborProperties.nodes.size())
                            if (properties.nodes.get(i) instanceof ConnectableElectricalNode n1 && neighborProperties.nodes.get(i) instanceof ConnectableElectricalNode n2) {

                                WireConnection connection = new WireConnection(n1, n2, 10, false);
                                if (!network.connections.contains(connection))
                                    network.connections.add(connection);
                            }
                    }
                }
            }
        }
        return false;
    }


    @Override
    public void onPlace() {
        IRealisticElectric.super.onPlace();
        connectNextTick = connectToNeighbors(level, this);
        updateNetwork(getBlockPos());
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
    public void addBehaviours(List<BlockEntityBehaviour> list) {
    }
}
