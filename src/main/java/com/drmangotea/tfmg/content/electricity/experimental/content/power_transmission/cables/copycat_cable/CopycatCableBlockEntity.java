package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables.copycat_cable;

import com.drmangotea.tfmg.content.electricity.base.ElectricBlockValues;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.WireConnection;
import com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.cables.CableProperties;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ConnectableElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.DirectionalElectricalNode;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.List;

public class CopycatCableBlockEntity extends CopycatBlockEntity implements IRealisticElectric, IHaveHoveringInformation {

    public CableProperties p;

    public boolean connectNextTick = false;

    public CopycatCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.p = new CableProperties(getPos(), Arrays.stream(Direction.values()).toList());

    }

    @Override
    public Level getWorld() {
        return level;
    }

    @Override
    public void tick() {
        super.tick();
        if (connectNextTick) {
            connectToNeighbors();
            connectNextTick = false;
        }
    }

    public void connectToNeighbors() {
        RealElectricalNetwork network = getNetwork(level);
        CableProperties properties = (CableProperties) network.members.get(getPos());
        network.removeConnections(this);
        for (Direction facing : properties.directions) {
            BlockPos pos = getBlockPos().relative(facing);
            if (level.getBlockEntity(pos) instanceof IRealisticElectric be) {
                ElectricalProperties neighborProperties = network.members.get(pos.asLong());
                if (neighborProperties == null) {
                    connectNextTick = true;
                    continue;
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
    }


    @Override
    public void onPlace() {
        IRealisticElectric.super.onPlace();
        connectToNeighbors();
        updateNetwork(getBlockPos());
    }

    @Override
    public CableProperties getProperties() {
        return p;
    }

    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
    }

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }


    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        // Validate Material
        if (getMaterial() != null && !clientPacket) {
            BlockState blockState = getBlockState();
            if (blockState == null)
                return;
            if (!(blockState.getBlock() instanceof CopycatBlock cb))
                return;
            BlockState acceptedBlockState = cb.getAcceptedBlockState(level, worldPosition, ItemStack.EMPTY, null);
            if (acceptedBlockState != null && getMaterial().is(acceptedBlockState.getBlock()))
                return;
            setMaterial(AllBlocks.COPYCAT_BASE.getDefaultState());
        }

    }
}
