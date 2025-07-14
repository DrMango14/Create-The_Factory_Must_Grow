package com.drmangotea.tfmg.content.electricity.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnection;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnectorBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.platform.CatnipServices;
import net.createmod.catnip.theme.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

    boolean destroyed();

    ElectricalNetwork getOrCreateElectricNetwork();

    default boolean hasElectricitySlot(Direction direction) {
        return true;
    }

    default void onPlaced() {

        if (getLevelAccessor() instanceof ServerLevel serverLevel)
            CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(BlockPos.of(getPos())),new ConnectNeightborsPacket(BlockPos.of(getPos())));
        ElectricalNetwork network = TFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
        setNetwork(getPos());
        getData().electricalNetworkId = getPos();
        network.add(this);

        BlockPos pos = BlockPos.of(getPos());
        getData().checkForLoopsNextTick = true;
        getOrCreateElectricNetwork().checkForLoops(BlockPos.of(getPos()));
        /// ////


        // for (Direction d : Direction.values()) {
        //     if (hasElectricitySlot(d))
        //         if (getLevelAccessor().getBlockEntity(pos.relative(d)) instanceof IElectric be) {
        //             if (be.hasElectricitySlot(d.getOpposite())) {
        //                 if (!be.destroyed()) {
//
//
        //                     for(IElectric member : be.getOrCreateElectricNetwork().members){
        //                         network.add(member);
        //                         member.setNetwork(this.getData().electricalNetworkId);
//
        //                     }
//
        //                 }
        //             }
        //         }
        // }
        updateNextTick();

        onConnected();
        sendStuff();

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

    default void updateUnpowered(List<BlockPos> alreadyChecked) {
        alreadyChecked.add(BlockPos.of(getPos()));
        updateNextTick();

        if(this instanceof CableConnectorBlockEntity connectorBE){
            for(CableConnection connection : connectorBE.connections){

                if(getLevelAccessor().getBlockEntity(connection.blockPos1) instanceof CableConnectorBlockEntity be2 &&!alreadyChecked.contains(BlockPos.of(be2.getPos()))
                ){
                 //   this.getLevelAccessor().setBlock(connection.blockPos1.above(2),Blocks.NETHER_BRICKS.defaultBlockState(),3);
                    be2.updateUnpowered(alreadyChecked);
                }
            }
        }

        for (Direction direction : Direction.values()) {
            if(getLevelAccessor().getBlockEntity(BlockPos.of(getPos()).relative(direction)) instanceof IElectric be&&!alreadyChecked.contains(BlockPos.of(be.getPos()))){
                be.updateUnpowered(alreadyChecked);
               // be.getLevelAccessor().setBlock(BlockPos.of(getPos()).above(2),Blocks.NETHERRACK.defaultBlockState(),3);
            }
        }
    }

    default boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CreateLang.translate("multimeter.header")
                .style(ChatFormatting.WHITE)
                .forGoggles(tooltip);

        if (getData().notEnoughtPower) {
            CreateLang.text("NOT ENOUGHT POWER")
                    .color(Color.RED)
                    .forGoggles(tooltip, 1);

            //  return true;
        }


        //CreateLang.text("Network Power ")
        //        .color(Color.RED)
        //        .forGoggles(tooltip, 1);


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
                .color(0xc98969)
                .forGoggles(tooltip, 1);
        CreateLang.text("   U = " + TFMGUtils.formatUnits(getData().getVoltage(), "V"))
                .color(0x4bbbcc)
                .forGoggles(tooltip, 1);
        CreateLang.text("   I = " + TFMGUtils.formatUnits(getCurrent(), "A"))
                .color(0x22a146)
                .forGoggles(tooltip, 1);
        CreateLang.text("   P = " + TFMGUtils.formatUnits(getPowerUsage(), "W"))
                .color(0xcc4b74)
                .forGoggles(tooltip, 1);
        if (getData().group.id != -1) {
            CreateLang.text("----------------------------")
                    .style(ChatFormatting.WHITE)
                    .forGoggles(tooltip);
            CreateLang.translate("multimeter.group")
                    .add(CreateLang.number(getData().group.id))
                    .color(0xd8db27)
                    .forGoggles(tooltip, 1);
        }

        if (isPlayerSneaking) {
            CreateLang.text("----------------------------")
                    .style(ChatFormatting.WHITE)
                    .forGoggles(tooltip);
            CreateLang.text("Network Power Generation: " + TFMGUtils.formatUnits(getNetworkPowerGeneration(), "W"))
                    .color(0xcc4b74)
                    .forGoggles(tooltip, 1);

            CreateLang.text("Network Power Consumption: " + TFMGUtils.formatUnits(getNetworkPowerUsage(), "W"))
                    .color(0xcc4b74)
                    .forGoggles(tooltip, 1);

        }

        return true;
    }

    default void updateNearbyNetworks(IElectric member) {
        if (true)
            return;
        //if (member.getData().getsOutsidePower) {

        for (Direction direction : Direction.values()) {
            if (member.getLevelAccessor().getBlockEntity(BlockPos.of(member.getPos()).relative(direction)) instanceof IElectric be && be.getData().getId() != member.getData().getId()) {
                be.getLevelAccessor().setBlock(BlockPos.of(be.getPos()).above(3), Blocks.GOLD_BLOCK.defaultBlockState(), 3);
                be.updateNextTick();
            }
        }
        //  }
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


    float resistance();

    int voltageGeneration();

    int powerGeneration();

    int frequencyGeneration();

    int getNetworkResistance();

    default int getMaxAmps() {
        return (int) getCurrent();
    }

    default float getCurrent() {
        return getData().getVoltage() == 0 || resistance() == 0 ? 0 : ((float) getData().getVoltage() / (float) resistance());
    }

    default float getCableCurrent() {
        float current = 0;
        List<Integer> groups = new ArrayList<>();
        for (IElectric member : getOrCreateElectricNetwork().members) {
            if (member.canBeInGroups())
                if (!groups.contains(member.getData().group.id)) {
                    current += member.getCurrent();
                    groups.add(member.getData().group.id);
                }
        }
        return current;
    }

    void updateNextTick();

    void updateNetwork();

    void sendStuff();

    void setVoltage(int newVoltage);

    void setFrequency(int newFrequency);

    void setNetworkResistance(int newUsage);


    void setNetwork(long network);

    default boolean canBeInGroups() {
        return false;
    }


}