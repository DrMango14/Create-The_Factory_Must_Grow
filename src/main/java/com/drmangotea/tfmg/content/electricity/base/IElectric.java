package com.drmangotea.tfmg.content.electricity.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnection;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnectorBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.theme.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.PacketDistributor;

import com.drmangotea.tfmg.content.electricity.network.large_switch.LargeSwitchBlockEntity;
import com.drmangotea.tfmg.content.electricity.network.transformer.large.LargeTransformerBlockEntity;
import java.util.ArrayList;
import java.util.List;

public interface IElectric {

    long getPos();

    LevelAccessor getLevelAccessor();

    default boolean destroyed() {
        return getData().destroyed;
    }

    default ElectricalNetwork getOrCreateElectricNetwork() {
        if (getLevelAccessor().getBlockEntity(BlockPos.of(getData().electricalNetworkId)) instanceof IElectric) {
            return TFMG.NETWORK_MANAGER.getOrCreateNetworkFor(
                    (IElectric) getLevelAccessor().getBlockEntity(BlockPos.of(getData().electricalNetworkId)));
        } else {
            ElectricNetworkManager.networks.get(getLevelAccessor()).remove(getData().electricalNetworkId);
            return TFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
        }
    }

    default boolean hasElectricitySlot(Direction direction) {
        return true;
    }

    default void onPlaced() {
        if (!getLevelAccessor().isClientSide()) {
            BlockPos pos = BlockPos.of(getPos());
            LevelChunk chunk = ((ServerLevel) getLevelAccessor()).getChunkAt(pos);
            TFMGPackets.getChannel().send(
                    PacketDistributor.TRACKING_CHUNK.with(() -> chunk),
                    new ConnectNeightborsPacket(pos));
        }
        ElectricalNetwork network = TFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
        setNetwork(getPos());
        getData().electricalNetworkId = getPos();
        network.add(this);

        getData().checkForLoopsNextTick = true;
        getOrCreateElectricNetwork().checkForLoops(BlockPos.of(getPos()));

        updateNextTick();
        onConnected();
        sendStuff();
    }

    default int getMaxVoltage() {
        return 1000;
    }

    default int getMaxCurrent() {
        return 0;
    }

    default boolean isCable() {
        return false;
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

    default void onRemoved() {
        getData().destroyed = true;
        for (Direction d : Direction.values()) {
            if (hasElectricitySlot(d))
                if (getLevelAccessor().getBlockEntity(BlockPos.of(getPos()).relative(d)) instanceof IElectric be
                        && be.hasElectricitySlot(d.getOpposite())) {
                    ElectricNetworkManager.networks.get(getLevelAccessor()).remove(be.getPos());
                    be.setNetwork(be.getPos());
                    be.onPlaced();
                    be.updateNextTick();
                }
        }
        if (getData().electricalNetworkId != getPos())
            getOrCreateElectricNetwork().getMembers().remove(this);
        if (getData().electricalNetworkId == getPos())
            ElectricNetworkManager.networks.get(getLevelAccessor()).remove(getData().getId());
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
            if (!getLevelAccessor().isClientSide()) {
                BlockPos pos = BlockPos.of(getPos());
                LevelChunk chunk = ((ServerLevel) getLevelAccessor()).getChunkAt(pos);
                TFMGPackets.getChannel().send(
                        PacketDistributor.TRACKING_CHUNK.with(() -> chunk),
                        new ElectricalBlockFailPacket(pos));
            }
            getData().failTimer = 0;
            sendStuff();
        } else if ((getData().voltage > getMaxVoltage() && getMaxVoltage() > 0)
                || (getCurrent() > getMaxCurrent() && getMaxCurrent() > 0)
                || (getData().highestCurrent > getMaxCurrent() && getMaxCurrent() > 0 && isCable())) {
            getData().failTimer++;
        }
    }

    default void updateUnpowered(List<BlockPos> alreadyChecked) {
        alreadyChecked.add(BlockPos.of(getPos()));
        updateNextTick();
        if (this instanceof CableConnectorBlockEntity connectorBE) {
            for (CableConnection connection : connectorBE.connections) {
                if (getLevelAccessor().getBlockEntity(connection.blockPos1) instanceof CableConnectorBlockEntity be2
                        && !alreadyChecked.contains(BlockPos.of(be2.getPos()))) {
                    be2.updateUnpowered(alreadyChecked);
                }
            }
        }
        for (Direction direction : Direction.values()) {
            if (getLevelAccessor().getBlockEntity(BlockPos.of(getPos()).relative(direction)) instanceof IElectric be
                    && !alreadyChecked.contains(BlockPos.of(be.getPos()))) {
                be.updateUnpowered(alreadyChecked);
            }
        }
    }

    default boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CreateLang.translate("multimeter.header")
                .style(ChatFormatting.WHITE)
                .forGoggles(tooltip);

        if (getData().notEnoughPower) {
            CreateLang.translate("multimeter.not_enough_power")
                    .color(Color.RED)
                    .forGoggles(tooltip, 1);
        }

