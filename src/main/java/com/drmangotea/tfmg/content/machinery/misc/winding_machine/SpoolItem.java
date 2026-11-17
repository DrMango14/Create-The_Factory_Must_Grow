package com.drmangotea.tfmg.content.machinery.misc.winding_machine;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.WireConnection;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large.LargeTransformerBlockEntity;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGPartialModels;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Objects;

import static com.drmangotea.tfmg.base.TFMGUtils.closestNode;

public class SpoolItem extends Item {

    public final int barColor;
    public final ResourceLocation cableTypeKey;

    public SpoolItem(Properties properties, int barColor, ResourceLocation cableTypeKey) {
        super(properties);
        this.barColor = barColor;
        this.cableTypeKey = cableTypeKey;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Vec3 clickPosition = context.getClickLocation();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        if (level.getBlockEntity(pos) instanceof WindingMachineBlockEntity be) {
            ItemStack oldSpool = ItemStack.EMPTY;
            if (!be.spool.isEmpty()) {
                oldSpool = be.spool;
            }
            be.spool = context.getItemInHand();
            context.getPlayer().setItemInHand(context.getHand(), oldSpool);
            be.sendData();
            be.setChanged();
//
            return InteractionResult.SUCCESS;
        }
        if (level.getBlockEntity(pos) instanceof IElectric be) {
            ConnectingElectricalNode node1 = closestNode(be, clickPosition);

            if (node1 == null) {
                return InteractionResult.PASS;
            }
            if (stack.get(TFMGDataComponents.POSITION) == null) {
                stack.set(TFMGDataComponents.POSITION, pos.asLong());
                stack.set(TFMGDataComponents.CONNECTOR_ID, node1.getLocalId());

            } else {
                ElectricalNetwork network = ElectricNetworkManager.getNetwork(level);

                BlockPos pos2 = BlockPos.of(stack.getOrDefault(TFMGDataComponents.POSITION, 0).longValue());
                int id = stack.getOrDefault(TFMGDataComponents.CONNECTOR_ID, 0);

                if (level.getBlockEntity(pos2) instanceof IElectric b) {
                    IElectric be2 = b;
                    if (b instanceof LargeTransformerBlockEntity t && !t.isMainPart) {
                        be2 = t.getControlledBlock();
                        ;
                    }

                    List<ElectricalNode> nodes = network.getNodes(be2.getPos());


                    ElectricalNode savedNode = nodes.get(0);

                    for (ElectricalNode n : nodes) {
                        if (n.getLocalId() == id) {
                            savedNode = n;
                        }
                    }

                    if (savedNode instanceof ConnectingElectricalNode node2) {

                        PartialModel model = TFMGPartialModels.STEEL_REINFORCED_CABLE;
                        if(context.getItemInHand().is(TFMGItems.COPPER_SPOOL)){
                            model = TFMGPartialModels.COPPER_CABLE;
                        }
                        if(context.getItemInHand().is(TFMGItems.ALUMINUM_SPOOL)){
                            model = TFMGPartialModels.ALUMINUM_CABLE;
                        }
                        if(context.getItemInHand().is(TFMGItems.CONSTANTAN_SPOOL)){
                            model = TFMGPartialModels.CONSTANTAN_CABLE;
                        }

                        network.connections.add(new WireConnection(node1, node2, 10, true,model));
                        float distance = TFMGUtils.getDistance(BlockPos.of(node1.pos).getBottomCenter(),BlockPos.of(node2.pos).getBottomCenter())*4;

                        stack.set(TFMGDataComponents.SPOOL_AMOUNT,(int)Math.max(0,stack.getOrDefault(TFMGDataComponents.SPOOL_AMOUNT,0)-distance));

                        network.update();
                        TFMG.ELECTRICAL_NETWORK_DATA.markDirty();
                        stack.remove(TFMGDataComponents.CONNECTOR_ID);
                        stack.remove(TFMGDataComponents.POSITION);
                        //    be.updateNetwork(BlockPos.of(be.getPos()));
                    }


                } else return InteractionResult.PASS;


            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level p_41448_, Player p_41449_) {
        stack.set(TFMGDataComponents.SPOOL_AMOUNT, 1000);
        super.onCraftedBy(stack, p_41448_, p_41449_);

    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching() && stack.getOrDefault(TFMGDataComponents.POSITION, 0f).longValue() != 0f) {
            stack.set(TFMGDataComponents.POSITION, 0l);
            stack.remove(TFMGDataComponents.POSITION);
            stack.remove(TFMGDataComponents.CONNECTOR_ID);
            if (level.isClientSide)
                player.displayClientMessage(TFMGLang.translateDirect("wires.removed_data")
                        .withStyle(ChatFormatting.YELLOW), true);
            return InteractionResultHolder.success(stack);

        }

        return super.use(level, player, hand);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {

        String text = TFMGLang.translateDirect("tooltip.coils").getString();

        tooltip.add(TFMGLang.text(text + stack.getOrDefault(TFMGDataComponents.SPOOL_AMOUNT, 0)).component().withStyle(ChatFormatting.GREEN)

        );

        if (stack.get(TFMGDataComponents.POSITION) == null)
            return;
        BlockPos pos = BlockPos.of(stack.get(TFMGDataComponents.POSITION));
        if (pos.asLong() != 0)
            tooltip.add(TFMGLang.text(pos.getX() + " " + pos.getY() + " " + pos.getZ()).component()
                    .withStyle(ChatFormatting.AQUA)
            );
        super.appendHoverText(stack, context, tooltip, flag);
    }




    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean p_41408_) {


        if (stack.get(TFMGDataComponents.SPOOL_AMOUNT) == null)
            return;

        if (stack.get(TFMGDataComponents.SPOOL_AMOUNT) == 0 && entity instanceof Player player && !stack.is(TFMGItems.EMPTY_SPOOL.get())) {
            player.getInventory().setItem(slot, TFMGItems.EMPTY_SPOOL.asStack());
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return !Objects.equals(cableTypeKey, TFMG.asResource("empty")) && TFMGRegistries.CABLE_TYPE_REGISTRY.containsKey(cableTypeKey);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return barColor;
    }

    @Override
    public int getBarWidth(ItemStack stack) {

        if (stack.get(TFMGDataComponents.SPOOL_AMOUNT) == null)
            return 13;

        return (int) (13f * ((float) stack.get(TFMGDataComponents.SPOOL_AMOUNT) / 1000));
    }

}
