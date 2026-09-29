package com.drmangotea.tfmg.content.electricity.experimental;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.nodes.ElectricalNode;
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

import static com.drmangotea.tfmg.base.TFMGUtils.closestNode;

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

            if (stack.get(TFMGDataComponents.POSITION) == null) {
                stack.set(TFMGDataComponents.POSITION, pos.asLong());
                stack.set(TFMGDataComponents.CONNECTOR_ID, node1.getLocalId());
                
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


                        network.connections.add(new WireConnection(node1, node2, 10,true));




                        network.update();
                        TFMG.ELECTRICAL_NETWORK_DATA.markDirty();
                        stack.remove(TFMGDataComponents.CONNECTOR_ID);
                        stack.remove(TFMGDataComponents.POSITION);
                        be.updateNetwork(BlockPos.of(be.getPos()));
                    }


                } else return InteractionResult.PASS;


            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }



}
