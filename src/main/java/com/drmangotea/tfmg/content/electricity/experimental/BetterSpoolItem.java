package com.drmangotea.tfmg.content.electricity.experimental;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.ElectricalNode;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class BetterSpoolItem extends Item {
    public BetterSpoolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Vec3 clickPosition = context.getClickLocation();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (level.getBlockEntity(pos) instanceof IRealisticElectric be) {
            ConnectingElectricalNode node1 = closestNode(be, clickPosition);
            TFMG.LOGGER.debug("Closest node is " + node1.getNetworkId());
            if (stack.get(TFMGDataComponents.POSITION) == null) {
                stack.set(TFMGDataComponents.POSITION, pos.asLong());
                stack.set(TFMGDataComponents.CONNECTOR_ID, node1.getLocalId());
                TFMG.LOGGER.debug("Saved node " + node1.getLocalId() + " " + node1.getNetworkId());
            } else {
                RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);

                BlockPos pos2 = BlockPos.of(stack.getOrDefault(TFMGDataComponents.POSITION, 0).longValue());
                int id = stack.getOrDefault(TFMGDataComponents.CONNECTOR_ID, 0);

                if (level.getBlockEntity(pos2) instanceof IRealisticElectric be2) {
                    List<ElectricalNode> nodes = be2.getProperties().nodes;

                    ElectricalNode savedNode = nodes.get(0);

                    for (ElectricalNode n : nodes) {
                        if (n.getLocalId() == id) {
                            savedNode = n;
                        }
                    }

                    if (savedNode instanceof ConnectingElectricalNode node2) {
                        network.connections.add(new WireConnection(node1, node2, 10));
                        network.update();
                        TFMG.ELECTRICAL_NETWORK_DATA.markDirty();
                        stack.remove(TFMGDataComponents.CONNECTOR_ID);
                        stack.remove(TFMGDataComponents.POSITION);
                    }


                } else return InteractionResult.PASS;


            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private ConnectingElectricalNode closestNode(IRealisticElectric be, Vec3 clickPosition) {
        Map<Vec3, ConnectingElectricalNode> connectors = new HashMap<>();

        BlockPos pos = BlockPos.of(be.getPos());

        be.getProperties().nodes.forEach(n -> {
            if (n instanceof ConnectingElectricalNode node) {
                CablePos position = node.getPosition().add(pos);
                connectors.put(new Vec3(position.x(), position.y(), position.z()), node);
            }
        });
        Map<Float, ConnectingElectricalNode> distances = new HashMap<>();
        connectors.forEach((c, n) -> {
            float distance = TFMGUtils.getDistance(clickPosition, c);
            distances.put(distance, n);
        });
        AtomicReference<Float> closestDistance = new AtomicReference<>((float) 1000);
        AtomicReference<ConnectingElectricalNode> closestConnector = new AtomicReference<>((ConnectingElectricalNode) be.getProperties().nodes.get(0));
        distances.forEach((f, n) -> {
            if (f < closestDistance.get()) {
                closestDistance.set(f);
                closestConnector.set(n);
            }
        });

        for (ElectricalNode node : RealElectricNetworkManager.getNetwork(be.getWorld()).getNodes(be.getPos())) {
            TFMG.LOGGER.debug("nodes are " + node.getNetworkId());
        }


        return closestConnector.get();
    }

}
