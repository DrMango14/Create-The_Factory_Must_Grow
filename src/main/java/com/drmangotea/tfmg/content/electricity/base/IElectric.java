package com.drmangotea.tfmg.content.electricity.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnection;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnectorBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.platform.CatnipServices;
import net.createmod.catnip.theme.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;


public interface IElectric {
    long getPos();

    LevelAccessor getLevelAccessor();

    default boolean destroyed() {
        return getData().destroyed();
    }

    default ElectricalNetwork getOrCreateElectricNetwork() {
        if (getLevelAccessor().getBlockEntity(BlockPos.of(getData().electricalNetworkId)) instanceof IElectric) {
            return TFMG.NETWORK_MANAGER.getOrCreateNetworkFor((IElectric) getLevelAccessor().getBlockEntity(BlockPos.of(getData().electricalNetworkId)));
        } else {
            ElectricNetworkManager.networks.get(getLevelAccessor())
                    .remove(getData().electricalNetworkId);
            return TFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
        }
    }

    default boolean hasElectricitySlot(Direction direction) {
        return true;
    }

    default void onPlaced() {

        if (getLevelAccessor() instanceof ServerLevel serverLevel)
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(BlockPos.of(getPos())), new ConnectNeightborsPacket(BlockPos.of(getPos())));
        ElectricalNetwork network = TFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
        setNetwork(getPos());
        getData().electricalNetworkId = getPos();
        network.add(this);


        getData().checkForLoopsNextTick = true;
        getOrCreateElectricNetwork().checkForLoops(BlockPos.of(getPos()));
        /// ////


        updateNextTick();

