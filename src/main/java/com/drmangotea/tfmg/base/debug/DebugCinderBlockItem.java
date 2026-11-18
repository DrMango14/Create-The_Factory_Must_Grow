package com.drmangotea.tfmg.base.debug;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlockEntity;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.content.devices.electric_motor.ElectricMotorBlockEntity;
import com.drmangotea.tfmg.content.electricity.content.devices.generators.three_phase_generator.ThreePhaseGeneratorBlockEntity;
import com.drmangotea.tfmg.content.electricity.content.devices.resistor.DebugResistorBlockEntity;
import com.drmangotea.tfmg.content.electricity.content.measuring_instruments.VoltMeterBlockEntity;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.energy_storage.accumulator_control_unit.AccumulatorControlUnitBlockEntity;
import com.drmangotea.tfmg.content.electricity.packets.NetworkLoadPacket;
import com.drmangotea.tfmg.content.electricity.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

@NothingNullByDefault
public class DebugCinderBlockItem extends Item {
    public DebugCinderBlockItem(Properties p) {
        super(p);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
		
		Player player = context.getPlayer();
		if (player == null) return InteractionResult.CONSUME;
		
		BlockEntity blockEntity = level.getBlockEntity(pos);
		
		if (blockEntity instanceof ElectricMotorBlockEntity be) {
            TFMG.LOGGER.debug("voltage: "+be.voltage[0]+" "+be.voltage[1]+" "+be.voltage[2]);
            TFMG.LOGGER.debug("power: "+be.power[0]+" "+be.power[1]+" "+be.power[2]);
            be.updateGeneratedRotation();
        }
        if (blockEntity instanceof ThreePhaseGeneratorBlockEntity be) {
            ElectricalNetwork network = ElectricNetworkManager.getNetwork(be.getWorld());
            network.setVoltageGen(be,100);

        }
        if (blockEntity instanceof AccumulatorControlUnitBlockEntity be) {
            ElectricalNetwork network = ElectricNetworkManager.getNetwork(be.getWorld());

            network.update();

            IdealVoltageSource source = network.getVoltageSource(be.getPos(),0);
            if(source == null){
                return InteractionResult.PASS;
            }

            TFMG.LOGGER.debug("generator: "+source.amplitude+" "+source.phaseOffset);
        }
        if (blockEntity instanceof DebugResistorBlockEntity be) {

            ElectricalNetwork network = ElectricNetworkManager.getNetwork(be.getWorld());

            network.setResistance(be, 0, TFMG.RANDOM.nextInt(5));

            return InteractionResult.SUCCESS;
        }
        if (blockEntity instanceof IElectric be && false) {
            //if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            //    NetworkLoadPacket packet = new NetworkLoadPacket(RealElectricNetworkManager.networks.values().stream().toList());
            //    CatnipServices.NETWORK.sendToClient(serverPlayer, packet);
            //}

            ElectricalNetwork network = ElectricNetworkManager.getNetwork(be.getWorld());

            // network.setVoltageGen(be, Create.RANDOM.nextInt(700));


            TFMG.LOGGER.debug("Member count: " + network.members.size());
            TFMG.LOGGER.debug("Node Count: " + network.nodes.size());
            TFMG.LOGGER.debug("Connection Count: " + network.connections.size());
            TFMG.LOGGER.debug("Resistor Count: " + network.resistors.size());
            TFMG.LOGGER.debug("This Block Node Count: " + be.getProperties().nodes.size());



        }

        if (blockEntity instanceof SteelTankBlockEntity be) {
            if (player.isCrouching() && be.getLevel() instanceof Level pLevel) {
                SteelTankBlock.updateTowerState(pLevel, be.getBlockPos(), false, false);
            }
			TFMG.LOGGER.debug("Distillation Tower? {}", be.isDistillationTower);
        }
        return InteractionResult.SUCCESS;
    }
}
