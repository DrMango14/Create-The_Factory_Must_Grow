package com.drmangotea.tfmg.content.electricity.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnection;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnectorBlockEntity;
import com.drmangotea.tfmg.content.electricity.network.large_switch.LargeSwitchBlockEntity;
import com.drmangotea.tfmg.content.electricity.network.transformer.large.LargeTransformerBlockEntity;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;

/**
 * data and actions for electric blocks
 */
public interface IElectric {

    /**
     * block's world position as a long
     */
    long getPos();

    /**
     * world the blocks is in
     */
    LevelAccessor getLevelAccessor();

    /**
     * @return true if the block is marked as removed
     */
    default boolean destroyed() {
        return getData().destroyed();
    }

    /**
     * checks if this block is part of a valid network
     *
     * @return block's network if valid, newly created network for this block otherwise
     */
    default ElectricalNetwork getOrCreateElectricNetwork() {
        if (getLevelAccessor().getBlockEntity(BlockPos.of(getData().electricalNetworkId)) instanceof IElectric) {
            return TFMG.NETWORK_MANAGER.getOrCreateNetworkFor((IElectric) getLevelAccessor().getBlockEntity(BlockPos.of(getData().electricalNetworkId)));
        } else {
            ElectricNetworkManager.networks.get(getLevelAccessor())
                    .remove(getData().electricalNetworkId);
            return TFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
        }
    }

    /**
     * tells the block which sides of it can be attached to the grid
     */
    default boolean hasElectricitySlot(Direction direction) {
        return true;
    }

    /**
     * initialization, called when the block is placed
     */
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

    /**
     * manages removal of this block after it is destroyed
     */
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

    /**
     * loads data
     */
    default void readElectricity(CompoundTag compound, boolean clientPacket) {
        if (!clientPacket)
            getData().connectNextTick = true;
    }

    /**
     * saves data
     */


    /**
     * handles action that need to happen every tick and checks for scheduled updates
     */
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

    default boolean isCable() {
        return false;
    }

    /**
     * handles actions that need to repeat every second
     */
    default void lazyTickElectricity() {

        if (getPowerUsage() > getData().networkPowerGeneration && !getData().notEnoughPower)
            onPlaced();

        if (getData().failTimer >= 4) {

            this.blockFail();
            if (getLevelAccessor() instanceof ServerLevel serverLevel)
                CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), new ElectricalBlockFailPacket(BlockPos.of(getPos())));
            getData().failTimer = 0;
            sendStuff();
        } else if ((getData().voltage > getMaxVoltage() && getMaxVoltage() > 0) || (getCurrent() > getMaxCurrent() && getMaxCurrent() > 0) || ((getData().highestCurrent > getMaxCurrent() && getMaxCurrent() > 0) && isCable())) {

            getData().failTimer++;
        }

    }


    default int getMaxVoltage() {
        return 1000;
    }

    default int getMaxCurrent() {
        return 0;
    }

    /**
     * handles connecting blocks into a network
     */
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

    /**
     * @return the block's world position
     */
    default BlockPos getBlockPos() {
        return BlockPos.of(getPos());
    }

    /**
     * tells blocks when the network doesn't have enough power
     */
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

    /**
     * the multimeter tooltip
     */
    default boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        TFMGTexts.header("multimeter").style(ChatFormatting.WHITE)
                .forGoggles(tooltip);


        if(getMaxVoltage()!=0&&getMaxVoltage()*0.8f < getData().getVoltage())
            TFMGLang.translate("multimeter.approaching_overvoltage").add(TFMGLang.text("("+ TFMGUtils.formatUnits(getMaxVoltage(),"V" +")"))).style(ChatFormatting.RED).forGoggles(tooltip);
        if(getMaxCurrent()!=0&&getMaxCurrent()*0.8f <getCurrent())
            TFMGLang.translate("multimeter.approaching_overcurrent").add(TFMGLang.text("("+ TFMGUtils.formatUnits(getMaxCurrent(),"A" +")"))).style(ChatFormatting.RED).forGoggles(tooltip);


        if (getData().notEnoughPower) TFMGTexts.Multimeter.notEnoughPower().forGoggles(tooltip, 1);

        if (voltageGeneration() > 0) {
            TFMGTexts.Multimeter.powerGenerated(powerGeneration()).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.voltageGenerated(voltageGeneration()).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.separator().forGoggles(tooltip);
        }
        if (resistance() != 0)
            TFMGTexts.Multimeter.resistance(voltageGeneration() > 0 ? getGeneratorResistance() : resistance()).forGoggles(tooltip, 1);
        TFMGTexts.Multimeter.voltage(getData().getVoltage()).forGoggles(tooltip, 1);
        TFMGTexts.Multimeter.current(resistance() == 0 ? getData().highestCurrent : getCurrent()).forGoggles(tooltip, 1);
        if (resistance() != 0)
            TFMGTexts.Multimeter.power(getPowerUsage()).forGoggles(tooltip, 1);


        if (isPlayerSneaking) {
            TFMGTexts.Multimeter.separator().forGoggles(tooltip);
            TFMGTexts.Multimeter.networkGeneration(getNetworkPowerGeneration()).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.networkConsumption(getNetworkPowerUsage()).forGoggles(tooltip, 1);
        }

        return true;
    }


    /**
     * contains data related to electricity
     */
    ElectricBlockValues getData();

    /**
     * @return true if the network has enough power
     */
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
                                // powerGeneration = Math.max(powerGeneration, be.getPowerUsage()) + 1;
                                powerGeneration = Math.max(powerGeneration, be.getMaxPowerOutput());
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

        getData().voltage = newVoltage;
    }


    default void setNetworkResistance(float newUsage) {

        getData().networkResistance = (int) newUsage;
    }


    default void setNetwork(long network) {
        getData().electricalNetworkId = network;
        if (network != getPos())
            ElectricNetworkManager.networks.get(getLevelAccessor())
                    .remove(getPos());
    }


}