        onConnected();
        sendStuff();

    }

    default void onRemoved() {
        this.getData().destroyed = true;
        for (Direction d : Direction.values()) {
            if (hasElectricitySlot(d))
                if (getLevelAccessor().getBlockEntity(BlockPos.of(getPos()).relative(d)) instanceof IElectric be && be.hasElectricitySlot(d.getOpposite())) {
                    ElectricNetworkManager.networks.get(getLevelAccessor())
                            .remove(be.getPos());
                    be.setNetwork(be.getPos());
                    be.onPlaced();
                    be.updateNextTick();
                }
        }
        if (getData().electricalNetworkId != getPos())
            getOrCreateElectricNetwork().getMembers().remove(this);
//
        if (getData().electricalNetworkId == getPos())
            ElectricNetworkManager.networks.get(getLevelAccessor())
                    .remove(getData().getId());
    }

    default void readElectricity(CompoundTag compound, boolean clientPacket) {
        getData().group = new ElectricalGroup(compound.getInt("GroupId"));
        getData().group.resistance = compound.getFloat("GroupResistance");
        if (!clientPacket)
            getData().connectNextTick = true;
    }

    default void writeElectricity(CompoundTag compound, boolean clientPacket) {
        compound.putInt("GroupId", getData().group.id);
        compound.putFloat("GroupResistance", getData().group.resistance);
    }

    default void tickElectricity() {
        if (getData().checkForLoopsNextTick) {
            getOrCreateElectricNetwork().checkForLoops(getBlockPos());
            getData().checkForLoopsNextTick = false;
        }
        if (getData().connectNextTick) {
            onPlaced();
            getData().connectNextTick = false;
        }
        if (getData().updateNextTick) {
            updateNetwork();
            getData().updateNextTick = false;
        }

        if (getData().updatePowerNextTick) {
            updateUnpowered(new ArrayList<>());
            getData().updatePowerNextTick = false;
        }
        if (getData().setVoltageNextTick) {
            setVoltage(getData().voltageSupply);
            getData().setVoltageNextTick = false;
        }
    }

    default void lazyTickElectricity() {
        if (getData().failTimer >= 4) {
            this.blockFail();
            getData().failTimer = 0;
            sendStuff();
        } else if ((getData().voltage > getMaxVoltage() && getMaxVoltage() > 0) || (getCurrent() > getMaxCurrent() && getMaxCurrent() > 0)) {
            getData().failTimer++;
        }
    }

    default int getMaxVoltage() {
        return 0;
    }

    default int getMaxCurrent() {
        return 0;
    }

    default void onConnected() {

        BlockPos pos = BlockPos.of(getPos());
        for (Direction d : Direction.values()) {
            if (hasElectricitySlot(d))
                if (getLevelAccessor().getBlockEntity(pos.relative(d)) instanceof IElectric be) {
                    if (be.hasElectricitySlot(d.getOpposite())) {
                        if (!be.destroyed()) {
                            getOrCreateElectricNetwork().add(be);
                            if (be.getData().getId() != getData().getId()) {
                                be.setNetwork(getData().getId());
                                be.onConnected();
                                if (!getLevelAccessor().isClientSide())
                                    sendStuff();
                            }
                        }
                    } else if (be.getData().getId() != getData().getId()) {
                        be.updateNextTick();
                    }
                }
        }
        sendStuff();

    }

    default BlockPos getBlockPos() {
        return BlockPos.of(getPos());
    }

    default void updateUnpowered(List<BlockPos> alreadyChecked) {
        alreadyChecked.add(BlockPos.of(getPos()));
        updateNextTick();

        if (this instanceof CableConnectorBlockEntity connectorBE) {
            for (CableConnection connection : connectorBE.connections) {

                if (getLevelAccessor().getBlockEntity(connection.blockPos1) instanceof CableConnectorBlockEntity be2 && !alreadyChecked.contains(BlockPos.of(be2.getPos()))
                ) {
                    be2.updateUnpowered(alreadyChecked);
                }
            }
        }

        for (Direction direction : Direction.values()) {
            if (getLevelAccessor().getBlockEntity(BlockPos.of(getPos()).relative(direction)) instanceof IElectric be && !alreadyChecked.contains(BlockPos.of(be.getPos()))) {
                be.updateUnpowered(alreadyChecked);
            }
        }
    }

    default boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        TFMGTexts.header("multimeter").style(ChatFormatting.WHITE)
                .forGoggles(tooltip);

        if (getData().notEnoughtPower) TFMGTexts.Multimeter.notEnoughPower().forGoggles(tooltip, 1);

        if (voltageGeneration() > 0) {
            TFMGTexts.Multimeter.powerGenerated(powerGeneration()).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.voltageGenerated(voltageGeneration()).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.separator().forGoggles(tooltip);
        }

        TFMGTexts.Multimeter.resistance(voltageGeneration() > 0 ? getGeneratorResistance() : resistance()).forGoggles(tooltip, 1);
        TFMGTexts.Multimeter.voltage(getData().getVoltage()).forGoggles(tooltip, 1);
        TFMGTexts.Multimeter.current(getCurrent()).forGoggles(tooltip, 1);
        TFMGTexts.Multimeter.power(getPowerUsage()).forGoggles(tooltip, 1);

        if (getData().group.id != -1) {
            TFMGTexts.Multimeter.separator().forGoggles(tooltip);
            TFMGTexts.Multimeter.group(getData().group.id).forGoggles(tooltip, 1);
        }

        if (isPlayerSneaking) {
            TFMGTexts.Multimeter.separator().forGoggles(tooltip);
            TFMGTexts.Multimeter.networkGeneration(getNetworkPowerGeneration()).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.networkConsumption(getNetworkPowerUsage()).forGoggles(tooltip, 1);
        }

        return true;
    }

    default void updateNearbyNetworks(IElectric member) {
        if (true)
            return;


        for (Direction direction : Direction.values()) {
            if (member.getLevelAccessor().getBlockEntity(BlockPos.of(member.getPos()).relative(direction)) instanceof IElectric be && be.getData().getId() != member.getData().getId()) {
                be.getLevelAccessor().setBlock(BlockPos.of(be.getPos()).above(3), Blocks.GOLD_BLOCK.defaultBlockState(), 3);
                be.updateNextTick();
            }
        }

    }

    ElectricBlockValues getData();

    default boolean canWork() {
        return !getData().notEnoughtPower;
    }

    default void blockFail() {

        getLevelAccessor().destroyBlock(BlockPos.of(getPos()), false);
    }

    default int getPowerUsage() {
        return (int) (getData().getVoltage() * getCurrent());
    }

    default int getNetworkPowerUsage(IElectric blocked) {
        int power = 0;
        for (IElectric member : getOrCreateElectricNetwork().members)
            if (member.getPos() != blocked.getPos()) {
                power += member.getPowerUsage();
            } else blocked.updateNextTick();
        return power;
    }

    default int getNetworkPowerUsage() {
        int power = 0;
        for (IElectric member : getOrCreateElectricNetwork().members)
            power += member.getPowerUsage();
        return power;
    }


    default int getNetworkPowerGeneration() {
        int power = 0;
        for (IElectric member : getOrCreateElectricNetwork().members)

            power += member.powerGeneration();
        return power;
    }

    default void onNetworkChanged(int oldVoltage, int oldPower) {
    }

    default float getGeneratorResistance() {
        if (getData().voltageSupply == 0)
            return 0;

        if ((float) getData().networkPowerGeneration * (float) getNetworkResistance() == 0)
            return 0;

        return (float) powerGeneration() / (float) getData().networkPowerGeneration * (float) getNetworkResistance();
    }

    default float getGeneratorLoad() {
        if (getNetworkPowerUsage() == 0)
            return 0;
        return (float) powerGeneration() / (float) getData().networkPowerGeneration * getNetworkPowerUsage();
    }


    default float resistance() {
        return 0;
    }


    default int voltageGeneration() {

        int voltageGeneration = 0;

        for (Direction direction : Direction.values()) {
            if (hasElectricitySlot(direction)) {

                if (getLevelAccessor().getBlockEntity(getBlockPos().relative(direction)) instanceof VoltageAlteringBlockEntity be)
                    if (be.getData().getId() != getData().getId())
                        if (be.getData().getVoltage() != 0)
                            if (be.hasElectricitySlot(direction)) {
                                voltageGeneration = Math.max(voltageGeneration, be.getOutputVoltage());
                                getData().getsOutsidePower = true;
                            }
            }
        }

        if (voltageGeneration == 0)
            getData().getsOutsidePower = false;

        return voltageGeneration;
    }


    default int powerGeneration() {

        int powerGeneration = 0;

        for (Direction direction : Direction.values()) {
            if (hasElectricitySlot(direction)) {

                if (getLevelAccessor().getBlockEntity(getBlockPos().relative(direction)) instanceof VoltageAlteringBlockEntity be && be.canWork()) {

                    if (be.getData().getId() != getData().getId())
                        if (be.getData().getVoltage() != 0)
                            if (be.hasElectricitySlot(direction)) {
                                powerGeneration = Math.max(powerGeneration, be.getPowerUsage()) + 1;
                                if (powerGeneration > be.getNetworkPowerGeneration()) {
                                    powerGeneration = 0;
                                    be.data.updatePowerNextTick = true;
                                }
                            }
                }
            }
        }

        return powerGeneration;
    }

    default int getNetworkResistance() {
        return getData().networkResistance;
    }

    default boolean networkUndersupplied() {
        return getNetworkPowerUsage() > getData().networkPowerGeneration;
    }


    default int getMaxAmps() {
        return (int) getCurrent();
    }

    default float getCurrent() {
        return getData().getVoltage() == 0 || resistance() == 0 ? 0 : ((float) getData().getVoltage() / (float) resistance());
    }

    default void updateNextTick() {
        getData().updateNextTick = true;
    }

    default void updateNetwork() {
        getOrCreateElectricNetwork().updateNetwork();
        if (getLevelAccessor() instanceof ServerLevel serverLevel)
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), new NetworkUpdatePacket(BlockPos.of(getPos())));
        sendStuff();
    }

    void sendStuff();


    default void setVoltage(int newVoltage) {


        if (canBeInGroups()) {
            getData().voltage = (int) (((float) resistance() / getData().group.resistance) * (float) getData().voltageSupply);
            return;
        }
        getData().voltage = newVoltage;
    }


    default void setNetworkResistance(int newUsage) {
        getData().networkResistance = newUsage;
    }


    default void setNetwork(long network) {
        getData().electricalNetworkId = network;
        if (network != getPos())
            ElectricNetworkManager.networks.get(getLevelAccessor())
                    .remove(getPos());
    }

    default boolean canBeInGroups() {
        return false;
    }


}