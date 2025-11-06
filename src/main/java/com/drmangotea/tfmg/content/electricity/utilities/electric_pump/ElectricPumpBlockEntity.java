package com.drmangotea.tfmg.content.electricity.utilities.electric_pump;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.electricity.base.ElectricBlockValues;
import com.drmangotea.tfmg.content.electricity.base.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.base.ElectricalGroup;
import com.drmangotea.tfmg.content.electricity.base.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.NetworkUpdatePacket;
import com.drmangotea.tfmg.content.electricity.base.VoltageAlteringBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.PipeConnection;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.math.BlockFace;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class ElectricPumpBlockEntity extends PumpBlockEntity implements IElectric {

    public ElectricBlockValues data = new ElectricBlockValues(getPos());
    int powerPercentage = 100;

    public ElectricPumpBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        setLazyTickRate(10);
        data.connectNextTick = true;

    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

        behaviours.add(new ElectricPumpTransferBehavior(this));
        registerAwardables(behaviours, FluidPropagator.getSharedTriggers());
        registerAwardables(behaviours, AllAdvancements.PUMP);
    }

    protected void distributePressureTo(Direction side) {


        BlockFace start = new BlockFace(worldPosition, side);
        boolean pull = isPullingOnSide(isFront(side));
        Set<BlockFace> targets = new HashSet<>();
        Map<BlockPos, Pair<Integer, Map<Direction, Boolean>>> pipeGraph = new HashMap<>();

        if (!pull)
            FluidPropagator.resetAffectedFluidNetworks(level, worldPosition, side.getOpposite());

        if (!hasReachedValidEndpoint(level, start, pull)) {

            pipeGraph.computeIfAbsent(worldPosition, $ -> Pair.of(0, new IdentityHashMap<>()))
                    .getSecond()
                    .put(side, pull);
            pipeGraph.computeIfAbsent(start.getConnectedPos(), $ -> Pair.of(1, new IdentityHashMap<>()))
                    .getSecond()
                    .put(side.getOpposite(), !pull);

            List<Pair<Integer, BlockPos>> frontier = new ArrayList<>();
            Set<BlockPos> visited = new HashSet<>();
            int maxDistance = (int) (FluidPropagator.getPumpRange()*Math.min(6.7f,data.getVoltage()*0.02));
            frontier.add(Pair.of(1, start.getConnectedPos()));

            while (!frontier.isEmpty()) {
                Pair<Integer, BlockPos> entry = frontier.remove(0);
                int distance = entry.getFirst();
                BlockPos currentPos = entry.getSecond();

                if (!level.isLoaded(currentPos))
                    continue;
                if (visited.contains(currentPos))
                    continue;
                visited.add(currentPos);
                BlockState currentState = level.getBlockState(currentPos);
                FluidTransportBehaviour pipe = FluidPropagator.getPipe(level, currentPos);
                if (pipe == null)
                    continue;

                for (Direction face : FluidPropagator.getPipeConnections(currentState, pipe)) {
                    BlockFace blockFace = new BlockFace(currentPos, face);
                    BlockPos connectedPos = blockFace.getConnectedPos();

                    if (!level.isLoaded(connectedPos))
                        continue;
                    if (blockFace.isEquivalent(start))
                        continue;
                    if (hasReachedValidEndpoint(level, blockFace, pull)) {
                        pipeGraph.computeIfAbsent(currentPos, $ -> Pair.of(distance, new IdentityHashMap<>()))
                                .getSecond()
                                .put(face, pull);
                        targets.add(blockFace);
                        continue;
                    }

                    FluidTransportBehaviour pipeBehaviour = FluidPropagator.getPipe(level, connectedPos);
                    if (pipeBehaviour == null)
                        continue;
                    if (pipeBehaviour instanceof ElectricPumpTransferBehavior)
                        continue;
                    if (visited.contains(connectedPos))
                        continue;
                    if (distance + 1 >= maxDistance) {
                        pipeGraph.computeIfAbsent(currentPos, $ -> Pair.of(distance, new IdentityHashMap<>()))
                                .getSecond()
                                .put(face, pull);
                        targets.add(blockFace);
                        continue;
                    }

                    pipeGraph.computeIfAbsent(currentPos, $ -> Pair.of(distance, new IdentityHashMap<>()))
                            .getSecond()
                            .put(face, pull);
                    pipeGraph.computeIfAbsent(connectedPos, $ -> Pair.of(distance + 1, new IdentityHashMap<>()))
                            .getSecond()
                            .put(face.getOpposite(), !pull);
                    frontier.add(Pair.of(distance + 1, connectedPos));
                }
            }
        }

        // DFS
        Map<Integer, Set<BlockFace>> validFaces = new HashMap<>();
        searchForEndpointRecursively(pipeGraph, targets, validFaces,
                new BlockFace(start.getPos(), start.getOppositeFace()), pull);
        float pressure = getPowerUsage() == 0 ? 0 : Math.min(1500,data.getVoltage()*2);
        for (Set<BlockFace> set : validFaces.values()) {
            int parallelBranches = Math.max(1, set.size() - 1);
            for (BlockFace face : set) {
                BlockPos pipePos = face.getPos();
                Direction pipeSide = face.getFace();

                if (pipePos.equals(worldPosition))
                    continue;

                boolean inbound = pipeGraph.get(pipePos)
                        .getSecond()
                        .get(pipeSide);
                FluidTransportBehaviour pipeBehaviour = FluidPropagator.getPipe(level, pipePos);
                if (pipeBehaviour == null)
                    continue;

                pipeBehaviour.addPressure(pipeSide, inbound, pressure / parallelBranches);
            }
        }

    }
    private boolean hasReachedValidEndpoint(LevelAccessor world, BlockFace blockFace, boolean pull) {
        BlockPos connectedPos = blockFace.getConnectedPos();
        BlockState connectedState = world.getBlockState(connectedPos);
        BlockEntity blockEntity = world.getBlockEntity(connectedPos);
        Direction face = blockFace.getFace();

        // facing a pump
        if (PumpBlock.isPump(connectedState) && connectedState.getValue(FACING)
                .getAxis() == face.getAxis() && blockEntity instanceof ElectricPumpBlockEntity) {
            ElectricPumpBlockEntity pumpBE = (ElectricPumpBlockEntity) blockEntity;
            return pumpBE.isPullingOnSide(pumpBE.isFront(blockFace.getOppositeFace())) != pull;
        }

        // other pipe, no endpoint
        FluidTransportBehaviour pipe = FluidPropagator.getPipe(world, connectedPos);
        if (pipe != null && pipe.canHaveFlowToward(connectedState, blockFace.getOppositeFace()))
            return false;

        // fluid handler endpoint
        if (blockEntity != null) {
            LazyOptional<IFluidHandler> capability =
                    blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, face.getOpposite());
            if (capability.isPresent())
                return true;
        }

        // open endpoint
        return FluidPropagator.isOpenEnd(world, blockFace.getPos(), face);
    }
    protected boolean isFront(Direction side) {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof PumpBlock))
            return false;
        Direction front = blockState.getValue(FACING);
        boolean isFront = side == front;
        return isFront;
    }

    //////////////////////




    @Override
    public LevelAccessor getLevelAccessor() {
        return level;
    }

    @Override
    public boolean destroyed() {
        return data.destroyed;
    }

    @Override
    public ElectricalNetwork getOrCreateElectricNetwork() {
        if (level.getBlockEntity(BlockPos.of(data.electricalNetworkId)) instanceof IElectric) {
            return TFMG.NETWORK_MANAGER.getOrCreateNetworkFor((IElectric) level.getBlockEntity(BlockPos.of(data.electricalNetworkId)));
        } else {
            ElectricNetworkManager.networks.get(getLevel())
                    .remove(data.electricalNetworkId);
            return TFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
    }

    @Override
    public ElectricBlockValues getData() {
        return data;
    }




    @Override
    public float resistance() {
        return 100;
    }



    @Override
    public int voltageGeneration() {

        int voltageGeneration = 0;

        for (Direction direction : Direction.values()) {
            if (hasElectricitySlot(direction)) {

                if (level.getBlockEntity(getBlockPos().relative(direction)) instanceof VoltageAlteringBlockEntity be)
                    if (be.getData().getId() != getData().getId())
                        if (be.getData().getVoltage() != 0)
                            if (be.hasElectricitySlot(direction)) {
                                voltageGeneration = Math.max(voltageGeneration, be.getOutputVoltage());
                                data.getsOutsidePower = true;
                            }
            }
        }

        if (voltageGeneration == 0)
            data.getsOutsidePower = false;

        return voltageGeneration;
    }

    @Override
    public int powerGeneration() {

        int powerGeneration = 0;

        for (Direction direction : Direction.values()) {
            if (hasElectricitySlot(direction)) {

                if (level.getBlockEntity(getBlockPos().relative(direction)) instanceof VoltageAlteringBlockEntity be) {
                    if (be.getData().getId() != getData().getId())
                        if (be.getData().getVoltage() != 0)
                            if (be.hasElectricitySlot(direction)) {
                                powerGeneration = Math.max(powerGeneration, be.getPowerUsage()) + 1;
                            }
                }
            }
        }

        return powerGeneration;
    }

    @Override
    public int frequencyGeneration() {
        return 0;
    }

    @Override
    public void updateNextTick() {
        data.updateNextTick = true;
    }

    @Override
    public void updateNetwork() {
        getOrCreateElectricNetwork().updateNetwork();
        if (!level.isClientSide)
            TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new NetworkUpdatePacket(BlockPos.of(getPos())));
        sendData();
    }

    @Override
    public void sendStuff() {
        sendData();
    }

    @Override
    public void setVoltage(int newVoltage) {


        if (canBeInGroups()) {
            data.voltage = (int) (((float) resistance() / data.group.resistance) * (float) data.voltageSupply);
            return;
        }
        data.voltage = newVoltage;
    }

    @Override
    public void setFrequency(int newFrequency) {
        data.frequency = newFrequency;
    }

    @Override
    public void setNetworkResistance(int newUsage) {
        data.networkResistance = newUsage;
    }

    @Override
    public int getNetworkResistance() {
        return data.networkResistance;
    }



    @Override
    public void setNetwork(long network) {
        this.data.electricalNetworkId = network;
        if (network != getPos())
            ElectricNetworkManager.networks.get(getLevel())
                    .remove(getPos());
    }




    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    @Override
    public void remove() {
        super.remove();
        this.data.destroyed = true;
        for (Direction d : Direction.values()) {
            if (hasElectricitySlot(d))
                if (getLevelAccessor().getBlockEntity(BlockPos.of(getPos()).relative(d)) instanceof IElectric be && be.hasElectricitySlot(d.getOpposite())) {
                    ElectricNetworkManager.networks.get(getLevel())
                            .remove(be.getPos());
                    be.setNetwork(be.getPos());
                    be.onPlaced();
                    be.updateNextTick();
                }
        }
        if (data.electricalNetworkId != getPos())
            getOrCreateElectricNetwork().getMembers().remove(this);

        if (data.electricalNetworkId == getPos())
            ElectricNetworkManager.networks.get(getLevel())
                    .remove(getData().getId());
    }

    @Override
    public void tick() {
        super.tick();
        if (data.connectNextTick) {
            onPlaced();
            data.connectNextTick = false;
        }
        if (data.updateNextTick) {
            updateNetwork();
            data.updateNextTick = false;
        }
        if (data.setVoltageNextTick) {
            setVoltage(data.voltageSupply);
            data.setVoltageNextTick = false;
        }

    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.putInt("GroupId", data.group.id);
        compound.putFloat("GroupResistance", data.group.resistance);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        data.group = new ElectricalGroup(compound.getInt("GroupId"));
        data.group.resistance = compound.getFloat("GroupResistance");
        if (!clientPacket)
            data.connectNextTick = true;
    }

    /// ////////////////
    class ElectricPumpTransferBehavior extends FluidTransportBehaviour {

        public ElectricPumpTransferBehavior(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public void tick() {
            super.tick();
            for (Map.Entry<Direction, PipeConnection> entry : interfaces.entrySet()) {
                boolean pull = isPullingOnSide(isFront(entry.getKey()));
                Couple<Float> pressure = entry.getValue().getPressure();
                pressure.set(pull,getPowerUsage() == 0 ? 0 : Math.min(1500,data.getVoltage()*2f));
                pressure.set(!pull, 0f);
            }
        }

        @Override
        public boolean canHaveFlowToward(BlockState state, Direction direction) {
            return isSideAccessible(direction);
        }

        @Override
        public AttachmentTypes getRenderedRimAttachment(BlockAndTintGetter world, BlockPos pos, BlockState state,
                                                        Direction direction) {
            AttachmentTypes attachment = super.getRenderedRimAttachment(world, pos, state, direction);
            if (attachment == AttachmentTypes.RIM)
                return AttachmentTypes.NONE;
            return attachment;
        }
    }
}
