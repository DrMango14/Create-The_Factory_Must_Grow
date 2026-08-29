package com.drmangotea.tfmg.base.debug;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlockEntity;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.blocks.DebugResistorBlockEntity;
import com.drmangotea.tfmg.content.electricity.experimental.blocks.ThreePhaseGeneratorBlockEntity;
import com.simibubi.create.Create;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;


public class DebugCinderBlockItem extends Item {
    public DebugCinderBlockItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (level.getBlockEntity(pos) instanceof ThreePhaseGeneratorBlockEntity be) {
            RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(be.getLevel());


        }
        if (level.getBlockEntity(pos) instanceof DebugResistorBlockEntity be) {

            RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(be.getWorld());

            network.setResistance(be, 0, Create.RANDOM.nextInt(700));

            return InteractionResult.SUCCESS;
        }
        if (level.getBlockEntity(pos) instanceof IRealisticElectric be) {
            //if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            //    NetworkLoadPacket packet = new NetworkLoadPacket(RealElectricNetworkManager.networks.values().stream().toList());
            //    CatnipServices.NETWORK.sendToClient(serverPlayer, packet);
            //}

            RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(be.getWorld());

            network.setVoltageGen(be, Create.RANDOM.nextInt(700));

            TFMG.LOGGER.debug("Member count: " + network.members.size());
            TFMG.LOGGER.debug("Node Count: " + network.nodes.size());
            TFMG.LOGGER.debug("Connection Count: " + network.connections.size());
            TFMG.LOGGER.debug("This Block Node Count: " + be.getProperties().nodes.size());

            network.connections.forEach(c -> {
                TFMG.LOGGER.debug("Connection1  " + c.node1().getPosition().x() + c.node1().getPosition().y() + c.node1().getPosition().z());
                TFMG.LOGGER.debug("Connection2  " + c.node2().getPosition().x() + c.node2().getPosition().y() + c.node2().getPosition().z());
            });

            if (context.getPlayer().isCrouching()) {
                TFMG.ELECTRICAL_NETWORK_DATA.markDirty();

            }


        }


        if (level.getBlockEntity(pos) instanceof SteelTankBlockEntity be) {
            if (!context.getPlayer().isCrouching()) {
                //SteelTankBlock.updateTowerState(be.getLevel(), be.getBlockPos(), true, false);
                // be.updateTemperature();
                TFMG.LOGGER.debug(String.valueOf(be.isDistillationTower));
                //be.sendData();
                //be.getControllerBE().sendData();
            } else {
                SteelTankBlock.updateTowerState(be.getLevel(), be.getBlockPos(), false, false);
                TFMG.LOGGER.debug(String.valueOf(be.isDistillationTower));
            }
        }
        return InteractionResult.SUCCESS;
    }
}