        if (voltageGeneration() > 0) {
            CreateLang.translate("multimeter.power_generated")
                    .add(Component.literal(TFMGUtils.formatUnits(powerGeneration(), "W")))
                    .color(0x852e4a)
                    .forGoggles(tooltip, 1);
            CreateLang.translate("multimeter.voltage_generated")
                    .add(Component.literal(TFMGUtils.formatUnits(voltageGeneration(), "V")))
                    .color(0x127799)
                    .forGoggles(tooltip, 1);
            CreateLang.text("----------------------------")
                    .style(ChatFormatting.WHITE)
                    .forGoggles(tooltip);
        }

        CreateLang.text("   R = " + TFMGUtils.formatUnits(voltageGeneration() > 0 ? getGeneratorResistance() : resistance(), "Ω"))
                .color(0xc98969).forGoggles(tooltip, 1);
        CreateLang.text("   U = " + TFMGUtils.formatUnits(getData().getVoltage(), "V"))
                .color(0x4bbbcc).forGoggles(tooltip, 1);
        CreateLang.text("   I = " + TFMGUtils.formatUnits(resistance() == 0 ? getData().highestCurrent : getCurrent(), "A"))
                .color(0x22a146).forGoggles(tooltip, 1);
        CreateLang.text("   P = " + TFMGUtils.formatUnits(getPowerUsage(), "W"))
                .color(0xcc4b74).forGoggles(tooltip, 1);

        if (isPlayerSneaking) {
            CreateLang.text("----------------------------")
                    .style(ChatFormatting.WHITE).forGoggles(tooltip);
            CreateLang.translate("multimeter.network_power_generation")
                    .add(Component.literal(TFMGUtils.formatUnits(getNetworkPowerGeneration(), "W")))
                    .color(0xcc4b74).forGoggles(tooltip, 1);
            CreateLang.translate("multimeter.network_power_consumption")
                    .add(Component.literal(TFMGUtils.formatUnits(getNetworkPowerUsage(), "W")))
                    .color(0xcc4b74).forGoggles(tooltip, 1);
        }

        return true;
    }

    ElectricBlockValues getData();

    default boolean canWork() {
        return !getData().notEnoughPower;
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
        if (getData().voltageSupply == 0) return 0;
        if ((float) getData().networkPowerGeneration * (float) getNetworkResistance() == 0) return 0;
        return (float) powerGeneration() / (float) getData().networkPowerGeneration * (float) getNetworkResistance();
    }

    default float getGeneratorLoad() {
        if (getNetworkPowerUsage() == 0) return 0;
        return (float) powerGeneration() / (float) getData().networkPowerGeneration * getNetworkPowerUsage();
    }

    default boolean networkUndersupplied() {
        return getNetworkPowerUsage() > getData().networkPowerGeneration;
    }

    default float getCurrent() {
        return getData().getVoltage() == 0 || resistance() == 0 ? 0
                : ((float) getData().getVoltage() / (float) resistance());
    }

    default BlockPos getBlockPos() {
        return BlockPos.of(getPos());
    }

    float resistance();

    int voltageGeneration();

    int powerGeneration();

    int getNetworkResistance();

    default void updateNextTick() {
        getData().updateNextTick = true;
    }

    default void updateNetwork() {
        getOrCreateElectricNetwork().updateNetwork();
        if (!getLevelAccessor().isClientSide()) {
            BlockPos pos = BlockPos.of(getPos());
            LevelChunk chunk = ((ServerLevel) getLevelAccessor()).getChunkAt(pos);
            TFMGPackets.getChannel().send(
                    PacketDistributor.TRACKING_CHUNK.with(() -> chunk),
                    new NetworkUpdatePacket(pos));
        }
        sendStuff();
    }

    void sendStuff();

    default void setVoltage(int newVoltage) {
        getData().voltage = newVoltage;
    }

    default void setNetworkResistance(float newUsage) {
        getData().networkResistance = (int) newUsage;
    }

    // Keep int overload for backward compat with existing callers in this codebase
    default void setNetworkResistance(int newUsage) {
        getData().networkResistance = newUsage;
    }

    void setNetwork(long network);

    default int getBlocksConnectedToNetworkCount(long id) {
        int count = 0;
        for (IElectric member : getOrCreateElectricNetwork().members) {
            if (member instanceof VoltageAlteringBlockEntity be && be.getControlledBlock() != null) {
                if (be.getControlledBlock().getData().getId() == id)
                    count++;
            }
            if (member instanceof LargeSwitchBlockEntity be && be.getControlledBlock() != null) {
                if (be.getControlledBlock().getData().getId() == id)
                    count++;
            }
            if (member instanceof LargeTransformerBlockEntity be && be.getControlledBlock() != null) {
                if (be.getControlledBlock().getData().getId() == id)
                    count++;
            }
        }
        return count;
    }

    // Kept for binary compat with any subclass overriding it; no longer used by core logic
    default boolean canBeInGroups() {
        return false;
    }

    default int getMaxAmps() {
        return (int) getCurrent();
    }
}
